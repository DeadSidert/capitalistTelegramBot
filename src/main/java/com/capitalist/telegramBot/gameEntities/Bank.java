package com.capitalist.telegramBot.gameEntities;

import com.capitalist.telegramBot.bot.builder.MessageBuilder;
import com.capitalist.telegramBot.model.User;
import com.capitalist.telegramBot.service.UserService;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class Bank {

    private final UserService userService;

    @Autowired
    public Bank(UserService userService) {
        this.userService = userService;
    }

    public SendMessage pay(Update update){
        int userId = update.getMessage().getFrom().getId();
        User user = userService.getOrCreate(userId);

        int gold = user.getGold();
        int oilCoin = user.getOilCoin();
        int eCoin = user.getECoin();
        int eCrypt = user.getECrypt();
        double ballsOne = Precision.round(user.getBallsOne(), 4);
        double ballsTwo = Precision.round(user.getBallsTwo(), 4);

        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        messageBuilder
                .line()
                .line("\uD83C\uDFE6 Банк\n" +
                        " \n" +
                        "Добро пожаловать в банк!\n" +
                        "Здесь Вы можете купить, обменять и вывести валюту.\n" +
                        "\n" +
                        "На вашем счету:\n" +
                        oilCoin+ " \uD83C\uDF11 OilCoin\n" +
                        eCoin +" \uD83C\uDF15 ECoin\n" +
                        gold +" \uD83D\uDCB0 Gold\n" +
                        eCrypt +" ⚡️ ECrypt\n" +
                        ballsOne +" \uD83D\uDD37 Баллы\n" +
                        ballsTwo +" \uD83D\uDD36 Баллы")
                .row()
                .button("\uD83D\uDCB8 Пополнение баланса(\uD83D\uDD36 \uD83D\uDD37 \uD83C\uDF15 \uD83C\uDF11)", "/payment")
                .row()
                .button("\uD83D\uDCB1 Обмен валют", "/changeMoney")
                .row()
                .button("\uD83D\uDCE4 Вывод баланса", "/output");

        return messageBuilder.build();
    }
}
