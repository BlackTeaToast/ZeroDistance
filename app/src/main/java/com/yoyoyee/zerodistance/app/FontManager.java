package com.yoyoyee.zerodistance.app;

/**
 * Created by futur on 2016/9/2.
 */
        import android.app.Activity;
        import android.graphics.Typeface;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

public class FontManager {
    public static final String TAG =" FontManager";
    public static void changeFonts(ViewGroup root, Activity act) {

        Typeface tf = Typeface.createFromAsset(act.getAssets(),
                "setofont.ttf");

        for (int i = 0; i < root.getChildCount(); i++) {
            Log.d(TAG, "changeFonts: "+String.valueOf(i));

            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                Log.d(TAG, "changeFonts: "+String.valueOf(i)+"_1");
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof Button) {
                Log.d(TAG, "changeFonts: "+String.valueOf(i)+"_2");
                ((Button) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                Log.d(TAG, "changeFonts: "+String.valueOf(i)+"_3");
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof ViewGroup) {
                Log.d(TAG, "changeFonts: "+String.valueOf(i)+"_4");
                changeFonts((ViewGroup) v, act);
            }
        }

    }
}