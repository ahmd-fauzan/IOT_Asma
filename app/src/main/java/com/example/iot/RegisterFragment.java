package com.example.iot;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;

public class RegisterFragment extends Fragment {

    private TextView etUsername;
    private TextView etDate;
    private TextView etEmail;
    private TextView etPassword;
    private TextView etConfirPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_register, container, false);;

        etUsername = view.findViewById(R.id.etUsername);
        etDate = view.findViewById(R.id.etDate);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirPassword = view.findViewById(R.id.etConfirPassword);

        Button btn = view.findViewById(R.id.btnRegister);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });
        return view;
    }

    private User createUser(){
        return new User(etUsername.getText().toString(), etDate.getText().toString());
    }

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

    public void registerAccount(String email, String password){
        DataModel.getAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Account").child(DataModel.getAuth().getCurrentUser().getUid());

                    myRef.child("User").setValue(createUser());

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}