package io.github.PercivalGebashe.portfolio_project_showcase.model.enums;

import lombok.Getter;

public enum EmailValidationStatusEnum {
    PENDING(1),
    VERIFIED(2),
    FAILED(3);

    private final int id;

    EmailValidationStatusEnum(int id) {
        this.id = id;
    }

    public static EmailValidationStatusEnum fromId(int id) {
        for (EmailValidationStatusEnum status : values()) {
            if (status.getId() == id) return status;
        }
        throw new IllegalArgumentException("Invalid EmailValidationStatus id: " + id);
    }

    public int getId() {
        return id;
    }
}