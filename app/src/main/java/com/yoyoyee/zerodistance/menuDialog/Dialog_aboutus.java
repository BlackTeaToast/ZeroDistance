package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.yoyoyee.zerodistance.R;

/**
 * Created by 楊霖村 on 2016/7/22.
 */
public class Dialog_aboutus extends Dialog {
    Button aboutexit;
    public Dialog_aboutus(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_aboutus);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.personal_page);
        aboutexit = (Button)findViewById(R.id.aboutexit);
        FrameLayout about_fl = (FrameLayout)findViewById(R.id.about_fl);
//        about_fl.setBackgroundDrawable(new BitmapDrawable());
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        aboutexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

    }
}
