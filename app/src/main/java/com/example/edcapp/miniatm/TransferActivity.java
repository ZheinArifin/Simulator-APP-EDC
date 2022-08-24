package com.example.edcapp.miniatm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edcapp.R;
import com.example.edcapp.tools.MyTools;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransferActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnTF, btnTT, btnST, btnSaldo;
    EditText etNorek, etNominal, etBerita;
    TextView txt_message;
    Spinner spBank;
    Button Lanjut;
    MyTools tools;
    public static final int uiRequestCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
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
                else if(item.toString().equals("BTN"))
                    setNoRek(16);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setNoRek(int i) {
        etNorek.setText("");
        etNorek.setFilters(new InputFilter[] { new InputFilter.LengthFilter(i) });
    }

    private void initView() {
        tools       = new MyTools();
        btnTF       = findViewById(R.id.btnTF);
        btnTT       = findViewById(R.id.btnTT);
        btnST       = findViewById(R.id.btnST);
        btnSaldo    = findViewById(R.id.btnCS);
        spBank      = findViewById(R.id.spBank);
        etNorek     = findViewById(R.id.etNorek);
        etNominal   = findViewById(R.id.etNominal);
        etBerita    = findViewById(R.id.etBerita);
        Lanjut      = findViewById(R.id.btnLanjut);
        txt_message = findViewById(R.id.txt_message);

        btnTF.setOnClickListener(this);
        btnTT.setOnClickListener(this);
        btnST.setOnClickListener(this);
        btnSaldo.setOnClickListener(this);
        Lanjut.setOnClickListener(this);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnTF:
                break;
            case R.id.btnTT:
                Intent tt   = new Intent(this, TarikTunai.class);
                startActivity(tt);
                finish();
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
                if (isEmpty(etNominal.getText().toString(), etNorek.getText().toString()) == 0){
                    SimpleDateFormat sdf    = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault());
                    Intent next             = new Intent();

                    next.putExtra("App", "PartnerApps");
                    next.putExtra("menu", "transfer");
                    next.putExtra("amount", tools.changeFormat(etNominal.getText().toString()));
                    next.putExtra("feeAmount",  "1500");
                    next.putExtra("DateTime",  sdf.format(new Date()));
                    next.putExtra("toCBC",  codeBank(spBank.getSelectedItem().toString()));
                    next.putExtra("toAcc",  etNorek.getText().toString());
                    next.putExtra("reference",  etBerita.getText().toString());
                    next.setClassName("com.pax_miniatm_ndp.edc", "com.pax_miniatm_ndp.pay.MainActivity");
                    startActivityForResult(next, uiRequestCode);
                }
                break;
        }
    }

    public String codeBank(String bank){
        if(bank.equals("BNI"))
            return "009";
        else if(bank.equals("BANK MANDIRI"))
            return "008";
        else if(bank.equals("BANK BRI"))
            return "002";
        else if(bank.equals("BANK BCA"))
            return "014";
        else if(bank.equals("BANK BTN"))
            return "200";
        else if(bank.equals("BANK BJB"))
            return "110";
        else if(bank.equals("BANK BRI"))
            return "002";
        else if(bank.equals("BANK CITY BANK"))
            return "031";
        else if(bank.equals("BANK NOBU"))
            return "503";
        else if(bank.equals("BANK MISTIKA"))
            return "151";
        else if(bank.equals("BANK NTT"))
            return "130";
        else if(bank.equals("BANK DINAR"))
            return "526";
        else if(bank.equals("BANK PANIN"))
            return "019";
        else if(bank.equals("BANK PAPUA"))
            return "032";
        else if(bank.equals("BANK NTB"))
            return "128";
        else if(bank.equals("BANK SYARIAH BUKOPIN"))
            return "521";
        else if(bank.equals("BANK SYARIAH INDONESIA"))
            return "451";
        else if(bank.equals("BANK MEGA"))
            return "426";
        else if(bank.equals("BANK KALSEL"))
            return "122";
        else if(bank.equals("BANK OCBC"))
            return "028";
        else if(bank.equals("BANK MEGA SYARIAH"))
            return "506";
        else if(bank.equals("BANK MAYAPADA"))
            return "097";
        else if(bank.equals("BANK BTPN"))
            return "213";
        else if(bank.equals("BANK BTPN SYARIAH"))
            return "547";
        else if(bank.equals("BANK CIMB NIAGA"))
            return "022";
        else if(bank.equals("BANK PERMATA"))
            return "013";
        else if(bank.equals("BANK BPD DIY"))
            return "112";
        else if(bank.equals("BANK JATIM"))
            return "114";
        else if(bank.equals("BANK KALTIMTARA"))
            return "124";
        else if(bank.equals("BANK SULSELBAR"))
            return "126";
        else if(bank.equals("BANK DANAMON"))
            return "011";
        else if(bank.equals("BANK JAGO"))
            return "542";
        else if(bank.equals("BANK MYBANK"))
            return "016";
        else
            return "0";
    }

    private int isEmpty(String a, String b){
        if(a.length() == 0){
            a = "0";
        }
        int nom     = Integer.valueOf(tools.changeFormat(a));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data );
        switch (requestCode) {
            case uiRequestCode:
                String dt = data.getStringExtra("Data");
                String respon = data.getStringExtra("Respon");

                etNominal.setText("");
                etNorek.setText("");
                etBerita.setText("");
                //Report
                Log.e("Data", dt );
                Log.e("Respone", respon );
                txt_message.setText("Respon : "+respon+"\n Data : "+dt);

                break;
        }
    }

}