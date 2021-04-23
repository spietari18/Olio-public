package net.kirte;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.fragment.app.Fragment;

public class LoginFormFragment extends Fragment {

    private EditText usernameField;
    private EditText passwordField;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch swRememberLogin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        usernameField = view.findViewById(R.id.editTextTextUsername);
        passwordField = view.findViewById(R.id.editTextTextPassword);
        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonPressed();
            }
        });
        Button newAccountButton = view.findViewById(R.id.newAccountButton);
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { newAccountButtonPressed(); }
        });
        swRememberLogin = view.findViewById(R.id.swRememberLogin);
        return view;
    }

    public void loginButtonPressed() {
        System.out.println("Login Button pressed...");
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        Boolean remember = swRememberLogin.isChecked();
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username or password is empty!");
            MainActivity.getInstance().makeToast("Käyttäjätunnus tai salasana on tyhjä!");
        }
        else {
            MainActivity.getInstance().login(username, password, remember);
        }
    }

    public void newAccountButtonPressed() {
        System.out.println("Create account button pressed...");
        MainActivity.getInstance().newUser();
    }

}
