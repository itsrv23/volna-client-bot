package com.taxivolna.volnaclientbot.listener;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.taxivolna.volnaclientbot.model.*;
import com.taxivolna.volnaclientbot.servise.BonusService;
import com.taxivolna.volnaclientbot.servise.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.taxivolna.volnaclientbot.model.Button.K_ENTER_PROMO;
import static com.taxivolna.volnaclientbot.model.TelegramUserStateEnum.*;

@Service
public class MessageBuilder {
    private final static Logger logger = LoggerFactory.getLogger(MessageBuilder.class);
    private final static String OUT_DEFAULT_MESSAGE = "Функция находится в разработке.  /start";
    private final static String SELECT_MENU = "Выберите пункт меню";
    private static String MESSAGE = "";
    TelegramUserStateEnum USER_STATE = MAIN_MENU;
    @Autowired
    private BonusService bonusService;
    @Autowired
    private UserService userService;

    /**
     *
     */

    // Должны получить еще id статуса
    public SendMessage getSendMessageWithReplyKeyboard(Update update) {

        Long userId = update.message().chat().id();
        SendMessage sendMessage = new SendMessage(userId, OUT_DEFAULT_MESSAGE);
        MenuKeyboard menuKeyboard = new MenuKeyboard();

        TelegramUser user = userService.findUser(update);

        if (user != null) {
            USER_STATE = user.getUserState().getName();
        }
        logger.info("user: " + user);

        if (update.message().text() != null) {
            MESSAGE = update.message().text();
            logger.info("MESSAGE: " + MESSAGE + ", " + "getSendMessage user: " + user);
        }

        // Координаты
        if (update.message().location() != null) {
            logger.info(update.message().location().toString());
            String answer = "r Location " + update.message().location().toString();
            return new SendMessage(userId, answer)
                    .replyMarkup(menuKeyboard.getReplyKeyboardMenu(null));
            /*TODO написать логику userState*/

        }

        // /start
        if (MESSAGE.startsWith("/start")) {
            String answer = "Добро пожаловать!";
            userService.updateState(user, MAIN_MENU);
            return new SendMessage(userId, answer)
                    .replyMarkup(menuKeyboard.getReplyKeyboardMenu(userService.getUserState(MAIN_MENU)));
        }


        //region Promo
        //Основное меню, кнопка промокод и нужен номер клиента
        if (USER_STATE.equals(MAIN_MENU) && user.getPhone() == null) {
            String answer = "Нажмите кнопку - " + Button.K_ASK_PHONE;
            userService.updateState(user, PROMO_ASK_PHONE);
            return new SendMessage(userId, answer)
                    .replyMarkup(menuKeyboard.getReplyKeyboardMenu(userService.getUserState(PROMO_ASK_PHONE)));
        }

        //Основное меню, кнопка промокод и номер уже есть
        if (MESSAGE.startsWith(K_ENTER_PROMO) && user.getPhone() != null) {
            String answer = "Введите Ваш промокод ";
            userService.updateState(user, PROMO_ENTER_CODE);
            return new SendMessage(userId, answer)
                    .replyMarkup(menuKeyboard.getReplyKeyboardMenu(userService.getUserState(PROMO_ENTER_CODE)));
        }

        // Контакт
        if (USER_STATE.equals(PROMO_ASK_PHONE) && update.message().contact() != null) {
            String phone = update.message().contact().phoneNumber();
            TelegramUser telegramUser = userService.savePhone(update);
            logger.info("Save phone number: " + telegramUser.toString());
            String answer = "Спасибо! Ваш номер " + userService.convertPhoneLocalFormat(phone) + ". Введите Ваш промокод ";
            userService.updateState(telegramUser, PROMO_ENTER_CODE);
            return new SendMessage(userId, answer)
                    .replyMarkup(menuKeyboard.getReplyKeyboardMenu(userService.getUserState(PROMO_ENTER_CODE)));

        }


        // Ввод промокода, кнопка назад
        if ((USER_STATE.equals(PROMO_ENTER_CODE) || USER_STATE.equals(PROMO_ASK_PHONE)) && MESSAGE.startsWith(Button.K_BACK)) {
            userService.updateState(user, MAIN_MENU);
            return new SendMessage(userId, SELECT_MENU)
                    .replyMarkup(menuKeyboard.getReplyKeyboardMenu(userService.getUserState(MAIN_MENU)));
        }

        // Ввод промокода, после того как поделились номером
        if (USER_STATE.equals(PROMO_ENTER_CODE)) {
            String answer = bonusService.activatePromo(MESSAGE.toUpperCase(), user.getPhone());
            userService.updateState(user, MAIN_MENU);
            return new SendMessage(userId, answer)
                    .replyMarkup(menuKeyboard.getReplyKeyboardMenu(userService.getUserState(MAIN_MENU)));

        }

        //region order

        // Новый заказ
        if (USER_STATE.equals(MAIN_MENU) && MESSAGE.startsWith(Button.K_NEW_ORDER)) {
            return new SendMessage(userId, OUT_DEFAULT_MESSAGE)
                    .replyMarkup(menuKeyboard.getReplyKeyboardMenu(userService.getUserState(MAIN_MENU)));
        }

        //endregion order


        //endregion Promo

        //Основное меню, кнопка далее
        if (USER_STATE.equals(MAIN_MENU) && MESSAGE.startsWith(Button.K_NEXT)) {
            userService.updateState(user, MAIN_MENU_2);
            SendMessage msg = new SendMessage(userId, SELECT_MENU)
                    .replyMarkup(menuKeyboard.getReplyKeyboardMenu(userService.getUserState(MAIN_MENU_2)));
            return msg;
        }

        //Основное меню, кнопка назад
        if (USER_STATE.equals(MAIN_MENU_2) && MESSAGE.startsWith(Button.K_BACK)) {
            userService.updateState(user, MAIN_MENU);
            return new SendMessage(userId, SELECT_MENU)
                    .replyMarkup(menuKeyboard.getReplyKeyboardMenu(userService.getUserState(MAIN_MENU)));
        }

        //region About
        //О нас
        if (MESSAGE.startsWith(Button.K_ABOUT_AS)) {
            String answer = "Операторы всегда на связи:\n" +
                    "\n" +
                    "В Алуште: +7 (978) 108-08-08\n" +
                    "В Севастополе: +7 (978) 727-07-27\n" +
                    "В Ялте: +7 (978) 055-05-05\n" +
                    "\n" +
                    "@SamaelKrd23 - по вопросам работоспособности и новому функционалу";
            return new SendMessage(userId, answer)
                    .replyMarkup(menuKeyboard.getInlineKeyboardMenu(Button.K_ABOUT_AS));
        }

        //endregion

        // Контакт
        if (update.message().contact() != null) {
            String phone = update.message().contact().phoneNumber();
            TelegramUser telegramUser = userService.savePhone(update);
            logger.info("Save phone number: " + telegramUser.toString());
            String answer = "Спасибо! Ваш номер " + userService.convertPhoneLocalFormat(phone);
            return new SendMessage(userId, answer)
                    .replyMarkup(menuKeyboard.getReplyKeyboardMenu(userService.getUserState(MAIN_MENU)));
        }

        return sendMessage;
    }

    public SendMessage getSendMessageWithInlineKeyboard(Update update) {
        return null;
    }
}
