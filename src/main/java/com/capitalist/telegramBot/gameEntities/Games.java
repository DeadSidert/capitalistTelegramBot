package com.capitalist.telegramBot.gameEntities;

import com.capitalist.telegramBot.bot.builder.MessageBuilder;
import com.capitalist.telegramBot.model.User;
import com.capitalist.telegramBot.service.CompanyService;
import com.capitalist.telegramBot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class Games {

    private final UserService userService;
    private final CompanyService companyService;

    public Games(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    public SendMessage dailyBonus(Update update){
        int userId = update.getMessage().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        User user = userService.getOrCreate(userId);

        if(user.getDailyBonus()){
            return messageBuilder
                    .line("Вы уже получили сегодня \uD83C\uDF81 Ежедневный бонус")
                    .build();
        }
        int oilCoin = 5 + (int)(Math.random()*20);
        int eCoin = 3 + (int)(Math.random()*10);

        user.setDailyBonus(true);
        user.setOilCoin(user.getOilCoin() + oilCoin);
        user.setECoin(user.getECoin() + eCoin);
        userService.update(user);

        return messageBuilder
                .line("\uD83C\uDF81 Ежедневный бонус\n" +
                        "   \n" +
                        "➕ Вам зачислен бонус:\n" +
                        oilCoin + " \uD83C\uDF11 OilCoin\n" +
                        eCoin + " \uD83C\uDF15 ECoin")
                .build();

    }
}
