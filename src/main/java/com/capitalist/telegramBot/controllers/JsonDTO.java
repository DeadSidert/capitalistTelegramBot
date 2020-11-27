package com.capitalist.telegramBot.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class JsonDTO {

    int MERCHANT_ID;	//ID Вашего магазина
    int AMOUNT; // Сумма заказа
    int intid;	// Номер операции Free-Kassa
    int MERCHANT_ORDER_ID;	//Ваш номер заказа
    String P_EMAIL;	// Email плательщика
    String P_PHONE;	// Телефон плательщика (если указан)
    int CUR_ID;	// ID электронной валюты, который был оплачен заказ (список валют)
    String SIGN;	// Подпись (методика формирования подписи в данных оповещения)
    String us_type;	// Дополнительные параметры с префиксом us_, переданные в форму оплаты(тут тип валюты)
}
