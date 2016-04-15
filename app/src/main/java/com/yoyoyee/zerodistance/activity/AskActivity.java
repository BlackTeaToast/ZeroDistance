package com.yoyoyee.zerodistance.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
    protected EditText ET;
    protected Button OK,NO;
    protected Boolean isAsk,isGroup,isQ;
    protected int group_mission_ID,q_a_ID;
    protected String a_Acontenttext,q_Qcontenttext;
    protected Boolean publisher;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        Intent intent =getIntent();
        isGroup=intent.getBooleanExtra("isGroup", false);
        isAsk=intent.getBooleanExtra("isAsk", true);
        group_mission_ID=intent.getIntExtra("group_missionID", 0);
        isQ=intent.getBooleanExtra("isQ", false);
        //toolbar 定位區
        toolbar= (Toolbar) findViewById(R.id.ask_Toolbar);


        //----------------------------------------------------------------------------------------------------------
        q_a_ID =intent.getIntExtra("q_a_ID",0);
        a_Acontenttext=intent.getStringExtra("content");
        q_Qcontenttext=intent.getStringExtra("q_Qcontenttext");
        findIDAndSetUse(isAsk,a_Acontenttext,q_Qcontenttext);

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
                            Toast.makeText(AskActivity.this, "完成", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onErrorResponse(String response) {
                            Toast.makeText(AskActivity.this, "失敗", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    ClientFunctions.publishMissionQA(q_a_ID, ET.getText().toString(), new ClientResponse() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(AskActivity.this, "完成", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onErrorResponse(String response) {
                            Toast.makeText(AskActivity.this, "發問失敗", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
            else if(isQ) {
                ClientFunctions.publishUpdateQuestion(q_a_ID, ET.getText().toString(), new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AskActivity.this, "完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        Toast.makeText(AskActivity.this, "發問失敗", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else  {
                ClientFunctions.publishQAAnswer(q_a_ID, ET.getText().toString(), new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AskActivity.this, "完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        Toast.makeText(AskActivity.this, "發問失敗", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            finish();
            }
        });

    }
    protected void findIDAndSetUse(Boolean isAsk,String a_Acontenttext,String q_Qcontenttext){
        ET = (EditText)findViewById(R.id.askEditText);
        OK = (Button)findViewById(R.id.askOk);
        NO = (Button)findViewById(R.id.askCancle);
        if(isAsk) {
            //ActionBar 設定區，主要為為了toolbar使用---------------------------------------------------
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(R.string.ask_for_title);
            OK.setText(R.string.ask_for_ok);
            NO.setText(R.string.ask_for_cancle);
        }
        else{
            try {
                if(!isQ) {
                    if (!a_Acontenttext.equals("null")) {
                        //ActionBar 設定區，主要為為了toolbar使用---------------------------------------------------
                        setSupportActionBar(toolbar);
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setTitle(R.string.ans_for_had_ans);
                    } else {
                        //ActionBar 設定區，主要為為了toolbar使用---------------------------------------------------
                        setSupportActionBar(toolbar);
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setTitle(R.string.ans_for_title);
                    }
                }
                else{
                    if(!q_Qcontenttext.equals("null")){
                        setSupportActionBar(toolbar);
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setTitle("編輯問題");
                    }
                }
            }
            catch (Exception ex){
                //ActionBar 設定區，主要為為了toolbar使用---------------------------------------------------
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle(R.string.ans_for_title);
            };
            OK.setText(R.string.ans_for_ok);
            NO.setText(R.string.ans_for_cancle);
        }
        try {
            if(!isQ) {
                if (!a_Acontenttext.equals("null")) {
                    ET.setText(a_Acontenttext);
                } else {
                }
            }
            else
                ET.setText(q_Qcontenttext);
        }
        catch (Exception ex){
        };
    }
}
