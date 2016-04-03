package com.yoyoyee.zerodistance.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;

public class RegisterSelectActivity extends AppCompatActivity {

    private Button buttonStudent;
    private Button buttonTeacher;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_select);

        buttonStudent = (Button)findViewById(R.id.buttonStudent);
        buttonTeacher = (Button)findViewById(R.id.buttonTeacher);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pDialog.setMessage("載入中 ...");

        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
                ClientFunctions.updateSchools(new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Intent i = new Intent(getApplicationContext(),
                                StudentRegisterActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
                ClientFunctions.updateSchools(new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Intent i = new Intent(getApplicationContext(),
                                TeacherRegisterActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        hideDialog();
                    }
                });

            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
