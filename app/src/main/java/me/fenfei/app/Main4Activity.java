package me.fenfei.app;

import androidx.appcompat.app.AppCompatActivity;
import me.fenfei.app.aidl.Add;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import static me.fenfei.app.MainActivity.TAG;
import static me.fenfei.app.MainActivity.value;


public class Main4Activity extends AppCompatActivity {

    private Add mAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        bindService();

        findViewById(R.id.sum_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    int sum = mAdd.add(1, 1);
                    Log.i(TAG, "====sum = " + sum);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void bindService() {
        Intent intent = new Intent(this, DoService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mAdd = Add.Stub.asInterface(service);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);
    }
}
