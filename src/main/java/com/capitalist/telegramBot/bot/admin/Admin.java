package com.capitalist.telegramBot.bot.admin;

import com.capitalist.telegramBot.bot.builder.MessageBuilder;
import com.capitalist.telegramBot.model.Payment;
import com.capitalist.telegramBot.model.User;
import com.capitalist.telegramBot.service.PaymentService;
import com.capitalist.telegramBot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class Admin {

    private final UserService userService;
    private final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
    private final PaymentService paymentService;

    public Admin(UserService userService, PaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
    }

    public SendMessage admin(Update update){
        int userId = update.getMessage().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        messageBuilder
                .line("Вы успешно авторизовались");
        createAdminMenu();

        return messageBuilder.build().setReplyMarkup(keyboard);
    }

    public SendMessage checkPayments(Update update){
        int userId = update.getMessage().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        List<Payment> payments = paymentService.findAllPaymentsNotSuccess();
        messageBuilder
                .line("Список всех запросов на вывод:");
        if (payments == null ||payments.isEmpty()){
           return messageBuilder
                    .line("Запросов не найдено").build();
        }
        for (Payment p : payments){
            String success = p.getSuccess() ? "Да" : "Нет";
            messageBuilder
                    .line("Номер запроса: " + p.getId() + " | userId: " + p.getUserId() + " | Сумма: "
                            + p.getSum() + " | Время запроса: " + p.getTime() + "| Выполнено: " + success);
        }
        return messageBuilder.build();
    }

    public SendMessage createPayment(Update update){
        int userId = update.getMessage().getFrom().getId();
        User user = userService.getOrCreate(userId);
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        messageBuilder
                .line("Введите номер транзакции");

        user.setPositions("create_payment");
        userService.update(user);

        return messageBuilder.build();
    }

    public SendMessage createPaymentImpl(Update update){
        int userId = update.getMessage().getFrom().getId();
        MessageBuilder messageBuilder = MessageBuilder.create(String.valueOf(userId));
        Payment p = null;
        try {
            p = paymentService.findById(Integer.parseInt(update.getMessage().getText()));
        }catch (Exception e){
            return messageBuilder.line("Вы ввели не число").build();
        }
        User user = userService.getOrCreate(p.getUserId());

        messageBuilder
                .line("Номер запроса: " + p.getId() + " | userId: " + p.getUserId() + " | Сумма: "
                        + p.getSum() + " | Время запроса: " + p.getTime() + " | Номер: " + user.getQiwi())
                .row()
                .button("Оплачено", "/success_" + p.getId())
                .row()
                .button("✖️ Отмена", "/cancel");
        return messageBuilder.build();
    }

    public void createAdminMenu(){
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Получить список юзеров ожидающих оплату");

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow.add("Выполнить оплату пользователю");

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("⬅️ Назад");

        rowList.add(keyboardRow);
        rowList.add(keyboardRow1);
        rowList.add(keyboardRow2);
        keyboard.setResizeKeyboard(true);
        keyboard.setKeyboard(rowList);
    }
}
