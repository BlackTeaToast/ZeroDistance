package com.yoyoyee.zerodistance.helper.table;

/**
 * Created by p1235 on 2016/3/30.
 */
public class SchoolsTable {

    public static final String TABLE_NAME = "schools";

    public static final String KEY_ID = "id";
    public static final String KEY_AREA = "area";
    public static final String KEY_COUNTY = "county";
    public static final String KEY_NAME = "name";

    public static final String CREATE_SCHOOLS_TABLE = "CREATE TABLE schools (\n" +
            " id INTEGER PRIMARY KEY,\n" +
            " area varchar(8) NOT NULL,\n" +
            " county varchar(6) NOT NULL,\n" +
            " name varchar(30) NOT NULL\n" +
            ")";


}
