package util;

import com.usecar.uescar.MyApplication;

/**
 * Created by sunxipeng on 2016/12/15.
 */
public class SharePreferenceUtils {

    //存入db_username
    public static void putLoginusername(String string) {
        MyApplication.getSharedPreferences().edit().putString("db_username", string).commit();
    }

    //获取username
    public static String getLognUsername() {
        return MyApplication.getSharedPreferences().getString("db_username", "");
    }

    //清除token
    public static void clearUsername() {
        MyApplication.getSharedPreferences().edit().remove("db_username").commit();
    }

    //存入db_password
    public static void putLoginpassword(String string) {
        MyApplication.getSharedPreferences().edit().putString("db_password", string).commit();
    }

    //获取db_password
    public static String getLognpassword() {
        return MyApplication.getSharedPreferences().getString("db_password", "");
    }

    //清除db_password
    public static void clearLoginpassword() {
        MyApplication.getSharedPreferences().edit().remove("db_password").commit();
    }

}
