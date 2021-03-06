package me.fenfei.messenger2.services;

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


/**
 * 双亲委托机制
 */
public class Messenger2Service extends Service {
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
                    //   java.lang.ClassNotFoundException: me.fenfei.messenger.services.pojo.Student
                    Bundle data = msg.getData();
//                    多设置一个步骤
                    //   android.os.BadParcelableException: ClassNotFoundException when unmarshalling: me.fenfei.messenger.services.pojo.Student
                    //加上下面的这句话，就会产生另外一个报错，也就是上面的报错
                    //保证结构和位置一样就好
                    Log.i(TAG, "=====================" + data.getClassLoader());

//                    data.setClassLoader(Bundle.class.getClassLoader());
                    data.setClassLoader(Student.class.getClassLoader());
                    // Class not found using the boot class loader; no stack trace available
                    //去掉的话就可以很好的实现
//                    这个是如何记住的???
//                    java.lang.ClassNotFoundException: me.fenfei.messenger.pojo.Student
                    Log.i(TAG, "=====================" + data.getClassLoader());
                    //验证
//                    dalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/me.fenfei.messenger-2Ief7oc-E20lg5IQf5ImHA==/base.apk"],nativeLibraryDirectories=[/data/app/me.fenfei.messenger-2Ief7oc-E20lg5IQf5ImHA==/lib/arm64, /system/lib64, /vendor/lib64]]]===
//                    =====================null

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
