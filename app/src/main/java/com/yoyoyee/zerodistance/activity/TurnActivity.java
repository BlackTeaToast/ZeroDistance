package com.yoyoyee.zerodistance.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;

public class TurnActivity extends AppCompatActivity {

    private Button btnMain;
    private Button btnMission;
    private Button btnNewMission;
    private Button btnGroup;
    private Button btnNewGroup;
    private Button btnQA;
    private TextView tvExplain;
    private TextView tvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);

        //test use btn
        btnMain = (Button) findViewById(R.id.buttonMain);
        btnMission = (Button) findViewById(R.id.buttonMission);
        btnNewMission = (Button) findViewById(R.id.buttonNewMission);
        btnGroup = (Button) findViewById(R.id.buttonGroup);
        btnNewGroup = (Button) findViewById(R.id.buttonNewGroup);
        btnQA = (Button) findViewById(R.id.buttonQA);
        tvExplain = (TextView) findViewById(R.id.textViewTurnActExplain);
        tvView = (TextView) findViewById(R.id.textViewTurnActView);

        tvExplain.setText("此頁面主要為用來轉跳各個Activity用，直接點選要跳的頁面即可，在轉跳後的頁面按上一頁可回到此頁，在此頁按上一頁可回到登入畫面");
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, MainActivity.class);
                startActivity(intent);
                tvView.setText("轉跳的頁面為" + btnMain.getText());
            }
        });
        btnMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, MissionActivity.class);
                startActivity(intent);
                tvView.setText("轉跳的頁面為" + btnMission.getText());
            }
        });
        btnNewMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, NewMissionActivity.class);
                startActivity(intent);
                tvView.setText("轉跳的頁面為" + btnNewMission.getText());
            }
        });
        btnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, GroupActivity.class);
                startActivity(intent);
                tvView.setText("轉跳的頁面為" + btnGroup.getText());
            }
        });
     /*   btnNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, NewGroupActivity.class);
                startActivity(intent);
                tvView.setText("轉跳的頁面為" + btnNewGroup.getText());
            }
        });*/
        btnQA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, QAActivity.class);
                startActivity(intent);
                tvView.setText("轉跳的頁面為"+btnQA.getText());
            }
        });

    }
}
