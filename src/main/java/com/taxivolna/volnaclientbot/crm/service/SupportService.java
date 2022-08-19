package com.taxivolna.volnaclientbot.crm.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.taxivolna.volnaclientbot.model.TelegramUser;

public interface SupportService {
    SendMessage sendToSupport(Update update, TelegramUser user);

    SendMessage sendToUser(Update update);

    boolean isSupportChat(Update update);

    Long parseUserId(String msg);
}
