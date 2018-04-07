package com.doanjava.nhantam.wifidoctor.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.doanjava.nhantam.wifidoctor.R;

/**
 * Created by Anderson on 24/03/2018.
 */

public class WDTextView extends AppCompatTextView {

    public WDTextView(Context context) {
        super(context);
        init(null);
    }

    public WDTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public WDTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WDTextView);
            String fontName = a.getString(R.styleable.WDTextView_fontTypeFaceName);
            try {
                if (fontName != null) {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                    setTypeface(myTypeface);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            a.recycle();
        }
    }



}

