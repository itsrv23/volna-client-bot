package com.taxivolna.volnaclientbot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "a_samael_telegram_client_user_state")
public class TelegramUserState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private TelegramUserStateEnum name;

    @Column(name = "comment")
    private String comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TelegramUserStateEnum getName() {
        return name;
    }

    public void setName(TelegramUserStateEnum name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelegramUserState that = (TelegramUserState) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TelegramUserState{" +
                "id=" + id +
                ", name=" + name +
                ", comment='" + comment + '\'' +
                '}';
    }
}