package com.example.simulatorapp.wifi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.simulatorapp.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestWifi extends AppCompatActivity implements View.OnClickListener {
    Button btnConect;
    EditText ipaddress, port;
    TextView status;
    private Socket socket;
    public static int SERVERPORT = 3003;
    InetAddress serverAddr;
    int stat;

    public static String SERVER_IP = "10.151.0.67";
    private ClientThread clientThread;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wifi);
        initView();
    }

    private void initView() {
        ipaddress   = findViewById(R.id.txt_ipaddress);
        port        = findViewById(R.id.txt_port);
        btnConect   = findViewById(R.id.btnConnect);
        status      = findViewById(R.id.status);

        btnConect.setOnClickListener(this);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConnect:
                if (ipaddress.getText().toString().equals("")){
                    ipaddress.setError("Data Tidak Boleh Kosong");
                }else if(port.getText().toString().equals("")){
                    port.setError("Data Tidak Boleh Kosong");
                }else{
                    SERVERPORT = Integer.parseInt(port.getText().toString());
                    SERVER_IP = ipaddress.getText().toString();
                    if(btnConect.getText().toString().toLowerCase().equals("connect")){
                        stat            = 0;
                        clientThread    = new ClientThread();
                        thread          = new Thread(clientThread);
                        thread.start();
                    }else{
                        stat            = -1;
                        updateMessage("Disconnected");
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
        }
    }

    public void updateMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(message.equals("Connected")){
                    btnConect.setText("Disconnect");
                }else{
                    btnConect.setText("Connect");
                }
                status.setText(message);
            }
        });
    }

    class ClientThread implements Runnable {
        private BufferedReader input;

        @Override
        public void run() {

            try {
                    serverAddr = InetAddress.getByName(ipaddress.getText().toString());
                    socket = new Socket(serverAddr, Integer.parseInt(port.getText().toString()));
                    updateMessage("Connected");
                    while (!Thread.currentThread().isInterrupted()) {
                        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String message = input.readLine();

                        if (message == null || "Disconnect".contentEquals(message)) {
                            Thread.interrupted();
                            message = "Server Disconnected";
                            updateMessage(message);

                            break;
                        }
                        updateMessage(message);
                    }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        void sendMessage(final String message) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (null != socket) {
                            PrintWriter out = new PrintWriter(new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream())),
                                    true);
                            out.println(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            //Do your refreshing
//            status();
//            //This basically reruns this runnable in 5 seconds
//            handler.postDelayed(this, 5000);
//        }
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != clientThread) {
            updateMessage("Disconnect");
            clientThread = null;
        }
    }
}