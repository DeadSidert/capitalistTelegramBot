package com.capitalist.telegramBot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "usr")
public class User {

    @Id
    private int userId;
    private Integer oilCoin;
    private Integer gold;
    private Integer eCoin;
    private Integer eCrypt;
    private Integer roubles;
    private Integer companyId;

    @Column(name = "references_url")
    private String referencesUrl;
    private String role;
    private String positions;

    @Column(name = "balls_1")
    private double ballsOne;

    @Column(name = "balls_2")
    private double ballsTwo;

    public User(int userId) {
        this.userId = userId;
        this.oilCoin = 0;
        this.gold = 0;
        this.eCoin = 0;
        this.eCrypt = 0;
        this.roubles = 0;
        this.referencesUrl = "";
        this.role = "user";
        this.positions = "back";
        this.ballsOne = 0.000;
        this.ballsTwo = 0.000;
        this.companyId = 0;
    }
}
