package com.yoyoyee.zerodistance.app;

import com.android.volley.toolbox.StringRequest;
import com.yoyoyee.zerodistance.helper.SessionFunctions;

public class AppConfig {
	// Server user login url
	public static String URL_LOGIN = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/login";

	// Server user register url
	public static String URL_STUDENT_REGISTER = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/register/studentRegister";

	// Server user register url
	public static String URL_TEACHER_REGISTER = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/register/teacherRegister";

	// Server schools url
	public static String URL_SCHOOLS = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/schools";

	// Server publish missions url
	public static String URL_PUBLISH_MISSION = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishMission";

    // Server publish missions QA url
    public static String URL_PUBLISH_MISSION_QA = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishMissionQA";

	// Server publish groups url
	public static String URL_PUBLISH_GROUP = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishGroup";

    // Server publish groups QA url
    public static String URL_PUBLISH_GROUP_QA = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishGroupQA";

	// Server get missions url
	public static String URL_GET_USER_MISSIONS = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getUserMissions";

	// Server get groups url
	public static String URL_GET_USER_GROUPS = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getUserGroups";

	// Server get mission qa url
	public static String URL_GET_MISSION_QA = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getMissionQA";

	// Server get group qa url
	public static String URL_GET_GROUP_QA = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getGroupQA";

    // Server get mission accept user url
    public static String URL_GET_MISSION_ACCEPT_USER = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getMissionAcceptUser";

    // Server get group accept user url
    public static String URL_GET_GROUP_ACCEPT_USER = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getGroupAcceptUser";

	// Server publish QA answer url
	public static String URL_PUBLISH_QA_ANSWER = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishQAAnswer";

    // Server publish mission accept url
    public static String URL_PUBLISH_MISSION_ACCEPT = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishMissionAccept";

    // Server publish group accept url
    public static String URL_PUBLISH_GROUP_ACCEPT = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishGroupAccept";

	// Server delete mission url
	public static String URL_DELETE_MISSION = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/delete/deleteMission";

	// Server delete group url
	public static String URL_DELETE_GROUP = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/delete/deleteGroup";

	// Server delete question url
	public static String URL_DELETE_QUESTION = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/delete/deleteQuestion";

    // Server remove mission accept url
    public static String URL_REMOVE_MISSION_ACCEPT = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/removeMissionAccept";

    // Server remove group accept url
    public static String URL_REMOVE_GROUP_ACCEPT = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/removeGroupAccept";

    // Server update mission url
    public static String URL_PUBLISH_UPDATE_MISSION = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/updateMission";

    // Server update group url
    public static String URL_PUBLISH_UPDATE_GROUP = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/updateGroup";

	// Server update question url
	public static String URL_PUBLISH_UPDATE_QUESTION = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/updateQuestion";

	// Server set mission finished url
	public static String URL_SET_MISSION_FINISHED = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/setMissionFinished";

	// Server set group finished url
	public static String URL_SET_GROUP_FINISHED = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/setGroupFinished";

	// Server publish group accept url
	public static String URL_PUBLISH_MISSION_IMAGE = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/publishMissionImage";

    // Server get mission image url
    public static String URL_GET_MISSION_IMAGE = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getMissionImage";

    // Server get mission image url
    public static String URL_GET_MISSION_IMAGE_COUNT = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getMissionImageCount";

	// Server get user accept missions image url
	public static String URL_GET_USER_ACCEPT_MISSIONS = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getUserAcceptMissions";

    // Server get user accept groups image url
    public static String URL_GET_USER_ACCEPT_GROUPS = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getUserAcceptGroups";

	// Server add user device token
	public static String URL_ADD_USER_DEVICE_TOKEN = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/addUserDeviceToken";

	// Server send confirmation email
	public static String URL_SEND_CONFIRMATION_EMAIL = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/publish/sendConfirmationEmail";

    // Server get user email confirm state
    public static String URL_GET_EMAIL_CONFIRM_STATE = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getEmailConfirmState";

	//Server get the last version
	public static String URL_GET_LAST_VERSION = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/version";

	// Server get user friends
	public static String URL_GET_USER_FRIENDS = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/friend/getUserFriends";

	// Server add user friends
	public static String URL_ADD_USER_FRIEND = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/friend/addUserFriend";

	// Server set User Introduction
	public static String URL_SET_USER_INTRODUCTION = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/user/setUserIntroduction";

}
