package com.yoyoyee.zerodistance.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.util.ArrayList;

public class TurnActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnMain;
    private Button btnMission;
    private Button btnNewMission;
    private Button btnGroup;
    private Button btnNewGroup;
    private Button btnQA;
    private Button btnUnitTest;
    private TextView tvExplain;
    private TextView tvView;
    private SQLiteHandler db;

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
        btnUnitTest = (Button) findViewById(R.id.btnUnitTest);
        tvExplain = (TextView) findViewById(R.id.textViewTurnActExplain);
        tvView = (TextView) findViewById(R.id.textViewTurnActView);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

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
        btnUnitTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientFunctions.updateMissions(new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Mission> missions = db.getMissions();
                        if(missions.size()>0){
                            Log.d(TAG, "onResponse: " + missions.get(0).getTitle()+" "+missions.get(0).finishedAt);
                        }

                    }

                    @Override
                    public void onErrorResponse(String response) {

                    }
                });
            }
        });

    }
}
