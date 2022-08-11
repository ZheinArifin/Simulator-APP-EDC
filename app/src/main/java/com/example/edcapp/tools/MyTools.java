package com.example.edcapp.tools;

import java.text.NumberFormat;
import java.util.Locale;

public class MyTools {
    public String changeFormat(String amount){
        return amount.replace(".","");
    }

    public String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getInstance(localeID);
        return formatRupiah.format(number);
    }
}
