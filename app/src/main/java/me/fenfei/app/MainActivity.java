package me.fenfei.app;

import androidx.appcompat.app.AppCompatActivity;
import me.fenfei.app.utils.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "print_value";
    public static int value = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("MainActivity");
        Log.i(TAG, value + "=====to==Main2Activity===========" + Utils.getProcessName());
        findViewById(R.id.to_next_activity).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                value = 1;
                Log.i(TAG, value + "=====to==Main2Activity===========");
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.to_next_activity2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                value = 2;
                Log.i(TAG, value + "====to=====Main3Activity=========");
                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.to_next_activity3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main4Activity.class);
                startActivity(intent);
            }
        });
    }
}
