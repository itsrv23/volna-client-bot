package com.taxivolna.volnaclientbot.model;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "a_samael_telegram_client_answers")
@Data
public class TelegramAnswer {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "answer")
    private String answer;


}
