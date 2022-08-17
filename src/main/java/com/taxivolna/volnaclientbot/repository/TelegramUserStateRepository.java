package com.taxivolna.volnaclientbot.repository;

import com.taxivolna.volnaclientbot.model.TelegramUserState;
import com.taxivolna.volnaclientbot.model.TelegramUserStateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramUserStateRepository extends JpaRepository<TelegramUserState, Long> {
    Optional<TelegramUserState> findFirstByName(TelegramUserStateEnum name);
}
