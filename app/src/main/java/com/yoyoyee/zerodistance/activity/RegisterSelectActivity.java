package com.yoyoyee.zerodistance.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;

public class RegisterSelectActivity extends AppCompatActivity {

    private Button buttonStudent;
    private Button buttonTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_select);

        buttonStudent = (Button)findViewById(R.id.buttonStudent);
        buttonTeacher = (Button)findViewById(R.id.buttonTeacher);

        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClientFunctions.updateSchools(new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        Intent i = new Intent(getApplicationContext(),
                                StudentRegisterActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onErrorResponse(String response) {

                    }
                });

            }
        });

        buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),
                        TeacherRegisterActivity.class);
                startActivity(i);

            }
        });
    }
}
