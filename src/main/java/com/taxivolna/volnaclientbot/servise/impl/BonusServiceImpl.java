package com.taxivolna.volnaclientbot.servise.impl;

import com.taxivolna.volnaclientbot.model.Bonus;
import com.taxivolna.volnaclientbot.model.BonusLog;
import com.taxivolna.volnaclientbot.repository.BonusLogRepository;
import com.taxivolna.volnaclientbot.repository.BonusRepository;
import com.taxivolna.volnaclientbot.servise.BonusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class BonusServiceImpl implements BonusService {

    private final static Logger logger = LoggerFactory.getLogger(BonusServiceImpl.class);

    private final BonusLogRepository bonusLogRepository;
    private final BonusRepository bonusRepository;

    public BonusServiceImpl(BonusLogRepository bonusLogRepository, BonusRepository bonusRepository) {
        this.bonusLogRepository = bonusLogRepository;
        this.bonusRepository = bonusRepository;
    }


    @Override
    public String activatePromo(String promo, String client) {
        Optional<Bonus> actualPromo = getActualPromo(promo);
        if(actualPromo.isEmpty()){
            return "Не удалось активировать промокод. Промокод не существует или устарел";
        }
        if(isAlreadyActivated(promo, client)){
            return "Промокод уже был активирован на этом номере ранее.";
        }
        BonusLog bonus = new BonusLog();
        bonus.setClient(client);
        bonus.setNamePromo(promo);
        bonusLogRepository.save(bonus);
        return "Промокод: " + promo + ", "
                + actualPromo.get().getAmount().toBigInteger()
                + " бонусов успешно активирован";


    }

    @Override
    public Optional<Bonus> getActualPromo(String promo){
        return bonusRepository.getBonusByNameAndEndDateAfter(promo, OffsetDateTime.now());
    }

    @Override
    public boolean isAlreadyActivated(String promo, String client) {
        return bonusLogRepository.findFirstByNamePromoAndAndClient(promo, client).isPresent();
    }
}
