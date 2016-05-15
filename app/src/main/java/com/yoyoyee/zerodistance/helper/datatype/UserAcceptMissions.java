package com.yoyoyee.zerodistance.helper.datatype;

import java.util.Date;

/**
 * Created by p1235 on 2016/5/15.
 */
public class UserAcceptMissions {

    public int missionID;
    public Date acceptedAt;

    public UserAcceptMissions() {

    }

    public UserAcceptMissions(int missionID, Date acceptedAt) {

        this.missionID = missionID;
        this.acceptedAt = acceptedAt;

    }

}
