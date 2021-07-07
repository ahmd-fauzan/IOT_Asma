package com.example.iot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class ProfileFragment extends Fragment {

    TextView username;
    TextView age;
    TextView deviceName;
    private ImageView qrImage;
    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        username = view.findViewById(R.id.txtUsername);
        age = view.findViewById(R.id.txtUmur);
        deviceName = view.findViewById(R.id.txtDeviceName);

        qrImage = view.findViewById(R.id.ivQrCode);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference("Account").child(auth.getCurrentUser().getUid());

        //Read data user dari firebase
        myRef.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);

                username.setText(user.getUsername());
                age.setText(calculateAge(stringToDate(user.getDateOfBirth())) + " Tahun");
                deviceName.setText(user.getDeviceName());
                setupUserQRcode(auth.getCurrentUser().getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button btnEdit = view.findViewById(R.id.btnEdit);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private int calculateAge(Calendar date){
        LocalDate localDate = LocalDate.of(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DATE));
        Period diff = Period.between(localDate, LocalDate.now());
        return diff.getYears();
    }

    private Calendar stringToDate(String date){
        Calendar calendar = Calendar.getInstance();
        try {//w  w w.  j a va 2 s  .co m
            calendar.setTime(new SimpleDateFormat("dd/MM/yyyy")
                    .parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    private void setupUserQRcode(String firebaseUID) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = multiFormatWriter.encode(firebaseUID, BarcodeFormat.QR_CODE, 500, 500);
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}