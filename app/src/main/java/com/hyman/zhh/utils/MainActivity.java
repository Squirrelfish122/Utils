package com.hyman.zhh.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyman.zhh.utils.network.NetworkImp;
import com.hyman.zhh.utils.network.NetworkListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkImp.getInstance(false).post("", null, new NetworkListener<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFail(String reason) {

            }
        });
    }
}
