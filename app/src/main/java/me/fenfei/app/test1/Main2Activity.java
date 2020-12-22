package me.fenfei.app.test1;

import androidx.appcompat.app.AppCompatActivity;

import me.fenfei.app.R;
import me.fenfei.app.utils.Utils;

import android.os.Bundle;
import android.util.Log;

import static me.fenfei.app.MainActivity.value;
import static me.fenfei.app.MainActivity.TAG;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setTitle("Main2Activity");


        Log.i(TAG, value + "=====this==Main2Activity===========" + Utils.getProcessName());
    }
}
