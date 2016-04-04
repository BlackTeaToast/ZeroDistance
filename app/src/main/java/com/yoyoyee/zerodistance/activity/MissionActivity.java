package com.yoyoyee.zerodistance.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.yoyoyee.zerodistance.R;

public class MissionActivity extends AppCompatActivity {

    //變數區=====================================
    private Toolbar toolbar;
    private boolean isMission;
    private CheckBox checkBox;
    //============================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        //findViewById--------------------------------------------------------------
        toolbar = (Toolbar)findViewById(R.id.mission_tool_bar);
        checkBox = (CheckBox)findViewById(R.id.checkBox2);
        

        //-------------------------------------------------------------------------------


        // 需先讀進以下變數才能正常顯示============================
        isMission = true;

        // =========================================================

        //設置toolbar標題
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(isMission){actionBar.setTitle("任務");}
        else{actionBar.setTitle("揪團");}
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });




    }
}
