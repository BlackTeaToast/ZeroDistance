package com.yoyoyee.zerodistance.helper.datatype;

import java.util.Date;

/**
 * Created by p1235 on 2016/4/11.
 */
public class GroupAccept {

    public int groupID;
    public String userUid;
    public String userName;
    public Date acceptedAt;

    public GroupAccept() {

    }

    public GroupAccept(int groupID, String userUid, String userName, Date acceptedAt) {

        this.groupID = groupID;
        this.userUid = userUid;
        this.userName = userName;
        this.acceptedAt = acceptedAt;

    }

}
