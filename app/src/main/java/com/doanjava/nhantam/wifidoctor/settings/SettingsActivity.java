package com.doanjava.nhantam.wifidoctor.settings;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.doanjava.nhantam.wifidoctor.R;
import com.doanjava.nhantam.wifidoctor.Utils.PreferenceManager;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private ImageView rippleBack;
    private SwitchCompat chkRealTimeProtection, chkWFDetection, chkWFBoost, chkWeather, chkSuperBoost;
    private TextView txtRealTimeProtection, txtWFDetection, txtWFBoost, txtWeather, txtSuperBoost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView ();
        inItData ();
    }

    private void inItData() {
        chkRealTimeProtection.setChecked(PreferenceManager.getInstance(this).getRealTimeProtection());
        chkWFDetection.setChecked(PreferenceManager.getInstance(this).getWifiDetection());
        chkWFBoost.setChecked(PreferenceManager.getInstance(this).getWifiBoost());
        chkWeather.setChecked(PreferenceManager.getInstance(this).getWeather());
        chkSuperBoost.setChecked(PreferenceManager.getInstance(this).getSuperBoost());



        txtRealTimeProtection.setText(chkRealTimeProtection.isChecked() ? getResources().getString(R.string.on) : getResources().getString(R.string.off));
        txtWFDetection.setText(chkWFBoost.isChecked() ? getResources().getString(R.string.on) : getResources().getString(R.string.off));
        txtWFBoost.setText(chkWFBoost.isChecked() ? getResources().getString(R.string.on) : getResources().getString(R.string.off));
        txtWeather.setText(chkWeather.isChecked() ? getResources().getString(R.string.on) : getResources().getString(R.string.off));
        txtSuperBoost.setText(chkSuperBoost.isChecked() ? getResources().getString(R.string.on) : getResources().getString(R.string.off));
    }

    private void initView() {
        rippleBack = findViewById(R.id.rippleBackSettings);
        chkRealTimeProtection= findViewById(R.id.chkRealTimeProtection);
        chkWFDetection = findViewById(R.id.chkWFDetection);
        chkWFBoost = findViewById(R.id.chkWFBoost);
        chkWeather = findViewById(R.id.chkWeather);
        chkSuperBoost = findViewById(R.id.chkSuperBoost);
        txtRealTimeProtection = findViewById(R.id.txtRealTimeProtection);
        txtWFDetection = findViewById(R.id.txtWFDetection);
        txtWFBoost = findViewById(R.id.txtWFBoost);
        txtWeather = findViewById(R.id.txtWeather);
        txtSuperBoost = findViewById(R.id.txtSuperBoost);


        rippleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chkRealTimeProtection.setOnCheckedChangeListener(this);
        chkWFDetection.setOnCheckedChangeListener(this);
        chkWFBoost.setOnCheckedChangeListener(this);
        chkWeather.setOnCheckedChangeListener(this);
        chkSuperBoost.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        switch (id){
            case R.id.chkRealTimeProtection:
                PreferenceManager.getInstance(SettingsActivity.this).setRealTimeProtection(b);
                if (b) txtRealTimeProtection.setText(getResources().getString(R.string.on));
                else txtRealTimeProtection.setText(getResources().getString(R.string.off));
                break;
            case R.id.chkWFDetection:
                PreferenceManager.getInstance(SettingsActivity.this).setWifiDetection(b);
                if (b) txtWFDetection.setText(getResources().getString(R.string.on));
                else txtWFDetection.setText(getResources().getString(R.string.off));
                break;
            case R.id.chkWFBoost:
                PreferenceManager.getInstance(SettingsActivity.this).setWifiBoost(b);
                if (b) txtWFBoost.setText(getResources().getString(R.string.on));
                else txtWFBoost.setText(getResources().getString(R.string.off));
                break;
            case R.id.chkWeather:
                PreferenceManager.getInstance(SettingsActivity.this).setWeather(b);
                if (b) txtWeather.setText(getResources().getString(R.string.on));
                else txtWeather.setText(getResources().getString(R.string.off));
                break;
            case R.id.chkSuperBoost:
                PreferenceManager.getInstance(SettingsActivity.this).setSuperBoost(b);
                if (b) txtSuperBoost.setText(getResources().getString(R.string.on));
                else txtSuperBoost.setText(getResources().getString(R.string.off));
                break;
        }
    }
}
