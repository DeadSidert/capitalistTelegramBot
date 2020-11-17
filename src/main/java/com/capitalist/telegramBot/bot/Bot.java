package com.capitalist.telegramBot.bot;

import com.capitalist.telegramBot.bot.admin.Admin;
import com.capitalist.telegramBot.bot.builder.MessageBuilder;
import com.capitalist.telegramBot.model.OilPump;
import com.capitalist.telegramBot.model.Powerhouse;
import com.capitalist.telegramBot.model.User;
import com.capitalist.telegramBot.service.OilPumpService;
import com.capitalist.telegramBot.service.PowerhouseService;
import com.capitalist.telegramBot.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
@ComponentScan(basePackages = "application.yaml")
@Slf4j
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    @Getter
    private String token;

    @Value("${bot.name}")
    @Getter
    private String name;

    @Value("${bot.admin}")
    private String botAdmin;

    private final UpdateReceiver updateReceiver;
    private final UserService userService;
    private final OilPumpService oilPumpService;
    private final PowerhouseService powerhouseService;
    private final Admin admin;


    @Override
    public synchronized void onUpdateReceived(Update update) {

        if (isText(update)){
           fabricTextTelegram(update);
        }
        else if (update.hasCallbackQuery()){
            fabricCallbackTelegram(update);
        }
        }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public void fabricTextTelegram(Update update){
        SendMessage sendMessage;
        String command = update.getMessage().getText();
        User user = userService.getOrCreate(update.getMessage().getFrom().getId());

        // проверка позиции
        if (!"back".equalsIgnoreCase(user.getPositions())){
            fabricPosition(update, user.getPositions());
        }

        if (command.split(" ").length > 1){
            String[] c = command.split(" ");
            if (c[0].equalsIgnoreCase("/start") && user.getReferId() == 0){
                user.setReferId(Integer.parseInt(c[1]));
                userService.update(user);
                sendMessage = updateReceiver.start(update);
                executeWithExceptionCheck(sendMessage);
            }
        }

        // старт
        if ("/start".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.start(update);
            executeWithExceptionCheck(sendMessage);
        }
        // доступ к админ панели /admin_bot
        else if("/admin_bot".equalsIgnoreCase(command)){
            if (user.getUserId() == Integer.parseInt(botAdmin) || user.getRole().equalsIgnoreCase("admin")){
              executeWithExceptionCheck(admin.admin(update));
            }
            else{
               sendMessage = MessageBuilder.create(user)
                       .line("Вы не админ!")
                       .build();
               executeWithExceptionCheck(sendMessage);
            }
        }
        // Обучение
        else if ("\uD83D\uDDDE".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.startTwo(update);
            executeWithExceptionCheck(sendMessage);
        }
        // второй шаг обучения
        else if ("train_1".equalsIgnoreCase(user.getPositions()) &&
                "\uD83D\uDCC3".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.startThree(update);
            executeWithExceptionCheck(sendMessage);
        }
        // третий шаг обучения
        else if ("train_2".equalsIgnoreCase(user.getPositions()) &&
                "\uD83D\uDCC4".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.startFour(update);
            executeWithExceptionCheck(sendMessage);
        }
        // четвертый шаг обучения
        else if ("train_3".equalsIgnoreCase(user.getPositions()) &&
                "\uD83D\uDDD2".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.startFive(update);
            executeWithExceptionCheck(sendMessage);
        }
        // пятый шаг обучения(конец)
        else if ("train_4".equalsIgnoreCase(user.getPositions()) &&
                "\uD83C\uDFC1".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.startSix(update);
            executeWithExceptionCheck(sendMessage);

            mainMenu(update);
        }
        // пропустить обучение
        else if ("\uD83D\uDCF0".equalsIgnoreCase(command)){
            mainMenu(update);
        }
        // нажатие на Банк в меню
        else if ("\uD83C\uDFE6 Банк".equalsIgnoreCase(command)){
           sendMessage = updateReceiver.pay(update);
           executeWithExceptionCheck(sendMessage);
        }
        // нажатие на Моя компания в меню
        else if ("\uD83C\uDFED Моя компания".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.mainCompany(update);
            executeWithExceptionCheck(sendMessage);
        }
        // нажатие на Назад в меню
        else if ("⬅️ Назад".equalsIgnoreCase(command) || "✖️ Отмена".equalsIgnoreCase(command)){
            mainMenu(update);
        }
        // нажатие на Рынок в меню
        else if ("\uD83D\uDED2 Рынок".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.mainMarket(update);
            executeWithExceptionCheck(sendMessage);
        }
        // нажатие на Нефтяные насосы в меню
        else if ("⛽️ Нефтяные насосы".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.oilPump(update);
            executeWithExceptionCheck(sendMessage);
        }
        // нажатие на Электростанция в меню
        else if ("\uD83D\uDD0C Электростанции".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.electric(update);
            executeWithExceptionCheck(sendMessage);
        }
        // нажатие на Статистика в меню
        else if ("\uD83D\uDCCA Статистика".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.stat(update);
            executeWithExceptionCheck(sendMessage);
        }
        // нажатие на Рефералы в меню
        else if ("\uD83D\uDC65 Рефералы".equalsIgnoreCase(command)){
            updateReceiver.referals(update).forEach(this::executeWithExceptionCheck);
        }
        // нажатие на Игры в меню
        else if ("\uD83C\uDF81 Ежедневный бонус".equalsIgnoreCase(command)){
             executeWithExceptionCheck(updateReceiver.dailyBonus(update));
        }
        // нажатие на Дополнительно в меню
        else if ("\uD83D\uDCA1 Дополнительно".equalsIgnoreCase(command)){
            executeWithExceptionCheck(updateReceiver.add(update));
        }
        // нажатие на Изменить название в меню
        else if ("\uD83D\uDCDD Название".equalsIgnoreCase(command)){
            executeWithExceptionCheck(updateReceiver.addName(update));
        }
        // помощь
        else if ("❓ Помощь".equalsIgnoreCase(command)){
            executeWithExceptionCheck(updateReceiver.help(update));
        }
        // сообщество
        else if ("\uD83D\uDCAC Сообщество".equalsIgnoreCase(command)){
            executeWithExceptionCheck(updateReceiver.community(update));
        }


    }
    public void fabricCallbackTelegram(Update update){
        CallbackQuery callbackQuery = update.getCallbackQuery();

        // выбор inline меню Акционер и реферал
        if ("/cancel".equalsIgnoreCase(callbackQuery.getData())){
            mainMenuCallback(update);
        }

        // выбор inline меню Акционер и реферал
        if ("/action".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.actionAndRef(update));
        }
        // выбор inline меню Игровой чат
        else if ("/chat".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.chat(update));
        }
        // выбор inline меню Реклама
        else if ("/advert".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.advert(update));
        }
        // продажа нефти на рынке
        else if ("/sell_oil".equalsIgnoreCase(callbackQuery.getData())){
            if (!updateReceiver.marketEnough(update.getCallbackQuery().getFrom().getId(), "oil")){
                    executeAnswerCallback(updateReceiver.alertOil(update));
            }
            else executeWithExceptionCheck(updateReceiver.sellOil(update));
        }
        // продажа энергии на рынке
        else if ("/sell_electric".equalsIgnoreCase(callbackQuery.getData())){
            if (!updateReceiver.marketEnough(update.getCallbackQuery().getFrom().getId(), "electric")){
                executeAnswerCallback(updateReceiver.alertElectric(update));
            }
            else executeWithExceptionCheck(updateReceiver.sellElectric(update));
        }
        // покупка нефтяной установки
        else if ("/buy_oilPump".equalsIgnoreCase(callbackQuery.getData())){
            updateReceiver.buyOilPump(update).forEach(this::executeWithExceptionCheck);

        }
        // покупка электростанции
        else if ("/buy_electric".equalsIgnoreCase(callbackQuery.getData())){
            updateReceiver.buyElectric(update).forEach(this::executeWithExceptionCheck);
        }
        // нажали на inline купить нефтяную установку (убираем последний символ из запроса тк он равен уровню установки)
        else if ("/buy_oilPump".equalsIgnoreCase(callbackQuery.getData().substring(0, callbackQuery.getData().length() - 1))){
            int level = Integer.parseInt(callbackQuery.getData().substring(callbackQuery.getData().length() - 1)) + 1;
            User user = userService.getOrCreate(callbackQuery.getFrom().getId());
            OilPump oilPump = oilPumpService.findById(level);
            // всплывающее окно при недостатке денег на счете
            if (user.getOilCoin() < oilPump.getPrice()){
              executeAnswerCallback(updateReceiver.creatAlertCallbackOilNotMoney(update));
            } else {
                executeWithExceptionCheck(updateReceiver.buyOilPumpImpl(update, level));
                   }
        }
        // нажали на inline купить электростанцию (убираем последний символ из запроса тк он равен уровню установки)
        else if ("/buy_electric".equalsIgnoreCase(callbackQuery.getData().substring(0, callbackQuery.getData().length() - 1))){
            int level = Integer.parseInt(callbackQuery.getData().substring(callbackQuery.getData().length() - 1)) + 1;
            User user = userService.getOrCreate(callbackQuery.getFrom().getId());
            Powerhouse powerhouse = powerhouseService.findById(level);
            // всплывающее окно при недостатке денег на счете
            if (user.getECoin() < powerhouse.getPrice()){
                executeAnswerCallback(updateReceiver.creatAlertCallbackElectricNotMoney(update));
            } else {
                executeWithExceptionCheck(updateReceiver.buyElectricImpl(update, level));
            }
        }
        // нажали на inline купить электростанцию оптом (убираем последний символ из запроса тк он равен уровню установки)
        else if ("/buy_electricOptom".equalsIgnoreCase(callbackQuery.getData().substring(0, callbackQuery.getData().length() - 1))){
            int level = Integer.parseInt(callbackQuery.getData().substring(callbackQuery.getData().length() - 1)) + 1;
            User user = userService.getOrCreate(callbackQuery.getFrom().getId());
            Powerhouse powerhouse = powerhouseService.findById(level);
            // всплывающее окно при недостатке денег на счете
            if (user.getECoin() < powerhouse.getPrice()){
                executeAnswerCallback(updateReceiver.creatAlertCallbackElectricNotMoney(update));
            } else {
                executeWithExceptionCheck(updateReceiver.buyElectricOptom(update, level));
            }
        }
        // нажали на inline купить нефтяную установку оптом (убираем последний символ из запроса тк он равен уровню установки)
        else if ("/buy_oilPumpOptom".equalsIgnoreCase(callbackQuery.getData().substring(0, callbackQuery.getData().length() - 1))){
            int level = Integer.parseInt(callbackQuery.getData().substring(callbackQuery.getData().length() - 1)) + 1;
            User user = userService.getOrCreate(callbackQuery.getFrom().getId());
            OilPump oilPump = oilPumpService.findById(level);
            // всплывающее окно при недостатке денег на счете
            if (user.getOilCoin() < oilPump.getPrice()){
                executeAnswerCallback(updateReceiver.creatAlertCallbackOilNotMoney(update));
            } else {
                executeWithExceptionCheck(updateReceiver.buyOilPumpOptom(update, level));
            }
        }
        // пополнение баланса
        else if ("/payment".equalsIgnoreCase(callbackQuery.getData())){
           executeWithExceptionCheck(updateReceiver.payment(update));
        }
        // пополнение баланса oilCoin
        else if ("/buy_oilCoin".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.paymentOilCoin(update));
        }
        // пополнение баланса oilCoin
        else if ("/buy_eCoin".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.paymentECoin(update));
        }
        // обмен валют
        else if ("/changeMoney".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.change(update));
        }
        // обмен Gold
        else if ("/change_balls".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.changeBalls(update));
        }
        // обмен Баллов
        else if ("/change_gold".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.changeGold(update));
        }
        // обмен eCrypt
        else if ("/change_ecrypt".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.changeEcrypt(update));
        }
        // вывод денег
        else if ("/output".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.withdraw(update));
        }
        // вывод gold
        else if ("/withdraw_gold".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.withdrawGold(update));
        }
        // вывод eCrypt
        else if ("/withdraw_ecrypt".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.withdrawECrypt(update));
        }
        // собрать энергию
        else if ("/send_toBoxElectric".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.electricToBox(update));
        }
        // собрать нефть
        else if ("/send_toBoxOil".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.oilToBox(update));
        }
        // изменить название компании
        else if ("/change_companyName".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.changeName(update));
        }
        // рестарт обучения
        else if ("/re_train".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.callbackStart(update));
            executeAnswerCallback(updateReceiver.creatAlertCallbackReturnTrain(update));
        }
        // faq
        else if ("/faq".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.faq(update));
        }
        // обратная связь
        else if ("/report".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.report(update));
        }
        else if ("/answer".equalsIgnoreCase(callbackQuery.getData().split("_")[0])){
            executeWithExceptionCheck(updateReceiver.answer(update));
        }
        // faq balls
        else if ("/faq_balls".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.faqBalls(update));
        }
        // faq actions
        else if ("/faq_actions".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.faqActions(update));
        }
        // faq withdraw
        else if ("/faq_with".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.faqWith(update));
        }
        // faq refs
        else if ("/faq_refs".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.faqRefs(update));
        }


    }

    public void fabricPosition(Update update, String position){
        // спрашивает кол-во электростанций
      if ("buyElectricOptom".equalsIgnoreCase(position.substring(0, position.length() - 1))){
          int level = Integer.parseInt(position.substring(position.length() - 1));
          int quantity = 0;
          try {
              quantity = Integer.parseInt(update.getMessage().getText());
              executeWithExceptionCheck(updateReceiver.buyElectricOptomImpl(update, level, quantity));
          } catch (Exception e){
              MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(update.getMessage().getFrom().getId()));
              executeWithExceptionCheck(messageBuilder
                      .line("Вы ввели не число")
                      .build());
          }
      }
         // спрашивает кол-во нефтяных установок
        else if ("buyOilPumpOptom".equalsIgnoreCase(position.substring(0, position.length() - 1))){
            int level = Integer.parseInt(position.substring(position.length() - 1));
            int quantity = 0;
            try {
                quantity = Integer.parseInt(update.getMessage().getText());
                executeWithExceptionCheck(updateReceiver.buyOilPumpOptomImpl(update, level, quantity));
            } catch (Exception e){
                MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(update.getMessage().getFrom().getId()));
                executeWithExceptionCheck(messageBuilder
                        .line("Вы ввели не число")
                        .build());
            }
        }
        else if ("change_ecrypt".equalsIgnoreCase(position)){
           executeWithExceptionCheck(updateReceiver.changeEcryptImpl(update));
        }
        else if ("change_gold".equalsIgnoreCase(position)){
          executeWithExceptionCheck(updateReceiver.changeGoldImpl(update));
        }
        else if ("change_balls".equalsIgnoreCase(position)){
          executeWithExceptionCheck(updateReceiver.changeBallsImpl(update));
        }
        else if ("withdraw_gold".equalsIgnoreCase(position)){
          executeWithExceptionCheck(updateReceiver.withdrawGoldImpl(update));
        }
        else if ("withdraw_ecrypt".equalsIgnoreCase(position)){
          executeWithExceptionCheck(updateReceiver.withdrawECryptImpl(update));
        }
        else if ("sell_oil".equalsIgnoreCase(position)){
           executeWithExceptionCheck(updateReceiver.sellOilImpl(update));
        }
        else if ("sell_electric".equalsIgnoreCase(position)){
           executeWithExceptionCheck(updateReceiver.sellElectricImpl(update));
        }
        else if ("change_name".equalsIgnoreCase(position)){
            executeWithExceptionCheck(updateReceiver.changeNameImpl(update));
        }
        else if ("report".equalsIgnoreCase(position)){
          executeWithExceptionCheck(updateReceiver.reportImplToUser(update));
          executeWithExceptionCheck(updateReceiver.reportImpl(update));
        }
        else if ("answer".equalsIgnoreCase(position.split("_")[0])){
          updateReceiver.answerImpl(update).forEach(this::executeWithExceptionCheck);
        }
    }

    public void mainMenu(Update update){
        User user = userService.getOrCreate(update.getMessage().getFrom().getId());
        user.setPositions("back");
        userService.update(user);

        SendMessage sendMessage = updateReceiver.endTrain(update);
        executeWithExceptionCheck(sendMessage);

        sendMessage = updateReceiver.endTrainMenu(update);
        executeWithExceptionCheck(sendMessage);
    }

    public void mainMenuCallback(Update update){
        User user = userService.getOrCreate(update.getCallbackQuery().getFrom().getId());
        user.setPositions("back");
        userService.update(user);

        SendMessage sendMessage = updateReceiver.main(update);
        executeWithExceptionCheck(sendMessage);

        sendMessage = updateReceiver.mainMenu(update);
        executeWithExceptionCheck(sendMessage);
    }

    public void executeWithExceptionCheck(SendMessage sendMessage) {
        try {
            execute(sendMessage);
            log.debug("Executed {}", sendMessage);
        } catch (TelegramApiException e) {
            log.error("Exception while sending message {} to user: {}", sendMessage, e.getMessage());
        }
    }

    public void executeAnswerCallback(AnswerCallbackQuery answerCallbackQuery) {
        try {
            execute(answerCallbackQuery);
            log.debug("Executed {}", answerCallbackQuery);
        } catch (TelegramApiException e) {
            log.error("Exception while sending answerCallback {} to user: {}", answerCallbackQuery, e.getMessage());
        }
    }

    public void sendStartReport() {
        executeWithExceptionCheck(new SendMessage()
                .setChatId(botAdmin)
                .setText("Bot start up is successful"));
        log.debug("Start report sent to Admin");
    }

    public boolean isText(Update update){
        return update!=null && !update.hasCallbackQuery() && update.hasMessage();
    }

    // каждый час работы электростанций и нефтяных установок
    @Scheduled(fixedDelay = 3600000)
    public void executeEveryTimeTask(){
        List<Powerhouse> powerhouses = powerhouseService.findAllUsersPower();
        List<OilPump> oilPumps = oilPumpService.findAllByUsersOil();

        powerhouses.forEach(powerhouse -> powerhouse.setProducted(powerhouse.getProducted() + powerhouse.getProduction()));
        oilPumps.forEach(oilPump -> oilPump.setProducted(oilPump.getProducted() + oilPump.getProduction()));

        powerhouses.forEach(powerhouseService::save);
        oilPumps.forEach(oilPumpService::save);
    }

    @Scheduled(fixedDelay = 86400000)
    public void dailyBonus(){
        List<User> users = userService.findAll();
        users.forEach(u -> u.setDailyBonus(false));
        users.forEach(userService::update);
    }

    @Scheduled(fixedDelay = 3600000)
    private void refCost(){
        List<User> users = userService.findUsersWithRefer();
        for (User u : users){
            User refer = userService.getOrCreate(u.getReferId());
            int oilCoin = 0;
            int eCoin = 0;
            List<Powerhouse> powerhouses = powerhouseService.findByUserId(u.getReferId());
            List<OilPump> oilPumps = oilPumpService.findByUserId(u.getReferId());

            eCoin = powerhouses.stream().mapToInt(Powerhouse::getProduction).sum();
            oilCoin = oilPumps.stream().mapToInt(OilPump::getProduction).sum();

            refer.setECoin(refer.getECoin() + eCoin);
            refer.setOilCoin(refer.getOilCoin() + oilCoin);
            userService.update(refer);
        }
    }

}
