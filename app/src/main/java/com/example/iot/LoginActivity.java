package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iot.ViewModel.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callFragment(new LoginFragment());
        tag = "Login";

        Button button = findViewById(R.id.btnSwitch);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setText(tag);

                if(tag == "Login"){
                    callFragment(new RegisterFragment());

                    tag = "SignUp";
                }
                else{
                    callFragment(new LoginFragment());
                    tag = "Login";
                }
            }
        });
    }

    private void callFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.form_container, fragment);
        ft.commit();
    }
}