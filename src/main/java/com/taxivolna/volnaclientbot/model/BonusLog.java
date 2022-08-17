package com.taxivolna.volnaclientbot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "a_samael_bonus_logs")
public class BonusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String namePromo;
    @Column(name = "clientid")
    private String client;
    @Column(name = "result")
    private String result;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamePromo() {
        return namePromo;
    }

    public void setNamePromo(String namePromo) {
        this.namePromo = namePromo;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BonusLog bonusLog = (BonusLog) o;
        return Objects.equals(id, bonusLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BonusLog{" +
                "id=" + id +
                ", namePromo='" + namePromo + '\'' +
                ", client='" + client + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
