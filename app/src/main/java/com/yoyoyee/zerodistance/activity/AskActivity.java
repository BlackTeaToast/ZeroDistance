package com.yoyoyee.zerodistance.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;

public class AskActivity extends AppCompatActivity {
    TextView TV;
    EditText ET;
    Button OK,NO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        TV = (TextView)findViewById(R.id.askTitle);
        ET = (EditText)findViewById(R.id.askEditText);
        OK = (Button)findViewById(R.id.askOk);
        NO = (Button)findViewById(R.id.askCancle);
        TV.setText(R.string.ask_for_title);
        OK.setText(R.string.ask_for_ok);
        NO.setText(R.string.ask_for_cancle);

        NO.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
