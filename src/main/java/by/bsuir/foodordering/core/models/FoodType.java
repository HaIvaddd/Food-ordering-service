package by.bsuir.foodordering.core.models;

import lombok.Getter;

@Getter
public enum FoodType {
    PIZZA("Пицца"),
    BURGER("Бургер"),
    SUSHI("Суши"),
    SALAD("Салат"),
    DESSERT("Десерт"),
    DRINK("Напиток");

    private final String displayName;

    FoodType(String displayName) {
        this.displayName = displayName;
    }
}

