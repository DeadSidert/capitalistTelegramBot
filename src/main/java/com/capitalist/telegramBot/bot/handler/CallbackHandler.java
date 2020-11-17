package com.capitalist.telegramBot.bot.handler;

import com.capitalist.telegramBot.bot.builder.MessageBuilder;
import com.capitalist.telegramBot.gameEntities.Market;
import com.capitalist.telegramBot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@ComponentScan(basePackages = "application.yaml")
public class CallbackHandler {

    private UserService userService;
    private final Market market;
    private List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    private List<InlineKeyboardButton> row = null;
    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

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
    public CallbackHandler(UserService userService, Market market) {
        this.userService = userService;
        this.market = market;
    }

    public CallbackHandler(Market market) {
        this.market = market;
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

    public AnswerCallbackQuery creatAlertCallbackOil(Update update){
        String id = update.getCallbackQuery().getId();
        int oil = market.getUserCompany((update.getCallbackQuery().getFrom().getId())).getOil();
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery
                .setCallbackQueryId(id)
                .setShowAlert(true)
                .setText("\uD83D\uDEAB Минимум для продажи 500 \uD83D\uDEE2 \n" +
                        "баррелей нефти, а у вас только " + oil + "\n" +
                        "баррелей нефти");
        return answerCallbackQuery;
    }

    public AnswerCallbackQuery creatAlertCallbackElectric(Update update){
        String id = update.getCallbackQuery().getId();
        int electric = market.getUserCompany((update.getCallbackQuery().getFrom().getId())).getElectric();
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery
                .setCallbackQueryId(id)
                .setShowAlert(true)
                .setText("\uD83D\uDEAB Минимум для продажи 500 \uD83D\uDD0B" +
                        "килловатт энергии, а у вас только " + electric + "\n" +
                        "килловатт энергии");
        return answerCallbackQuery;
    }

    public AnswerCallbackQuery creatAlertCallbackOilNotMoney(Update update){
        String id = update.getCallbackQuery().getId();
        int userId = update.getCallbackQuery().getFrom().getId();
        int money = userService.getOrCreate(userId).getOilCoin();
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery
                .setCallbackQueryId(id)
                .setShowAlert(true)
                .setText("\uD83D\uDEAB У вас не хватает \uD83C\uDF11 OilCoin для" +
                        "\nпокупки этого насоса! Ваш баланс: " + money +"\n" +
                        "OilCoin");
        return answerCallbackQuery;
    }

    public AnswerCallbackQuery creatAlertCallbackElectricNotMoney(Update update){
        String id = update.getCallbackQuery().getId();
        int userId = update.getCallbackQuery().getFrom().getId();
        int money = userService.getOrCreate(userId).getECoin();
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery
                .setCallbackQueryId(id)
                .setShowAlert(true)
                .setText("\uD83D\uDEAB У вас не хватает \uD83C\uDF15 ECoin для" +
                        "\nпокупки этого насоса! Ваш баланс: " + money +"\n" +
                        "ECoin");
        return answerCallbackQuery;
    }

    public AnswerCallbackQuery creatAlertCallbackReturnTrain(Update update){
        String id = update.getCallbackQuery().getId();
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery
                .setCallbackQueryId(id)
                .setShowAlert(true)
                .setText("Обучение началось!");
        return answerCallbackQuery;
    }

    public SendMessage main(Update update){
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        int userId = update.getCallbackQuery().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        messageBuilder
                .line()
                .line("\uD83D\uDCDC Доска объявлений")
                .row()
                .button("\uD83D\uDCAC Игровой ЧАТ", "/chat")
                .row()
                .button("Акционер \uD83E\uDD1D Реферал", "/action")
                .row()
                .button("\uD83D\uDCC6 РЕКЛАМА В БОТЕ", "/advert");

        return messageBuilder.build();
    }

    public SendMessage mainMenu(Update update){
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        int userId = update.getCallbackQuery().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        messageBuilder
                .line()
                .line("\uD83D\uDCC3 Главное меню");

        createMenuMain();
        return messageBuilder.build().setReplyMarkup(replyKeyboardMarkup);
    }

    public void createMenuMain(){
        ArrayList<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add("\uD83C\uDFED Моя компания");
        keyboardRow.add("\uD83C\uDFEB Биржа");

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("\uD83D\uDED2 Рынок");
        keyboardRow1.add("\uD83C\uDFE6 Банк");

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add("\uD83C\uDF81 Ежедневный бонус");
        keyboardRow2.add("\uD83D\uDCA1 Дополнительно");

        rows.add(keyboardRow);
        rows.add(keyboardRow1);
        rows.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(rows);
    }

}
