package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import util.CommonConfig;

/**
 * Created by sunxipeng on 2016/11/7.
 */
public class UserDaoImpl implements UserDao {

    private final DBHelper dbHelper;
    private List<String> parent_data;

    public UserDaoImpl(Context context) {

        dbHelper = new DBHelper(context);

    }

    @Override
    public void insertSqlName( String username, String lv,String sqlname) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into user(username,lv,sqlname) values(?,?,?)", new Object[]{username, lv,sqlname});
        db.close();

    }

    @Override
     public void updateUser(String sqlname) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update user set sqlname = ? where username = ? and lv = ?",
                new Object[]{sqlname, CommonConfig.USRNAME,CommonConfig.LV});
        db.close();
    }

    @Override
    public List<String> getParents() {

        parent_data = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user where username = ?", new String[]{CommonConfig.USRNAME});
        while (cursor.moveToNext()){

            String lv = cursor.getString(cursor.getColumnIndex("lv"));
            if(!parent_data.contains(lv)){
                parent_data.add(lv);
            }
        }
        return parent_data;
    }

    @Override
    public List<List<String>> getChild() {

        List<List<String>> child_total = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(int i=0; i<parent_data.size(); i++){
            List<String> child_data = new ArrayList<>();
            Cursor cursor = db.rawQuery("select * from user where username = ? and lv = ?",
                    new String[]{CommonConfig.USRNAME,parent_data.get(i)});
            while (cursor.moveToNext()){
                String sqlname = cursor.getString(cursor.getColumnIndex("sqlname"));
                if(!child_data.contains(sqlname)&& sqlname!=null){

                    child_data.add(sqlname);
                }
            }
            child_total.add(child_data);

        }
        return child_total;
    }


    @Override
    public boolean isExists(String username, String lv, String sqlname) {
        return false;
    }

    @Override
    public List<String> getAllCount(String account) {

        List<String> parent_data = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user where username = ?", new String[]{CommonConfig.USRNAME});
        while (cursor.moveToNext()){

            String lv = cursor.getString(cursor.getColumnIndex("lv"));
            if(!parent_data.contains(lv)){
                parent_data.add(lv);
            }
        }
        return parent_data;
    }
}
