package com.taxivolna.volnaclientbot.repository;

import com.taxivolna.volnaclientbot.model.BonusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BonusLogRepository extends JpaRepository<BonusLog, Integer> {
    Optional<BonusLog> findFirstByNamePromoAndAndClient(String promo,String client);
}
