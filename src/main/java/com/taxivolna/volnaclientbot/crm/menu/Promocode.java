package com.taxivolna.volnaclientbot.crm.menu;

import com.taxivolna.volnaclientbot.crm.MenuKeyboard;
import com.taxivolna.volnaclientbot.servise.UserService;
import org.springframework.stereotype.Service;

@Service
public class Promocode {

    private final MenuKeyboard menuKeyboard;
    private final UserService userService;

    public Promocode(MenuKeyboard menuKeyboard, UserService userService) {
        this.menuKeyboard = menuKeyboard;
        this.userService = userService;
    }


}
