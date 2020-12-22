package me.fenfei.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static me.fenfei.app2.MainActivity.TAG;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setTitle("Main2Activity");


        try {
            //获取程序A的context
            Context ctx = this.createPackageContext(
                    "me.fenfei.app", Context.CONTEXT_IGNORE_SECURITY);
            String msg = ctx.getString(R.string.app_name);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
