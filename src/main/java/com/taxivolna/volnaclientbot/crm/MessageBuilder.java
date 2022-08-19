package com.taxivolna.volnaclientbot.crm;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import com.taxivolna.volnaclientbot.crm.menu.About;
import com.taxivolna.volnaclientbot.crm.menu.Contact;
import com.taxivolna.volnaclientbot.crm.service.impl.SupportServiceImpl;
import com.taxivolna.volnaclientbot.model.Button;
import com.taxivolna.volnaclientbot.model.TelegramUser;
import com.taxivolna.volnaclientbot.model.TelegramUserStateEnum;
import com.taxivolna.volnaclientbot.servise.BonusService;
import com.taxivolna.volnaclientbot.servise.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.taxivolna.volnaclientbot.model.Button.K_ENTER_PROMO;
import static com.taxivolna.volnaclientbot.model.TelegramUserStateEnum.*;

@Service
//@Scope("prototype")
public class MessageBuilder {
    private final static Logger logger = LoggerFactory.getLogger(MessageBuilder.class);
    private final static String OUT_DEFAULT_MESSAGE = "Функция находится в разработке.  /start";
    private final static String SELECT_MENU = "Выберите пункт меню";
    private final static String SUPPORT_ANSWER = "Оставьте Ваше обращение\n" + "Если Вы участник акции, пришлите фотографию отзыва";


    @Value("${telegram.bot.support.chat}")
    private Long supportChat;
    @Autowired
    private BonusService bonusService;
    @Autowired
    private UserService userService;
    @Autowired
    private About about;
    @Autowired
    private Contact contact;

    @Autowired
    private SupportServiceImpl supportServiceImpl;

    @Autowired
    private TelegramBot bot;


    /**
     *
     */

