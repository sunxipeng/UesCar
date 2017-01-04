package net;

/**
 * Created by sunxipeng on 2016/12/13.
 */
public class HttpHostHolder {

    // static final String HOST = "http://192.168.1.100:8080/";
    public static final String HOST = "http://115.29.150.49:8080/com.lizhi.company_war%20exploded/";


    public static String login(String username, String password) {

        return HOST + Consts.URL_LOGIN + "?db_username=" + username + "&db_password=" + password;
    }

    public static String userBox(String username, String password){

        return HOST + Consts.URL_USEBOX + "?db_username=" + username + "&db_password=" + password;
    }


    public static String addBox(String parentName) {

        return HOST + Consts.URL_ADDTABLE + "?parentName=" + parentName;
    }

    public static String addTbale(String parentName, String childName) {

        return HOST + Consts.URL_ADDTABLE + "?parentName=" + parentName + "&childName=" + childName;
    }

    public static String getBoardDetail(String parentName, String childName) {

        return HOST + Consts.URL_BOARDDETAIL + "?boxName=" + parentName + "&boardName=" + childName;
    }

    public static String dropbox() {

        return HOST + Consts.URL_DROPBOX;
    }

    public static String deletetable(String parentName, String childName) {

        return HOST + Consts.URL_DROPTABLE + "?boxname=" + parentName + "&boardname=" + childName;
    }
}
