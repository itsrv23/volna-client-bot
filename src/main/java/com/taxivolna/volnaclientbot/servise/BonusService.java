package com.taxivolna.volnaclientbot.servise;

import com.taxivolna.volnaclientbot.model.Bonus;

import java.util.Optional;

public interface BonusService {
    String activatePromo(String promo, String client);
    Optional<Bonus> getActualPromo(String promo);
    boolean isAlreadyActivated(String promo, String client);
}
