package by.bsuir.foodordering.core.models;

import lombok.Getter;

@Getter
public enum UserType {
    USER("Клиент"),
    ADMIN("Администратор");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }
}