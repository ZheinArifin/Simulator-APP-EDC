package com.example.edcapp.miniatm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.edcapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CekSaldo extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnTF, btnTT, btnST, btnSaldo;
    Button btnLanjut;
    TextView txt_message;
    public static final int uiRequestCode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_saldo);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();
    }

    private void initView() {
        btnTF       = findViewById(R.id.btnTF);
        btnTT       = findViewById(R.id.btnTT);
        btnST       = findViewById(R.id.btnST);
        btnSaldo    = findViewById(R.id.btnCS);
        btnLanjut   = findViewById(R.id.btnLanjut);
        txt_message = findViewById(R.id.txt_message);


        btnTF.setOnClickListener(this);
        btnTT.setOnClickListener(this);
        btnST.setOnClickListener(this);
        btnSaldo.setOnClickListener(this);
        btnLanjut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTF:
                Intent tf = new Intent(this, TransferActivity.class);
                startActivity(tf);
                finish();
                break;
            case R.id.btnTT:
                Intent tt   = new Intent(this, TarikTunai.class);
                startActivity(tt);
                finish();
                break;
            case R.id.btnST:
                Intent st = new Intent(this, SetorTunai.class);
                startActivity(st);
                finish();
                break;
            case R.id.btnCS:
                break;
            case R.id.btnLanjut:
                SimpleDateFormat tgl = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                Intent cs = new Intent();

                cs.putExtra("App", "PartnerApps");
                cs.putExtra("menu", "inquiry");
                cs.putExtra("feeAmount", "4000");
                cs.putExtra("DateTime", tgl.format(new Date()));
                cs.setClassName("com.pax_miniatm_ndp.edc", "com.pax_miniatm_ndp.pay.MainActivity");
                startActivityForResult(cs, uiRequestCode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data );
        switch (requestCode) {
            case uiRequestCode:
                String dt = data.getStringExtra("Data");
                String respon = data.getStringExtra("Respon");


                //Report
                Log.e("Data", dt );
                Log.e("Respone", respon );
                txt_message.setText("Respon : "+respon+"\n Data : "+dt);

                break;
        }
    }


}