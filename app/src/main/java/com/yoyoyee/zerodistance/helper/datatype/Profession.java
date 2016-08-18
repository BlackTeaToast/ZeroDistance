package com.yoyoyee.zerodistance.helper.datatype;

/**
 * Created by red on 2016/8/18.
 */
public class Profession {

    private int professionNum;

    Profession(int professionNum) {
        this.professionNum = professionNum;
    }

    public String getProfessionName() {
        return getProfessionName(professionNum);
    }

    public static String getProfessionName(int professionNum) {
        switch (professionNum) {
            case 0:
                return "學徒";
            case 1:
                return "戰士";
            case 2:
                return "法師";
            case 3:
                return "盜賊";
            case 9:
            case 99:
            case 999:
                return "管理者";
            default:
                return "未知";
        }
    }

    @Override
    public String toString() {
        return getProfessionName();
    }

}
