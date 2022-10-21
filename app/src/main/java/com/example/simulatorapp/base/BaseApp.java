package com.example.simulatorapp.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.pax.dal.IDAL;
import com.pax.market.android.app.sdk.BaseApiService;
import com.pax.market.android.app.sdk.StoreSdk;
import com.pax.neptunelite.api.NeptuneLiteUser;

public class BaseApp extends Application {
    private static IDAL dal;
    private static Context appContext;
    //todo make sure to replace with your own app's appkey and appsecret
    private static final String appkey = "FLAZZNDP000806202200";
    private static final String appSecret = "FLAZZNDP0008062022000FLAZZBCA00080620220";
    //todo please make sure get the correct SN here, for pax device you can integrate NeptuneLite SDK to get the correct SN
    private static final String SN = Build.SERIAL;
    public static SharedPreferences appPreferences;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "PARAMS";
    private static final String TAG = BaseApp.class.getSimpleName();
    private boolean isReadyToUpdate=true;


    @Override
    public void onCreate() {
        super.onCreate();

        //CEK EDC INGENICO FUNCTION
        appContext = getApplicationContext();
        dal = getDal();
        initPaxStoreSdk();
        appPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE); // this Preference comes for free from the library

//        initPaxStoreSdk();
//        appPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); // this Preference comes for free from the library


    }

    public static IDAL getDal(){
        if(dal == null){
            try {
                long start = System.currentTimeMillis();
                dal = NeptuneLiteUser.getInstance().getDal(appContext);
                Log.i("Test","get dal cost:"+(System.currentTimeMillis() - start)+" ms");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(appContext, "error occurred,DAL is null.", Toast.LENGTH_LONG).show();
            }
        }
        return dal;
    }

    private void initPaxStoreSdk() {
        //todo 1. Init AppKey，AppSecret and SN, make sure the appkey and appSecret is corret.
        StoreSdk.getInstance().init(getApplicationContext(), appkey, appSecret,  new BaseApiService.Callback() {
            @Override
            public void initSuccess() {
                Log.i(TAG, "initSuccess.");
                initInquirer();
            }

            @Override
            public void initFailed(RemoteException e) {
                Log.i(TAG, "initFailed: "+e.getMessage());
                Toast.makeText(getApplicationContext(), "Cannot get API URL from PAXSTORE, Please install PAXSTORE first.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void initInquirer() {
        //todo 2. Init checking of whether app can be updated

        StoreSdk.getInstance().initInquirer(new StoreSdk.Inquirer() {
            @Override
            public boolean isReadyUpdate() {
                Log.i(TAG, "call business function....isReadyUpdate = " + isReadyToUpdate);
                //todo call your business function here while is ready to update or not
                return isReadyToUpdate;
            }
        });
    }

//    public boolean isReadyToUpdate() {
//        return isReadyToUpdate;
//    }

//    public void setReadyToUpdate(boolean readyToUpdate) {
//        isReadyToUpdate = readyToUpdate;
//        if(isReadyToUpdate){
//            Toast.makeText(getApplicationContext(), getString(R.string.label_ready_to_update), Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(getApplicationContext(), getString(R.string.label_not_ready_to_update), Toast.LENGTH_SHORT).show();
//        }
//    }
}
