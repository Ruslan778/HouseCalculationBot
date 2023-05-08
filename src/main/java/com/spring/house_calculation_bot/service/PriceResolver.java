package com.spring.house_calculation_bot.service;

import com.spring.house_calculation_bot.model.CalcParam;
import com.spring.house_calculation_bot.model.HouseCalculation;

public interface PriceResolver {
    HouseCalculation calcTotalPrice(CalcParam calcParam);
}