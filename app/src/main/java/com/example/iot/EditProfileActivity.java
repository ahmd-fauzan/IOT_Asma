package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.iot.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etUsername;
    private  EditText etTanggalLahir;
    private EditText etDeviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etUsername = findViewById(R.id.etEditUsername);
        etTanggalLahir = findViewById(R.id.etEditTanggalLahir);
        etDeviceName = findViewById(R.id.etEditDeviceName);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSave = findViewById(R.id.btnSave);

        //Menampilkan date saat EditText tanggal lahir ditekan
        etTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        //Jika Button Cancel ditekan
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Account").child(auth.getCurrentUser().getUid());

                //Write data user terbaru ke firebase
                myRef.child("User").setValue(new User(etUsername.getText().toString(), etTanggalLahir.getText().toString(), etDeviceName.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);

                            startActivity(intent);
                        }
                    }
                });
            }

        });
    }

    //Menampilkan date
    public void showDate(){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "data");
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

                etTanggalLahir.setText(hari + "/" + bulan + "/" + tahun);
            }
        });
    }
}