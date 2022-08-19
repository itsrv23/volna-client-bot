package com.taxivolna.volnaclientbot.crm.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.taxivolna.volnaclientbot.model.TelegramUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SupportServiceImpl implements com.taxivolna.volnaclientbot.crm.service.SupportService {
    @Autowired
    private TelegramBot bot;
    @Value("${telegram.bot.support.chat}")
    private Long supportChat;

    private final Logger logger = LoggerFactory.getLogger(SupportServiceImpl.class);

    public SendMessage sendToSupport(Update update, TelegramUser user) {
        ForwardMessage forwardMessage = new ForwardMessage(supportChat, update.message().chat().id(), update.message().messageId());
        StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(update.message().chat().id()).append(",\n");
        if (user.getPhone() != null) {
            sb.append("phone: ").append(user.getPhone()).append(",\n");
        }
        if (update.message().text() != null) {
            sb.append("message: ").append(update.message().text());
        }
        SendMessage message = new SendMessage(supportChat, sb.toString());

        if (update.message().text() == null) {
            SendResponse execute = bot.execute(forwardMessage);
            logger.info("Forward message response: {}", execute);
        }
        return message;
    }

    public SendMessage sendToUser(Update update) {
        if (update.message().replyToMessage().forwardFrom() == null) {
            logger.debug("Пользователь скрыл свой аккаунт от пересылки, из за вот этого получается весь геморрой");
            if (update.message().replyToMessage().text() != null && update.message().text() != null) {
                logger.debug("replyToMessage text:  {}", update.message().replyToMessage().text());
                logger.debug("text: {}", update.message().text());
                Long id = parseUserId(update.message().replyToMessage().text());
                if (id != 0) {
                    return new SendMessage(id, update.message().text());
                }
            }
        } else {
            Long id = update.message().replyToMessage().forwardFrom().id();
            logger.debug("update.message.replyToMessage.forwardFrom.id:  {}", id);
            return new SendMessage(id, update.message().text());
        }
        return new SendMessage(supportChat, "Не удалось отправить сообщение. Пользователь скрыл свои данные.\n" +
                "Ответьте на сообщение с id  пользователя");
    }

    public boolean isSupportChat(Update update) {
        if (update.message().chat().id() != null) {
            return update.message().chat().id().equals(supportChat);
        }
        return false;
    }

    public Long parseUserId(String msg) {
        // id: 1226737000,
        Pattern pattern = Pattern.compile("id: (\\d*),");
        Matcher matcher = pattern.matcher(msg);
        if (!matcher.find()) {
            return null;
        }
        return Long.valueOf(matcher.group(1));
    }
}
