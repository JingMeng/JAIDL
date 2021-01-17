package me.fenfei.messenger.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import me.fenfei.messenger.R;
import me.fenfei.messenger.services.aidl.Student;

public class BundleAActivity extends AppCompatActivity {

    public static final String TAG = "BundleAActivity";

    public static Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle_a);

        findViewById(R.id.to_next_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BundleAActivity.this,BundleBActivity.class);
                 bundle = new Bundle();
                bundle.putParcelable("cxzcxz",new Student("zyy"));
                intent.putExtra("test",bundle);
                Log.i(TAG,"==================="+bundle.hashCode());
                startActivity(intent);
            }
        });
    }
}
