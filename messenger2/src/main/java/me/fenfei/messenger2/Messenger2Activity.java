package me.fenfei.messenger2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Class not found when unmarshalling: me.fenfei.messenger.pojo.Student
 *
 * https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
 *
 *
 * https://stackoverflow.com/questions/1996294/problem-unmarshalling-parcelables
 *
 *
 */
public class Messenger2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger2);
    }
}
