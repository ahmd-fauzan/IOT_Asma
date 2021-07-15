package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroAdapter introAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0;
    Button btnGetStarted;
    Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //when this activity on full launch we need to check if its openened before or not

        if (restorePresData()){

            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();

        }

        setContentView(R.layout.activity_tutorial);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 123);
        }

        //ini views
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator =findViewById(R.id.tab_indikator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        //fill list screen
        List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Welcome App!","Aplikasi yang dapat membantu penderita asmauntuk mendeteksi kondisi ruang dan kondisi tubuh. Alat deteksi ini dapat menunjukan kadar debu suatu ruangan dan juga bisa melakukan monitoring, dan kondisi jantung.",R.drawable.welcome));
        mList.add(new ScreenItem("Step 1","Login menggunakan akun yang telah terdaftar, jika belum memiliki akun silahkan melakukan registtasi akun terlebih dahulu.",R.drawable.tutor1));
        mList.add(new ScreenItem("Step 2","Aktifkan alat dengan menekan tombol power yang terletak pada samping kanan/kiri alat.",R.drawable.tutor2));
        mList.add(new ScreenItem("Step 3","Pilih mode yang kamu inginkan. jika anda ingin melakukan pengecekkan kondisi ruangan tekan tombol 'ROOM COND', jika anda ingin melakukan pengecekan kondisi tubuh (Detak Jantung) tekan tombol 'HEAR BEAT' kemudian masukan salah satu jari anda ke lubang yang ada pada gambar.",R.drawable.tutor3));

        //setup view pager
        screenPager = findViewById(R.id.screen_viewpager);
        introAdapter = new IntroAdapter(this,mList);
        screenPager.setAdapter(introAdapter);

        //st up tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);

        //next button click listener

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                position = screenPager.getCurrentItem();
                if(position <  mList.size()){

                    position++;
                    screenPager.setCurrentItem(position);

                }


                if (position == mList.size()-1){//when we rech to the last screen

                    // TODO : show  the GETSTARTED Button and hide the indicator and the next button

                    loadLastScreen();


                }
            }


        });

        //tab layout add change listener

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1){

                    loadLastScreen();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //get started button click listener

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //open main activity

                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);

                //also we need to save a boolean value storoge so next time the user run the app
                // we could know that he is already checked the intro screen activity
                //i'm going to use shared preference to that process

                savePrefsData();
                finish();


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResult){
        switch (requestCode){
            case 123 :
                if(grantResult[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permission, grantResult);
        }
    }
    private boolean restorePresData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return isIntroActivityOpnendBefore;

    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();
    }


    private void loadLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an  anomation the getstarted button
        // setup animation
        btnGetStarted.setAnimation(btnAnim);


    }
}