package com.amn.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    DOCTOR,
    PATIENT,
    PHARMACIST;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
