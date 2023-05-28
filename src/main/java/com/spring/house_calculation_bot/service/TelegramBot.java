package com.spring.house_calculation_bot.service;

import com.spring.house_calculation_bot.config.BotConfig;
import com.spring.house_calculation_bot.exceptions.HouseParamValidationException;
import com.spring.house_calculation_bot.model.CalcParam;
import com.spring.house_calculation_bot.model.HouseCalculation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private PriceResolver priceResolver;
    Map<Long, CalcParam> cacheMap = new HashMap<>();
    final BotConfig config;
    static final String HELP_TEXT = "Этот бот создан для рассчёта примерной стоимости поэтапного строительства дома.\n\n" +
            "Выполнять команды можно из главного меню или вводом в строку набора сообщения:\n" +
            "Используй команду /start чтобы запустить бота.\n" +
            "Команду /restart чтобы начать рассчёт заново.\n" +
            "Команду /help для просмотра доступных команд.";
    final String UNKNOWN_MATERIAL_MSG = "Мне не знаком данный вид конструкции ... выберите из предложенных:";
    final String ASK_FOR_AREA = "Введите площадь дома в строке ввода сообщения";

    public TelegramBot(BotConfig config) {
        this.config = config;

        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "start calculation"));
        listOfCommands.add(new BotCommand("/restart", "restart calculation"));
        listOfCommands.add(new BotCommand("/help", "get info how to use this bot"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        long chatId = update.getMessage().getChatId();
        if (!cacheMap.containsKey(chatId)) {
            cacheMap.put(chatId, new CalcParam(0, null, null, null, 0));
        }
        CalcParam calcParam = cacheMap.get(chatId);

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            if (messageText.equals("/start")) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            } else if (messageText.equals("Обнулить данные и начать рассчёт заново") || messageText.equals("/restart")) {
                cacheMap.put(chatId, new CalcParam(0, null, null, null, 0));
                sendMessage(chatId, "Данные обнулены. Чтобы начать новый расчёт нажмите на кнопку.", Keyboards.startKeyboardMarkup());
            } else if (messageText.equals("/help")) {
                sendMessage(chatId, HELP_TEXT, Keyboards.startKeyboardMarkup());
            } else if (calcParam.getFloors() == 0) {
                try {
                    int floors = Integer.parseInt(messageText);
                    calcParam.setFloors(floors);
                    sendMessage(chatId, askForMaterial("стен"), Keyboards.wallsKeyboardMarkup());
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Введите этажность дома.\n" +
                            "Этажность дома должна состоять из цифр (1, 2, 3):", Keyboards.floorsKeyboardMarkup());
                } catch (HouseParamValidationException e) {
                    sendMessage(chatId, e.getMessage(), Keyboards.floorsKeyboardMarkup());
                    log.error("Error occurred: " + e.getMessage());
                }
            } else if (calcParam.getWallMaterial() == null) {
                switch (messageText.toLowerCase()) {
                    case "газобетон":
                    case "газоблок":
                        calcParam.setWallMaterial(CalcParam.WallMaterial.AEROCRETE);
                        sendMessage(chatId, askForMaterial("фундамента"), Keyboards.basementKeyboardMarkup());
                        break;
                    case "керамический кирпич":
                    case "кирпич":
                    case "кирпичный":
                        calcParam.setWallMaterial(CalcParam.WallMaterial.BRICK);
                        sendMessage(chatId, askForMaterial("фундамента"), Keyboards.basementKeyboardMarkup());
                        break;
                    case "тёплая керамика":
                        calcParam.setWallMaterial(CalcParam.WallMaterial.CERAMIC_BLOCK);
                        sendMessage(chatId, askForMaterial("фундамента"), Keyboards.basementKeyboardMarkup());
                        break;
                    default:
                        sendMessage(chatId, UNKNOWN_MATERIAL_MSG, Keyboards.wallsKeyboardMarkup());
                }
            } else if (calcParam.getBaseType() == null) {
                switch (messageText.toLowerCase()) {
                    case "свайно-ростверковый":
                        calcParam.setBaseType(CalcParam.BaseType.PILE_GRILLAGE);
                        sendMessage(chatId, askForMaterial("кровли"), Keyboards.roofKeyboardMarkup());
                        break;
                    case "забивные сваи":
                        calcParam.setBaseType(CalcParam.BaseType.DRIVEN_PILES);
                        sendMessage(chatId, askForMaterial("кровли"), Keyboards.roofKeyboardMarkup());
                        break;
                    case "монолитная плита":
                        calcParam.setBaseType(CalcParam.BaseType.MONOLITHIC_SLAB);
                        sendMessage(chatId, askForMaterial("кровли"), Keyboards.roofKeyboardMarkup());
                        break;
                    default:
                        sendMessage(chatId, UNKNOWN_MATERIAL_MSG, Keyboards.basementKeyboardMarkup());
                }
            } else if (calcParam.getRoofType() == null) {
                switch (messageText.toLowerCase()) {
                    case "металлочерепица":
                        calcParam.setRoofType(CalcParam.RoofType.METAL_TILE);
                        sendMessage(chatId, ASK_FOR_AREA, Keyboards.restartKeyboardMarkup());
                        break;
                    case "профнастил":
                        calcParam.setRoofType(CalcParam.RoofType.PROFILED_SHHETING);
                        sendMessage(chatId, ASK_FOR_AREA, Keyboards.restartKeyboardMarkup());
                        break;
                    case "битумная черепица":
                        calcParam.setRoofType(CalcParam.RoofType.SHINGLES);
                        sendMessage(chatId, ASK_FOR_AREA, Keyboards.restartKeyboardMarkup());
                        break;
                    default:
                        sendMessage(chatId, UNKNOWN_MATERIAL_MSG, Keyboards.roofKeyboardMarkup());
                }
            } else if (calcParam.getHouseArea() == 0) {
                try {
                    messageText = messageText.replaceAll(",", ".");
                    double area = Double.parseDouble(messageText);
                    calcParam.setHouseArea(area);
                    sendMessage(chatId, "Начинаем рассчёт...", null);
                    try {
                        Thread.sleep(750);
                    } catch (InterruptedException e) {
                        log.error("Error occurred:" + e.getMessage());
                    }
                    HouseCalculation houseCalculation = priceResolver.calcTotalPrice(calcParam);
                    sendMessage(chatId, houseCalculation.toString(), Keyboards.restartKeyboardMarkup());
                    sendMessage(chatId, "Для нового рассчёта отправьте любое сообщение либо нажмите на кнопку.", Keyboards.restartKeyboardMarkup());
                    cacheMap.remove(chatId);
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Введено некорректное число.\n" +
                            ASK_FOR_AREA, Keyboards.restartKeyboardMarkup());
                } catch (HouseParamValidationException e) {
                    sendMessage(chatId, "Введено некорректное число.\n" +
                            e.getMessage(), Keyboards.restartKeyboardMarkup());
                    log.error("Error occurred: " + e.getMessage());
                }
            } else {
                sendMessage(chatId, "Команда не распознана.\n" +
                        HELP_TEXT, Keyboards.restartKeyboardMarkup());

                log.error("Вы что-то не предусмотрели: " +
                        "ChatID: " + chatId +
                        calcParam +
                        "\nТекст команды: " + messageText);
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Приветствую, " + name + "! Здесь ты можешь рассчитать примерную стоимость строительства дома.\n\nДля начала нажми кнопку ниже ↓ ↓ ↓";
        log.info("Replied to user " + name);

        sendMessage(chatId, answer, Keyboards.startKeyboardMarkup());
    }

    private void sendMessage(long chatId, String textToSend, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private String askForMaterial(String materialType) {
        return "Выберите тип " + materialType + " Вашего дома:";
    }
}
