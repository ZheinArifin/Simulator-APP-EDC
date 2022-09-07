package com.example.simulatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final Calendar myCalendar = Calendar.getInstance();
    EditText date, txt_amount, txt_package, tipAmount, qty, liter, product, txt_class, transaction, additional, server_port, ip_port;
    FloatingActionButton btn_send, btn_send1;
    Button btn_Connect;
    DatePickerDialog.OnDateSetListener dates;
    TimePickerDialog.OnTimeSetListener dates1;
    TextView txtMessage,status;
    String currentDateandTime, Status;
    Locale INDONESIA = new Locale("in", "ID");
    public static final int uiRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_amount  = findViewById(R.id.txt_amount);
        tipAmount   = findViewById(R.id.txt_tipAmount);
        qty         = findViewById(R.id.txt_quantity);
        liter       = findViewById(R.id.txt_liter);
        product     = findViewById(R.id.txt_produk);
        txtMessage  = findViewById(R.id.txt_message);

        txt_package = findViewById(R.id.txt_package);

        txt_class = findViewById(R.id.txt_class);
        btn_send    = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(this);
        SimpleDateFormat sdf = new SimpleDateFormat("ss", Locale.getDefault());
        currentDateandTime = sdf.format(new Date());

    }

    

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                Intent intent = new Intent();
                intent.putExtra("app", "com.pax_pm_ndp.edc");
                intent.putExtra("menu", "sale");
                intent.putExtra("amount", txt_amount.getText().toString());
                intent.putExtra("DateTime", sdf.format(new Date()));
                intent.putExtra("TransId", "PURCHASE");
                intent.putExtra("tip", this.tipAmount.getText().toString());
                intent.putExtra("quantity", this.qty.getText().toString());
                intent.putExtra("liter", this.liter.getText().toString());
                intent.putExtra("product", this.product.getText().toString());
                //"com.pax_pm_ndp.edc", "com.pax_pm_ndp.pay.MainActivity"
                intent.setClassName(txt_package.getText().toString(), txt_class.getText().toString());
                startActivityForResult(intent, 1);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        switch (requestCode){
            case uiRequestCode:


                String dt = data.getStringExtra("Data");
                String respon = data.getStringExtra("Respon");
                txtMessage.setText("");

                txtMessage.setText("Data : \n" + dt + "\nRespon : \n" + respon);
                System.out.println(dt);
                StringBuilder sb = new StringBuilder();
                sb.append("data respon = ");
                sb.append(dt);
                Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
                Log.e("abdul", "data respon = " + dt);


                break;
            default:
                Log.e("error", "data respon = "+requestCode );
                break;
        }



    }

    void status(String message){
        status.setText(message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(this, Home.class);
        startActivity(in);
        finish();
    }

    public void back(View view) {
        Intent in = new Intent(this, Home.class);
        startActivity(in);
        finish();
    }
}