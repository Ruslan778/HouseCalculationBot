package com.spring.house_calculation_bot.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class HouseCalculation {
    private ComponentCalculation basementPrice;
    private ComponentCalculation wallsPrice;
    private ComponentCalculation roofPrice;
    private ComponentCalculation preFinishingDecoration;
    private ComponentCalculation housePrice;
    private double totalAmount;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ComponentCalculation {
        private double costOfWork;
        private double costOfMaterials;
    }

    @Override
    public String toString() {
        return String.format(
                "Этап №1. Фундамент.\n" +
                        "Стоимость работ:  %,.0f руб.\n" +
                        "Стоимость материалов:  %,.0f руб.\n\n" +

                        "Этап №2. Возведение коробки (кирпичная кладка стен, межэтажные перекрытия, окна).\n" +
                        "Стоимость работ:  %,.0f руб.\n" +
                        "Стоимость материалов:  %,.0f руб.\n\n" +

                        "Этап №3. Кровля.\n" +
                        "Стоимость работ:  %,.0f руб.\n" +
                        "Стоимость материалов:  %,.0f руб.\n\n" +

                        "Этап №4. Предчистовая отделка (монтаж электроснабжения, отопление, штукатурка стен, стяжка пола)\n" +
                        "Стоимость работ:  %,.0f руб.\n" +
                        "Стоимость материалов:  %,.0f руб.\n\n" +

                        "Итоговая стоимость работ:  %,.0f руб.\n" +
                        "Итоговая стоимость материалов:  %,.0f руб.\n\n" +

                        "Общая стоимость возведения дома:  %,.0f руб.",

                basementPrice.costOfWork, basementPrice.costOfMaterials,
                wallsPrice.costOfWork, wallsPrice.costOfMaterials,
                roofPrice.costOfWork, roofPrice.costOfMaterials,
                preFinishingDecoration.costOfWork, preFinishingDecoration.costOfMaterials,
                housePrice.costOfWork, housePrice.costOfMaterials,
                totalAmount);
    }
}
