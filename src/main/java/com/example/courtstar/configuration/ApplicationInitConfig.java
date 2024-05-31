package com.example.courtstar.configuration;

import com.example.courtstar.entity.Account;
import com.example.courtstar.entity.Role;
import com.example.courtstar.repositories.AccountReponsitory;
import com.example.courtstar.services.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
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
    @Bean
    ApplicationRunner applicationContext(AccountReponsitory accountReponsitory) {
        return args->{
            if(accountReponsitory.findByEmail("Admin@gmail.com").isEmpty()) {
                Account.AccountBuilder builder = Account.builder();
                builder.email("Admin@gmail.com");
                builder.password(passwordEncoder.encode("admin"));
                Account account = builder
                        .build();
                var roles = roleService.findAllById("ADMIN");
                account.setRoles(new HashSet<>(roles));
                accountReponsitory.save(account);
                log.warn("admin user has been created with email admin@gmail.com and password admin");
            }
        };
    }
}
