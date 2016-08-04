package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;

/**
 * Created by 楊霖村 on 2016/7/22.
 */
public class Dialog_aboutus extends Dialog {
    Button aboutexit;
    Context context;
    public Dialog_aboutus(Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_aboutus);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutexit = (Button)findViewById(R.id.aboutexit);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        aboutexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        setheight();
        setFontSize();
    }
    private void setheight(){
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.85); // 高度设置为屏幕的0.6
        getWindow().setAttributes(p);
    }
    private void setFontSize() {
        TextView textViewTemp;
        textViewTemp = (TextView) findViewById(R.id.talktous);
        SpannableString content = new SpannableString(context.getResources().getString(R.string.talktous));//畫底線
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textViewTemp.setText(content);//畫底線
    }
}
