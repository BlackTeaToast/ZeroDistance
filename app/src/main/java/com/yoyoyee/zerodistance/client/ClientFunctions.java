package com.yoyoyee.zerodistance.client;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yoyoyee.zerodistance.app.AppConfig;
import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionManager;
import com.yoyoyee.zerodistance.helper.datatype.Mission;
import com.yoyoyee.zerodistance.helper.datatype.School;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by p1235 on 2016/3/28.
 */
public class ClientFunctions {

    public static final String TAG = AppController.class.getSimpleName();
    private static final SQLiteHandler db = AppController.getDB();
    private static final SessionManager session  = AppController.getSession();

    public ClientFunctions() {

    }

    /**
     * function to verify login details in mysql db
     * */
    public static void checkLogin(final String email, final String password, final ClientResponse clientResponse) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String isTeacher = user.getString("is_teacher");
                        String name = user.getString("name");
                        String nickName = user.getString("nick_name");
                        String schoolID = user.getString("school_id");
                        String studentID = user.getString("student_id");
                        String email = user.getString("email");
                        String createdAt = user.getString("created_at");
                        String accessKey = user.getString("access_key");

                        // Inserting row in users table
                        db.addUser(isTeacher, name, nickName, schoolID, studentID, email, uid,
                                createdAt, accessKey);

                        clientResponse.onResponse("登入成功!");

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        clientResponse.onErrorResponse(errorMsg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    clientResponse.onErrorResponse("Json error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                clientResponse.onErrorResponse(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void registerStudent(final String name, final String nickName,
                                       final String schoolID, final String studentID,
                                       final String email, final String password,
                                       final ClientResponse clientResponse) {

        // Tag used to cancel the request
        String tag_string_req = "req_register_student";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STUDENT_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");

                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);
                        clientResponse.onResponse("註冊成功! 您現在可以登入!");

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        clientResponse.onErrorResponse(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                clientResponse.onErrorResponse(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("nick_name", nickName);
                params.put("school_id", schoolID);
                params.put("student_id", studentID);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void registerTeacher(final String name, final String schoolID,
                                       final String email, final String password,
                                       final ClientResponse clientResponse) {
        // Tag used to cancel the request
        String tag_string_req = "req_register_teacher";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_TEACHER_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");

                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);
                        clientResponse.onResponse("註冊成功! 您現在可以登入!");

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        clientResponse.onErrorResponse(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                clientResponse.onErrorResponse(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("school_id", schoolID);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void updateMissions(final ClientResponse clientResponse) {
        String tag_string_req = "req_update_missions";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_USER_MISSIONS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray schools = jObj.getJSONArray("missions");
                        //Log.d(TAG, "onResponse: "+schools.toString());
                        ArrayList<Mission> missionList = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSSZ");

                        for(int i=0; i<schools.length(); i++) {
                            JSONObject mission = schools.getJSONObject(i);
                            try {
                                missionList.add(new Mission(mission.getInt("id"),
                                        mission.getString("user_uid"),
                                        mission.getString("school_id"),
                                        mission.getString("title"),
                                        mission.getBoolean("is_urgent"),
                                        mission.getInt("need_num"),
                                        mission.getInt("current_num"),
                                        mission.getString("content"),
                                        mission.getString("reward"),
                                        dateFormat.parse(mission.getString("created_at")),
                                        dateFormat.parse(mission.getString("exp_at")),
                                        mission.getBoolean("is_running"),
                                        mission.getBoolean("is_finished"),
                                        dateFormat.parse(mission.getString("finishedAt"))));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //Log.d(TAG, "onResponse: add school to schoolList");

                        }

                        // Inserting row in users table
                        db.updateMissions(missionList);
                        clientResponse.onResponse("missions get");

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        clientResponse.onErrorResponse(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                clientResponse.onErrorResponse(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", db.getUserUid());
                params.put("access_key", db.getUserAccessKey());

                return params;
            }

        };;

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishMission(final String title, final boolean isUrgent, final int needNum,
                                      final String content, final String reward, final Date expAt,
                                      final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_mission";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUBLISH_MISSION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("上傳任務成功");

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        clientResponse.onErrorResponse(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                clientResponse.onErrorResponse(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", db.getUserUid());
                params.put("access_key", db.getUserAccessKey());
                params.put("title", title);
                params.put("is_urgent", String.valueOf(isUrgent));
                params.put("need_num", String.valueOf(needNum));
                params.put("content", content);
                params.put("reward", reward);
                params.put("exp_at", sdf.format(expAt));

                return params;
            }

        };;

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishGroup(final String title, final String needNum,  final String Content,
                                    final String imagePath, final String voicePath,
                                    final String videoPath, final Date expAt,
                                    final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_group";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SCHOOLS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray schools = jObj.getJSONArray("schools");
                        //Log.d(TAG, "onResponse: "+schools.toString());
                        ArrayList<School> schoolList = new ArrayList<>();
                        for(int i=0; i<schools.length(); i++) {
                            JSONObject school = schools.getJSONObject(i);
                            schoolList.add(new School(school.getInt("id"), school.getString("area"),
                                    school.getString("county"),school.getString("name")));
                            //Log.d(TAG, "onResponse: add school to schoolList");

                        }

                        // Inserting row in users table
                        db.updateSchools(schoolList);
                        clientResponse.onResponse("schools get");

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        clientResponse.onErrorResponse(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                clientResponse.onErrorResponse(error.getMessage());
            }
        }) ;

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void updateMissions() {

    }

    public static void updateGroups() {

    }

    public static void updateSchools(final ClientResponse clientResponse) {
        String tag_string_req = "req_getSchools";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_SCHOOLS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray schools = jObj.getJSONArray("schools");
                        //Log.d(TAG, "onResponse: "+schools.toString());
                        ArrayList<School> schoolList = new ArrayList<>();
                        for(int i=0; i<schools.length(); i++) {
                            JSONObject school = schools.getJSONObject(i);
                            schoolList.add(new School(school.getInt("id"), school.getString("area"),
                                    school.getString("county"),school.getString("name")));
                            //Log.d(TAG, "onResponse: add school to schoolList");

                        }

                        // Inserting row in users table
                        db.updateSchools(schoolList);
                        clientResponse.onResponse("schools get");

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        clientResponse.onErrorResponse(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                clientResponse.onErrorResponse(error.getMessage());
            }
        }) ;

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
