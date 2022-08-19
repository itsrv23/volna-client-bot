package com.taxivolna.volnaclientbot.crm.menu;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.taxivolna.volnaclientbot.crm.MenuKeyboard;
import com.taxivolna.volnaclientbot.model.TelegramUser;
import com.taxivolna.volnaclientbot.servise.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.taxivolna.volnaclientbot.model.TelegramUserStateEnum.PROMO_ENTER_CODE;

@Service
public class Contact {
    private final Logger logger = LoggerFactory.getLogger(Contact.class);
    private final MenuKeyboard menuKeyboard;
    private final UserService userService;

    public Contact(MenuKeyboard menuKeyboard, UserService userService) {
        this.menuKeyboard = menuKeyboard;
        this.userService = userService;
    }

    public TelegramUser saveContact(Update update){
        return userService.savePhone(update);
    }

    public String getAnswer(TelegramUser user){
        return "Спасибо! Ваш номер " + userService.convertPhoneLocalFormat(user.getPhone());

    }

    public SendMessage getSendMessageAnswerEnterPromo(TelegramUser user){
        logger.info("Save phone number: " + user.toString());
        String answer = "Спасибо! Ваш номер " + userService.convertPhoneLocalFormat(user.getPhone()) + ". Введите Ваш промокод ";
        return new SendMessage(user.getUserId(), answer)
                .replyMarkup(menuKeyboard.getReplyKeyboardMenu(userService.getUserState(PROMO_ENTER_CODE)));

    }


}
