package com.capitalist.telegramBot.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@ComponentScan(basePackages = "application.yaml")
public class PaymentHandler {

    @Value("${bot.token}")
    private String token;

    public SendInvoice payment(Update update){
        int userId = update.getMessage().getFrom().getId();
        String textSum = update.getCallbackQuery().getData().split("_")[1]; // после _ будет сумма
        String type = update.getCallbackQuery().getData().split("_")[0]; // здесь тип валюты
        int sum = 0;
        String name = "";

        if ("/buyOilCoinImpl".equalsIgnoreCase(type)){
            name = "\uD83C\uDF11 OilCoin";
        }
        else if ("/buyECoinCoinImpl".equalsIgnoreCase(type)){
            name = "\uD83C\uDF15 ECoin";
        }

        try {
            sum = Integer.parseInt(textSum);
        } catch (Exception e){
            log.error("Ошибка перевода суммы в int в PaymentHandler");
        }
        LabeledPrice price = new LabeledPrice();
        List<LabeledPrice> labeledPriceList = new ArrayList<>();
        price.setAmount(sum);
        price.setLabel("\uD83D\uDCB8Покупка " + name);
        labeledPriceList.add(price);

        SendInvoice sendInvoice = new SendInvoice();
        sendInvoice.setChatId(userId);
        sendInvoice.setCurrency("RUB");
        sendInvoice.setProviderToken(token);
        sendInvoice.setPrices(labeledPriceList);
        sendInvoice.setDescription("Покупка валюты");
        sendInvoice.setIsFlexible(false);
        sendInvoice.setStartParameter("capitalist-bot-fry");
        sendInvoice.setPayload("capitalistInvoice_" + userId + "_price_" + sum);
        return sendInvoice;
    }
}
