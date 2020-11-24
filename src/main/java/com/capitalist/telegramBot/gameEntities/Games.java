package com.capitalist.telegramBot.gameEntities;

import com.capitalist.telegramBot.bot.builder.MessageBuilder;
import com.capitalist.telegramBot.model.Channel;
import com.capitalist.telegramBot.model.User;
import com.capitalist.telegramBot.service.ChannelService;
import com.capitalist.telegramBot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Games {

    private final UserService userService;
    private final ChannelService channelService;

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
        List<Channel> channelList = channelService.findAll();

        if (user.isJoined() || channelList.isEmpty()){
            return messageBuilder
                    .line("\uD83C\uDF81 Ежедневный бонус\n" +
                            "   \n" +
                            "➕ Вам зачислен бонус:\n" +
                            oilCoin + " \uD83C\uDF11 OilCoin\n" +
                            eCoin + " \uD83C\uDF15 ECoin")
                    .build();
        }

        return messageBuilder
                .line("\uD83C\uDF81 Ежедневный бонус\n" +
                        "   \n" +
                        "➕ Вам зачислен бонус:\n" +
                        oilCoin + " \uD83C\uDF11 OilCoin\n" +
                        eCoin + " \uD83C\uDF15 ECoin")
                .row()
                .button("Дополнительный бонус Х20 \uD83D\uDD25", "/getBonusForJoin")
                .build();
    }

    public SendMessage getBonusForJoin(Update update){
        int userId = update.getCallbackQuery().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));

        List<Channel> channelList = channelService.findAll();
        messageBuilder.line("Вам нужно подписаться на каналы снизы:\n");

        channelList.forEach(e ->{
            messageBuilder.row().buttonWithUrl("Подписаться", "....", e.getUrl());
        });

        messageBuilder.row().button("\uD83D\uDCCE Проверить", "/checkChannel");

        return messageBuilder.build();
    }
}
