package com.batariloa.reactiveblogbackend.user;


import lombok.Getter;

@Getter

public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private String value;

    Role(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
