package com.taxivolna.volnaclientbot.crm.menu;

import com.pengrad.telegrambot.request.SendMessage;
import com.taxivolna.volnaclientbot.crm.MenuKeyboard;
import com.taxivolna.volnaclientbot.model.Button;
import org.springframework.stereotype.Service;

@Service
public class About {

    private final MenuKeyboard menuKeyboard;

    public About(MenuKeyboard menuKeyboard) {
        this.menuKeyboard = menuKeyboard;
    }

    public SendMessage getAbout(long userId){
        String answer = "Операторы всегда на связи:\n" +
                "\n" +
                "В Алуште: +7 (978) 108-08-08\n" +
                "В Севастополе: +7 (978) 727-07-27\n" +
                "В Ялте: +7 (978) 055-05-05\n" +
                "\n" +
                "@nikiv70 - по вопросам работоспособности и новому функционалу";
        return new SendMessage(userId, answer)
                .replyMarkup(menuKeyboard.getInlineKeyboardMenu(Button.K_ABOUT_AS));
    }
}
