package com.yoyoyee.zerodistance.helper.datatype;

import java.util.Date;

/**
 * Created by p1235 on 2016/5/15.
 */
public class UserAcceptGroups {

    public int groupID;
    public Date acceptedAt;

    public UserAcceptGroups() {

    }

    public UserAcceptGroups(int groupID, Date acceptedAt) {

        this.groupID = groupID;
        this.acceptedAt = acceptedAt;

    }

}
