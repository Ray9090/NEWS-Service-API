package com.example.userservice.init;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.ProfileRole;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class Init {

    @Value("${init.data}")
    private boolean init;

    private final UserService userService;
    protected User user_1 = null;

    @PostConstruct
    public void init() {
        if (!init) {
            return;
        }
        userService.createUser(user_1());
    }

        private UserDTO user_1() {
        ProfileRole profileRoles = ProfileRole.ADMIN_ROLE;

        return UserDTO.builder()
                .firstName("Admin")
                .lastName("Admin")
                .email("admin@gmail.com")
                .password("Admin@123")
                .role(profileRoles)
                .build();
    }

}
