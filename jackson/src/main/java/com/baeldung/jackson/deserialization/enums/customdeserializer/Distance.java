package com.baeldung.jackson.deserialization.enums.customdeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum Distance {

    KILOMETER("km", 1000), MILE("miles", 1609.34), METER("meters", 1), INCH("inches", 0.0254), CENTIMETER("cm", 0.01), MILLIMETER("mm", 0.001);

    private String unit;
    private final double meters;

    private Distance(String unit, double meters) {
        this.unit = unit;
        this.meters = meters;
    }

    public double getMeters() {
        return meters;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}