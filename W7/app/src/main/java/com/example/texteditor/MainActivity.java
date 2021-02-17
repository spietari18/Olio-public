package com.example.texteditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
    }

    public void loadText(View view) {
        EditText fileNameField = (EditText) findViewById(R.id.fileName);
        EditText editField = (EditText) findViewById(R.id.editorTextBox);
        String fileName = fileNameField.getText().toString();
        try {
            InputStream ins = context.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String row = "";
            editField.setText("");
            while((row=br.readLine()) != null) {
                editField.append(row + "\n");
            }
            ins.close();
        } catch (IOException e) {
            Log.e("IOException", "Virhe lukemisessa");
        } finally {
            System.out.println("Ladattu " + fileName);
        }
    }

    public void saveText(View view) {
        EditText fileNameField = (EditText) findViewById(R.id.fileName);
        EditText editField = (EditText) findViewById(R.id.editorTextBox);
        String fileName = fileNameField.getText().toString();
        String text = editField.getText().toString();
        try {
            OutputStreamWriter ous = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            ous.write(text);
            ous.close();
        } catch (IOException e){
            Log.e("IOException", "Virhe kirjoittamisessa");
        } finally {
            System.out.println("Kirjoitettu " + fileName);
        }
    }
}