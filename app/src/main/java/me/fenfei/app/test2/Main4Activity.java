package me.fenfei.app.test2;

import androidx.appcompat.app.AppCompatActivity;

import me.fenfei.app.R;
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


public class Main4Activity extends AppCompatActivity {

    private Add mAdd;
    private boolean remote = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        if (remote) {
            bindService();
        } else {
            bindService2();
        }

        findViewById(R.id.sum_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int sum = 0;
                    if (remote) {
                        sum = mAdd.add(1, 1);
                    } else {
                        sum = stubBinder.add(1, 1);
                    }
                    Log.i(TAG, "====sum = " + sum);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void bindService() {
//        Intent intent = new Intent(this, DoService.class);
        Intent intent = new Intent();
        Class clazz = DoService.class;
        intent.setClassName(clazz.getPackage().getName(), clazz.getName());
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

    private DoService.StubBinder stubBinder;

    /**
     * 做基本的测试
     * 这个地方返回的是一个代理对象，并不是原始对象，所以做类型转换的时候报错了
     * 如果是在同一个进程则不会报错 {@link Main5Activity#bindService()}
     * <p>
     * ‘’java.lang.ClassCastException: android.os.BinderProxy cannot be cast to me.fenfei.app.test2.DoService$StubBinder
     * ‘’    at me.fenfei.app.test2.Main4Activity$3.onServiceConnected(Main4Activity.java:87)
     * ‘’    at android.app.LoadedApk$ServiceDispatcher.doConnected(LoadedApk.java:1730)
     * ‘’    at android.app.LoadedApk$ServiceDispatcher$RunConnection.run(LoadedApk.java:1762)
     * ‘’    at android.os.Handler.handleCallback(Handler.java:873)
     * ‘’    at android.os.Handler.dispatchMessage(Handler.java:99)
     * ‘’    at android.os.Looper.loop(Looper.java:193)
     * ‘’    at android.app.ActivityThread.main(ActivityThread.java:6669)
     * ‘’    at java.lang.reflect.Method.invoke(Native Method)
     * ‘’    at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)
     * ‘’    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:858)
     */
    private void bindService2() {
        Intent intent = new Intent(this, DoService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    stubBinder = (DoService.StubBinder) service;
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
