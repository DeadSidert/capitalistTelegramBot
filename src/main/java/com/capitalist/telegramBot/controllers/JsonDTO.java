package com.capitalist.telegramBot.controllers;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class JsonDTO {

    int MERCHANT_ID;	//ID Вашего магазина
    int AMOUNT; // Сумма заказа
    int intid;	// Номер операции Free-Kassa
    int MERCHANT_ORDER_ID;	//Ваш номер заказа -- в моем случае userId
    String P_EMAIL;	// Email плательщика
    String P_PHONE;	// Телефон плательщика (если указан)
    int CUR_ID;	// ID электронной валюты, который был оплачен заказ (список валют)
    String SIGN; // Подпись (методика формирования подписи в данных оповещения)
    String us_type;	// Дополнительные параметры с префиксом us_, переданные в форму оплаты(тут тип валюты)

    @Override
    public String toString() {
        return "JsonDTO{" +
                "MERCHANT_ID=" + MERCHANT_ID +
                ", AMOUNT=" + AMOUNT +
                ", intid=" + intid +
                ", MERCHANT_ORDER_ID=" + MERCHANT_ORDER_ID +
                ", P_EMAIL='" + P_EMAIL + '\'' +
                ", P_PHONE='" + P_PHONE + '\'' +
                ", CUR_ID=" + CUR_ID +
                ", SIGN='" + SIGN + '\'' +
                ", us_type='" + us_type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonDTO jsonDTO = (JsonDTO) o;
        return MERCHANT_ID == jsonDTO.MERCHANT_ID &&
                AMOUNT == jsonDTO.AMOUNT &&
                intid == jsonDTO.intid &&
                MERCHANT_ORDER_ID == jsonDTO.MERCHANT_ORDER_ID &&
                CUR_ID == jsonDTO.CUR_ID &&
                Objects.equals(P_EMAIL, jsonDTO.P_EMAIL) &&
                Objects.equals(P_PHONE, jsonDTO.P_PHONE) &&
                Objects.equals(SIGN, jsonDTO.SIGN) &&
                Objects.equals(us_type, jsonDTO.us_type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(MERCHANT_ID, AMOUNT, intid, MERCHANT_ORDER_ID, P_EMAIL, P_PHONE, CUR_ID, SIGN, us_type);
    }
}
