package com.example.w11.ui.settings;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.w11.MainActivity;
import com.example.w11.R;
import com.example.w11.SettingsData;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private Switch edit_sw;
    private Button saveButton;
    private SeekBar fontSb;
    private SeekBar heightSb;
    private SeekBar widthSb;
    private Switch caps_sw;
    private EditText displayText;
    private Spinner langSpinner;
    ArrayAdapter<String> adapter;
    String[] langs = new String[] {
            "Suomi", "Svenska", "English"
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        edit_sw = root.findViewById(R.id.edit_sw);
        saveButton = root.findViewById(R.id.saveButton);
        fontSb = root.findViewById(R.id.font_sb);
        fontSb.setMin(8);
        fontSb.setMax(20);
        heightSb = root.findViewById(R.id.height_sb);
        heightSb.setMin(100);
        heightSb.setMax(1000);
        widthSb = root.findViewById(R.id.width_sb);
        widthSb.setMin(100);
        widthSb.setMax(1000);
        caps_sw = root.findViewById(R.id.caps_sw);
        displayText = root.findViewById(R.id.editDisplayText);
        langSpinner = (Spinner) root.findViewById(R.id.lang_sel);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, langs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(adapter);
        loadValues();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsData SD = SettingsData.getInstance();
                SD.setFont(fontSb.getProgress());
                SD.setHeight(heightSb.getProgress());
                SD.setWidth(widthSb.getProgress());
                SD.setCaps(caps_sw.isChecked());
                SD.setEdit(edit_sw.isChecked());
                SD.setDisplayText(displayText.getText().toString());
                SD.setLang(langSpinner.getSelectedItem().toString());
                if (langSpinner.getSelectedItem().toString().equals("Suomi")) {
                    setLocale("fi");
                } else if (langSpinner.getSelectedItem().toString().equals("English")) {
                    setLocale("en");
                } else {
                    Toast.makeText(getView().getContext(), "No translation found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    private void setLocale(String locale) {
        Resources res = getContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(locale);
        res.updateConfiguration(conf, dm);
        getActivity().recreate();
    }

    private void loadValues() {
        SettingsData SD = SettingsData.getInstance();
        fontSb.setProgress(SD.getFont());
        heightSb.setProgress(SD.getHeight());
        widthSb.setProgress(SD.getWidth());
        caps_sw.setChecked(SD.getCaps());
        edit_sw.setChecked(SD.getEdit());
        displayText.setText(SD.getDisplayText());
        int pos = adapter.getPosition(SD.getLang());
        langSpinner.setSelection(pos);
    }
}