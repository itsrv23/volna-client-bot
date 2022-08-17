package com.taxivolna.volnaclientbot.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity(name = "a_samael_telegram_client_users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "phone")
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_state_id")
    private TelegramUserState userState;

    @Column(name = "dt_create")
    private OffsetDateTime dtCreate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TelegramUser that = (TelegramUser) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
