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
    private int userPercent;
    private int electricProduct;
    private int oilProduct;

    public Company() {
        this.oil = 0;
        this.electric = 0;
        this.name = "Без названия";
        this.userPercent = 70;
        this.electricProduct = 0;
        this.oilProduct = 0;
    }
}
