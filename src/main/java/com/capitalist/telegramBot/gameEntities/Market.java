package com.capitalist.telegramBot.gameEntities;

import com.capitalist.telegramBot.bot.builder.MessageBuilder;
import com.capitalist.telegramBot.model.Company;
import com.capitalist.telegramBot.model.User;
import com.capitalist.telegramBot.service.CompanyService;
import com.capitalist.telegramBot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class Market {

    private final UserService userService;
    private final CompanyService companyService;

    @Autowired
    public Market(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    public SendMessage mark(Update update){
        int userId = update.getMessage().getFrom().getId();
        Company company = getUserCompany(userId);

        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        messageBuilder
                .line()
                .line("\uD83D\uDED2 Рынок\n" +
                        "  \n" +
                        "Добро пожаловать на рынок!\n" +
                        "Здесь Вы можете продать ресурсы со склада.\n" +
                        "\n" +
                        "\uD83D\uDCE6 На складе:\n" +
                        company.getOil() + " \uD83D\uDEE2 баррелей нефти\n" +
                        company.getElectric() + " \uD83D\uDD0B киловатт энергии\n" +
                        "\n" +
                        "Расценки продажи:\n" +
                        "500 баррелей нефти = 1 \uD83D\uDCB0 Gold и 2 \uD83C\uDF11 OilCoin.\n" +
                        "500 киловатт энергии = 1 ⚡️ ECrypt  и 2 \uD83C\uDF15 ECoin\n" +
                        "\n" +
                        "Минимум для продажи: 500 \uD83D\uDEE2 баррелей нефти / 500 \uD83D\uDD0B киловатт энергии")
                .row()
                .button("Продать \uD83D\uDEE2 баррели нефти", "/sell_oil")
                .row()
                .button("Продать \uD83D\uDD0B киловатт энергии", "/sell_electric");
        return messageBuilder.build();
    }

    public SendMessage sellOil(Update update) {
        int userId = update.getCallbackQuery().getFrom().getId();
        Company company = getUserCompany(userId);
        User user = userService.getOrCreate(userId);
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));

        user.setPositions("sell_oil");
        userService.update(user);

        return messageBuilder
                .line("Введите кол-во \uD83D\uDEE2 баррелей нефти для продажи")
                .build();
    }
     // ввести кол-во нефти для продажи
    public SendMessage sellOilImpl(Update update) {
        int userId = update.getMessage().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        int quantity = 0;
        int gold = 0;
        int oilCoin = 0;

        try {
            quantity = Integer.parseInt(update.getMessage().getText());
        }
        catch (Exception e){
            return messageBuilder
                    .line("Вы ввели не число")
                    .build();
        }

        Company company = getUserCompany(userId);
        User user = userService.getOrCreate(userId);

        if (company.getOil() < quantity){
            return messageBuilder
                    .line("Недостаточно нефти на складе!")
                    .build();
        }

        gold = quantity / 500;
        oilCoin = quantity / 250;
        company.setOil(company.getOil() - quantity);
        companyService.update(company);

        user.setPositions("back");
        user.setGold(user.getGold() + gold);
        user.setOilCoin(user.getOilCoin() + oilCoin);
        userService.update(user);

        return messageBuilder
                .line("Вы продали " + " \uD83D\uDEE2 баррелей нефти\n")
                .line("Получили: " + gold + "\uD83D\uDCB0 Gold и " + oilCoin + " \uD83C\uDF11 OilCoin.")
                .build();
    }

    public SendMessage sellElectric(Update update) {
        int userId = update.getCallbackQuery().getFrom().getId();
        Company company = getUserCompany(userId);
        User user = userService.getOrCreate(userId);
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));

        user.setPositions("sell_electric");
        userService.update(user);

        return messageBuilder
                .line("Введите кол-во \uD83D\uDD0B киловатт энергии")
                .build();
    }

    // ввести кол-во энергии для продажи
    public SendMessage sellElectricImpl(Update update) {
        int userId = update.getMessage().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        int quantity = 0;
        int eCrypt = 0;
        int eCoin = 0;

        try {
            quantity = Integer.parseInt(update.getMessage().getText());
        }
        catch (Exception e){
            return messageBuilder
                    .line("Вы ввели не число")
                    .build();
        }

        Company company = getUserCompany(userId);
        User user = userService.getOrCreate(userId);

        if (company.getElectric() < quantity){
            return messageBuilder
                    .line("Недостаточно энергии на складе!")
                    .build();
        }

        eCrypt = quantity / 500;
        eCoin = quantity / 250;
        company.setElectric(company.getElectric() - quantity);
        companyService.update(company);

        user.setECrypt(user.getECrypt() + eCrypt);
        user.setECoin(user.getECoin() + eCoin);
        user.setPositions("back");
        userService.update(user);

        return messageBuilder
                .line("Вы продали " + " \uD83D\uDD0B киловатт энергии\n")
                .line("Получили: " + eCrypt + "⚡️ ECrypt и " + eCoin + " \uD83C\uDF15 ECoin.")
                .build();
    }


    public boolean enoughOil(int id){
        Company company = getUserCompany(id);
        return company.getOil() >=500;
    }

    public boolean enoughElectric(int id){
        Company company = getUserCompany(id);
        return company.getElectric() >=500;
    }

    public Company getUserCompany(int id){
        User user = userService.getOrCreate(id);
        return companyService.getOrCreate(user.getCompanyId());
    }


}
