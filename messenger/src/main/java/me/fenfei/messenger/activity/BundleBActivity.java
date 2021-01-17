package me.fenfei.messenger.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import me.fenfei.messenger.R;
import me.fenfei.messenger.services.aidl.Student;

public class BundleBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle_b);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("test");
        ClassLoader classLoader = bundle.getClassLoader();
        Student student =   bundle.getParcelable("cxzcxz");
        Log.i(BundleAActivity.TAG,student+"==================="+classLoader);
    }
}
