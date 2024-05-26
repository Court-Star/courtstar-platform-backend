package com.example.courtstar.configuration;

import com.example.courtstar.entity.Account;
import com.example.courtstar.reponsitory.AccountReponsitory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.courtstar.enums.Role;

import java.util.HashSet;

@Configuration
@Slf4j
public class ApplicationInitConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationContext(AccountReponsitory accountReponsitory) {
        return args->{
            if(accountReponsitory.findByEmail("Admin@gmail.com").isEmpty()) {
                var role = new HashSet<String>();
                role.add(Role.ADMIN.name());
                Account account =Account.builder()
                        .email("Admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .role(role)
                        .build();
                accountReponsitory.save(account);
                log.warn("admin user has been created with email admin@gmail.com and password admin");
            }
        };
    }
}
