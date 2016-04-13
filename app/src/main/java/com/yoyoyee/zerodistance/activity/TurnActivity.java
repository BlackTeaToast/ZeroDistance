package com.yoyoyee.zerodistance.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.client.MultipartRequest;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class TurnActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    final int requireCodefromSdcard=101,requireCodefromCamara=100;
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
    private ImageView imageView;
    private ProgressDialog pDialog;
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
        btnUnitTest = (Button) findViewById(R.id.buttonUnitTest);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonAchievement = (Button) findViewById(R.id.buttonPrice);
        tvExplain = (TextView) findViewById(R.id.textViewTurnActExplain);
        tvView = (TextView) findViewById(R.id.textViewTurnActView);
        imageView = (ImageView) findViewById(R.id.imageView);

        String t = ClientFunctions.getMissionImageUrl(81,0);
        Log.d(TAG, "onCreate: " + t);
        //imageView.setImageBitmap(getImageBitmap(t));

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

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

                /*ClientFunctions.updateMissionAcceptUser(29, new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        QueryFunctions.getMissionAceeptUser();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });

                ClientFunctions.updateGroupAcceptUser(1, new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        QueryFunctions.getGroupAceeptUser();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });*/

                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) { //if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE權限，
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showMessageOKCancel(getResources().getString(R.string.restorereadpermission_new_mission_and_group), new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                        }
                        else {
                            ActivityCompat.requestPermissions(getParent() , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requireCodefromSdcard);
                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                Pickimg();
                            }
                        }
                    }
                    else
                        Pickimg();
                }
                else{
                    Pickimg();
                }

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

    public void Pickimg(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requireCodefromSdcard);
        }
        Intent intent =new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, requireCodefromSdcard);


    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.okbuttom_new_mission_and_group), okListener)
                .setNegativeButton(getResources().getString(R.string.cancelbuttom_new_mission_and_group), null)
                .create()
                .show();
    }

    protected void onActivityResult(int requireCode,int resultCode,Intent data){
        super.onActivityResult(requireCode, resultCode, data);
        switch (requireCode) {
            case requireCodefromCamara: {

            }
            case requireCodefromSdcard: {
                if (resultCode == Activity.RESULT_OK ) {
                    BitmapFactory.Options option = new BitmapFactory.Options();
                    //option.inJustDecodeBounds =true;//只讀圖檔資訊
                    option.inSampleSize = 1;//設定縮小倍率，2為1/2倍
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg", option); //讀取圖檔資訊，存入option中，已進行修改

                    // Bitmap bitmap = ThumbnailUtils.extractThumbnail(bitmapOutPut, bitmapOutPut.getWidth()/5, bitmapOutPut.getHeight()/5); //圖片壓縮

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                        Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
                    pDialog.setMessage("上傳中 ...");
                    showDialog();
                    ClientFunctions.uploadImage(81, picturePath, new ClientResponse() {
                        @Override
                        public void onResponse(String response) {
                            hideDialog();
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onErrorResponse(String response) {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else {
                    Toast.makeText(this, R.string.notakepicture_new_mission_and_group, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);

            bm = BitmapFactory.decodeStream(aURL.openConnection().getInputStream());

            return bm;
        } catch (Exception e) {
            Log.d(TAG, "Error getting bitmap", e);
            return null;
        }

    }
    public static Bitmap GetURLBitmap(URL url)
    {
        try
        {
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream isCover = conn.getInputStream();
            Bitmap bmpCover = BitmapFactory.decodeStream(isCover);
            isCover.close();
            return bmpCover;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is,"");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
