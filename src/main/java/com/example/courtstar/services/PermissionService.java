package com.example.courtstar.services;

import com.example.courtstar.dto.request.PermissionRequest;
import com.example.courtstar.dto.response.PermissionResponse;
import com.example.courtstar.entity.Permission;
import com.example.courtstar.mapper.PermissionMapper;
import com.example.courtstar.repositories.PermissionReponsitory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PermissionService {
    PermissionMapper permissionMapper;
    PermissionReponsitory permissionReponsitory;
    public PermissionResponse Create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission=permissionReponsitory.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> GetAll(){
        var permissions = permissionReponsitory.findAll();
        return permissions.stream()
                .map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String name){
        permissionReponsitory.deleteById(name);
    }
}
