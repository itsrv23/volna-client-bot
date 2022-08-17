package com.taxivolna.volnaclientbot.model;

public enum TelegramUserStateEnum {
    MAIN_MENU("Основное меню"),
    MAIN_MENU_2("Дополнительное основное меню"),
    CLIENT_CABINET("Кабинет клиента"),
    DRIVER_CABINET("Кабинет водителя"),
    PROMO_ASK_PHONE("Запрашиваем телефон"),
    PROMO_ENTER_CODE("Ждем ввода промокода");

    private final String name;

    TelegramUserStateEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
