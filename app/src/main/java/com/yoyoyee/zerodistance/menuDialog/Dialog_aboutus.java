package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;

/**
 * Created by 楊霖村 on 2016/7/22.
 */
public class Dialog_aboutus extends Dialog {
    Button aboutexit;
    Context context;
    TextView talktous;
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
        talktous = (TextView) findViewById(R.id.talktous);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        aboutexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        talktous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setsentmail();
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
    private void setsentmail(){
        Log.i("Send email", "");

        String[] TO = {"yoyoyeeee@gmail.com"};
//        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "給YoYoYeeee小組的一封信");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "請在這裡寫下你的想法");

        try {
            context.startActivity(Intent.createChooser(emailIntent, "請選擇要使用的mail程式..."));
            cancel();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
