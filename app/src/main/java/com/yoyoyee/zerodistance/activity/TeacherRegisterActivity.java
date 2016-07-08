/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.yoyoyee.zerodistance.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionManager;

import java.util.ArrayList;

public class TeacherRegisterActivity extends Activity {
    private static final String TAG = TeacherRegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private Spinner spinnerArea;
    private Spinner spinnerCounty;
    private Spinner spinnerSchool;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private String[] areaArray;
    private String[] countyArray;
    private String[] schoolArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        spinnerArea = (Spinner) findViewById(R.id.spinnerArea);
        spinnerCounty = (Spinner) findViewById(R.id.spinnerCounty);
        spinnerSchool = (Spinner) findViewById(R.id.spinnerSchool);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(TeacherRegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String area = spinnerArea.getSelectedItem().toString();
                String county = spinnerCounty.getSelectedItem().toString();
                String school = spinnerSchool.getSelectedItem().toString();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirmPassword = inputConfirmPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() &&
                        !confirmPassword.isEmpty() && spinnerArea.getSelectedItemPosition()!=0 &&
                        spinnerCounty.getSelectedItemPosition()!=0 &&
                        spinnerSchool.getSelectedItemPosition()!=0) {

                    String schoolID = String.valueOf(db.getSchoolID(area, county, school));
                    if(password.equals(confirmPassword)) {
                        registerTeacher(name, schoolID, email, password);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "兩次密碼輸入不相同，請確認密碼是否輸入正確！", Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "輸入資料不完整！", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                finish();
            }
        });

        countyArray = new String[]{"選擇學校縣市"};
        schoolArray = new String[]{"選擇學校"};
        ArrayList<String> area = db.getSchoolsArea();
        area.add(0, "選擇學校區域");
        areaArray = area.toArray(new String[0]);

        ArrayAdapter<String> adapterCounty = new ArrayAdapter<>(this , R.layout.spinner_item, countyArray);
        ArrayAdapter<String> adapterSchool = new ArrayAdapter<>(this , R.layout.spinner_item, schoolArray);
        ArrayAdapter<String> adapterArea = new ArrayAdapter<>(this , R.layout.spinner_item, areaArray);
        spinnerArea.setAdapter(adapterArea);
        spinnerCounty.setAdapter(adapterCounty);
        spinnerSchool.setAdapter(adapterSchool);

        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    ArrayList<String> county = db.getSchoolsCounty(parent.getSelectedItem().toString());
                    county.add(0, "選擇學校縣市");
                    countyArray = county.toArray(new String[0]);
                    ArrayAdapter<String> adapterCounty = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, countyArray);
                    spinnerCounty.setAdapter(adapterCounty);
                    Log.d(TAG, "onItemSelected: " + position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    ArrayList<String> school = db.getSchoolsName(parent.getSelectedItem().toString());
                    school.add(0, "選擇學校");
                    schoolArray = school.toArray(new String[0]);
                    ArrayAdapter<String> adapterSchool = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, schoolArray);
                    spinnerSchool.setAdapter(adapterSchool);
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {

                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerTeacher(final String name, final String schoolID ,final String email,
                              final String password) {

        pDialog.setMessage("註冊中 ...");
        showDialog();

        ClientFunctions.registerTeacher(name, schoolID, email, password, new ClientResponse() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                // Launch login activity
                finish();
            }

            @Override
            public void onErrorResponse(String response) {
                hideDialog();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
