package com.yoyoyee.zerodistance.helper.datatype;

import java.util.Date;

/**
 * Created by p1235 on 2016/4/11.
 */
public class MissionAccept {

    public int missionID;
    public String userUid;
    public String userName;
    public Date acceptedAt;

    public MissionAccept() {

    }

    public MissionAccept(int missionID, String userUid, String userName, Date acceptedAt) {

        this.missionID = missionID;
        this.userUid = userUid;
        this.userName = userName;
        this.acceptedAt = acceptedAt;

    }

}
