package com.example.edcapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edcapp.miniatm.TransferActivity;
import com.example.edcapp.wifi.TestWifi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Home extends AppCompatActivity implements View.OnClickListener {
    TextView date, time, times;
    ImageView bg;
    ImageButton btnUI, btnWifi, btnMiniAtm, btnSoon;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String dateTime;
    Locale id;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initTime();
        id  = new Locale("in", "ID");

        calendar            = Calendar.getInstance();
        simpleDateFormat    = new SimpleDateFormat("EEEE, dd MMMM yyyy", id);
        dateTime            = simpleDateFormat.format(calendar.getTime()).toString();
        date.setText(dateTime);



    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()
        {
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            times.setText(currentTime);
            Home.this.mHandler.postDelayed(m_Runnable, 1000);
        }
    };


    void initView(){
        time    = findViewById(R.id.time);
        times   = findViewById(R.id.times);
        date    = findViewById(R.id.tgl);
        bg      = new ImageView(this);
        bg      = findViewById(R.id.bg);
        btnUI   = findViewById(R.id.btnUI);
        btnWifi = findViewById(R.id.btnWifi);
        btnMiniAtm  = findViewById(R.id.btnMini);
        btnSoon = findViewById(R.id.btnSoon);

        btnUI.setOnClickListener(this);
        btnWifi.setOnClickListener(this);
        btnMiniAtm.setOnClickListener(this);
        btnSoon.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initTime();
        this.mHandler = new Handler();
        this.mHandler.postDelayed(m_Runnable,1000);
    }

    void initTime(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
           time.setText("Selamat Pagi");
           bg.setImageResource(R.drawable.pagi);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            time.setText("Selamat Siang");
            bg.setImageResource(R.drawable.siang);
        }else if(timeOfDay >= 16 && timeOfDay < 18){
            time.setText("Selamat Sore");
            bg.setImageResource(R.drawable.sore);
        }else if(timeOfDay >= 18 && timeOfDay < 24){
            time.setText("Selamat Malam");
            bg.setImageResource(R.drawable.malam);
        }
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        times.setText(currentTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUI:
                Intent ui   =  new Intent(this, MainActivity.class);
                startActivity(ui);
                break;
            case R.id.btnWifi:
                Intent wifi = new Intent(this, TestWifi.class);
                startActivity(wifi);
                break;
            case R.id.btnMini:
                Intent mini = new Intent(this, TransferActivity.class);
                startActivity(mini);
                break;
            case R.id.btnSoon:
                Toast.makeText(this, "Fitur Belum Tersedia", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}