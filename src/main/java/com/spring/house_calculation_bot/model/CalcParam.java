package com.spring.house_calculation_bot.model;

import com.spring.house_calculation_bot.exceptions.HouseParamValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CalcParam {

    private int floors;
    private WallMaterial wallMaterial;
    private BaseType baseType;
    private RoofType roofType;
    private double houseArea;

    public void setFloors(int floors) {
        if (floors > 0 && floors <= 3) {
            this.floors = floors;
        } else if (floors <= 0) {
            throw new HouseParamValidationException("Этажность дома должна быть больше 0.");
        } else {
            throw new HouseParamValidationException("Согласно нормам ИЖС этажность дома не может превышать 3 этажей.");
        }
    }

    public void setWallMaterial(WallMaterial wallMaterial) {
        this.wallMaterial = wallMaterial;
    }
    public void setBaseType(BaseType baseType) {
        this.baseType = baseType;
    }
    public void setRoofType(RoofType roofType) {
        this.roofType = roofType;
    }
    public void setHouseArea(double houseArea) {
        if (houseArea > 0) {
            this.houseArea = houseArea;
        } else {
            throw new HouseParamValidationException("Площадь дома должна быть больше 0.");
        }
    }

    @Getter
    public enum WallMaterial {
        AEROCRETE("Газобетон", 0.95, 1.07),
        CERAMIC_BLOCK("Тёплая керамика", 1.0, 1.11),
        BRICK("Керамический кирпич", 1.0, 1.0);

        private final String ruName;
        private final double workCoefficient;
        private final double materialCoefficient;

        WallMaterial(String ruName, double workCoefficient, double materialCoefficient) {
            this.ruName = ruName;
            this.workCoefficient = workCoefficient;
            this.materialCoefficient = materialCoefficient;
        }
    }

    @Getter
    public enum BaseType {
        PILE_GRILLAGE("Свайно-ростверковый", 1.0, 1.0),
        DRIVEN_PILES("Забивные сваи", 1.1, 1.15),
        MONOLITHIC_SLAB("Монолитная плита", 1.05, 1.1);

        private final String ruName;
        private final double workCoefficient;
        private final double materialCoefficient;

        BaseType(String ruName, double workCoefficient, double materialCoefficient) {
            this.ruName = ruName;
            this.workCoefficient = workCoefficient;
            this.materialCoefficient = materialCoefficient;
        }
    }
    @Getter
    public enum RoofType {
        METAL_TILE("Металлочерепица", 1.0, 1.0),
        PROFILED_SHHETING("Профнастил", 1.0, 0.9),
        SHINGLES("Битумная черепица", 1.1, 1.1);

        private final String ruName;
        private final double workCoefficient;
        private final double materialCoefficient;

        RoofType(String ruName, double workCoefficient, double materialCoefficient) {
            this.ruName = ruName;
            this.workCoefficient = workCoefficient;
            this.materialCoefficient = materialCoefficient;
        }
    }

    @Override
    public String toString() {
        return "CalcParam{" +
                "floors=" + floors +
                ", wallMaterial=" + wallMaterial +
                ", baseType=" + baseType +
                ", roofType=" + roofType +
                ", houseArea=" + houseArea +
                '}';
    }
}
