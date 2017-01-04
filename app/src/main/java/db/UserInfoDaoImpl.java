package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bean.User;

/**
 * Created by sunxipeng on 2016/11/7.
 */
public class UserInfoDaoImpl implements UserInfoDao {

    private DBHelper mHelper = null;

    public UserInfoDaoImpl(Context context) {

        mHelper = new DBHelper(context);
    }

    @Override
    public void insertUserInfo(String username, String password) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("insert into user_info(account,password) values(?,?)", new Object[]{username, password});
        db.close();
    }

    @Override
    public List<User> getUserInfos() {

        List<User> mdata = new ArrayList<>();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user_info",null);
        while (cursor.moveToNext()){
            User user = new User();
            user.username = cursor.getString(cursor.getColumnIndex("account"));
            user.password = cursor.getString(cursor.getColumnIndex("password"));
            mdata.add(user);
        }
        cursor.close();
        db.close();
        return mdata;
    }

    @Override
    public boolean isExists(String username) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user_info where account = ?", new String[]{username});
        boolean exists = cursor.moveToNext();
        cursor.close();
        db.close();
        return exists;
    }

}
