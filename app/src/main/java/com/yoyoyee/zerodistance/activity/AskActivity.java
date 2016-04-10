package com.yoyoyee.zerodistance.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;

public class AskActivity extends AppCompatActivity {
    protected TextView TV;
    protected EditText ET;
    protected Button OK,NO;
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
        OK.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent =getIntent();
                if (intent.getBooleanExtra("isGroup",false)) {
                    ClientFunctions.publishGroupQA(intent.getIntExtra("group_missionID", 0), ET.getText().toString(), new ClientResponse() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(AskActivity.this,"發問成功", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onErrorResponse(String response) {
                            Toast.makeText(AskActivity.this,"發問失敗", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else if (intent.getBooleanExtra("isGroup",false))
                {
                    ClientFunctions.publishMissionQA(intent.getIntExtra("group_missionID", 0), ET.getText().toString(), new ClientResponse() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(AskActivity.this,"發問成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onErrorResponse(String response) {
                            Toast.makeText(AskActivity.this,"發問失敗", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // ClientFunctions.publishMissionQA();
                }
                finish();
            }
        });

    }
}
