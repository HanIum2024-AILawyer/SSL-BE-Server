package com.lawProject.SSL.global.oauth.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String name;
    private String role;

    public UserDTO(String username, String name, String role) {
        this.username = username;
        this.name = name;
        this.role = role;
    }
}
