package com.taxivolna.volnaclientbot.servise.impl;

import com.pengrad.telegrambot.model.Update;
import com.taxivolna.volnaclientbot.model.TelegramUserStateEnum;
import com.taxivolna.volnaclientbot.model.TelegramUser;
import com.taxivolna.volnaclientbot.model.TelegramUserState;
import com.taxivolna.volnaclientbot.repository.TelegramUserRepository;
import com.taxivolna.volnaclientbot.repository.TelegramUserStateRepository;
import com.taxivolna.volnaclientbot.servise.UserService;
import com.taxivolna.volnaclientbot.listener.MessageBuilder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.taxivolna.volnaclientbot.model.TelegramUserStateEnum.MAIN_MENU;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TelegramUserRepository userRepository;
    @Autowired
    private TelegramUserStateRepository stateRepository;

    private final static Logger logger = LoggerFactory.getLogger(MessageBuilder.class);


    @Override
    public void updateState(TelegramUser user, TelegramUserStateEnum newState) {
        logger.info("Set user state: " + getUserState(newState));
        user.setUserState(getUserState(newState));
        userRepository.save(user);
    }

    @Override
    public TelegramUser findUser(Update update) {
        return userRepository.findByUserId(getUserId(update)).orElseGet(() -> createUser(update));
    }

    @Override
    public TelegramUser createUser(Update update) {
        TelegramUser user = new TelegramUser();
        user.setUserId(getUserId(update));
        user.setUserState(getUserState(MAIN_MENU));
        logger.info("New user was created: " + user);
        return userRepository.save(user);
    }

    @NotNull
    @Override
    public TelegramUserState getUserState(TelegramUserStateEnum stateEnum) {
        return stateRepository.findFirstByName(stateEnum).orElseGet(() -> createUserState(stateEnum));
    }

    @Override
    public TelegramUserState createUserState(TelegramUserStateEnum stateEnum) {
        TelegramUserState state = new TelegramUserState();
        state.setComment(stateEnum.getName());
        state.setName(stateEnum);
        return stateRepository.save(state);
    }

    @Override
    public TelegramUser savePhone(Update update) {
        TelegramUser user = findUser(update);
        // В теле может прийти null
        if(update.message().contact() != null) {
            user.setPhone(convertPhoneLocalFormat(update.message().contact().phoneNumber()));
        }
        return userRepository.save(user);
    }

    @Override
    public String convertPhoneLocalFormat(String phone) {
        return phone.replaceFirst("^(\\+7|7)","8");
    }

    private Long getUserId(Update update) {
        return update.message().chat().id();
    }
}
