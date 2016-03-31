package com.yoyoyee.zerodistance.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.yoyoyee.zerodistance.R;

public class GroupActivity extends AppCompatActivity {

    //變數區=====================================
    private Toolbar toolbar;
    private boolean isMission;

    //============================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        //findViewById--------------------------------------------------------------
        toolbar = (Toolbar)findViewById(R.id.group_tool_bar);


        //-------------------------------------------------------------------------------


        // 需先讀進以下變數才能正常顯示============================
        isMission = false;

        // =========================================================

        //設置toolbar標題
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(isMission){actionBar.setTitle("任務");}
        else{actionBar.setTitle("揪團");}


    }
}
