package com.spring.house_calculation_bot.service;

import com.spring.house_calculation_bot.model.CalcParam;
import com.spring.house_calculation_bot.model.HouseCalculation;
import org.junit.Test;
import static org.junit.Assert.*;

public class PriceResolverImplTest {

    @Test
    public void calcTotalPrice() {
        CalcParam calcParam = new CalcParam(
                1,
                CalcParam.WallMaterial.BRICK,
                CalcParam.BaseType.DRIVEN_PILES,
                CalcParam.RoofType.METAL_TILE,
                100
        );

        double area = calcParam.getHouseArea();

        double basementWorkPrice = 1900 * calcParam.getBaseType().getWorkCoefficient() * area;
        double basementMaterialPrice = 5000 * calcParam.getBaseType().getMaterialCoefficient() * area;

        double wallWorkPrice = 4200 * calcParam.getWallMaterial().getWorkCoefficient() * area;
        double wallMaterialsPrice = 8700 * calcParam.getWallMaterial().getMaterialCoefficient() * area;

        double roofWorkPrice = 1830 * calcParam.getRoofType().getWorkCoefficient() * area / calcParam.getFloors();
        double roofMaterialsPrice = 5000 * calcParam.getRoofType().getMaterialCoefficient() * area / calcParam.getFloors();

        double preFinishDecWorkPrice = 3200 * area;
        double preFinishDecMaterialsPrice = 5650 * area;

        double workPrice = basementWorkPrice + wallWorkPrice + roofWorkPrice + preFinishDecWorkPrice;
        double materialsPrice = basementMaterialPrice + wallMaterialsPrice + roofMaterialsPrice + preFinishDecMaterialsPrice;

        double totalAmountPrice = workPrice + materialsPrice;

        HouseCalculation actual = new PriceResolverImpl().calcTotalPrice(calcParam);

        assertEquals(basementWorkPrice, actual.getBasementPrice().getCostOfWork(), 0.01);
        assertEquals(basementMaterialPrice, actual.getBasementPrice().getCostOfMaterials(), 0.01);
        assertEquals(wallWorkPrice, actual.getWallsPrice().getCostOfWork(), 0.01);
        assertEquals(wallMaterialsPrice, actual.getWallsPrice().getCostOfMaterials(), 0.01);
        assertEquals(roofWorkPrice, actual.getRoofPrice().getCostOfWork(), 0.01);
        assertEquals(roofMaterialsPrice, actual.getRoofPrice().getCostOfMaterials(), 0.01);
        assertEquals(preFinishDecWorkPrice, actual.getPreFinishingDecoration().getCostOfWork(), 0.01);
        assertEquals(preFinishDecMaterialsPrice, actual.getPreFinishingDecoration().getCostOfMaterials(), 0.01);
        assertEquals(workPrice, actual.getHousePrice().getCostOfWork(), 0.01);
        assertEquals(materialsPrice, actual.getHousePrice().getCostOfMaterials(), 0.01);
        assertEquals(totalAmountPrice, actual.getTotalAmount(), 0.01);
    }
}