package com.capitalist.telegramBot.bot;

import com.capitalist.telegramBot.bot.Handler.CallbackHandler;
import com.capitalist.telegramBot.bot.Handler.StartHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@Slf4j
@ComponentScan(basePackages = "application.yaml")
public class UpdateReceiver {

    private final StartHandler startHandler;
    private final CallbackHandler callbackHandler;

    @Autowired
    public UpdateReceiver(StartHandler startHandler, CallbackHandler callbackHandler) {
        this.startHandler = startHandler;
        this.callbackHandler = callbackHandler;
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



}
