package io.github.PercivalGebashe.portfolio_project_showcase.model.enums;

import lombok.Getter;

public enum RoleEnum {
    ROLE_USER(1),
    ROLE_ADMIN(2);

    private final int id;

    RoleEnum(int id) { this.id = id; }

    public static RoleEnum fromId(int id) {
        for (RoleEnum r : values()) {
            if (r.getId() == id) return r;
        }
        throw new IllegalArgumentException("Invalid Role id: " + id);
    }

    public int getId() {
        return id;
    }
}
