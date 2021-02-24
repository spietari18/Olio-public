package com.example.w8;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class Receipt {
    private String name;
    private Double price;
    private static Receipt receipt = new Receipt();

    public Receipt() { }

    public static Receipt getInstance() {
        return receipt;
    }

    public void print(Context context, String fileName) {
        String r = "******* RECEIPT *******\n\nBottle: " + name + "\nPrice: " + String.format("%1$,.2f", price) + "\n***********************";
        try {
            OutputStreamWriter ous = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            ous.write(r);
            ous.close();
        } catch (IOException e) {
            Log.e("IOException", "Virhe kuitin kirjoittamisessa");
        } finally {
            System.out.println("Kuitti kirjoitettu");
        }
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
