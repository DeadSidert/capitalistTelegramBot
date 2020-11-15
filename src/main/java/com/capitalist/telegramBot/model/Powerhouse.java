package com.capitalist.telegramBot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Powerhouse {

    @Id
    private int powerhouseId;
    private String name;
    private Integer level;

}
