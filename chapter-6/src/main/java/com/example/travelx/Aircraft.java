package com.example.travelx;

public class Aircraft {
    private String manufacturer;
    private String model;
    private double maxDistance;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    @Override
    public String toString() {
        return "Aircraft {" +
                " manufacturer = \"" + manufacturer + "\"" +
                ", model = \"" + model + "\"" +
                ", maxDistance = " + maxDistance +
                " }";
    }
}
