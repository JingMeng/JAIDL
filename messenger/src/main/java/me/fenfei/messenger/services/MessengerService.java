package me.fenfei.messenger.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import me.fenfei.messenger.pojo.Student;

public class MessengerService extends Service {
    /**
     * Command to the service to display a message
     */
    public static final int MSG_SAY_HELLO = 1;
    public static final int MSG_SAY_HELLO2 = 2;

    /**
     * Handler of incoming messages from clients.
     */
    static class IncomingHandler extends Handler {
        private static final String TAG = "IncomingHandler";
        private Context applicationContext;

        IncomingHandler(Context context) {
            applicationContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_SAY_HELLO2:
                    Toast.makeText(applicationContext, "hello!hello!", Toast.LENGTH_SHORT).show();
                    //   java.lang.ClassNotFoundException: me.fenfei.messenger.pojo.Student
                    Bundle data = msg.getData();
                    //按照方案代码，一下子就可以做到
                    data.setClassLoader(Student.class.getClassLoader());
                    Student student = data.getParcelable("msg");
                    Log.i(TAG, student + "");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    Messenger mMessenger;

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        mMessenger = new Messenger(new IncomingHandler(this));
        return mMessenger.getBinder();
    }
}
