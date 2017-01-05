package activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.ResponseInfo;
import com.usecar.uescar.R;

import net.HttpConfigs;
import net.HttpHostHolder;
import net.XHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import bean.BoardInfo;
import sddata.ICReaderApi;
import util.CommonConfig;
import util.CommonUtil;
import util.ProgressDialogUtils;
import util.SharePreferenceUtils;
import zxing.activity.CaptureActivity;

/**
 * Created by sunxipeng on 2016/11/8.
 */
public class UserDetailActivity extends BaseActivity implements View.OnClickListener, XHttpUtil.HttpCallBack {

    private EditText et_opname;
    private TextView tv_operate;
    private TextView tv_position;
    private EditText et_code;
    private EditText et_message;
    private EditText et_tool;
    private String operete;
    private String position;
    private ProgressDialogUtils progressDialogUtils;
    private String boxname;
    private String boardname;
    private String db_username;
    private String db_password;
    private String keycode;
    private String code_mark;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userdetail;
    }

    @Override
    protected void initView() {


        et_code = ((EditText) findViewById(R.id.et_code));
        et_message = ((EditText) findViewById(R.id.et_message));
        et_tool = ((EditText) findViewById(R.id.et_tool));

        tv_operate = (TextView) findViewById(R.id.tv_operate);
        tv_position = (TextView) findViewById(R.id.tv_position);

        et_opname = (EditText) findViewById(R.id.et_opname);

        findViewById(R.id.bt_update).setOnClickListener(this);
        findViewById(R.id.bt_canle).setOnClickListener(this);
        findViewById(R.id.bt_sd_read).setOnClickListener(this);
        //扫描二维码操作
        findViewById(R.id.bt_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(UserDetailActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });

        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadData() {
        Intent intent = getIntent();
        boxname = intent.getStringExtra("boxname");
        boardname = intent.getStringExtra("boardname");
        operete = intent.getStringExtra("operete");
        position = intent.getStringExtra("position");

        this.db_username = SharePreferenceUtils.getLognUsername();
        this.db_password = SharePreferenceUtils.getLognpassword();

        tv_operate.setText(operete);
        tv_position.setText(position + "");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            et_code.setText(scanResult);
            this.code_mark = "1";
            this.keycode = scanResult;
        }
    }

    private String op_code;
    private String op_message;
    private String op_name;
    private String op_tool;
    private BoardInfo boardInfo;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_update:
                //网络请求处理
                op_name = this.et_opname.getText().toString();
                op_message = this.et_message.getText().toString();
                op_tool = this.et_tool.getText().toString();
                op_code = this.et_code.getText().toString();
                if ((this.op_name.isEmpty()) || (this.op_message.isEmpty()) || (this.op_tool.isEmpty()) || (this.op_code.isEmpty())) {
                    Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                boardInfo = new BoardInfo();
                boardInfo.set姓名(this.op_name);
                boardInfo.set备注(this.op_message);
                boardInfo.set操作(operete);
                boardInfo.set日期(CommonUtil.getDate());
                boardInfo.set时间(CommonUtil.getTime());
                request();
                break;

            case R.id.bt_canle:
                //关闭界面
                finish();
                break;

            case R.id.bt_sd_read:
                //读取ic卡数据
                readData();
                break;
        }
    }

    private UsbManager manager;
    private UsbDevice device;
    private ICReaderApi api;

    private void readData() {

        if ((this.device == null) || (this.manager == null)) {
            Toast.makeText(this, "未检测到设备", Toast.LENGTH_SHORT).show();
            return;
        }

        String text = new String();
        byte mode = (byte) 0;
        byte blk_add = (byte) 16;
        byte num_blk = (byte) 1;
        byte[] snr = {-1, -1, -1, -1, -1, -1};
        byte[] buffer = new byte[16 * num_blk]; // data read
        int result = api.API_PCDRead(mode, blk_add, num_blk, snr, buffer);
        //text = showStatue(text, result);
        if (result == 0) {
            text = showData(text, snr, 0, 3);
            et_code.setText(text);
            this.code_mark = "2";
        } else {
            Toast.makeText(this, "读取数据失败，请正确放置ic卡位置", Toast.LENGTH_SHORT).show();
            et_code.setText(null);
        }

    }

    private String showData(String text, byte[] data, int pos,
                            int len) {
        ArrayList<String> test = new ArrayList<>();
        String dStr = "";
        for (int i = 0; i < len; i++) {
            dStr = String.format("%02x", data[i + pos]);
            test.add(dStr.toUpperCase());
        }
        Collections.reverse(test);
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : test) {
            stringBuffer.append(CommonUtil.hexString2binaryString(string));
        }
        return String.valueOf(Integer.valueOf(stringBuffer.toString(), 2));
    }

    private void request() {
        this.progressDialogUtils = new ProgressDialogUtils(this);
        this.progressDialogUtils.show();

        String str = HttpHostHolder.updatestate(boxname, boardname, position, operete, db_username, db_password, op_name, op_message, op_tool, op_code, code_mark, keycode);
        XHttpUtil.getXHttpUtilInstance().dogetfromserver(str, this, HttpConfigs.RESULT_CEODE_1);
    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo, int resultCode) {

        try {
            JSONObject localJSONObject = new JSONObject((String) responseInfo.result);
            String status = localJSONObject.optString("status");
            String message = localJSONObject.optString("message");
            if (status.equals("1")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                Intent localIntent = new Intent(CommonConfig.REFRESH_DETAIL_BOARD);
                Bundle localBundle = new Bundle();
                localBundle.putSerializable("operate_result", boardInfo);
                localIntent.putExtras(localBundle);
                sendBroadcast(localIntent);
                finish();
            }
            this.progressDialogUtils.dismiss();
        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();
        }

        progressDialogUtils.dismiss();
    }

    @Override
    public void onFail(String errorInfo, int resultCode) {

        progressDialogUtils.dismiss();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        this.device = ((UsbDevice) intent.getParcelableExtra("device"));
        this.manager = ((UsbManager) getSystemService(USB_SERVICE));
        if ((this.device != null) && (this.manager != null)) {
            this.api = new ICReaderApi(this.device, this.manager);
        } else {
            Toast.makeText(this, "未检测到设备", Toast.LENGTH_SHORT).show();
        }

    }
}
