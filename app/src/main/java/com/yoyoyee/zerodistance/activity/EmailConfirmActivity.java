package com.yoyoyee.zerodistance.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.SessionManager;

/**
 * Created by p1235 on 2016/7/24.
 */
public class EmailConfirmActivity extends Activity {

    private Button btnResendConfirmEmail;
    private Button btnAlreadyConfirm;
    private Button btnLogout;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirm);

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn() && session.isConfirmed()) {
            // User is already logged in. Take him to main activity
            ClientFunctions.registerDevice(new ClientResponse() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorResponse(String response) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(EmailConfirmActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnResendConfirmEmail = (Button)findViewById(R.id.buttonResendConfirmEmail);
        btnAlreadyConfirm = (Button)findViewById(R.id.buttonAlreadyConfirm);
        btnLogout = (Button)findViewById(R.id.buttonLogout);

        btnResendConfirmEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientFunctions.sendConfirmationEmail(new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnAlreadyConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientFunctions.getEmailConfirmState(new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        if(Boolean.parseBoolean(response)) {
                            session.setUserIsConfirmed(true);
                            ClientFunctions.registerDevice(new ClientResponse() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onErrorResponse(String response) {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent = new Intent(EmailConfirmActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "尚未通過信箱驗證", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLogin(false);
                Intent intent = new Intent(EmailConfirmActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
