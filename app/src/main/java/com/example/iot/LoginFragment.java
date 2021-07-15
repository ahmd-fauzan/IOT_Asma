package com.example.iot;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.iot.ViewModel.DataListener;
import com.example.iot.ViewModel.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {

    private FirebaseHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ProgressBar progressBar = view.findViewById(R.id.loginProgress);
        progressBar.setVisibility(View.INVISIBLE);
        helper = FirebaseHelper.getInstance();

        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPassword = view.findViewById(R.id.etPassword);
        Button btn = view.findViewById(R.id.btnLogin);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    //Login account dengan email dan password

}