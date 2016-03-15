package com.yoyoyee.zerodistance.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yoyoyee.zerodistance.R;

public class TurnActivity extends AppCompatActivity {

    private Button btnMain;
    private Button btnMission;
    private Button btnNewMission;
    private Button btnGroup;
    private Button btnNewGroup;
    private Button btnQA;

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

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, MissionActivity.class);
                startActivity(intent);
            }
        });
        btnNewMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, NewMissionActivity.class);
                startActivity(intent);
            }
        });
        btnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, GroupActivity.class);
                startActivity(intent);
            }
        });
        btnNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, NewGroupActivity.class);
                startActivity(intent);
            }
        });
        btnQA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, QAActivity.class);
                startActivity(intent);
            }
        });

    }
}
