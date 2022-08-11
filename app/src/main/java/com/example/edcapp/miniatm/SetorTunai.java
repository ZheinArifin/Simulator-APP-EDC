package com.example.edcapp.miniatm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.edcapp.R;
import com.example.edcapp.tools.MyTools;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SetorTunai extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnTF, btnTT, btnST, btnSaldo;
    Button btnLanjut;
    EditText etNominal, etNorek;
    MyTools tools;
    Spinner spBank;
    public static final int uiRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setor_tunai);
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

        spBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item.toString().equals("BCA"))
                    setNoRek(10);
                else if(item.toString().equals("BNI"))
                    setNoRek(10);
                else if(item.toString().equals("Mandiri"))
                    setNoRek(13);
                else if(item.toString().equals("BRI"))
                    setNoRek(15);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        tools       = new MyTools();
        btnTF       = findViewById(R.id.btnTF);
        btnTT       = findViewById(R.id.btnTT);
        btnST       = findViewById(R.id.btnST);
        btnSaldo    = findViewById(R.id.btnCS);
        etNominal   = findViewById(R.id.etNominal);
        btnLanjut   = findViewById(R.id.btnLanjut);
        spBank      = findViewById(R.id.spBank);
        etNorek     = findViewById(R.id.etNorek);

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
                Intent tt   = new Intent(this, TarikTunai.class);
                startActivity(tt);
                finish();
                break;
            case R.id.btnST:
                break;
            case R.id.btnCS:
                Intent cs   = new Intent(this, CekSaldo.class);
                startActivity(cs);
                finish();
                break;
            case R.id.btnLanjut:
                if(isEmpty(etNominal.getText().toString(), etNorek.getText().toString()) == 0){
                    Toast.makeText(this, "Fitur Belum Tersedia", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void setNoRek(int i) {
        etNorek.setText("");
        etNorek.setFilters(new InputFilter[] { new InputFilter.LengthFilter(i) });
    }

    private int isEmpty(String a, String b){
        if(a.length() == 0){
            a = "0";
        }
        long nom    = Integer.valueOf(tools.changeFormat(a));
        int x       = 0;
        if (TextUtils.isEmpty(a) || a.equals(0) || nom == 0){
            etNominal.setError("Nominal Tidak Boleh Kosong!");
            x       = 1;
        }

        if (TextUtils.isEmpty(b) || a.equals(0)){
            etNorek.setError("Nomor Rekening Tidak Boleh Kosong!");
            x       = 1;
        }
        return x;
    }
}