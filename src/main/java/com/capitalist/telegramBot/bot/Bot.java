package com.capitalist.telegramBot.bot;

import com.capitalist.telegramBot.model.User;
import com.capitalist.telegramBot.service.UserService;
import com.capitalist.telegramBot.util.TelegramUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


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

        // старт
        if ("/start".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.start(update);
            executeWithExceptionCheck(sendMessage);
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
        if ("\uD83C\uDFE6 Банк".equalsIgnoreCase(command)){
           sendMessage = updateReceiver.pay(update);
           executeWithExceptionCheck(sendMessage);
        }
        // нажатие на Моя компания в меню
        if ("\uD83C\uDFED Моя компания".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.mainCompany(update);
            executeWithExceptionCheck(sendMessage);
        }
        if ("⬅️ Назад".equalsIgnoreCase(command)){
            mainMenu(update);
        }

        if ("\uD83D\uDED2 Рынок".equalsIgnoreCase(command)){
            sendMessage = updateReceiver.mainMarket(update);
            executeWithExceptionCheck(sendMessage);
        }

    }
    public void fabricCallbackTelegram(Update update){
        CallbackQuery callbackQuery = update.getCallbackQuery();

        // выбор inline меню Акционер и реферал
        if ("/action".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.actionAndRef(update));
        }
        // выбор inline меню Игровой чат
        if ("/chat".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.chat(update));
        }
        // выбор inline меню Реклама
        if ("/advert".equalsIgnoreCase(callbackQuery.getData())){
            executeWithExceptionCheck(updateReceiver.advert(update));
        }
        // продажа нефти на рынке
        if ("/sell_oil".equalsIgnoreCase(callbackQuery.getData())){
            if (!updateReceiver.marketEnough(update.getCallbackQuery().getFrom().getId(), "oil")){
                try {
                    execute(updateReceiver.alertOil(update));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        // продажа энергии на рынке
        if ("/sell_electric".equalsIgnoreCase(callbackQuery.getData())){
            if (!updateReceiver.marketEnough(update.getCallbackQuery().getFrom().getId(), "electric")){
                try {
                    execute(updateReceiver.alertElectric(update));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public void mainMenu(Update update){
        SendMessage sendMessage = updateReceiver.endTrain(update);
        executeWithExceptionCheck(sendMessage);

        sendMessage = updateReceiver.endTrainMenu(update);
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

    public void sendStartReport() {
        executeWithExceptionCheck(new SendMessage()
                .setChatId(botAdmin)
                .setText("Bot start up is successful"));
        log.debug("Start report sent to Admin");
    }

    public boolean isText(Update update){
        return update!=null && !update.hasCallbackQuery() && update.hasMessage();
    }

}
