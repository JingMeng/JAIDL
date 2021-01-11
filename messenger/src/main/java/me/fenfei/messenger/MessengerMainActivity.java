package me.fenfei.messenger;

import androidx.appcompat.app.AppCompatActivity;
import me.fenfei.messenger.pojo.Student;
import me.fenfei.messenger.services.MessengerService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

/**
 * https://www.yuque.com/androidzhixin/androidzhixin/8b5835119d2bde1770d2a3bb7d33bcd9
 * <p>
 * https://juejin.cn/post/6844903665795334158
 */
public class MessengerMainActivity extends AppCompatActivity {

    private static final String TAG = "MessengerMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.say_hello2services2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sayHello(view);
            }
        });
    }

    /**
     * Messenger for communicating with the service.
     */
    Messenger mService = null;

    /**
     * Flag indicating whether we have called bind on the service.
     */
    boolean bound;

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = new Messenger(service);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            bound = false;
        }
    };

    public void sayHello(View v) {
        if (!bound) {
            return;
        }
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO2, 0, 0);
        Bundle bundle = new Bundle();

        bundle.putParcelable("msg", new Student("zyy"));
        msg.setData(bundle);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service

        if (true) {
            Intent intent = new Intent(this, MessengerService.class);
            bindService(intent, mConnection,
                    Context.BIND_AUTO_CREATE);
        } else {
            ComponentName componentName = new ComponentName("me.fenfei.messenger2", "me.fenfei.messenger2.services.Messenger2Service");
            Intent intent = new Intent();
            intent.setComponent(componentName);
            boolean success = bindService(intent, mConnection,
                    Context.BIND_AUTO_CREATE);
            Log.i(TAG,success+"==============");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
    }
}
