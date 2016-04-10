package com.yoyoyee.zerodistance.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionManager;
import com.yoyoyee.zerodistance.helper.datatype.Group;
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
    private Button buttonLogout;
    private Button buttonAchievement;
    private TextView tvExplain;
    private TextView tvView;
    private SQLiteHandler db;
    private SessionManager session;


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
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonAchievement = (Button) findViewById(R.id.buttonPrice);

        tvExplain = (TextView) findViewById(R.id.textViewTurnActExplain);
        tvView = (TextView) findViewById(R.id.textViewTurnActView);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

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
        btnNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, NewGroupActivity.class);
                startActivity(intent);
                tvView.setText("轉跳的頁面為" + btnNewGroup.getText());
            }
        });
        btnQA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, QAActivity.class);
                startActivity(intent);
                tvView.setText("轉跳的頁面為"+btnQA.getText());
            }
        });
        buttonAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, AchievementActivity.class);
                startActivity(intent);
                tvView.setText("轉跳的頁面為"+buttonAchievement.getText());
            }
        });
        btnUnitTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientFunctions.updateMissions(new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Mission> missions = QueryFunctions.getMissions();
                        if (missions.size() > 0) {
                            Log.d(TAG, "onResponse: " + missions.get(0).getTitle() + " " + missions.get(0).createdAt + " " + missions.get(0).finishedAt);
                        }

                        Mission mission = QueryFunctions.getMission(missions.get(0).id);
                        Log.d(TAG, "onResponse: " + mission.getTitle() + " " + mission.createdAt + " " + mission.finishedAt);
                    }

                    @Override
                    public void onErrorResponse(String response) {

                    }
                });

                ClientFunctions.updateGroups(new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Group> groups = QueryFunctions.getGroups();
                        if (groups.size() > 0) {
                            Log.d(TAG, "onResponse: " + groups.get(0).getTitle() + " " + groups.get(0).createdAt + " " + groups.get(0).finishedAt);
                            Group group = QueryFunctions.getGroup(groups.get(0).id);
                            Log.d(TAG, "onResponse: " + group.getTitle() + " " + group.createdAt + " " + group.finishedAt);
                        }


                    }

                    @Override
                    public void onErrorResponse(String response) {

                    }
                });
                /*ClientFunctions.publishMissionQA(30, "ANDROID TEST", new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });
                ClientFunctions.publishGroupQA(1, "sad sad", new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });*/
                ClientFunctions.updateMissionQA(30, new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        QueryFunctions.getQAs();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });


    }
    @Override
    public void onResume(){
        super.onResume();

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(TurnActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
