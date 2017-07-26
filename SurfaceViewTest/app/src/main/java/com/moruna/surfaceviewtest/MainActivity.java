package com.moruna.surfaceviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.moruna.surfaceviewtest.Wave.WaveView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new SinView(this));
//        setContentView(R.layout.activity_main);
          setContentView(new WaveView(this));
    }
}
