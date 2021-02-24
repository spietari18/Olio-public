package com.example.w8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context context = null;

    BottleDispenser BD = BottleDispenser.getInstance();
    private SeekBar seekBar;
    private Spinner bottleSpinner;
    private Spinner sizeSpinner;
    private ArrayAdapter<String> bottleAdapter;
    private ArrayAdapter<String> sizeAdapter;
    private ArrayList<String> bottleSize = new ArrayList<String>();
    private ArrayList<String> bottleName = new ArrayList<String>();
    private Receipt receipt = Receipt.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bottleSize.add("1.5");
        bottleSize.add("0.5");
        bottleSize.add("0.33");
        bottleName.add("Pepsi Max");
        bottleName.add("Coca-Cola Zero");
        bottleName.add("Fanta Zero");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView seekBarProgress = findViewById(R.id.seekBarProgress);
        seekBar = (SeekBar) findViewById(R.id.moneyAmount);
        seekBar.setMax(20);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarProgress.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        bottleSpinner = findViewById(R.id.selectBottleMenu);
        bottleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bottleName);
        bottleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bottleSpinner.setAdapter(bottleAdapter);
        sizeSpinner = findViewById(R.id.selectBottleSize);
        sizeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bottleSize);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
        context = MainActivity.this;


//        bottleAdapter = new ArrayAdapter<Bottle>(this, android.R.layout.simple_spinner_item, BD.listBottles());
//        bottleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        bottleAdapter.setNotifyOnChange(true);
//        bottleSpinner.setAdapter(bottleAdapter);
    }

    public void addMoney(View view) {
        float money = seekBar.getProgress();
        TextView textConsole = findViewById(R.id.textConsole);
        textConsole.append(BD.addMoney(money));
        seekBar.setProgress(0);
    }

    public void getSelectedBottle(View view) {
        String size = (String) sizeSpinner.getSelectedItem();
        String name = (String) bottleSpinner.getSelectedItem();
        System.out.println("OSTO: " + name + size);
//        Bottle bottle = (Bottle) bottleSpinner.getSelectedItem();
//        buyBottle(bottle);
        buyBottle(name, size);
    }

    public void buyBottle(String name, String size) {
        TextView textConsole = findViewById(R.id.textConsole);
        int bottleNo = BD.findBottle(name, size);
        if (bottleNo != -1) {receipt = BD.getReceipt(receipt, bottleNo);}
        textConsole.append(BD.buyBottle(bottleNo));
    }

    public void returnMoney(View view) {
        TextView textConsole = findViewById(R.id.textConsole);
        textConsole.append("Money returned " + String.format("%1$,.2f", BD.returnMoney()) + "\n");
    }

    public void receipt(View view) {
        String fileName = "receipt.txt";
        receipt.print(context, fileName);
    }
}