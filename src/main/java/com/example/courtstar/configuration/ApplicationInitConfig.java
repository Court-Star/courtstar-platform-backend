package com.example.courtstar.configuration;

import com.example.courtstar.constant.PredefinedRole;
import com.example.courtstar.entity.Account;
import com.example.courtstar.entity.Role;
import com.example.courtstar.repositories.AccountReponsitory;
import com.example.courtstar.repositories.RoleReponsitory;
import com.example.courtstar.services.RoleService;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@Slf4j
public class ApplicationInitConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @NonFinal
    static final String ADMIN_EMAIL = "Admin@gmail.com";
    @NonFinal
    static final String ADMIN_PASSWORD = "admin";
    @Bean
    ApplicationRunner applicationContext(AccountReponsitory accountReponsitory, RoleReponsitory roleReponsitory) {
        log.info("Initializing application.....");
        roleReponsitory.save(Role.builder()
                .name(PredefinedRole.CUSTOMER_ROLE)
                .description("Customer role")
                .build());
        roleReponsitory.save(Role.builder()
                .name(PredefinedRole.MANAGER_ROLE)
                .description("Manager role")
                .build());
        roleReponsitory.save(Role.builder()
                .name(PredefinedRole.STAFF_ROLE)
                .description("Staff role")
                .build());

        Role adminRole = roleReponsitory.save(Role.builder()
                .name(PredefinedRole.ADMIN_ROLE)
                .description("Admin role")
                .build());
        return args->{
            if(accountReponsitory.findByEmail(ADMIN_EMAIL).isEmpty()) {



                var roles = new HashSet<Role>();
                roles.add(adminRole);

                Account user = Account.builder()
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();
                accountReponsitory.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}