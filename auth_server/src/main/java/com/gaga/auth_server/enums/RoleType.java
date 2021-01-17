package com.gaga.auth_server.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
public enum RoleType {
    MANAGER("ROLE_MANAGER"), MEMBER("ROLE_MEMBER"), DEFAULT("ROLE_DEFAULT");

    private String roleName;

    RoleType(String roleName) {
        this.roleName = roleName;
    }

    public boolean isCorrectName(String name) {
        return name.equalsIgnoreCase(this.roleName);
    }

    public static RoleType getRoleByName(String roleName) {
        return Arrays.stream(RoleType.values()).filter(r -> r.isCorrectName(roleName)).findFirst().orElseThrow(() -> new NoSuchElementException("🔥 error: no have ROLE"));
    }
}
