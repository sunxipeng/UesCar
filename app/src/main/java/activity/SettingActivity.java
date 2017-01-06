package activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.usecar.uescar.R;

import util.CommonConfig;
import util.SharePreferenceUtils;

/**
 * Created by Administrator on 2017/1/5.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView bt_logout;
    private TextView bt_add_address;
    private ImageView iv_left;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

        bt_logout = (TextView) findViewById(R.id.bt_logout);
        bt_logout.setOnClickListener(this);
        bt_add_address = (TextView) findViewById(R.id.bt_add_address);
        bt_add_address.setOnClickListener(this);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_address:

                startActivity(new Intent(this, BaiduMapActity.class));
                break;
            case R.id.bt_logout:
                SharePreferenceUtils.clearUsername();
                SharePreferenceUtils.clearLoginpassword();
                if ((!SharePreferenceUtils.getLognUsername().equals("")) || (!SharePreferenceUtils.getLognpassword().equals(""))) {

                    Toast.makeText(this, "登出失败", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                    sendBroadcast(new Intent(CommonConfig.CLOSEMAINACTIVITY));
                    finish();
                }

                break;
            case R.id.iv_left:
                finish();
                break;
        }
    }
}
