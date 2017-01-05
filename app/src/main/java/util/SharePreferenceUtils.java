package util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.usecar.uescar.MyApplication;

public class SharePreferenceUtils
{
    public static void clearLoginpassword()
    {
        MyApplication.getSharedPreferences().edit().remove("db_password").commit();
    }

    public static void clearUsername()
    {
        MyApplication.getSharedPreferences().edit().remove("db_username").commit();
    }

    public static String getLognUsername()
    {
        return MyApplication.getSharedPreferences().getString("db_username", "");
    }

    public static String getLognpassword()
    {
        return MyApplication.getSharedPreferences().getString("db_password", "");
    }

    public static String getUserPermision()
    {
        return MyApplication.getSharedPreferences().getString("user_permision", "");
    }

    public static void putLoginpassword(String paramString)
    {
        MyApplication.getSharedPreferences().edit().putString("db_password", paramString).commit();
    }

    public static void putLoginusername(String paramString)
    {
        MyApplication.getSharedPreferences().edit().putString("db_username", paramString).commit();
    }

    public static void putUserPermision(String paramString)
    {
        MyApplication.getSharedPreferences().edit().putString("user_permision", paramString).commit();
    }
}