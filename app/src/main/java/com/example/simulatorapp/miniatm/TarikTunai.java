package com.example.simulatorapp.miniatm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.simulatorapp.R;
import com.example.simulatorapp.tools.MyTools;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TarikTunai extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnTF, btnTT, btnST, btnSaldo;
    EditText etNominal;
    Spinner spBank;
    MyTools tools;
    Button btnLanjut;
    TextView txt_message;

    public static final int uiRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarik_tunai);
        tools       = new MyTools();
        initView();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etNominal.addTextChangedListener(new TextWatcher() {
            String current = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    etNominal.removeTextChangedListener(this);

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String cleanString = s.toString().replaceAll(replaceable,
                            "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    NumberFormat formatter = NumberFormat
                            .getCurrencyInstance(local);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setParseIntegerOnly(true);
                    String formatted = formatter.format((parsed));

                    String replace = String.format("[Rp\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String clean = formatted.replaceAll(replace, "");

                    current = formatted;
                    etNominal.setText(clean);
                    etNominal.setSelection(clean.length());
                    etNominal.addTextChangedListener(this);
                }
            }
        });
    }

    private void initView() {
        btnTF       = findViewById(R.id.btnTF);
        btnTT       = findViewById(R.id.btnTT);
        btnST       = findViewById(R.id.btnST);
        btnSaldo    = findViewById(R.id.btnCS);
        spBank      = findViewById(R.id.spBank);
        btnLanjut   = findViewById(R.id.btnLanjut);
        etNominal   = findViewById(R.id.etNominal);
        txt_message = findViewById(R.id.txt_message);


        btnTF.setOnClickListener(this);
        btnTT.setOnClickListener(this);
        btnST.setOnClickListener(this);
        btnSaldo.setOnClickListener(this);
        btnLanjut.setOnClickListener(this);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnTF:
                Intent tf   = new Intent(this, TransferActivity.class);
                startActivity(tf);
                finish();
                break;
            case R.id.btnTT:
                //Intent tt   = new Intent(this, TarikTunai.class);
                //startActivity(tt);
                //finish();
                break;
            case R.id.btnST:
                Intent st   = new Intent(this, SetorTunai.class);
                startActivity(st);
                finish();
                break;
            case R.id.btnCS:
                Intent cs   = new Intent(this, CekSaldo.class);
                startActivity(cs);
                finish();
                break;
            case R.id.btnLanjut:
                if(isEmpty(etNominal.getText().toString()) == 0){
                    SimpleDateFormat sdf    = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    Intent next             = new Intent();

                    next.putExtra("App", "PartnerApps");
                    next.putExtra("menu", "withdrawal");
                    next.putExtra("amount", tools.changeFormat(etNominal.getText().toString()));
                    next.putExtra("feeAmount",  "1500");
                    next.putExtra("DateTime",  sdf.format(new Date()));
                    next.setClassName("com.pax_miniatm_ndp.edc", "com.pax_miniatm_ndp.pay.MainActivity");
                    startActivityForResult(next, uiRequestCode);
                }
                break;
        }
    }

    private int isEmpty(String a){
        if(a.length() == 0){
            a = "0";
        }
        long nom    = Integer.valueOf(tools.changeFormat(a));
        int x       = 0;
        if (TextUtils.isEmpty(a) || a.equals(0) || nom == 0){
            etNominal.setError("Nominal Tidak Boleh Kosong!");
            x       = 1;
        }

        return x;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    { super.onActivityResult( requestCode, resultCode, data );
        switch (requestCode) {
            case uiRequestCode:
                String dt = data.getStringExtra("Data");
                String respon = data.getStringExtra("Respon");

                etNominal.setText("");
                //Report
                Log.e("Data", dt );
                Log.e("Respone", respon );
                txt_message.setText("Respon : "+respon+"\n Data : "+dt);

                break;
        }
    }
}