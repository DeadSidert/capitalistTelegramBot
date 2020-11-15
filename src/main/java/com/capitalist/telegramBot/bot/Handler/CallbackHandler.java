package com.capitalist.telegramBot.bot.Handler;

import com.capitalist.telegramBot.bot.builder.MessageBuilder;
import com.capitalist.telegramBot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@ComponentScan(basePackages = "application.yaml")
public class CallbackHandler {

    private UserService userService;

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.chat}")
    private String chatUrl;

    @Value("${bot.urlPayments}")
    private String urlPayments;

    @Value("${bot.urlStat}")
    private String urlStat;

    @Value("${bot.adminUsername}")
    private String adminUsername;

    @Autowired
    public CallbackHandler(UserService userService) {
        this.userService = userService;
    }

    public CallbackHandler() {
    }

    public SendMessage actionAndRef(Update update){
        int userId = update.getCallbackQuery().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        messageBuilder
                .line()
                .line("Акционер \uD83E\uDD1D Реферал\n" +
                        "\n" +
                        "Дорогой Друг!\n" +
                        "\n" +
                        "Если Вы являетесь Акционером, то в разделе \uD83C\uDFEB Биржа - \uD83D\uDCD1 " +
                        "Мои акции Вы можете оставить сообщение для своих Рефералов, в котором можете указать свои контактные данные для более тесного сотрудничества с ними.\n" +
                        "\n" +
                        "Если же Вы являетесь Рефералом, то загляните в раздел \uD83C\uDFEB Биржа -" +
                        " \uD83D\uDCBC Мой акционер, возможно Ваш Акционер оставил там для Вас сообщение и хочет помочь Вам в развитии \uD83C\uDFED Вашей компании.");

        return messageBuilder.build();
    }

    public SendMessage advert(Update update){
        int userId = update.getCallbackQuery().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        messageBuilder
                .line()
                .line("\uD83D\uDCC6 РЕКЛАМА В БОТЕ \n" +
                        "\n" +
                        "Приветствуем тебя, дорогой друг \uD83D\uDC4B !!!!\n" +
                        "\n" +
                        "По всем вопросам касательно размещения рекламы в нашем боте обращайтесь:\n" +
                        "\uD83D\uDCE9 @"+ adminUsername+" \uD83D\uDCE9\n" +
                        "\n" +
                        "\uD83D\uDCCA Статистика проекта: \n" +
                        "\n" +
                        urlStat+"\n" +
                        "\n" +
                        "\uD83D\uDCDE Чат проекта:\n" +
                        "\n" +
                        "@"+ chatUrl + "\n" +
                        " \n" +
                        "\uD83D\uDCB5 Выплаты проекта:\n" +
                        "\n" +
                        urlPayments+"\n" +
                        " \n" +
                        "\uD83D\uDCCA По рекламе: \uD83D\uDCCA\n" +
                        "\n" +
                        "@" + adminUsername);

        return messageBuilder.build();
    }

    public SendMessage chat(Update update){
        int userId = update.getCallbackQuery().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        messageBuilder
                .line()
                .line("\uD83D\uDCAC Игровой ЧАТ\n" +
                        "\n" +
                        "Присоединяйтесь к официальному чату игрового сообщества CapitalistGame, чтобы получить помощь и консультацию, обсудить происходящее в игре, а так же чтобы найти соратников\uD83D\uDC65 и компаньонов\uD83E\uDD1D:  \n" +
                        "\uD83D\uDCAC @"+ chatUrl +" \uD83D\uDCAC\n" +
                        "\n" +
                        "\uD83C\uDF81 Если Вы ранее не вступали в чат, Вы получите награду.");

        return messageBuilder.build();
    }
}
