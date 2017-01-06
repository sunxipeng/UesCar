package net;

import bean.MarkDetailMessage;

/**
 * Created by sunxipeng on 2016/12/13.
 */
public class HttpHostHolder {

    static final String HOST = "http://192.168.1.101:8080/";
    //public static final String HOST = "http://115.29.150.49:8080/com.lizhi.company_war%20exploded/";


    public static String login(String username, String password) {

        return HOST + Consts.URL_LOGIN + "?db_username=" + username + "&db_password=" + password;
    }

    public static String userBox(String username, String password) {

        return HOST + Consts.URL_USEBOX + "?db_username=" + username + "&db_password=" + password;
    }


    public static String addBox(String parentName, String username, String password) {

        return HOST + Consts.URL_ADDTABLE + "?parentName=" + parentName + "&db_username=" + username + "&db_password=" + password;
    }

    public static String addTbale(String parentName, String childName, String username, String password) {

        return HOST + Consts.URL_ADDTABLE + "?parentName=" + parentName + "&childName=" + childName + "&db_username=" + username + "&db_password=" + password;
    }

    public static String getBoardDetail(String parentName, String childName, String username, String password) {

        return HOST + Consts.URL_BOARDDETAIL + "?boxName=" + parentName + "&boardName=" + childName + "&db_username=" + username + "&db_password=" + password;
    }

    public static String dropbox() {

        return HOST + Consts.URL_DROPBOX;
    }

    public static String deletetable(String parentName, String childName) {

        return HOST + Consts.URL_DROPTABLE + "?boxname=" + parentName + "&boardname=" + childName;
    }

    public static String deletekey() {
        return HOST + Consts.URL_DELETEKEYSERVLET;
    }

    public static String addkey() {
        return HOST + Consts.URL_ADDKEYSERVLET;
    }

    public static String updatestate(String boxname, String boardname, String position, String operete, String db_username, String db_password, String op_name, String op_message, String op_tool, String op_code, String code_mark, String keycode) {

        return HOST + Consts.URL_UPDATEBOARDSERVLET + "?boxname=" + boxname + "&boardname=" + boardname + "&db_username=" + db_username + "&db_password=" + db_password + "&position=" + position + "&operate=" + operete + "&opname=" + op_name + "&message=" + op_message + "&code_mark=" + code_mark + "&keycode=" + keycode;

    }

    public static String getmarkdetail(String username, String password) {

        return HOST + Consts.URL_GETMARKDETAILSERVLET + "?db_username=" + username + "&db_password=" + password;
    }

    public static String addMarkPosition(String username, String password, String str_address, String str_number, MarkDetailMessage markDetailMessage) {

        return HOST + Consts.URL_DETAILPOSITIONSERVLET + "?db_username=" + username + "&db_password=" + password + "&addrStr=" + str_address + "&icnumber=" + str_number
                + "&latitude=" + markDetailMessage.getLatitude() + "&longitude=" + markDetailMessage.getLongitude() + "&walk=" + markDetailMessage.getWalk();
    }
}
