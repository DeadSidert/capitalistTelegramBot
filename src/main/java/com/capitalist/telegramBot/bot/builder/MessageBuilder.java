package com.capitalist.telegramBot.bot.builder;

import com.capitalist.telegramBot.model.User;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class MessageBuilder {

    @Setter
    private String chatId;
    private StringBuilder sb = new StringBuilder();
    private List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    private List<InlineKeyboardButton> row = null;

    private MessageBuilder() {
    }

    public static MessageBuilder create(String chatId) {
        MessageBuilder builder = new MessageBuilder();
        builder.setChatId(chatId);
        return builder;
    }

    public static MessageBuilder create(User user) {
        return create(String.valueOf(user.getUserId()));
    }

    public MessageBuilder line(String text) {
        sb.append(text);
        return line();
    }

    public MessageBuilder line() {
        sb.append(String.format("%n"));
        return this;
    }

    public MessageBuilder row() {
        addRowToKeyboard();
        row = new ArrayList<>();
        return this;
    }

    public MessageBuilder button(String text, String callbackData) {
        row.add(new InlineKeyboardButton().setText(text).setCallbackData(callbackData));
        return this;
    }

    public MessageBuilder buttonWithUrl(String text, String callbackData, String url) {
        row.add(new InlineKeyboardButton().setText(text).setCallbackData(callbackData).setUrl(url));
        return this;
    }

    public MessageBuilder buttonWithArguments(String text, String callbackData) {
        return button(text, callbackData + " " + text);
    }

    public SendMessage build() {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .enableMarkdown(true)
                .setText(sb.toString());

        addRowToKeyboard();

        if (!keyboard.isEmpty()) {
            sendMessage.setReplyMarkup(new InlineKeyboardMarkup().setKeyboard(keyboard));
        }

        return sendMessage;
    }

    private void addRowToKeyboard() {
        if (row != null) {
            keyboard.add(row);
        }
    }
}
