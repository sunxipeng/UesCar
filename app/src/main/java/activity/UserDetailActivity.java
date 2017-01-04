package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.usecar.uescar.R;

import zxing.activity.CaptureActivity;

/**
 * Created by sunxipeng on 2016/11/8.
 */
public class UserDetailActivity extends BaseActivity {

    private EditText et_opname;
    private TextView tv_operate;
    private TextView tv_position;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userdetail;
    }

    @Override
    protected void initView() {

        tv_operate = (TextView) findViewById(R.id.tv_operate);
        tv_position = (TextView) findViewById(R.id.tv_position);

        et_opname = (EditText) findViewById(R.id.et_opname);
        findViewById(R.id.bt_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(UserDetailActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });
    }

    @Override
    protected void loadData() {
        Intent intent = getIntent();
        String boxname = intent.getStringExtra("boxname");
        String boardname = intent.getStringExtra("boardname");
        String operete = intent.getStringExtra("operete");
        int position = intent.getIntExtra("position", 0);

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
            et_opname.setText(scanResult);
        }
    }
}
