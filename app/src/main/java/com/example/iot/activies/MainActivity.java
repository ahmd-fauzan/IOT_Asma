package com.example.iot.activies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.iot.R;
import com.example.iot.fragments.CallFragment;
import com.example.iot.fragments.HistoryFragment;
import com.example.iot.fragments.HomeFragment;
import com.example.iot.fragments.ProfileFragment;
import com.example.iot.models.History;
import com.example.iot.models.User;
import com.example.iot.viewmodels.AsmaViewModel;
import com.example.iot.viewmodels.AsmaViewModelFactory;
import com.example.iot.viewmodels.DataListener;
import com.example.iot.repository.FirebaseHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

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

    int notifikasi = 1;
    FirebaseHelper helper;

    //FIREBASE

    //User
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = FirebaseHelper.getInstance();

        //Jika tidak terdapat user yang login, maka pindah ke activityLogin
        if(helper.getAccountUser() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        AsmaViewModelFactory factory = new AsmaViewModelFactory();
        AsmaViewModel viewModel = new ViewModelProvider(this, factory).get(AsmaViewModel.class);

        viewModel.readUser();
        viewModel.readKontak();

        //Button untuk logout
        ExtendedFloatingActionButton btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.logOut();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                return;
            }
        });

        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(new HomeFragment());
        fragmentList.add(new HistoryFragment(getApplicationContext()));
        fragmentList.add(new CallFragment(getApplicationContext()));
        fragmentList.add(new ProfileFragment());

        ViewPager viewPager = findViewById(R.id.viewPager);
        SpaceTabLayout tabLayout = findViewById(R.id.spaceTabLayout);
        tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, null);

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
                            history.setDebu(Float.parseFloat(temp.getString("dustDensity")));
                            history.setKbb(Integer.parseInt(temp.getString("humidity")));
                            history.setDetak(temp.getInt("heartRate"));

                            //Insert data terbaru dari antares pada firebase
                            helper.insertHistory(history, new DataListener() {
                                @Override
                                public void onProcess() {

                                }

                                @Override
                                public void onCompleteListener() {

                                }
                            });
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
                        currIndex = array.length();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //Insert data Sensor ke firebase

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