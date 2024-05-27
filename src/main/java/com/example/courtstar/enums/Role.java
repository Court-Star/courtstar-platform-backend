package com.example.courtstar.enums;

import java.util.HashMap;
import java.util.Map;

public enum Role {
    ADMIN(1),
    CENTRE_MANAGER(2),
    CENTRE_STAFF(3),
    CUSTOMER(4) ;

    private final int value;
    private static final Map<Integer, Role> map = new HashMap<>();

    static {
        for (Role role : Role.values()) {
            map.put(role.value, role);
        }
    }

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Role fromValue(int value) {
        Role role = map.get(value);
        if (role != null) {
            return role;
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
}
