package com.taxivolna.volnaclientbot.repository;

import com.taxivolna.volnaclientbot.model.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Integer> {

    Optional<Bonus> getBonusByNameAndEndDateAfter(String name, OffsetDateTime dateTime);
}
