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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


    private String TAG = "ANTARES-API";
    AntaresHTTPAPI antaresHTTPAPI;
    private String dataDevice;
    DatabaseReference myRef;

    public static int currIndex = 0;
    private static String ACCESS_KEY = "334a5259011c3dfd:565865ee6bbac5d4";
    HistoryFactory historyFactory;

    User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        historyFactory = HistoryFactory.getInstance();

        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        ExtendedFloatingActionButton btnLogout = findViewById(R.id.btnLogout);
        ExtendedFloatingActionButton btnRefresh = findViewById(R.id.btnRefresh);
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
        myRef = database.getReference("Account").child(auth.getCurrentUser().getUid());

        List<Fragment> fragmentList = new ArrayList<>();





        myRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);

                fragmentList.add(new HomeFragment(user.getUsername()));
                fragmentList.add(new HistoryFragment(getApplicationContext()));
                fragmentList.add(new CallFragment(getApplicationContext()));
                fragmentList.add(new ProfileFragment(user, auth.getCurrentUser().getUid()));

                ViewPager viewPager = findViewById(R.id.viewPager);
                SpaceTabLayout tabLayout = findViewById(R.id.spaceTabLayout);
                tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        antaresHTTPAPI = new AntaresHTTPAPI();
        antaresHTTPAPI.addListener(this);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresHTTPAPI.getLatestDataofDevice("9e73d1645d6a3669:81bdb72d9318e03f", "SistemMonitoringPenderitaAsma", user.getDeviceName());
            }
        });
    }


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
                            //JSONObject temp = new JSONObject(dataDevice);
                            History history = new History(dateToString(), 90, 30, 50);
                            insertHistory(history);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Log.d(TAG, dataDevice);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void insertHistory(History history){
        myRef.child("History").child(String.valueOf(currIndex)).setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private String dateToString(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }
}