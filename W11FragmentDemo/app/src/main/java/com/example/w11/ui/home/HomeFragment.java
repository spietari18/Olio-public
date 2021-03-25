package com.example.w11.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.w11.R;
import com.example.w11.SettingsData;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    private TextView textView;
    private EditText textEdit;
    private TextView displayText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textView = root.findViewById(R.id.textView_home);
        textEdit = root.findViewById(R.id.editText_home);
        displayText = root.findViewById(R.id.displayTextView);
        if (SettingsData.getInstance().getEdit() == false) {
            textEdit.setEnabled(false);
            textEdit.setText("");
            textView.setText(SettingsData.getInstance().getText().toString());
            System.out.println("TextView: " + SettingsData.getInstance().getText().toString());
        } else {
            textEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    SettingsData.getInstance().setText(textEdit.getText().toString());
                }
            });
            textEdit.setEnabled(true);
            textEdit.setText(SettingsData.getInstance().getText().toString());
            textView.setText("");
        }
        textView.setTextSize(SettingsData.getInstance().getFont());
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) textView.getLayoutParams();
        params.height = SettingsData.getInstance().getHeight();
        params.width = SettingsData.getInstance().getWidth();
        textView.setAllCaps(SettingsData.getInstance().getCaps());
        displayText.setText(SettingsData.getInstance().getDisplayText().toString());
        return root;
    }
}