package com.capitalist.telegramBot.bot;

import com.capitalist.telegramBot.bot.Handler.CallbackHandler;
import com.capitalist.telegramBot.bot.Handler.StartHandler;
import com.capitalist.telegramBot.gameEntities.Bank;
import com.capitalist.telegramBot.gameEntities.Market;
import com.capitalist.telegramBot.gameEntities.MyCompany;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@Slf4j
@ComponentScan(basePackages = "application.yaml")
public class UpdateReceiver {

    private final StartHandler startHandler;
    private final CallbackHandler callbackHandler;
    private final Bank bank;
    private final MyCompany myCompany;
    private final Market market;

    @Autowired
    public UpdateReceiver(StartHandler startHandler, CallbackHandler callbackHandler, Bank bank, MyCompany myCompany, Market market) {
        this.startHandler = startHandler;
        this.callbackHandler = callbackHandler;
        this.bank = bank;
        this.myCompany = myCompany;
        this.market = market;
    }


    public SendMessage start(Update update){
        return startHandler.start(update);
    }

    public SendMessage startTwo(Update update){
        return startHandler.stepTwo(update);
    }

    public SendMessage startThree(Update update){
        return startHandler.stepThree(update);
    }

    public SendMessage startFour(Update update){
        return startHandler.stepFour(update);
    }

    public SendMessage startFive(Update update){
        return startHandler.stepFive(update);
    }

    public SendMessage startSix(Update update){
        return startHandler.stepSix(update);
    }

    public SendMessage endTrain(Update update){
        return startHandler.endTrain(update);
    }

    public SendMessage endTrainMenu(Update update){
        return startHandler.endTrainMenu(update);
    }

    public SendMessage actionAndRef(Update update){
        return callbackHandler.actionAndRef(update);
    }

    public SendMessage chat(Update update){
        return callbackHandler.chat(update);
    }

    public SendMessage advert(Update update){
        return callbackHandler.advert(update);
    }

    public SendMessage pay(Update update){
        return bank.pay(update);
    }

    public SendMessage mainCompany(Update update){
        return myCompany.main(update);
    }

    public SendMessage mainMarket(Update update){
        return market.mark(update);
    }

    public boolean marketEnough(int userId, String type){
        return type.equalsIgnoreCase("oil") ? market.enoughOil(userId) : market.enoughElectric(userId);
    }

    public AnswerCallbackQuery alertOil(Update update){
        return callbackHandler.creatAlertCallbackOil(update);
    }

    public AnswerCallbackQuery alertElectric(Update update){
        return callbackHandler.creatAlertCallbackElectric(update);
    }



}
