package activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by sunxipeng on 2016/10/29.
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaidumap();
        setContentView(getLayoutId());
        initView();
        loadData();
    }

    public void initBaidumap(){

    }
    protected abstract int getLayoutId();

    protected abstract void initView();
    
    protected abstract void loadData();
}
