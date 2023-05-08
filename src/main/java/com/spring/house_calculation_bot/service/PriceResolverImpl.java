package com.spring.house_calculation_bot.service;

import com.spring.house_calculation_bot.model.CalcParam;
import com.spring.house_calculation_bot.model.HouseCalculation;
import org.springframework.stereotype.Service;

@Service
public class PriceResolverImpl implements PriceResolver {
    private final int BASEMENT_COST_OF_WORK = 1900;        // нулевой цикл (фундамент + перекрытия 1 этажа)
    private final int BASEMENT_COST_OF_MATERIALS = 5000;   // нулевой цикл (фундамент + перекрытия 1 этажа)
    private final int WALL_COST_OF_WORK = 4200;
    private final int WALL_COST_OF_MATERIALS = 8700;
    private final int INTERFLOOR_OVERLAP_COST_OF_WORK = 730;
    private final int INTERFLOOR_OVERLAP_COST_OF__MATERIALS = 2500;
    private final int ROOF_COST_OF_WORK = 1830;
    private final int ROOF_COST_OF_MATERIALS = 5000;
    private final int PREFINISHING_DECORATION_COST_OF_WORK = 3200;
    private final int PREFINISHING_DECORATION_COST_OF_MATERIALS = 5650;
    private final double FLOORS_COEFFICIENT = 4.0/3.0;

    @Override
    public HouseCalculation calcTotalPrice(CalcParam calcParam) {

        HouseCalculation.ComponentCalculation costOfBasement = calcCostOfBasement(calcParam);
        HouseCalculation.ComponentCalculation costOfWall = calcCostOfWall(calcParam);
        HouseCalculation.ComponentCalculation costOfRoof = calcCostOfRoof(calcParam);
        HouseCalculation.ComponentCalculation costOfPreFinishingDecoration = calcCostOfPreFinishingDecoration(calcParam);
        HouseCalculation.ComponentCalculation subTotalPrice = calcSubTotalPrice(costOfBasement, costOfWall, costOfRoof, costOfPreFinishingDecoration);
        double totalAmount = subTotalPrice.getCostOfWork() + subTotalPrice.getCostOfMaterials();

        return new HouseCalculation(costOfBasement, costOfWall, costOfRoof, costOfPreFinishingDecoration, subTotalPrice, totalAmount);
    }

    /**
     * Сеттером класса CalcParam гарантируется значение поля floors 1, 2 или 3
     */
    private HouseCalculation.ComponentCalculation calcCostOfBasement(CalcParam calcParam) {

        double costOfWork = BASEMENT_COST_OF_WORK * calcParam.getHouseArea() / calcParam.getFloors();
        double costOfMaterials = BASEMENT_COST_OF_MATERIALS * calcParam.getHouseArea() / calcParam.getFloors();

        costOfWork *= calcParam.getBaseType().getWorkCoefficient();
        costOfMaterials *= calcParam.getBaseType().getMaterialCoefficient();

        return new HouseCalculation.ComponentCalculation(costOfWork, costOfMaterials);
    }

    private HouseCalculation.ComponentCalculation calcCostOfWall(CalcParam calcParam) {

        double area = calcParam.getHouseArea();
        double costOfWork;
        double costOfMaterials;

        if (calcParam.getFloors() == 1) {
            costOfWork = WALL_COST_OF_WORK * area;
            costOfMaterials = WALL_COST_OF_MATERIALS * area;
        } else {
            double interfloorOverlapWork = INTERFLOOR_OVERLAP_COST_OF_WORK * area * (calcParam.getFloors() - 1);
            double interfloorOverlapMaterials = INTERFLOOR_OVERLAP_COST_OF__MATERIALS * area * (calcParam.getFloors() - 1);

            costOfWork = WALL_COST_OF_WORK * area * FLOORS_COEFFICIENT * (calcParam.getFloors() - 1) + interfloorOverlapWork;
            costOfMaterials = WALL_COST_OF_MATERIALS * area * FLOORS_COEFFICIENT * (calcParam.getFloors() - 1) + interfloorOverlapMaterials;
        }

        costOfWork *= calcParam.getWallMaterial().getWorkCoefficient();
        costOfMaterials *= calcParam.getWallMaterial().getMaterialCoefficient();

        return new HouseCalculation.ComponentCalculation(costOfWork, costOfMaterials);
    }

    private HouseCalculation.ComponentCalculation calcCostOfRoof(CalcParam calcParam) {

        double costOfWork = ROOF_COST_OF_WORK * calcParam.getHouseArea() / calcParam.getFloors();
        double costOfMaterials = ROOF_COST_OF_MATERIALS * calcParam.getHouseArea() / calcParam.getFloors();

        costOfWork *= calcParam.getRoofType().getWorkCoefficient();
        costOfMaterials *= calcParam.getRoofType().getMaterialCoefficient();

        return new HouseCalculation.ComponentCalculation(costOfWork, costOfMaterials);
    }

    private HouseCalculation.ComponentCalculation calcCostOfPreFinishingDecoration(CalcParam calcParam) {

        double costOfWork = PREFINISHING_DECORATION_COST_OF_WORK * calcParam.getHouseArea();
        double costOfMaterials = PREFINISHING_DECORATION_COST_OF_MATERIALS * calcParam.getHouseArea();

        return new HouseCalculation.ComponentCalculation(costOfWork, costOfMaterials);
    }

    private HouseCalculation.ComponentCalculation calcSubTotalPrice(HouseCalculation.ComponentCalculation... costOfStages) {

        double costOfWork = 0.0;
        double costOfMaterials = 0.0;

        for (HouseCalculation.ComponentCalculation stage : costOfStages) {
            costOfWork += stage.getCostOfWork();
            costOfMaterials += stage.getCostOfMaterials();
        }
        return new HouseCalculation.ComponentCalculation(costOfWork, costOfMaterials);
    }
}