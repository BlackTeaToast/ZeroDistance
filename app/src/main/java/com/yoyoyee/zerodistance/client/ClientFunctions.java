package com.yoyoyee.zerodistance.client;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yoyoyee.zerodistance.app.AppConfig;
import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionManager;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.GroupAccept;
import com.yoyoyee.zerodistance.helper.datatype.Mission;
import com.yoyoyee.zerodistance.helper.datatype.MissionAccept;
import com.yoyoyee.zerodistance.helper.datatype.QA;
import com.yoyoyee.zerodistance.helper.datatype.School;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by p1235 on 2016/3/28.
 */
public class ClientFunctions {

    public static final String TAG = AppController.class.getSimpleName();
    private static SQLiteHandler db = AppController.getDB();
    private static SessionManager session  = AppController.getSession();

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

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String isTeacher = user.getString("is_teacher");
                        String name = user.getString("name");
                        String nickName = user.getString("nick_name");
                        final String schoolID = user.getString("school_id");
                        String studentID = user.getString("student_id");
                        String email = user.getString("email");
                        String createdAt = user.getString("created_at");
                        String accessKey = user.getString("access_key");

                        session.setUserEmail(email);
                        session.setUserPassword(password);
                        session.setUserUid(uid);
                        session.setUserName(name);
                        session.setUserNickName(nickName);
                        session.setIsTeacher(Integer.valueOf(isTeacher)!=0);
                        session.setUserStudentID(studentID);
                        session.setUserSchoolID(Integer.valueOf(schoolID));
                        session.setUserAccessKey(accessKey);

                        ClientFunctions.updateSchools(new ClientResponse() {
                            @Override
                            public void onResponse(String response) {
                                session.setUserSchoolName(QueryFunctions.getSchoolName(Integer.valueOf(schoolID)));
                            }

                            @Override
                            public void onErrorResponse(String response) {

                            }
                        });

                        // Inserting row in users table
                        db.addUser(isTeacher, name, nickName, schoolID, studentID, email, uid,
                                createdAt, accessKey);
                        session.setLogin(true);
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
                        JSONArray missions = jObj.getJSONArray("missions");

