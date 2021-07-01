package com.example.iot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    List<History> histories = new ArrayList<>();

    User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = DataModel.getAuth();

        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Account").child(auth.getCurrentUser().getUid());


        List<Fragment> fragmentList = new ArrayList<>();



        myRef.child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i = 0; i < snapshot.getChildrenCount(); i++){
                    History history = snapshot.child(String.valueOf(i)).getValue(History.class);
                    histories.add(history);
                }

                myRef.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);

                        fragmentList.add(new HomeFragment(user.getUsername()));
                        fragmentList.add(new HistoryFragment(getApplicationContext(), histories));
                        fragmentList.add(new CallFragment(getApplicationContext()));

                        ViewPager viewPager = findViewById(R.id.viewPager);
                        SpaceTabLayout tabLayout = findViewById(R.id.spaceTabLayout);
                        tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*

        //antaresHTTPAPI = new AntaresHTTPAPI();
        //antaresHTTPAPI.addListener(this);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresHTTPAPI.getLatestDataofDevice("9e73d1645d6a3669:81bdb72d9318e03f", "SistemMonitoringPenderitaAsma", "SistemMonitoringPenderitaAsma");
                History history = new History(tanggal, detak jantung, kelembaban, debu);
                historyList.add(history);
                

            }
        });*/


    }

    private List<History> CreateData(){
        List<History> listData = new ArrayList<>();
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        return listData;
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