package com.moruna.aidlclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.moruna.aidlservice.IAIDLService;

public class AIDLClientActivity extends AppCompatActivity {
    private static final String TAG = "AIDLClientActivity";
    private IAIDLService aidlService;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidlService = IAIDLService.Stub.asInterface(service);
            try {
                String str = aidlService.toUpperCase("i am from aidl service");
                Log.e(TAG, "onServiceConnected: str=" + str);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidlclient);
        Log.e(TAG, "onCreate: " );
        Intent intent = new Intent("com.moruna.aidlservice.IAIDLService");
        //android 5.0以上需要不能隐式启动，不然会报错，需要指定报名
        intent.setPackage("com.moruna.aidlservice");
        bindService(intent, connection, BIND_AUTO_CREATE);
    }
}
