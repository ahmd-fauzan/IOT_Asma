package com.example.iot;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    private User user;
    private String uid;
    private ImageView qrImage;
    public ProfileFragment(User user, String uid){
        this.user = user;
        this.uid = uid;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView username = view.findViewById(R.id.txtUsername);
        TextView age = view.findViewById(R.id.txtUmur);
        qrImage = view.findViewById(R.id.ivQrCode);

        username.setText(user.getUsername());
        age.setText(calculateAge(stringToDate(user.getDateOfBirth())) + " Tahun");
        setupUserQRcode(uid);
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