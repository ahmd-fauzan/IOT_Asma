package com.example.iot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iot.Model.Debu;
import com.example.iot.Model.DetakJantung;
import com.example.iot.Model.History;
import com.example.iot.Model.Kelembaban;
import com.example.iot.Model.User;
import com.example.iot.ViewModel.DataListener;
import com.example.iot.ViewModel.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    FirebaseHelper helper;

    TextView txtKondisiTubuh;
    TextView txtKondisiRuangan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtKondisiRuangan = view.findViewById(R.id.txtKondisiRuangan);
        txtKondisiTubuh = view.findViewById(R.id.txtKondisiTubuh);

        helper = FirebaseHelper.getInstance();

        TextView txtDetak = view.findViewById(R.id.txtDetakJantung);
        TextView txtKbb = view.findViewById(R.id.txtKelembaban);
        TextView txtDebu = view.findViewById(R.id.txtDebu);
        TextView username = view.findViewById(R.id.username);

        helper.readLatestHistory(new DataListener() {
            @Override
            public void onCompleteListener() {
                History history = helper.getLatestHistory();
                User user = helper.getUser();

                username.setText(user.getUsername());
                txtDetak.setText(history.getDetak() +"");
                txtKbb.setText(history.getKbb() + "");
                txtDebu.setText((int)history.getDebu() + "");

                List<DetakJantung> detakJantung = helper.getDetakJantungs();
                List<Debu> debus = helper.getDebus();
                List<Kelembaban> kelembabans = helper.getKelembabans();
                int umur = calculateAge(stringToDate(user.getDateOfBirth()));

                for(DetakJantung detak : detakJantung){
                    if(umur >= detak.getMinUmur() && umur <= detak.getMaxUmur()){
                        if(history.getDetak() >= detak.getMinDetak() && history.getDetak() <= detak.getMaxDetak()){
                            txtKondisiTubuh.setText("Kondisi Tubuh Normal");
                        }
                        else{
                            txtKondisiTubuh.setText("Kondisi Tubuh Tidak Normal");
                        }
                        break;
                    }
                }

                for(Debu debu : debus){
                    if(history.getDebu() >= debu.getMinKadar() && history.getDebu() <= debu.getMaxKadar()){
                        txtKondisiRuangan.setText("Kondisi Debu : " + debu.getKondisi() + "\n");
                        break;
                    }
                }

                for(Kelembaban kelembaban : kelembabans){
                    if(history.getKbb() >= kelembaban.getMinKadar() && history.getKbb() <= kelembaban.getMaxKadar()){
                        txtKondisiRuangan.append("Kelembaban Ruangan : " + kelembaban.getKondisi());
                        break;
                    }
                }
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
}