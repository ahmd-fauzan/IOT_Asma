package com.example.iot;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iot.Model.Kontak;
import com.example.iot.Model.User;
import com.example.iot.ViewModel.DataListener;
import com.example.iot.ViewModel.FirebaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterFragment extends Fragment {

    private EditText etUsername;
    private EditText etDate;
    private EditText etEmail;
    private EditText etDeviceName;
    private EditText etPassword;
    private EditText etConfirPassword;
    private EditText etNameCall;
    private EditText etTelpCall;
    private ImageView btnAdd;

    private LinearLayout parent;

    FirebaseHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_register, container, false);;

        ProgressBar progressBar = view.findViewById(R.id.registerProgress);
        progressBar.setVisibility(View.INVISIBLE);
        helper = FirebaseHelper.getInstance();

        etUsername = view.findViewById(R.id.etUsername);
        etDate = view.findViewById(R.id.etDate);
        etEmail = view.findViewById(R.id.etEmail);
        etDeviceName = view.findViewById(R.id.etDeviceName);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirPassword = view.findViewById(R.id.etConfirPassword);

        parent = view.findViewById(R.id.form_call);

        btnAdd = view.findViewById(R.id.ivAdd);

        ImageView btnDelete = view.findViewById(R.id.ivDelete);

        //Menghapus field form emergency cell
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = parent.getChildCount();
                parent.removeViewAt(pos - 1);
                parent.removeViewAt(pos - 2);
            }
        });

        //Menambahkan field form call emergency
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linear = new LinearLayout(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linear.setLayoutParams(params);
                linear.setOrientation(LinearLayout.HORIZONTAL);

                TextView txtName = new TextView(getContext());
                txtName.setText("Nama :");
                txtName.setTextAppearance(R.style.TextAppearance_AppCompat_Subhead);

                etNameCall = new EditText(getContext());
                etNameCall.setLayoutParams(params);

                linear.addView(txtName);
                linear.addView(etNameCall);

                LinearLayout linear2 = new LinearLayout(getContext());
                linear2.setLayoutParams(params);
                linear.setOrientation(LinearLayout.HORIZONTAL);

                TextView txtCall = new TextView(getContext());
                txtCall.setText("Telp :");
                txtCall.setTextAppearance(R.style.TextAppearance_AppCompat_Subhead);

                etTelpCall = new EditText(getContext());
                etTelpCall.setLayoutParams(params);

                linear2.addView(txtCall);
                linear2.addView(etTelpCall);

                parent.addView(linear);
                parent.addView(linear2);
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        Button btn = view.findViewById(R.id.btnRegister);

        //Register account
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.registerAccount(etEmail.getText().toString(), etPassword.getText().toString(), new DataListener() {
                    @Override
                    public void onProcess() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCompleteListener() {
                        helper.initializeAuth();
                        progressBar.setVisibility(View.INVISIBLE);
                        helper.insertUser(createUser(), new DataListener() {
                            @Override
                            public void onProcess() {
                                progressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onCompleteListener() {
                                progressBar.setVisibility(View.INVISIBLE);
                                helper.insertKontak(createKontaks(), new DataListener() {
                                    @Override
                                    public void onProcess() {
                                        progressBar.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onCompleteListener() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        return view;
    }

    //Membuat object user
    private User createUser(){
        return new User(etUsername.getText().toString(), etDate.getText().toString(), etDeviceName.getText().toString());
    }

    //Membuat object list kontak
    private List<Kontak> createKontaks(){
        List<Kontak> temps = new ArrayList<>();

        Kontak kontak = new Kontak();

        for(int i = 0; i < parent.getChildCount(); i++){
            LinearLayout temp = (LinearLayout)parent.getChildAt(i);

            EditText et = (EditText)temp.getChildAt(1);


            if(i % 2 != 0){
                kontak.setTelp(et.getText().toString());
                temps.add(kontak);
                kontak = null;
            }else {
                kontak = new Kontak();
                kontak.setName(et.getText().toString());
            }
        }
        return temps;
    }

    //Menampilkan date
    public void showDate(){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "data");
        datePickerFragment.setOnDateClickListener(new DatePickerFragment.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                String tahun = ""+calendar.get(Calendar.YEAR);
                String bulan;
                String hari;
                if((calendar.get(Calendar.MONTH) + 1) < 10)
                    bulan = "0"+(calendar.get(Calendar.MONTH) + 1);
                else
                    bulan = ""+(calendar.get(Calendar.MONTH ) + 1);

                if(calendar.get(Calendar.DAY_OF_MONTH) < 10)
                    hari = "0"+calendar.get(Calendar.DAY_OF_MONTH);
                else
                    hari = ""+calendar.get(Calendar.DAY_OF_MONTH);

                etDate.setText(hari + "/" + bulan + "/" + tahun);
            }
        });
    }
}