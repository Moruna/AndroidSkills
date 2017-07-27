package com.moruna.imagecachetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.moruna.imagecachetest.Util.BitmapUtil;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private BitmapUtil bitmapUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bitmapUtil = new BitmapUtil();
        imageView = (ImageView) findViewById(R.id.image);
        bitmapUtil.display(imageView, "http://pic129.nipic.com/" +
                "file/20170516/20614752_221848813000_2.jpg");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapUtil.display(imageView, "http://pic129.nipic.com/" +
                        "file/20170516/20614752_221848813000_2.jpg");
            }
        });
    }
}
