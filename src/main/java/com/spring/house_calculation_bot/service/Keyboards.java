package com.spring.house_calculation_bot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboards {

    public static ReplyKeyboardMarkup restartKeyboardMarkup() {

        ReplyKeyboardMarkup restartKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> restartKeyboardRows = new ArrayList<>();

        KeyboardRow restartRow = new KeyboardRow();
        restartRow.add("Обнулить данные и начать рассчёт заново");
        restartKeyboardRows.add(restartRow);

        restartKeyboardMarkup.setKeyboard(restartKeyboardRows);

        return restartKeyboardMarkup;
    }
    public static ReplyKeyboardMarkup startKeyboardMarkup() {

        ReplyKeyboardMarkup restartKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> restartKeyboardRows = new ArrayList<>();

        KeyboardRow restartRow = new KeyboardRow();

        restartRow.add("Рассчитать стоимость строительства");
        restartKeyboardRows.add(restartRow);

        restartKeyboardMarkup.setKeyboard(restartKeyboardRows);

        return restartKeyboardMarkup;
    }
    public static ReplyKeyboardMarkup floorsKeyboardMarkup() {

        ReplyKeyboardMarkup floorsKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> floorsKeyboardRows = new ArrayList<>();

        KeyboardRow floorsRow1 = new KeyboardRow();
        floorsRow1.add("1");
        floorsRow1.add("2");
        floorsRow1.add("3");
        KeyboardRow floorsRow2 = new KeyboardRow();
        floorsRow2.add("Обнулить данные и начать рассчёт заново");

        floorsKeyboardRows.add(floorsRow1);
        floorsKeyboardRows.add(floorsRow2);
        floorsKeyboardMarkup.setKeyboard(floorsKeyboardRows);

        return floorsKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup wallsKeyboardMarkup() {

        ReplyKeyboardMarkup wallsKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> wallsKeyboardRows = new ArrayList<>();

        KeyboardRow wallsRow1 = new KeyboardRow();
        wallsRow1.add("Газобетон");
        wallsRow1.add("Керамический кирпич");
        wallsRow1.add("Тёплая керамика");

        KeyboardRow wallsRow2 = new KeyboardRow();
        wallsRow2.add("Обнулить данные и начать рассчёт заново");

        wallsKeyboardRows.add(wallsRow1);
        wallsKeyboardRows.add(wallsRow2);

        wallsKeyboardMarkup.setKeyboard(wallsKeyboardRows);

        return wallsKeyboardMarkup;
    }
    public static ReplyKeyboardMarkup basementKeyboardMarkup() {

        ReplyKeyboardMarkup basementKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> basementKeyboardRows = new ArrayList<>();

        KeyboardRow basementRow1 = new KeyboardRow();
        basementRow1.add("Свайно-ростверковый");
        basementRow1.add("Забивные сваи");
        basementRow1.add("Монолитная плита");

        KeyboardRow basementRow2 = new KeyboardRow();
        basementRow2.add("Обнулить данные и начать рассчёт заново");

        basementKeyboardRows.add(basementRow1);
        basementKeyboardRows.add(basementRow2);

        basementKeyboardMarkup.setKeyboard(basementKeyboardRows);

        return basementKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup roofKeyboardMarkup() {

        ReplyKeyboardMarkup roofKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> roofKeyboardRows = new ArrayList<>();

        KeyboardRow roofRow1 = new KeyboardRow();
        roofRow1.add("Металлочерепица");
        roofRow1.add("Профнастил");
        roofRow1.add("Битумная черепица");

        KeyboardRow roofRow2 = new KeyboardRow();
        roofRow2.add("Обнулить данные и начать рассчёт заново");

        roofKeyboardRows.add(roofRow1);
        roofKeyboardRows.add(roofRow2);

        roofKeyboardMarkup.setKeyboard(roofKeyboardRows);

        return roofKeyboardMarkup;
    }
}
