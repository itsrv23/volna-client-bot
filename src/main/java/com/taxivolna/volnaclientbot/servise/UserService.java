package com.taxivolna.volnaclientbot.servise;

import com.pengrad.telegrambot.model.Update;
import com.taxivolna.volnaclientbot.model.TelegramUserStateEnum;
import com.taxivolna.volnaclientbot.model.TelegramUser;
import com.taxivolna.volnaclientbot.model.TelegramUserState;

public interface UserService {
    TelegramUser updateState(TelegramUser user, TelegramUserStateEnum newState);
    TelegramUser findUserOrCreate(Update update);
    TelegramUser createUser(Update update);
    TelegramUserState getUserState(TelegramUserStateEnum stateEnum);
    TelegramUserState createUserState(TelegramUserStateEnum stateEnum);
    TelegramUser savePhone(Update update);
    String convertPhoneLocalFormat(String phone);
}
