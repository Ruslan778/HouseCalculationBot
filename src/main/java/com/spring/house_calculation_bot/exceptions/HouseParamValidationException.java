package com.spring.house_calculation_bot.exceptions;

public class HouseParamValidationException extends RuntimeException {
    public HouseParamValidationException (String msg) {
        super(msg);
    }
}