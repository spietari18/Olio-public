package com.example.w8;

public class Bottle {
    private String name;
    private String manufacturer;
    private double total_energy;
    private double size;
    private double price;
    public Bottle(){
        name = "Pepsi Max";
        manufacturer = "Pepsi";
        total_energy = 0.3;
        size = 0.5;
        price = 1.80;
    }
    public Bottle(String nameIn, String manuf, double totE, double sizeIn, double priceIn){
        name = nameIn;
        manufacturer = manuf;
        total_energy = totE;
        size = sizeIn;
        price = priceIn;
    }

    public String getName(){
        return name;
    }
    public String getManufacturer(){
        return manufacturer;
    }
    public double getEnergy(){
        return total_energy;
    }
    public double getSize() {
        return size;
    }
    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return (name + " " + String.format("%1$,.1f", size));
    }
}
