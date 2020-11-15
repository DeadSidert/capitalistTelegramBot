package com.capitalist.telegramBot.gameEntities;

import com.capitalist.telegramBot.bot.builder.MessageBuilder;
import com.capitalist.telegramBot.service.CompanyService;
import com.capitalist.telegramBot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyCompany {

    private final UserService userService;
    private final CompanyService companyService;

    private final ReplyKeyboardMarkup keyboardMarkups = new ReplyKeyboardMarkup();

    @Autowired
    public MyCompany(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    public SendMessage main(Update update){
        String userId = String.valueOf(update.getMessage().getFrom().getId());
        MessageBuilder messageBuilder = MessageBuilder.create(userId);
        messageBuilder
                .line()
                .line("\uD83C\uDFED Моя компания\n" +
                        "  \n" +
                        "Здесь Вы найдете основную информацию по Вашей компании.");

        createMenu();
        return messageBuilder.build().setReplyMarkup(keyboardMarkups);
    }

    public void createMenu(){
        List<KeyboardRow> rowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("⛽️ Нефтяные насосы");
        keyboardRow.add("\uD83D\uDD0C Электростанции");

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("\uD83C\uDFD6 Офшорная зона");

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add("\uD83D\uDCCA Статистика");
        keyboardRow2.add("\uD83D\uDC65 Рефералы");

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add("\uD83D\uDCDC Задания");
        keyboardRow3.add("⬅️ Назад");

        rowList.add(keyboardRow);
        rowList.add(keyboardRow1);
        rowList.add(keyboardRow2);
        rowList.add(keyboardRow3);

        keyboardMarkups.setKeyboard(rowList);
    }
}
