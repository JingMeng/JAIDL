package me.fenfei.messenger2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.BaseBundle;
import android.os.Bundle;
import android.util.Log;

/**
 * Class not found when unmarshalling: me.fenfei.messenger.services.pojo.Student
 * <p>
 * https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
 * <p>
 * <p>
 * https://stackoverflow.com/questions/1996294/problem-unmarshalling-parcelables
 */
public class Messenger2Activity extends AppCompatActivity {

    private static final String TAG = "Messenger2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger2);

        ClassLoader classLoader1 = Activity.class.getClassLoader();
        ClassLoader classLoader = this.getClassLoader();
        Log.i(TAG, classLoader1 + "===================" + classLoader);
        ClassLoader classLoader2 = Bundle.class.getClassLoader();
        ClassLoader classLoader3 = BaseBundle.class.getClassLoader();
        Log.i(TAG, classLoader2 + "===================" + classLoader3);
        //问题1.进程间传递消息，ClassLoader 为什么会丢弃了，如果丢了的话，那就不是那个clasLoader了，加载的类都改变了
        //是真的丢失了吗？

        //

        ClassLoader classLoader4 = Object.class.getClassLoader();
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        Log.i(TAG, classLoader4 + "===================" + systemClassLoader);


//        BaseBundle  这个类并没有实现序列化的事情
        //Bundle 实现了
        //writeToParcelInner 在序列化的时候并没有处理这个classloader
    }
}
