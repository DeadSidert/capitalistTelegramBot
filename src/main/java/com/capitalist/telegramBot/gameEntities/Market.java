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

    public Company getUserCompany(int id){
        User user = userService.getOrCreate(id);
        return companyService.getOrCreate(user.getCompanyId());
    }

    public boolean enoughOil(int id){
        Company company = getUserCompany(id);
        return company.getOil() >=500;
    }

    public boolean enoughElectric(int id){
        Company company = getUserCompany(id);
        return company.getElectric() >=500;
    }
}
