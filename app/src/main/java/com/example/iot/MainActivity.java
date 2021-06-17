package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;
import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;

public class MainActivity extends AppCompatActivity/* implements AntaresHTTPAPI.OnResponseListener*/ {


    private String TAG = "ANTARES-API";
    AntaresHTTPAPI antaresHTTPAPI;
    private String dataDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HistoryFragment(getApplicationContext()));
        fragmentList.add(new CallFragment(getApplicationContext()));

        ViewPager viewPager = findViewById(R.id.viewPager);
        SpaceTabLayout tabLayout = findViewById(R.id.spaceTabLayout);
        tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, null);


        /*

        ColorDrawable color = new ColorDrawable(Color.parseColor("#a5d1c7"));
        actionBar.setBackgroundDrawable(color);
        actionBar.setCustomView(R.layout.action_bar_layout);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_place, new HomeFragment());
        ft.commit();
        actionBar.hide();

        BottomNavigationView navigation = findViewById(R.id.bottomNavigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch (item.getItemId()){
                    case R.id.home:{
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_place, new HomeFragment());
                        ft.commit();
                        actionBar.hide();
                        break;
                    }
                    case R.id.history :{
                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.fragment_place, new HistoryFragment(getApplicationContext()));
                        ft1.commit();
                        actionBar.show();
                        ((TextView)actionBar.getCustomView().findViewById(R.id.label)).setText("History");
                        break;
                    }
                    case R.id.call : {
                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                        ft2.replace(R.id.fragment_place, new CallFragment(getApplicationContext()));
                        ft2.addToBackStack(null);
                        ft2.commit();
                        actionBar.show();
                        ((TextView)actionBar.getCustomView().findViewById(R.id.label)).setText("Emergency Call");
                        break;
                    }
                }

                return true;
            }
        });*/
        //antaresHTTPAPI = new AntaresHTTPAPI();
        //antaresHTTPAPI.addListener(this);

        /*
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresHTTPAPI.getLatestDataofDevice("9e73d1645d6a3669:81bdb72d9318e03f", "SistemMonitoringPenderitaAsma", "SistemMonitoringPenderitaAsma");
            }
        });*/


    }

    /*
    @Override
    public void onResponse(AntaresResponse antaresResponse) {
        Log.d(TAG, Integer.toString(antaresResponse.getRequestCode()));
        if(antaresResponse.getRequestCode() == 0){
            try {
                JSONObject body = new JSONObject(antaresResponse.getBody());
                dataDevice = body.getJSONObject("m2m:cin").getString("con");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject temp = new JSONObject(dataDevice);
                            txtData.setText("Temperature Ruangan: " + temp.getInt("temperature") + "     Kelembaban Ruangan" + temp.getInt("humidity"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                Log.d(TAG, dataDevice);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }*/
}