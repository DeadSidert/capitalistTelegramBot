package com.capitalist.telegramBot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int companyId;
    private int oil;
    private int electric;
    private String name;

    public Company() {
        this.oil = 0;
        this.electric = 0;
        this.name = "Без названия";
    }
}
