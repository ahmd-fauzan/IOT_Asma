package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;
import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;

public class MainActivity extends AppCompatActivity implements AntaresHTTPAPI.OnResponseListener {

    //ANTARES
    private String TAG = "ANTARES-API";
    AntaresHTTPAPI antares;
    private static String ACCESS_KEY = "9e73d1645d6a3669:81bdb72d9318e03f";
    private static String PROJECT_NAME = "SistemMonitoringPenderitaAsma";
    private int currIndex = 0;

    //FIREBASE
    DatabaseReference myRef;

    //User
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize firebase authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Jika tidak terdapat user yang login, maka pindah ke activityLogin
        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        //Button untuk logout
        ExtendedFloatingActionButton btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                return;
            }
        });

        //Read data user from firebase every data change and in first run
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Account").child(auth.getCurrentUser().getUid());

        myRef.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);

                List<Fragment> fragmentList = new ArrayList<>();

                fragmentList.add(new HomeFragment(user.getUsername()));
                fragmentList.add(new HistoryFragment(getApplicationContext()));
                fragmentList.add(new CallFragment(getApplicationContext()));
                fragmentList.add(new ProfileFragment());

                ViewPager viewPager = findViewById(R.id.viewPager);
                SpaceTabLayout tabLayout = findViewById(R.id.spaceTabLayout);
                tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, null);

                content();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Initilize antares
        antares = new AntaresHTTPAPI();
        antares.addListener(this);


    }

    //Read data terbaru dari antares
    private void readData(){
        antares.getLatestDataofDevice(0, ACCESS_KEY, PROJECT_NAME, user.getDeviceName());
    }

    //** Respon saat menggunakan method antares **//
    @Override
    public void onResponse(AntaresResponse antaresResponse) {

        //** Jika REQUEST_CODE 0 maka mengambil data terbaru dari antares
        if(antaresResponse.getRequestCode() == 0){
            try {
                String body = new JSONObject(antaresResponse.getBody()).getJSONObject("m2m:cin").getString("con");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject temp = new JSONObject(body);
                            History history = new History();
                            history.setDate(dateToString());
                            history.setDebu(temp.getInt("dustDensity"));
                            history.setKbb(temp.getInt("humidity"));
                            history.setDetak(temp.getInt("heartRate"));

                            //Insert data terbaru dari antares pada firebase
                            insertHistory(history);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        //** Jika REQUEST_CODE 1 maka cek jumlah panjang data antares. Jika total data di antares berubah (ada data baru) maka read data terbaru
        if(antaresResponse.getRequestCode() == 1){
            JSONObject body = null;
            try {
                body = new JSONObject(antaresResponse.getBody());
                JSONArray array = body.getJSONArray("m2m:uril");

                Log.d(TAG, array.length() + "");

                if(currIndex == 0  && array.length() != 0){
                    currIndex = array.length();
                }
                else {
                    if(currIndex != array.length()){
                        readData();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //Insert data Sensor ke firebase
    private void insertHistory(History history){
        myRef.child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long index = snapshot.getChildrenCount();

                myRef.child("History").child(String.valueOf(index)).setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("STATE", "Data berhasil disimpan");
                        }
                        else {
                            Log.d("STATE", "Data gagal disimpan");
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Mengubah tipe data Date to String
    private String dateToString(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    //Setiap 1 minute cek data antares apakah berubah atau tidak
    public void content(){
        antares.getDataIDofDevice(1, ACCESS_KEY, PROJECT_NAME, user.getDeviceName());

        refresh(1000);
    }

    //Setiap satu menit refresh, cek jumlah data antares
    public void refresh(int milisecond){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };

        handler.postDelayed(runnable, milisecond * 60);
    }
}