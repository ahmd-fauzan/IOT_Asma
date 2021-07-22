package com.example.iot.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.iot.activies.MainActivity;
import com.example.iot.R;
import com.example.iot.viewmodels.DataListener;
import com.example.iot.repository.FirebaseHelper;


public class LoginFragment extends Fragment {

    private FirebaseHelper helper;
    EditText etEmail;
    EditText etPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ProgressBar progressBar = view.findViewById(R.id.loginProgress);
        progressBar.setVisibility(View.INVISIBLE);
        helper = FirebaseHelper.getInstance();

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        Button btn = view.findViewById(R.id.btnLogin);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEmail())
                    return;

                if(!validatePassword())
                    return;

                helper.loginAccount(etEmail.getText().toString(), etPassword.getText().toString(), new DataListener() {
                    @Override
                    public void onProcess() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCompleteListener() {
                        progressBar.setVisibility(View.INVISIBLE);
                        helper.initializeAuth();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
            }
        });


        return view;
    }

    private boolean validateEmail() {

        String val = etEmail.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            etEmail.setError("Field can not  be empity");
            return false;
        }

        if (!val.matches(checkEmail)){
            etEmail.setError("Invalid etEmail!");
            return false;
        }
        return true;

    }

    private boolean validatePassword() {

        String val = etPassword.getText().toString().trim();

        if (val.isEmpty()) {
            etPassword.setError("Field can not  be empity");
            return false;
        }

        return true;

    }
    //Login account dengan etEmail dan etPassword

}