package com.yoyoyee.zerodistance.client;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yoyoyee.zerodistance.app.AppConfig;
import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionManager;
import com.yoyoyee.zerodistance.helper.datatype.Friend;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.GroupAccept;
import com.yoyoyee.zerodistance.helper.datatype.Mission;
import com.yoyoyee.zerodistance.helper.datatype.MissionAccept;
import com.yoyoyee.zerodistance.helper.datatype.QA;
import com.yoyoyee.zerodistance.helper.datatype.School;
import com.yoyoyee.zerodistance.helper.datatype.User;
import com.yoyoyee.zerodistance.helper.datatype.UserAcceptGroups;
import com.yoyoyee.zerodistance.helper.datatype.UserAcceptMissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

/**
 * Created by p1235 on 2016/3/28.
 */
public class ClientFunctions {

    public static final String TAG = ClientFunctions.class.getSimpleName();
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
                        String isConfirmed = user.getString("is_confirmed");
                        int profession = user.getInt("profession");
                        int level = user.getInt("level");
                        int exp = user.getInt("exp");
                        int money = user.getInt("money");
                        int strength = user.getInt("strength");
                        int intelligence = user.getInt("intelligence");
                        int agile = user.getInt("agile");
                        String introduction = user.getString("introduction");

                        session.setUserEmail(email);
                        session.setUserPassword(password);
                        session.setUserUid(uid);
                        session.setUserName(name);
                        session.setUserNickName(nickName);
                        session.setIsTeacher(Integer.valueOf(isTeacher)!=0);
                        session.setUserStudentID(studentID);
                        session.setUserSchoolID(Integer.valueOf(schoolID));
                        session.setUserAccessKey(accessKey);
                        session.setUserIsConfirmed(Integer.valueOf(isConfirmed)!=0);
                        session.setUserProfession(profession);
                        session.setUserLevel(level);
                        session.setUserExp(exp);
                        session.setUserMoney(money);
                        session.setUserStrength(strength);
                        session.setUserIntelligence(intelligence);
                        session.setUserAgile(agile);
                        session.setUserIntroduction(introduction);

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
                                createdAt, accessKey, isConfirmed);
                        session.setLogin(true);
                        clientResponse.onResponse("登入成功!");

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        clientResponse.onErrorResponse("登入失敗請重試 " + errorMsg);
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
                                        mission.getInt("strength"),
                                        mission.getInt("intelligence"),
                                        mission.getInt("agile"),
                                        dateFormat.parse(mission.getString("created_at")),
                                        dateFormat.parse(mission.getString("exp_at")),
                                        mission.getInt("is_running")!=0,
                                        mission.getInt("is_finished")!=0,
                                        dateFormat.parse(mission.getString("finished_at"))));
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

    public static void publishMission(final String title, final boolean isUrgent, final int needNum,
                                      final String place, final String content, final String reward,
                                      final int strength, final int intelligence, final int agile,
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
                params.put("strength", String.valueOf(strength));
                params.put("intelligence", String.valueOf(intelligence));
                params.put("agile", String.valueOf(agile));
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

    public static void setMissionFinished(final int missionID,
                                           final ClientResponse clientResponse) {
        String tag_string_req = "req_set_mission_finished";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SET_MISSION_FINISHED, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("修改完成任務成功");

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

    public static void setGroupFinished(final int groupID, final ClientResponse clientResponse) {
        String tag_string_req = "req_set_group_finished";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SET_GROUP_FINISHED, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("修改完成揪團成功");

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

    public static void removeGroupAccept(final int groupID,
                                           final ClientResponse clientResponse) {
        String tag_string_req = "req_remove_group_accept";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REMOVE_GROUP_ACCEPT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse("取消接受揪團成功");

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

    public static void publishUpdateMission(final int missionID, final String title,
                                            final boolean isUrgent, final int needNum,
                                            final String place, final String content,
                                            final String reward, final Date expAt,
                                            final int strength, final int intelligence,
                                            final int agile, final ClientResponse clientResponse) {
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
                params.put("mission_id", String.valueOf(missionID));
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("title", title);
                params.put("is_urgent", isUrgent?"1":"0");
                params.put("need_num", String.valueOf(needNum));
                params.put("place", place);
                params.put("content", content);
                params.put("reward", reward);
                params.put("strength", String.valueOf(strength));
                params.put("intelligence", String.valueOf(intelligence));
                params.put("agile", String.valueOf(agile));
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
                params.put("group_id", String.valueOf(groupID));
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
        try {
            String accessKey = URLEncoder.encode(session.getUserAccessKey(), "UTF-8");
            return AppConfig.URL_GET_MISSION_IMAGE + "?uid=" + session.getUserUid() + "&access_key="
                    + accessKey + "&mission_id=" + String.valueOf(missionID)
                    + "&image_num=" + String.valueOf(imageNum);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "getMissionImageUrl: " + e.getMessage());
        }
        return null;
    }

    public static void updateUserAcceptMissions(final ClientResponse clientResponse) {
        String tag_string_req = "req_update_user_accept_missions";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_USER_ACCEPT_MISSIONS, new Response.Listener<String>() {

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

                        ArrayList<UserAcceptMissions> list = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        for(int i=0; i<missions.length(); i++) {
                            JSONObject mission = missions.getJSONObject(i);
                            try {
                                list.add(new UserAcceptMissions(
                                        mission.getInt("mission_id"),
                                        dateFormat.parse(mission.getString("accepted_at"))));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        // Inserting row in users table
                        db.updateUserAcceptMissions(list);
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

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void updateUserAcceptGroups(final ClientResponse clientResponse) {
        String tag_string_req = "req_update_user_accept_groups";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_USER_ACCEPT_GROUPS, new Response.Listener<String>() {

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

                        ArrayList<UserAcceptGroups> list = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        for(int i=0; i<groups.length(); i++) {
                            JSONObject group = groups.getJSONObject(i);
                            try {
                                list.add(new UserAcceptGroups(
                                        group.getInt("group_id"),
                                        dateFormat.parse(group.getString("accepted_at"))));
                                //Log.d(TAG, "onResponse: " + mission.getString("user_name"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        // Inserting row in users table
                        db.updateUserAcceptGroups(list);
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
                Log.e(TAG, "sendConfirmationEmail Error: " + error.networkResponse.statusCode + ", "
                        + error.getMessage());
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

    public static void registerDevice(final ClientResponse clientResponse) {
        String tag_string_req = "req_register_device";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_USER_DEVICE_TOKEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        // Inserting row in users table

                        clientResponse.onResponse("設備註冊成功!");

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        clientResponse.onErrorResponse(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    clientResponse.onErrorResponse(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "sendConfirmationEmail Error: " + error.networkResponse.statusCode + ", "
                        + error.getMessage());
                clientResponse.onErrorResponse(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                FirebaseMessaging.getInstance().subscribeToTopic("all");//註冊所有群組

                params.put("uid", session.getUserUid());
                params.put("token", FirebaseInstanceId.getInstance().getToken());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void sendConfirmationEmail(final ClientResponse clientResponse) {
        String tag_string_req = "req_send_confirm_email";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEND_CONFIRMATION_EMAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        // Inserting row in users table

                        clientResponse.onResponse("已進行發送");

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        clientResponse.onErrorResponse(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    clientResponse.onErrorResponse(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "sendConfirmationEmail Error: " + error.networkResponse.statusCode + ", "
                        + error.getMessage());
                clientResponse.onErrorResponse(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();

                params.put("uid", session.getUserUid());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void getEmailConfirmState(final ClientResponse clientResponse) {
        String tag_string_req = "req_get_email_confirm_state";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_EMAIL_CONFIRM_STATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try  {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        // Inserting row in users table
                        Boolean isConfirmed = jObj.getBoolean("is_confirmed");

                        clientResponse.onResponse(isConfirmed.toString());

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        clientResponse.onErrorResponse(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    clientResponse.onErrorResponse(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getEmailConfirmState Error: " + error.networkResponse.statusCode + ", "
                        + error.getMessage());
                clientResponse.onErrorResponse(error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();

                params.put("uid", session.getUserUid());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void updateFriends(final ClientResponse clientResponse) {
        String tag_string_req = "req_update_friends";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_USER_FRIENDS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray friends = jObj.getJSONArray("friends");
                        JSONArray invites = jObj.getJSONArray("invites");
                        ArrayList<Friend> list = new ArrayList<>();

                        for(int i=0; i<friends.length(); i++) {
                            JSONObject friend = friends.getJSONObject(i);

                            list.add(new Friend(friend.getString("friend_uid"),
                                    friend.getInt("is_teacher")!=0,
                                    friend.getString("name"),
                                    friend.getString("nick_name"),
                                    friend.getInt("school_id"),
                                    friend.getString("student_id"),
                                    friend.getString("email"),
                                    friend.getInt("profession"),
                                    friend.getInt("level"),
                                    friend.getInt("exp"),
                                    friend.getInt("money"),
                                    friend.getInt("strength"),
                                    friend.getInt("intelligence"),
                                    friend.getInt("agile"),
                                    friend.getString("introduction"),
                                    friend.getInt("is_accepted")!=0,
                                    false));

                        }

                        for(int i=0; i<invites.length(); i++) {
                            JSONObject invite = invites.getJSONObject(i);

                            list.add(new Friend(invite.getString("user_uid"),
                                    invite.getInt("is_teacher")!=0,
                                    invite.getString("name"),
                                    invite.getString("nick_name"),
                                    invite.getInt("school_id"),
                                    invite.getString("student_id"),
                                    invite.getString("email"),
                                    invite.getInt("profession"),
                                    invite.getInt("level"),
                                    invite.getInt("exp"),
                                    invite.getInt("money"),
                                    invite.getInt("strength"),
                                    invite.getInt("intelligence"),
                                    invite.getInt("agile"),
                                    invite.getString("introduction"),
                                    invite.getInt("is_accepted")!=0,
                                    true));
                        }

                        // Inserting row in users table
                        db.updateFriends(list);
                        clientResponse.onResponse("friends get");

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
                Log.e(TAG, "Update Friends Error: " + error.networkResponse.statusCode + ", "
                        + error.getMessage());
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

    /**
     * 輸入好友UID，增加當前使用者好友
     * onResponse:
     *    1 - 增加好友成功，已成為好友
     *    2 - 增加好友成功，等待回覆
     * onErrorResponse:
     *   -1 - 增加好友錯誤
     *   -2 - 增加失敗，已加入好友
     *   -3 - 伺服器未知錯誤
     *   -4 - 該好友不存在
     *   -5 - 不能加自己好友
     * -400 - 內部錯誤(ex未連上網路)
     * -401 - 格式錯誤
     * -402 - 驗證錯誤
     * @param uid 好友UID
     * @param clientResponse
     */
    public static void addFriend(final String uid, final ClientResponse clientResponse) {
        String tag_string_req = "req_add_friend";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_USER_FRIEND, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String statusCode = jObj.getString("status_code");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        clientResponse.onResponse(statusCode);

                    } else {

                        // Error occurred in registration. Get the error
                        // message

                        clientResponse.onErrorResponse(statusCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    clientResponse.onErrorResponse("-400");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Add Friend Error: " + error.networkResponse.statusCode + ", "
                        + error.getMessage());
                clientResponse.onErrorResponse("-400");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("friend_uid", uid);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * 輸入好友UID，刪除當前使用者好友
     * onResponse:
     *    1 - 刪除好友成功
     * onErrorResponse:
     *   -1 - 刪除好友錯誤
     *   -2 - 刪除失敗，沒有改動
     *   -3 - 伺服器未知錯誤
     *   -4 - 該好友不存在
     *   -5 - Uid不能是自己
     * -400 - 內部錯誤(ex未連上網路)
     * -401 - 格式錯誤
     * -402 - 驗證錯誤
     * @param uid 好友UID
     * @param clientResponse
     */
    public static void deleteFriend(final String uid, final ClientResponse clientResponse) {
        String tag_string_req = "req_delete_friend";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_USER_FRIEND, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String statusCode = jObj.getString("status_code");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        clientResponse.onResponse(statusCode);

                    } else {

                        // Error occurred in registration. Get the error
                        // message

                        clientResponse.onErrorResponse(statusCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    clientResponse.onErrorResponse("-400");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Add Friend Error: " + error.networkResponse.statusCode + ", "
                        + error.getMessage());
                clientResponse.onErrorResponse("-400");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("friend_uid", uid);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * 修改當前使用者介紹
     * onResponse:
     *    1 - 修改使用者介紹成功
     * onErrorResponse:
     *   -1 - 修改使用者介紹失敗
     * -400 - 內部錯誤(ex未連上網路)
     * -401 - 格式錯誤
     * -402 - 驗證錯誤
     * @param introduction
     * @param clientResponse
     */
    public static void setUserIntroduction(final String introduction,
                                            final ClientResponse clientResponse) {
        String tag_string_req = "req_set_user_introduction";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SET_USER_INTRODUCTION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String statusCode = jObj.getString("status_code");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse(statusCode);

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Log.e(TAG, "onResponse: " + errorMsg);
                        clientResponse.onErrorResponse(statusCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    clientResponse.onErrorResponse("-400");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "setUserIntroduction Error: " + error.getMessage());
                clientResponse.onErrorResponse("-400");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("introduction", introduction);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * 搜尋好友
     * onErrorResponse:
     * -400 - 內部錯誤(ex未連上網路)
     * -401 - 格式錯誤
     * -402 - 驗證錯誤
     * @param text
     * @param clientResponseUser
     */
    public static void searchFriend(final String text, final ClientResponseUser clientResponseUser) {
        String tag_string_req = "req_search_friend";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEARCH_FRIEND, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray usersJSON = jObj.getJSONArray("users");
                        User[] users = new User[usersJSON.length()];
                        for(int i=0; i<usersJSON.length(); i++) {
                            JSONObject user = usersJSON.getJSONObject(i);
                            users[i] = (new User(user.getString("unique_id"),
                                    user.getInt("is_teacher")!=0,
                                    user.getString("name"),
                                    user.getString("nick_name"),
                                    user.getInt("school_id"),
                                    user.getString("student_id"),
                                    user.getString("email"),
                                    user.getInt("profession"),
                                    user.getInt("level"),
                                    user.getInt("exp"),
                                    user.getInt("money"),
                                    user.getInt("strength"),
                                    user.getInt("intelligence"),
                                    user.getInt("agile"),
                                    user.getString("introduction")));
                        }

                        clientResponseUser.onResponse(users);

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Log.e(TAG, "searchFriend onResponse: " + errorMsg);
                        clientResponseUser.onErrorResponse(-400);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "searchFriend Error: " + error.networkResponse.statusCode + ", "
                        + error.getMessage());
                clientResponseUser.onErrorResponse(-400);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("text", text);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * 修改任務使用者星數
     * onResponse:
     *    1 - 評分成功
     *    2 - 評分未更動
     * onErrorResponse:
     *   -1 - 非任務創立者
     * -400 - 內部錯誤(ex未連上網路)
     * -401 - 格式錯誤
     * -402 - 驗證錯誤
     * @param missionID
     * @param userUid
     * @param stars
     * @param clientResponse
     */
    public static void setMissionUserStar(final int missionID, final String userUid,
                                          final int stars, final ClientResponse clientResponse) {
        String tag_string_req = "req_set_user_introduction";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SET_MISSION_USER_STAR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String statusCode = jObj.getString("status_code");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        clientResponse.onResponse(statusCode);

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Log.e(TAG, "setMissionUserStar onResponse: " + errorMsg);
                        clientResponse.onErrorResponse(statusCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    clientResponse.onErrorResponse("-400");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "setMissionUserStar Error: " + error.getMessage());
                clientResponse.onErrorResponse("-400");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<>();
                params.put("uid", session.getUserUid());
                params.put("access_key", session.getUserAccessKey());
                params.put("mission_id", String.valueOf(missionID));
                params.put("user_uid", userUid);
                params.put("stars", String.valueOf(stars));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * 修改任務多個使用者星數
     * onResponse:
     *    1 - 評分成功
     *    2 - 評分未更動
     * onErrorResponse:
     *   -1 - 非任務創立者
     * -400 - 內部錯誤(ex未連上網路)
     * -401 - 格式錯誤
     * -402 - 驗證錯誤
     * @param missionID
     * @param userStar uid和stars
     * @param clientResponseInteger
     */
    public static void setMissionUsersStar(final int missionID, HashMap<String,Integer> userStar,
                                           final ClientResponseInteger clientResponseInteger) {

        String tag_json_req = "req_set_mission_users_star";

        try {

            JSONObject jsonObj = new JSONObject();
            JSONArray jsonAry = new JSONArray();

            jsonObj.put("uid", session.getUserUid());
            jsonObj.put("access_key", session.getUserAccessKey());
            jsonObj.put("mission_id", missionID);
            for (Entry<String, Integer> entry : userStar.entrySet()) {
                JSONObject user = new JSONObject();
                user.put("uid", entry.getKey());
                user.put("stars", entry.getValue());
                jsonAry.put(user);
            }
            jsonObj.put("users", jsonAry);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    AppConfig.URL_SET_MISSION_USERS_STAR, jsonObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean error = response.getBoolean("error");
                        int statusCode = response.getInt("status_code");
                        if (!error) {
                            clientResponseInteger.onResponse(statusCode);
                        } else {
                            clientResponseInteger.onErrorResponse(statusCode);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        clientResponseInteger.onErrorResponse(-400);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "setMissionUsersStar onErrorResponse: ", error);
                    clientResponseInteger.onErrorResponse(-400);
                }
            });

            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_req);

        } catch (JSONException e) {
            e.printStackTrace();
            clientResponseInteger.onErrorResponse(-400);
        }
    }
}
