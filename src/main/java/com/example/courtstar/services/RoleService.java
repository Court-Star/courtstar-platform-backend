package com.example.courtstar.services;

import com.example.courtstar.dto.request.RoleRequest;
import com.example.courtstar.dto.response.RoleResponse;
import com.example.courtstar.entity.Role;
import com.example.courtstar.mapper.RoleMapper;
import com.example.courtstar.repositories.PermissionReponsitory;
import com.example.courtstar.repositories.RoleReponsitory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleReponsitory roleReponsitory;
    PermissionReponsitory permissionReponsitory;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        var permissions = permissionReponsitory.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleReponsitory.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles() {
        return roleReponsitory.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public List<Role> findAllById(String name) {
        return roleReponsitory.findAllByName(name);
    }

    public void delete(String role) {
        roleReponsitory.deleteById(role);
    }
}
