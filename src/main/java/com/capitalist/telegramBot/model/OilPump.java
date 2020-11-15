package com.capitalist.telegramBot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "oil_pump", schema = "public", catalog = "capitalist_bot")
@Getter
@Setter
public class OilPump {

    @Id
    private int pumpId;
    private String name;
    private Integer level;

}
