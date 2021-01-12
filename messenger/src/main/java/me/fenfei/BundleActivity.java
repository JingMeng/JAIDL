package me.fenfei;

import androidx.appcompat.app.AppCompatActivity;
import me.fenfei.messenger.R;
import me.fenfei.messenger.services.pojo.Student;

import android.os.Bundle;
import android.util.Log;

public class BundleActivity extends AppCompatActivity {

    private static final String TAG = "BundleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle);

        Bundle bundle = new Bundle();
        bundle.putParcelable("student", new Student("zyy"));
        bundle.getParcelable("student");
        int size = bundle.size();
        Log.i(TAG, "========BundleActivity=====" + size);


        //你想探讨的是classLoader的问题
        ClassLoader defaultLoader = bundle.getClassLoader();

        ClassLoader studentLoader = Student.class.getClassLoader();
        try {
            Class<?> parcelableClass = Class.forName(Student.class.getName(), false /* initialize */, studentLoader);

            Log.i(TAG, defaultLoader + "============" + parcelableClass+"==========="+studentLoader);
        } catch (Exception e) {
            //: java.lang.ClassNotFoundException: me.fenfei.messenger.services.pojo.Student
            e.printStackTrace();
        }


//        https://blog.csdn.net/ylyg050518/article/details/72638852
//        在大部分情况下mParcelledData都是null，因此unparcel()直接返回。当使用构造函数public Bundle(Bundle b)创建Bundle时，会给mParcelledData赋值，具体实现如下：

    }
}
