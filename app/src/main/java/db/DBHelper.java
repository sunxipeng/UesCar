package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sunxipeng on 2016/11/6.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "user.db";
    private static final String SQL_DROP = "drop table if exists user_info";
    private static final String SQL_DROP_USER = "drop table if exists user";

    private static final String SQL_CREATE = "create table user_info(id integer primary key autoincrement,"+
            "account text,password text)";

    private static final String USERSQL_CREATE = "create table user(id integer primary key autoincrement,"+
            "lv text,sqlname text,username text)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CREATE);
        sqLiteDatabase.execSQL(USERSQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(SQL_DROP);
        sqLiteDatabase.execSQL(SQL_CREATE);

        sqLiteDatabase.execSQL(SQL_DROP_USER);
        sqLiteDatabase.execSQL(USERSQL_CREATE);

    }
}
