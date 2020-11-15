package com.capitalist.telegramBot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Setter
@Getter
public class Actions {

    @Id
    private int actionsId;
    private String nameCompany;

}