                        ArrayList<Mission> missionList = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        for(int i=0; i<missions.length(); i++) {
                            JSONObject mission = missions.getJSONObject(i);
                            try {
                                missionList.add(new Mission(mission.getInt("id"),
                                        mission.getString("user_uid"),
                                        mission.getString("user_name"),
                                        mission.getInt("school_id"),
                                        mission.getString("title"),
                                        mission.getInt("is_urgent")!=0,
                                        mission.getInt("need_num"),
                                        mission.getInt("current_num"),
                                        mission.getString("place"),
                                        mission.getString("content"),
                                        mission.getString("reward"),
                                        dateFormat.parse(mission.getString("created_at")),
                                        dateFormat.parse(mission.getString("exp_at")),
                                        mission.getInt("is_running")!=0,
                                        mission.getInt("is_finished")!=0,
                                        dateFormat.parse(mission.getString("finished_at"))));
                                Log.d(TAG, "onResponse: " + mission.getString("exp_at"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

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
                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void updateGroups(final ClientResponse clientResponse) {
        String tag_string_req = "req_update_groups";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_USER_GROUPS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray groups = jObj.getJSONArray("groups");

                        ArrayList<Group> groupList = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        for(int i=0; i<groups.length(); i++) {
                            JSONObject group = groups.getJSONObject(i);
                            try {
                                groupList.add(new Group(group.getInt("id"),
                                        group.getString("user_uid"),
                                        group.getString("user_name"),
                                        group.getInt("school_id"),
                                        group.getString("title"),
                                        group.getInt("need_num"),
                                        group.getInt("current_num"),
                                        group.getString("place"),
                                        group.getString("content"),
                                        dateFormat.parse(group.getString("created_at")),
                                        dateFormat.parse(group.getString("exp_at")),
                                        group.getInt("is_running")!=0,
                                        group.getInt("is_finished")!=0,
                                        dateFormat.parse(group.getString("finished_at"))));
                                Log.d(TAG, "onResponse: " + group.getString("exp_at"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        // Inserting row in users table
                        db.updateGroups(groupList);
                        clientResponse.onResponse("groups get");

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
                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishMission(final String title, final boolean isUrgent, final int needNum,
                                      final String place, final String content, final String reward,
                                      final Date expAt, final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_mission";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUBLISH_MISSION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String insertID = jObj.getString("insert_id");
                        clientResponse.onResponse(insertID);

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
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("title", title);
                params.put("is_urgent", isUrgent?"1":"0");
                params.put("need_num", String.valueOf(needNum));
                params.put("place", place);
                params.put("content", content);
                params.put("reward", reward);
                params.put("exp_at", sdf.format(expAt));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishGroup(final String title, final int needNum, final String place,
                                      final String content, final Date expAt,
                                      final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_group";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUBLISH_GROUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String insertID = jObj.getString("insert_id");
                        clientResponse.onResponse(insertID);

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
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("title", title);
                params.put("need_num", String.valueOf(needNum));
                params.put("place", place);
                params.put("content", content);
                params.put("exp_at", sdf.format(expAt));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishMissionQA(final int missionID, final String question,
                                    final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_mission_QA";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUBLISH_MISSION_QA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("上傳任務QA成功");

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

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("mission_id", String.valueOf(missionID));
                params.put("question", question);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishGroupQA(final int groupID, final String question,
                                        final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_group_QA";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUBLISH_GROUP_QA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("上傳揪團QA成功");

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

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("group_id", String.valueOf(groupID));
                params.put("question", question);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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

    public static void updateMissionQA(final int missionID, final ClientResponse clientResponse) {
        String tag_string_req = "req_update_mission_qas";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_MISSION_QA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray qas = jObj.getJSONArray("qas");

                        ArrayList<QA> QAList = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        for(int i=0; i<qas.length(); i++) {
                            JSONObject qa = qas.getJSONObject(i);
                            try {
                                QAList.add(new QA(
                                        qa.getInt("id"),
                                        qa.getString("user_uid"),
                                        qa.getString("user_name"),
                                        qa.getString("question"),
                                        qa.getString("answer"),
                                        qa.getInt("is_answered")!=0,
                                        dateFormat.parse(qa.getString("created_at")),
                                        dateFormat.parse(qa.getString("answered_at"))));
                                Log.d(TAG, "onResponse: " + qa.getString("question"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        // Inserting row in users table
                        db.updateQAs(QAList);
                        clientResponse.onResponse("QAs get");

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
                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("mission_id", String.valueOf(missionID));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void updateGroupQA(final int groupID, final ClientResponse clientResponse) {
        String tag_string_req = "req_update_mission_qas";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_GROUP_QA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray qas = jObj.getJSONArray("qas");

                        ArrayList<QA> QAList = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        for(int i=0; i<qas.length(); i++) {
                            JSONObject qa = qas.getJSONObject(i);
                            try {
                                QAList.add(new QA(
                                        qa.getInt("id"),
                                        qa.getString("user_uid"),
                                        qa.getString("user_name"),
                                        qa.getString("question"),
                                        qa.getString("answer"),
                                        qa.getInt("is_answered")!=0,
                                        dateFormat.parse(qa.getString("created_at")),
                                        dateFormat.parse(qa.getString("answered_at"))));
                                Log.d(TAG, "onResponse: " + qa.getString("question"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        // Inserting row in users table
                        db.updateQAs(QAList);
                        clientResponse.onResponse("QAs get");

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
                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("group_id", String.valueOf(groupID));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishQAAnswer(final int qaID, final String answer,
                                      final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_QA_Answer";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUBLISH_QA_ANSWER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("上傳QA Answer成功");

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

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("qa_id", String.valueOf(qaID));
                params.put("answer", answer);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishMissionAccept(final int missionID,
                                            final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_mission_accept";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUBLISH_MISSION_ACCEPT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("接受任務成功");

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

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("mission_id", String.valueOf(missionID));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishGroupAccept(final int groupID,
                                            final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_group_accept";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUBLISH_GROUP_ACCEPT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("接受揪團成功");

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

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("group_id", String.valueOf(groupID));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void updateMissionAcceptUser(final int missionID, final ClientResponse clientResponse) {
        String tag_string_req = "req_update_mission_accept_user";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_MISSION_ACCEPT_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray qas = jObj.getJSONArray("mission_accept");

                        ArrayList<MissionAccept> maList = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        for(int i=0; i<qas.length(); i++) {
                            JSONObject qa = qas.getJSONObject(i);
                            try {
                                maList.add(new MissionAccept(
                                        qa.getInt("mission_id"),
                                        qa.getString("user_uid"),
                                        qa.getString("user_name"),
                                        dateFormat.parse(qa.getString("accepted_at"))));
                                Log.d(TAG, "onResponse: " + qa.getString("user_name"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        // Inserting row in users table
                        db.updateMissionAcceptUser(maList);
                        clientResponse.onResponse("Mission accept user get");

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
                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("mission_id", String.valueOf(missionID));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void updateGroupAcceptUser(final int groupID, final ClientResponse clientResponse) {
        String tag_string_req = "req_update_group_accept_user";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_GROUP_ACCEPT_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray qas = jObj.getJSONArray("group_accept");

                        ArrayList<GroupAccept> gaList = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        for(int i=0; i<qas.length(); i++) {
                            JSONObject qa = qas.getJSONObject(i);
                            try {
                                gaList.add(new GroupAccept(
                                        qa.getInt("group_id"),
                                        qa.getString("user_uid"),
                                        qa.getString("user_name"),
                                        dateFormat.parse(qa.getString("accepted_at"))));
                                Log.d(TAG, "onResponse: " + qa.getString("user_name"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        // Inserting row in users table
                        db.updateGroupAcceptUser(gaList);
                        clientResponse.onResponse("Group accept user get");

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
                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("group_id", String.valueOf(groupID));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void deleteMission(final int missionID,
                                            final ClientResponse clientResponse) {
        String tag_string_req = "req_delete_mission";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_MISSION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("刪除任務成功");

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

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("mission_id", String.valueOf(missionID));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void deleteGroup(final int groupID,
                                          final ClientResponse clientResponse) {
        String tag_string_req = "req_delete_group";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_GROUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("刪除揪團成功");

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

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("group_id", String.valueOf(groupID));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void deleteQuestion(final int qaID,
                                   final ClientResponse clientResponse) {
        String tag_string_req = "req_delete_question";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_QUESTION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("刪除問題成功");

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

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("qa_id", String.valueOf(qaID));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void removeMissionAccept(final int missionID,
                                      final ClientResponse clientResponse) {
        String tag_string_req = "req_remove_mission_accept";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REMOVE_MISSION_ACCEPT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("取消接受任務成功");

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

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("mission_id", String.valueOf(missionID));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishUpdateMission(final int missionID, final String title,
                                            final boolean isUrgent, final int needNum,
                                            final String place, final String content,
                                            final String reward, final Date expAt,
                                            final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_update_mission";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUBLISH_UPDATE_MISSION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("修改任務成功");

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
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("title", title);
                params.put("is_urgent", isUrgent?"1":"0");
                params.put("need_num", String.valueOf(needNum));
                params.put("place", place);
                params.put("content", content);
                params.put("reward", reward);
                params.put("exp_at", sdf.format(expAt));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishUpdateGroup(final int groupID, final String title, final int needNum,
                                          final String place, final String content,
                                          final Date expAt, final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_update_group";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUBLISH_UPDATE_GROUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("修改揪團成功");

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
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("title", title);
                params.put("need_num", String.valueOf(needNum));
                params.put("place", place);
                params.put("content", content);
                params.put("exp_at", sdf.format(expAt));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void publishUpdateQuestion(final int qaID, final String question,
                                       final ClientResponse clientResponse) {
        String tag_string_req = "req_publish_update_question";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUBLISH_UPDATE_QUESTION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("修改Question成功");

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

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("qa_id", String.valueOf(qaID));
                params.put("question", question);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void uploadMissionImage(final int missionID, final String imagePath,
                                   final ClientResponse clientResponse) {
        String tag_string_req = "req_upload_mission_image";

        Map<String,String> params = new HashMap<>();
        params.put("uid", session.getUserUid());
        params.put("access_key", session.getUserAccessKey());
        params.put("mission_id", String.valueOf(missionID));

        MultipartRequest mulReq = new MultipartRequest(AppConfig.URL_PUBLISH_MISSION_IMAGE
                +"?uid="+session.getUserUid()+"&access_key="+session.getUserAccessKey()+"&mission_id="
                +missionID, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                clientResponse.onErrorResponse(error.getMessage());
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                clientResponse.onResponse(response);
            }
        }, new File(imagePath), params);
        AppController.getInstance().addToRequestQueue(mulReq, tag_string_req);

    }

    public static void getMissionImageCount(final int missionID,
                                            final ClientResponse clientResponse) {
        String tag_string_req = "req_get_mission_image_count";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_MISSION_IMAGE_COUNT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String count = jObj.getString("count");
                        clientResponse.onResponse(count);

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
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("mission_id", String.valueOf(missionID));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static String getMissionImageUrl(int missionID, int imageNum) {
        return new String(AppConfig.URL_GET_MISSION_IMAGE + "?uid=" + session.getUserUid() + "&access_key="
                + session.getUserAccessKey() + "&mission_id=" + String.valueOf(missionID)
                + "&image_num=" + String.valueOf(imageNum));
    }
}
