package com.taxivolna.volnaclientbot.crm;

import com.pengrad.telegrambot.model.request.*;
import com.taxivolna.volnaclientbot.model.TelegramUserState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.taxivolna.volnaclientbot.model.Button.*;
import static com.taxivolna.volnaclientbot.model.TelegramUserStateEnum.*;

@Service
public class MenuKeyboard {
    Logger logger = LoggerFactory.getLogger(MenuKeyboard.class);

    private Keyboard getDefaultReplayKeyboardMenu() {
        return new ReplyKeyboardMarkup(K_ENTER_PROMO, K_SUPPORT)
                .addRow(K_ABOUT_AS, K_NEXT)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
    }


    public Keyboard getReplyKeyboardMenu(TelegramUserState userState) {
        if (userState == null) {
            return getDefaultReplayKeyboardMenu();
        }
        logger.info("Invoked MenuKeyboard.getMenu, userState:" + userState);
        Keyboard keyboard;

        // Основное
        if (userState.getName().equals(MAIN_MENU)) {
            return getDefaultReplayKeyboardMenu();
        }
        // Основное 2
        if (userState.getName().equals(MAIN_MENU_2)) {
            keyboard = new ReplyKeyboardMarkup(K_CLIENT_CABINET, K_DRIVER_CABINET)
                    .addRow(K_BACK, K_NEW_ORDER)
                    .resizeKeyboard(true)
                    .selective(true);
            return keyboard;
        }
        //Запрашиваем номер для промокода
        if (userState.getName().equals(PROMO_ASK_PHONE)) {
            keyboard = new ReplyKeyboardMarkup(new KeyboardButton(K_ASK_PHONE).requestContact(true))
                    .addRow(K_BACK, K_SUPPORT)
                    .resizeKeyboard(true)
                    .selective(true);
            return keyboard;
        }
        // Ввод промокода
        if (userState.getName().equals(PROMO_ENTER_CODE)) {
            keyboard = new ReplyKeyboardMarkup(K_BACK)
                    .resizeKeyboard(true)
                    .selective(true);
            return keyboard;
        }
        // Support
        if(userState.getName().equals(SUPPORT_ASK_PHONE)){
            keyboard = new ReplyKeyboardMarkup(new KeyboardButton(K_BACK),
                    new KeyboardButton(K_ASK_PHONE).requestContact(true))
                    .resizeKeyboard(true)
                    .selective(true);
            return keyboard;
        }

        if(userState.getName().equals(SUPPORT)){
            keyboard = new ReplyKeyboardMarkup(K_BACK)
                    .resizeKeyboard(true)
                    .selective(true);
            return keyboard;
        }

        return getDefaultReplayKeyboardMenu();
    }

    public Keyboard getInlineKeyboardMenu(String callBack) {
        // О нас
        if (callBack.equals(K_ABOUT_AS)) {
            InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                    new InlineKeyboardButton(I_SITE).url(I_LINK_SITE)
            ).addRow(new InlineKeyboardButton(I_GOOGLE_PLAY).url(I_LINK_GOOGLE_PLAY)
            ).addRow(new InlineKeyboardButton(I_APPSTORE).url(I_LINK_APPSTORE));
            return inlineKeyboard;
        }
        return null;
    }
}