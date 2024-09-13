package com.lawProject.SSL.global.oauth.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String userId;
    private String name;
    private String role;

    public UserDTO(String userId, String name, String role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }
}
