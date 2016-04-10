package com.yoyoyee.zerodistance.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
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
    protected Boolean isAsk,isGroup;
    protected int group_mission_ID,q_a_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        Intent intent =getIntent();
        isGroup=intent.getBooleanExtra("isGroup", false);
        isAsk=intent.getBooleanExtra("isAsk",true);
        group_mission_ID=intent.getIntExtra("group_missionID", 0);
        q_a_ID =intent.getIntExtra("q_a_ID",0);
        findIDAndSetUse(isAsk);
        NO.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        OK.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(isAsk) {
                    if (isGroup) {
                        ClientFunctions.publishGroupQA(group_mission_ID, ET.getText().toString(), new ClientResponse() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(AskActivity.this, "發問成功", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onErrorResponse(String response) {
                                Toast.makeText(AskActivity.this, "發問失敗", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        ClientFunctions.publishMissionQA(group_mission_ID, ET.getText().toString(), new ClientResponse() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(AskActivity.this, "發問成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onErrorResponse(String response) {
                                Toast.makeText(AskActivity.this, "發問失敗", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else{
                    ClientFunctions.publishQAAnswer(q_a_ID, ET.getText().toString(), new ClientResponse() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(AskActivity.this,"回答成功",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onErrorResponse(String response) {
                            Toast.makeText(AskActivity.this,response,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                finish();
            }
        });

    }
    protected void findIDAndSetUse(Boolean isAsk){
        TV = (TextView)findViewById(R.id.askTitle);
        ET = (EditText)findViewById(R.id.askEditText);
        OK = (Button)findViewById(R.id.askOk);
        NO = (Button)findViewById(R.id.askCancle);
        if(isAsk) {
            TV.setText(R.string.ask_for_title);
            OK.setText(R.string.ask_for_ok);
            NO.setText(R.string.ask_for_cancle);
        }
        else{
            TV.setText(R.string.ans_for_title);
            OK.setText(R.string.ans_for_ok);
            NO.setText(R.string.ans_for_cancle);
        }
    }
}