    // Должны получить еще id статуса
    public SendMessage getSendMessageWithReplyKeyboard(Update update) {
        String MESSAGE = "";
        Long userId = update.message().chat().id();

        TelegramUser USER = userService.findUserOrCreate(update);
        TelegramUserStateEnum USER_STATE = USER.getUserState().getName();
        SendMessage sendMessage = new SendMessage(userId, OUT_DEFAULT_MESSAGE);

        logger.debug("USER: " + USER);

        if (update.message().text() != null) {
            MESSAGE = update.message().text();
            logger.debug("MESSAGE: " + MESSAGE + ", " + "getSendMessage USER: " + USER);
        }

        // Координаты
        if (update.message().location() != null) {
            logger.info(update.message().location().toString());
            String answer = "r Location " + update.message().location().toString();
            return new SendMessage(userId, answer)
                    .replyMarkup(getMenuKeyboard(USER));
            /*TODO написать логику userState*/

        }

        // /start
        if (MESSAGE.startsWith("/start")) {
            String answer = "Добро пожаловать!";
            USER = userService.updateState(USER, MAIN_MENU);
            return new SendMessage(userId, answer)
                    .replyMarkup(getMenuKeyboard(USER));
        }

        //region About
        //О нас
        if (MESSAGE.startsWith(Button.K_ABOUT_AS)) {
            return about.getAbout(userId);
        }

        //endregion

        //region Promo
        //Основное меню, кнопка промокод и нужен номер клиента
        if (USER_STATE.equals(MAIN_MENU) && MESSAGE.startsWith(K_ENTER_PROMO) && USER.getPhone() == null) {
            String answer = "Нажмите кнопку - " + Button.K_ASK_PHONE;
            USER = userService.updateState(USER, PROMO_ASK_PHONE);
            return new SendMessage(userId, answer)
                    .replyMarkup(getMenuKeyboard(USER));
        }

        //Основное меню, кнопка промокод и номер уже есть
        if (MESSAGE.startsWith(K_ENTER_PROMO) && USER.getPhone() != null) {
            String answer = "Введите Ваш промокод ";
            USER = userService.updateState(USER, PROMO_ENTER_CODE);
            return new SendMessage(userId, answer)
                    .replyMarkup(getMenuKeyboard(USER));
        }

        // Контакт
        if (USER_STATE.equals(PROMO_ASK_PHONE) && update.message().contact() != null) {
            USER = contact.saveContact(update);
            USER = userService.updateState(USER, PROMO_ENTER_CODE);
            return contact.getSendMessageAnswerEnterPromo(USER);
        }


        // Ввод промокода, кнопка назад
        if ((USER_STATE.equals(PROMO_ENTER_CODE) || USER_STATE.equals(PROMO_ASK_PHONE)) && MESSAGE.startsWith(Button.K_BACK)) {
            return goToMainMenu(USER);
        }

        // Ввод промокода, после того как поделились номером
        if (USER_STATE.equals(PROMO_ENTER_CODE)) {
            String answer = bonusService.activatePromo(MESSAGE.toUpperCase(), USER.getPhone());
            USER = userService.updateState(USER, MAIN_MENU);
            return new SendMessage(userId, answer)
                    .replyMarkup(getMenuKeyboard(USER));

        }
        //endregion Promo

        //region New order

        // Новый заказ
        if (USER_STATE.equals(MAIN_MENU) && MESSAGE.startsWith(Button.K_NEW_ORDER)) {
            return new SendMessage(userId, OUT_DEFAULT_MESSAGE)
                    .replyMarkup(getMenuKeyboard(USER));
        }

        //endregion New order

        //region Main Menu

        //Основное меню, кнопка далее
        if (USER_STATE.equals(MAIN_MENU) && MESSAGE.startsWith(Button.K_NEXT)) {
            USER = userService.updateState(USER, MAIN_MENU_2);
            return new SendMessage(userId, SELECT_MENU)
                    .replyMarkup(getMenuKeyboard(USER));
        }

        //Основное меню, кнопка назад
        if (USER_STATE.equals(MAIN_MENU_2) && MESSAGE.startsWith(Button.K_BACK)) {
            return goToMainMenu(USER);
        }

        //endregion Main Menu

        //region Support


        //Нет телефона
        if (MESSAGE.startsWith(Button.K_SUPPORT) && USER.getPhone() == null) {
            USER = userService.updateState(USER, SUPPORT_ASK_PHONE);
            return new SendMessage(userId, SUPPORT_ANSWER)
                    .replyMarkup(getMenuKeyboard(USER));
        }

        // нажали поделиться телефоном
        if (USER_STATE.equals(SUPPORT_ASK_PHONE) && update.message().contact() != null) {
            USER = contact.saveContact(update);
            USER = userService.updateState(USER, SUPPORT);
            return new SendMessage(userId, "Спасибо!")
                    .replyMarkup(getMenuKeyboard(USER));
        }
        //Есть телефон
        if (MESSAGE.startsWith(Button.K_SUPPORT) && USER.getPhone() != null) {
            USER = userService.updateState(USER, SUPPORT);
            return new SendMessage(userId, SUPPORT_ANSWER)
                    .replyMarkup(getMenuKeyboard(USER));
        }
        // нажата кнопка назад
        if ((USER_STATE.equals(SUPPORT) || USER_STATE.equals(SUPPORT_ASK_PHONE)) && MESSAGE.startsWith(Button.K_BACK)) {
            return goToMainMenu(USER);
        }
        if (USER_STATE.equals(SUPPORT) || USER_STATE.equals(SUPPORT_ASK_PHONE)) {
            logger.debug("Зашли в метод формирования сообщения в поддержку");
            return supportServiceImpl.sendToSupport(update, USER);
            //todo подумать о дефолтном ответе пользователю, с графиком работы отдела ТП
        }
        // Отвечает сотрудник чата поддержки
        if (supportServiceImpl.isSupportChat(update)) {
            return supportServiceImpl.sendToUser(update);
        }

        //endregion Support
        // Контакт
        if (update.message().contact() != null) {
            TelegramUser telegramUser = contact.saveContact(update);
            return new SendMessage(userId, contact.getAnswer(telegramUser));
        }

        return sendMessage;
    }


    public SendMessage goToMainMenu(TelegramUser user) {
        user = userService.updateState(user, MAIN_MENU);
        return new SendMessage(user.getUserId(), SELECT_MENU)
                .replyMarkup(getMenuKeyboard(user));
    }

    private Keyboard getMenuKeyboard(TelegramUser user) {
        MenuKeyboard menuKeyboard = new MenuKeyboard();
        return menuKeyboard.getReplyKeyboardMenu(user.getUserState());
    }

    public SendMessage getSendMessageWithInlineKeyboard(Update update) {
        return null;
    }
}
