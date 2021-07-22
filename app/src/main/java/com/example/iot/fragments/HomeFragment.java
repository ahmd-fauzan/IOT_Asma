package com.example.iot.fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iot.activies.MainActivity;
import com.example.iot.R;
import com.example.iot.models.Debu;
import com.example.iot.models.DetakJantung;
import com.example.iot.models.History;
import com.example.iot.models.Kelembaban;
import com.example.iot.models.User;
import com.example.iot.viewmodels.AsmaViewModel;
import com.example.iot.viewmodels.AsmaViewModelFactory;
import com.example.iot.viewmodels.DataListener;
import com.example.iot.repository.FirebaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    FirebaseHelper helper;

    TextView txtKondisiTubuh;
    TextView txtKondisiRuangan;
    User user;

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
        TextView txtDetak = view.findViewById(R.id.txtDetakJantung);
        TextView txtKbb = view.findViewById(R.id.txtKelembaban);
        TextView txtDebu = view.findViewById(R.id.txtDebu);
        TextView username = view.findViewById(R.id.username);

        AsmaViewModelFactory factory = new AsmaViewModelFactory();
        AsmaViewModel viewModel = new ViewModelProvider(getActivity(), factory).get(AsmaViewModel.class);

        viewModel.getUser().observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                username.setText(user.getUsername());

                viewModel.getHistory().observe(getActivity(), new Observer<List<History>>() {
                    @Override
                    public void onChanged(List<History> histories) {
                    int size = histories.size();

                    if(size == 0)
                    return;

                    History history = histories.get(size - 1);

                    txtDetak.setText(history.getDetak() +"");
                    txtKbb.setText(history.getKbb() + "");
                    txtDebu.setText((int)history.getDebu() + "");

                        viewModel.getDetak().observe(getActivity(), new Observer<List<DetakJantung>>() {
                            @Override
                            public void onChanged(List<DetakJantung> detakJantungs) {
                                int umur = calculateAge(stringToDate(user.getDateOfBirth()));

                                for(DetakJantung detak : detakJantungs){
                                    if(umur >= detak.getMinUmur() && umur <= detak.getMaxUmur()){
                                        if(history.getDetak() >= detak.getMinDetak() && history.getDetak() <= detak.getMaxDetak()){
                                            txtKondisiTubuh.setText("Kondisi Tubuh Normal");
                                        }
                                        else{
                                            tampilNotifikasi("Kondisi Tubuh", "Bahaya Kondisi Tubuh Tidak Normal");
                                            txtKondisiTubuh.setText("Kondisi Tubuh Tidak Normal");
                                        }
                                        break;
                                    }
                                }

                                viewModel.getDebu().observe(getActivity(), new Observer<List<Debu>>() {
                                    @Override
                                    public void onChanged(List<Debu> debus) {

                                        for(Debu debu : debus){
                                            if(history.getDebu() >= debu.getMinKadar() && history.getDebu() <= debu.getMaxKadar()){
                                                txtKondisiRuangan.setText("Kondisi Debu : " + debu.getKondisi() + "\n");
                                                break;
                                            }
                                        }
                                    }
                                });

                                viewModel.getKelembaban().observe(getActivity(), new Observer<List<Kelembaban>>() {
                                    @Override
                                    public void onChanged(List<Kelembaban> kelembabans) {
                                        for(Kelembaban kelembaban : kelembabans){
                                            if(history.getKbb() >= kelembaban.getMinKadar() && history.getKbb() <= kelembaban.getMaxKadar()){
                                                txtKondisiRuangan.append("Kelembaban Ruangan : " + kelembaban.getKondisi());
                                                break;
                                            }
                                        }
                                    }
                                });

                                tampilNotifikasi("Kondisi Ruangan", txtKondisiRuangan.getText().toString());
                            }
                        });
                    }
                });

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

    private void tampilNotifikasi(String titleMessage, String message) {
        Intent intent;
        PendingIntent pendingIntent;
        NotificationManager notifManager = (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);

        String id = "ID_KOMPI";
        String title = "funtechsy";
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
        }
        builder = new NotificationCompat.Builder(getContext(), id);
        intent = new Intent(getContext().getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(getContext().getApplicationContext(), 0, intent, 0);
        builder.setContentTitle(titleMessage)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setTicker("tes")
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setPriority(Notification.PRIORITY_HIGH);
        Notification notification = builder.build();
        notifManager.notify(0, notification);
    }
}