package com.amn.dto;

import com.amn.entity.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;

    // Extra doctor fields
    private String phone;
    private String specialization;
}
