package activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.http.ResponseInfo;
import com.usecar.uescar.R;

import net.HttpConfigs;
import net.HttpHostHolder;
import net.XHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import bean.BeseBean;
import util.ProgressDialogUtils;
import util.SharePreferenceUtils;

/**
 * Created by sunxipeng on 2016/12/15.
 */
public class LoginActivity extends BaseActivity implements XHttpUtil.HttpCallBack, View.OnClickListener {

    private ProgressDialogUtils progressDialogUtils;
    private EditText et_account;
    private EditText et_password;
    private String account;
    private String password;
    private long mExitTime;
    private String message;
    private String permision;
    private String status;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        //判断是否登录
        if (islogin()) {
            startActivity(new Intent(this, TestActivity.class));
            finish();
        }
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        //点击确实的监听
        findViewById(R.id.btn_ok).setOnClickListener(this);

    }

    @Override
    protected void loadData() {

    }

    private boolean islogin() {
        if (!SharePreferenceUtils.getLognUsername().equals("")) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        if (TextUtils.isEmpty(et_account.getText().toString().trim())
                || TextUtils.isEmpty(et_password.getText().toString().trim())) {

            Toast.makeText(LoginActivity.this, "账户或密码不能为空", Toast.LENGTH_SHORT).show();
        } else {

            account = et_account.getText().toString().trim();
            password = et_password.getText().toString().trim();

            progressDialogUtils = new ProgressDialogUtils(LoginActivity.this);
            progressDialogUtils.show();
            String url = HttpHostHolder.login(account, password);
            XHttpUtil.getXHttpUtilInstance().dogetfromserver(url, LoginActivity.this, HttpConfigs.RESULT_CEODE_1);

        }
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - this.mExitTime > 2000L) {
                Toast.makeText(this, "再按一次退出爱车app", Toast.LENGTH_SHORT).show();
                this.mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo, int resultCode) {

        String result = responseInfo.result;
        JSONObject localJSONObject = null;
        try {
            localJSONObject = new JSONObject(result);
            this.status = localJSONObject.optString("status");
            this.message = localJSONObject.optString("message");
            this.permision = localJSONObject.optString("permision");
            if (this.status.equals("1")) {
                SharePreferenceUtils.putLoginusername(this.account);
                SharePreferenceUtils.putLoginpassword(this.password);
                SharePreferenceUtils.putUserPermision(this.permision);
                SharePreferenceUtils.getUserPermision();
                startActivity(new Intent(this, TestActivity.class));
                Toast.makeText(this, this.message, Toast.LENGTH_SHORT).show();
                this.progressDialogUtils.dismiss();
                finish();
                return;
            } else {
                //登陆失败
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                progressDialogUtils.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFail(String errorInfo, int resultCode) {

        Toast.makeText(this, "连接服务器失败", Toast.LENGTH_SHORT).show();
        progressDialogUtils.dismiss();
    }
}
