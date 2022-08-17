package com.taxivolna.volnaclientbot.repository;

import com.taxivolna.volnaclientbot.model.TelegramAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramDefaultAnswerRepository extends JpaRepository<TelegramAnswer, Long> {
}
