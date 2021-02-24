package com.example.w8;

import java.util.ArrayList;

public class BottleDispenser {
    private final ArrayList<Bottle> bottles;
    private double money;
    private static BottleDispenser BD = new BottleDispenser();

    public BottleDispenser() {
        bottles = new ArrayList<>();
        Bottle B1 = new Bottle();
        bottles.add(B1);
        Bottle B2 = new Bottle("Pepsi Max", "Pepsi", 0.3, 1.5, 2.2);
        bottles.add(B2);
        Bottle B3 = new Bottle("Coca-Cola Zero", "Coca-Cola", 0.2, 0.5, 2.0);
        bottles.add(B3);
        Bottle B4 = new Bottle("Coca-Cola Zero", "Coca-Cola", 1.2, 1.5, 2.5);
        bottles.add(B4);
        Bottle B5 = new Bottle("Fanta Zero", "Coca-Cola", 0.7, 0.5, 1.95);
        bottles.add(B5);
        money = 0;
    }

    public static BottleDispenser getInstance() {
        return BD;
    }

    public String addMoney(float money) {
        this.money += money;
        return "Klink! Added more money!\n";
    }

    public String buyBottle(int bottleNo) {
        if (bottles.size() < 1) {
            return "Out of bottles!\n";
        }
        String ret = "";
        if ((bottleNo == -1) || (bottles.get(bottleNo) == null)) {
            ret = ret + "Bottle not found\n";
            return ret;
        }
        Bottle B = bottles.get(bottleNo);
        if (money >= B.getPrice()) {
            money -= B.getPrice();
            ret = ret + "KACHUNK! " + B.getName() + " came out of the dispenser!\n";
            bottles.remove(bottleNo);
        }
        else if (money < B.getPrice()) {
            ret = ret + "Add money first!\n";
        }
        return ret;
    }

    public int findBottle(String name, String size) {
        for (int i = 0; i < bottles.size(); i++) {
            if ((bottles.get(i).getName().equals(name)) && (bottles.get(i).getSize() == Double.parseDouble(size))) {
                System.out.println("Found: " + bottles.get(i).getName() + " " + bottles.get(i).getSize() + " " + Integer.toString(i));
                return i;
            }
        }
        return -1;
    }

    public Receipt getReceipt(Receipt receipt, int bottleNo) {
        String name = bottles.get(bottleNo).getName();
        Double price = (bottles.get(bottleNo).getPrice());
        receipt.setName(name);
        receipt.setPrice(price);
        return receipt;
    }

    public double returnMoney() {
        double r = money;
        money = 0;
        return r;
    }

    public ArrayList<Bottle> listBottles() {
        return bottles;
    }
}
