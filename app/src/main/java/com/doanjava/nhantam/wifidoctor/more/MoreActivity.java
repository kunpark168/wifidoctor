package com.doanjava.nhantam.wifidoctor.more;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doanjava.nhantam.wifidoctor.R;
import com.doanjava.nhantam.wifidoctor.settings.SettingsActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout layoutAdsDetail;
    private ProgressBar progressBar;
    private LinearLayout rippleRate, rippleSettings, rippleShare, rippleFeedback;
    private ImageView rippleBackMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        initView ();
    }

    private void initView() {
        layoutAdsDetail = findViewById(R.id.layoutAdsMore);
        progressBar = findViewById(R.id.progressBar);
        rippleRate = findViewById(R.id.rippleRate);
        rippleSettings = findViewById(R.id.rippleSettings);
        rippleShare = findViewById(R.id.rippleShare);
        rippleFeedback = findViewById(R.id.rippleFeedBack);
        rippleBackMore = findViewById(R.id.rippleBackMore);

        
        MobileAds.initialize(this, getResources().getString(R.string.ADMOB_APP_ID));
        rippleRate.setOnClickListener(this);
        rippleSettings.setOnClickListener(this);
        rippleShare.setOnClickListener(this);
        rippleFeedback.setOnClickListener(this);
        rippleBackMore.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNativeAd();
    }
    private void loadNativeAd() {


        AdLoader.Builder builder = new AdLoader.Builder(this, getResources().getString(R.string.ADMOB_AD_UNIT_ID));
        AdLoader adLoader = builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                // An app install ad loaded successfully, call this method again to
                // load the next ad.
                NativeAppInstallAdView adView = (NativeAppInstallAdView) LayoutInflater.from(MoreActivity.this)
                        .inflate(R.layout.ad_app_install, null);
                populateAppInstallAdView(ad, adView);
                layoutAdsDetail.setVisibility(View.VISIBLE);
                layoutAdsDetail.removeAllViews();
                layoutAdsDetail.addView(adView);


            }
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // A native ad failed to load. Call this method again to load
                // the next ad in the items list.
                android.util.Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                        + " load another.");
                loadNativeAd();
            }
        }).build();
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adLoader.loadAd(adRequest);
    }
    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd,
                                          NativeAppInstallAdView adView) {
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        progressBar.setVisibility(View.GONE);
        VideoController vc = nativeAppInstallAd.getVideoController();

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                // Publishers should allow native ads to complete video playback before refreshing
                // or replacing them with another ad in the same UI location.
                super.onVideoEnd();
            }
        });

        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
        ((ImageView) adView.getIconView()).setImageDrawable(
                nativeAppInstallAd.getIcon().getDrawable());

        MediaView mediaView = adView.findViewById(R.id.appinstall_media);
        ImageView mainImageView = adView.findViewById(R.id.appinstall_image);

        // Apps can check the VideoController's hasVideoContent property to determine if the
        // NativeAppInstallAd has a video asset.
        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mainImageView.setVisibility(View.GONE);
        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);

            // At least one image is guaranteed.
            List<NativeAd.Image> images = nativeAppInstallAd.getImages();
            mainImageView.setImageDrawable(images.get(0).getDrawable());
        }

        // These assets aren't guaranteed to be in every NativeAppInstallAd, so it's important to
        // check before trying to display them.

        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction().toString());

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAppInstallAd);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rippleRate :
                rateApp (MoreActivity.this);
                break;
            case R.id.rippleFeedBack :
                feedBack ();
                break;
            case R.id.rippleShare:
                shareApp ();
                break;
            case R.id.rippleSettings :
                settingApps ();
                break;
            case R.id.rippleBackMore :
                finish();
                break;
        }
    }

    private void settingApps(){
        startActivity(new Intent(MoreActivity.this, SettingsActivity.class));
    }

    private void feedBack() {

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"wifidoctor.feedback@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject_feedback));
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.message_feedback));
        startActivity(Intent.createChooser(emailIntent, "Complete action using"));
    }

    private void shareApp() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "WiFi Doctor has boosted my internet speed for 50%! You should try it too.");
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.security.wifi.boost");
        startActivity(Intent.createChooser(intent, "Invite Friends"));
    }

    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + "com.security.wifi.boost");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + "com.security.wifi.boost")));
        }
    }
}
