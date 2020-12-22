package me.fenfei.app.test2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import me.fenfei.app.R;

import static me.fenfei.app.MainActivity.TAG;

public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        bindService();
        findViewById(R.id.sum_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int sum = 0;
                    sum = binder.add(1, 1);
                    Log.i(TAG, "====sum = " + sum);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    DoService.StubBinder binder;

    public void bindService() {
        Intent intent = new Intent(this, DoService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    binder = (DoService.StubBinder) service;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);
    }
}
