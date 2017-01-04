package com.usecar.uescar;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by sunxipeng on 2016/12/15.
 */
public class MyApplication extends Application {

    private static MyApplication mContext;

    //设置全局唯一的sharedPreferences
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    /**
     * 获取全局的context
     *
     * @return
     */
    public static MyApplication getContext() {
        if (null == mContext) {
            mContext = new MyApplication();
        }
        return mContext;
    }

    public static SharedPreferences getSharedPreferences() {

        if (sharedPreferences == null) {

            return sharedPreferences = getContext().getSharedPreferences("count", MODE_PRIVATE);
        }
        return sharedPreferences;
    }

}
