package me.fenfei.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import static me.fenfei.app.MainActivity.TAG;
import static me.fenfei.app.MainActivity.value;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        setTitle("Main3Activity");


        Log.i(TAG, value + "=====this==Main3Activity===========");
    }
}
