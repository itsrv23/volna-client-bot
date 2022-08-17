package com.taxivolna.volnaclientbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.taxivolna.volnaclientbot.repository.TelegramUserStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private MessageBuilder messageBuilder;

    @Autowired
    private TelegramUserStateRepository stateRepository;

    @Autowired
    private TelegramBot bot;

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            //На основании статуса пользователя, будем давать ответ и клавиатуру
            try {
                if (update.callbackQuery() != null) {
                    logger.info("!!!!!!!update.callbackQuery().data() = " + update.callbackQuery().data());
                }
                SendMessage sendMessage = messageBuilder.getSendMessageWithReplyKeyboard(update);
                SendResponse execute = bot.execute(sendMessage);
                logger.info("execute result: " + execute);
            } catch (Exception exception) {
                logger.info("exception: {}", exception.getMessage());
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
