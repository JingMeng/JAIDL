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
        int size = bundle.size();
        Log.i(TAG, "========BundleActivity=====" + size);


        //你想探讨的是classLoader的问题
    }
}
