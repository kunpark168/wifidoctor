package com.doanjava.nhantam.wifidoctor.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.doanjava.nhantam.wifidoctor.R;
import com.doanjava.nhantam.wifidoctor.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressSplash = findViewById(R.id.progressSplash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressSplash.setVisibility(View.GONE);
                startActivity(new Intent(SplashActivity.this, NextToMainActivity.class));
            }
        }, 7000);
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
    }
}
