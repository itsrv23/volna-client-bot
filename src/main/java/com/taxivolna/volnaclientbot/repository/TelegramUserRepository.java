package com.taxivolna.volnaclientbot.repository;

import com.taxivolna.volnaclientbot.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    @Query(value = "select * from a_samael_telegram_client_users where user_id =?1 limit 1", nativeQuery = true)
    Optional<TelegramUser> findByUserId(Long id);
}
