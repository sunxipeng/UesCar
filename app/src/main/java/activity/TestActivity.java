package activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.usecar.uescar.R;

import net.HttpConfigs;
import net.HttpHostHolder;
import net.XHttpUtil;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.ExpandAadapter;
import adapter.SQLAdapter;
import bean.BeseBean;
import bean.BoardInfo;
import bean.BoxMessageBean;
import db.UserDaoImpl;
import util.CommonConfig;
import util.ProgressDialogUtils;
import util.SharePreferenceUtils;
import view.WheelView;

/**
 * Created by sunxipeng on 2016/11/7.
 */
public class TestActivity extends BaseActivity implements View.OnClickListener, XHttpUtil.HttpCallBack {


    private TextView bt_left;
    private TextView bt_main;
    private DrawerLayout drawerLayout;
    private ExpandableListView expandableListView;
    private List<String> parent;
    private List<List<String>> mdata;
    private AlertDialog linkDialog;
    private ExpandAadapter expandAadapter;
    private SQLAdapter sqlAdapter;
    private TextView tv_main;
    private ListView lv_sql1;
    private String str;
    private String boxname;
    private String boardname;
    private BroadcastReceiver broadcastReceiver;
    private int currentpositon;
    private AlertDialog warn_dialog;
    private ProgressDialogUtils progressDialogUtils;
    private TextView tv_username;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;

    }

    @Override
    protected void initView() {

        parent = new ArrayList<>();
        mdata = new ArrayList<>();

        registerBroadCastReciver();

        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_main = (TextView) findViewById(R.id.tv_main);
        bt_left = (TextView) findViewById(R.id.bt_left);
        bt_left.setOnClickListener(this);
        bt_main = (TextView) findViewById(R.id.bt_main);
        bt_main.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        expandableListView = (ExpandableListView) findViewById(R.id.expandlist);
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                //需要删除的位置
                currentpositon = (int) view.getTag(R.layout.parent_item);
                showWarnDialog();

                return false;
            }
        });

        lv_sql1 = (ListView) findViewById(R.id.lv_sql);
        lv_sql1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                startActivity(new Intent(TestActivity.this, UserDetailActivity.class));
            }
        });

        lv_sql1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("TestActivity,我点击的第一个参数", String.valueOf(i));
                Log.d("TestActivity,我点击的第二个参数", String.valueOf(l));
                showOperationDialog(i);
                return true;
            }
        });


    }

    private String operete = null;

    private void showOperationDialog(final int position) {

        ArrayList<String> data = new ArrayList<>();
        data.add("取用");
        data.add("出售");
        data.add("出租");
        data.add("取车");
        operete = data.get(0);

        final AlertDialog operate_dialog = new AlertDialog.Builder(this).create();
        View operare_view = LayoutInflater.from(this).inflate(R.layout.operate_view, null);
        WheelView wheel_view = (WheelView) operare_view.findViewById(R.id.wheel_view);
        wheel_view.setOffset(1);
        wheel_view.setItems(data);
        wheel_view.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);

                operete = item;
                //Toast.makeText(TestActivity.this, "selectedIndex: " + selectedIndex + ", item: " + item, Toast.LENGTH_SHORT).show();
            }
        });
        operare_view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Log.d("TestActivity", operete);
                operate_dialog.dismiss();
                startActivity(new Intent(TestActivity.this, UserDetailActivity.class)
                        .putExtra("boxname", boxname).putExtra("boardname", boardname)
                        .putExtra("operete", operete).putExtra("position", position));
            }
        });

        operate_dialog.setCancelable(false);
        operate_dialog.setView(operare_view, 0, 0, 0, 0); // 设置 view
        operate_dialog.show();
    }


    private void registerBroadCastReciver() {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (intent.getAction()) {

                    case "refresh":
                        notifyparent();
                        break;

                    case "close":
                        boxname = intent.getStringExtra("parent");
                        boardname = intent.getStringExtra("child");
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        tv_main.setVisibility(View.INVISIBLE);
                        lv_sql1.setVisibility(View.VISIBLE);
                        queryBoardDetail(boxname, boardname);

                        break;
                }

            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConfig.REFRESH);
        intentFilter.addAction(CommonConfig.CLOSE);
        registerReceiver(broadcastReceiver, intentFilter);

    }

    /**
     * board详情页网络请求数据
     *
     * @param parentName
     * @param childName
     */
    private void queryBoardDetail(String parentName, String childName) {
        progressDialogUtils = new ProgressDialogUtils(this);
        progressDialogUtils.show();
        String url = HttpHostHolder.getBoardDetail(parentName, childName);
        XHttpUtil.getXHttpUtilInstance().dogetfromserver(url, this, HttpConfigs.RESULT_CEODE_3);
    }

    public void inittest() {

        View account_view = LayoutInflater.from(this).inflate(R.layout.account_footview, null);

        expandableListView.addFooterView(account_view);
        account_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showParentDialog();

            }
        });

        expandAadapter = new ExpandAadapter(this, parent, mdata);
        expandableListView.setAdapter(expandAadapter);
    }


    private void showParentDialog() {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        linkDialog = adb.create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_new, null);

        final EditText et_editor = (EditText) view.findViewById(R.id.et_editor);
        //点击确实的监听
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str = et_editor.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(TestActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                } else {

                    //进行网络请求，插入成功之后，局部刷新界面
                    addData();
                    linkDialog.dismiss();

                }

            }
        });
        //点击取消的监听
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkDialog.dismiss();
            }
        });
        linkDialog.setCancelable(false);
        linkDialog.setView(view, 0, 0, 0, 0); // 设置 view
        linkDialog.show();
    }

    /**
     * 添加设备
     */
    private void addData() {

        progressDialogUtils = new ProgressDialogUtils(this);
        String url = HttpHostHolder.addBox(str);
        XHttpUtil.getXHttpUtilInstance().dogetfromserver(url, this, HttpConfigs.RESULT_CEODE_2);

    }

    private void notifyparent() {

        UserDaoImpl userDao = new UserDaoImpl(this);
        parent.clear();
        parent.addAll(userDao.getParents());
        mdata.clear();
        mdata.addAll(userDao.getChild());
        expandableListView.setVisibility(View.VISIBLE);
        expandAadapter.notifyDataSetChanged();
        sqlAdapter.notifyDataSetChanged();
    }

    @Override
    protected void loadData() {

        String account = SharePreferenceUtils.getLognUsername();
        String password = SharePreferenceUtils.getLognpassword();
        tv_username.setText(account);
        progressDialogUtils = new ProgressDialogUtils(TestActivity.this);
        progressDialogUtils.show();
        String url = HttpHostHolder.userBox(account, password);
        XHttpUtil.getXHttpUtilInstance().dogetfromserver(url, TestActivity.this, HttpConfigs.RESULT_CEODE_1);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_left:
                drawerLayout.openDrawer(Gravity.LEFT);


                break;

            case R.id.bt_main:
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
        }
    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo, int resultCode) {

        String result = responseInfo.result;
        switch (resultCode) {

            case HttpConfigs.RESULT_CEODE_1:

                BeseBean beseBean = new BeseBean();
                List<BoxMessageBean> data = beseBean.parse(result).getContent();

                for (int i = 0; i < data.size(); i++) {

                    String boxname = data.get(i).boxname;
                    parent.add(boxname);
                    System.out.println(boxname);
                    List<String> boarddata = data.get(i).boarddata;
                    mdata.add(boarddata);
                }
                inittest();
                if (progressDialogUtils != null) {
                    progressDialogUtils.dismiss();
                }
                drawerLayout.openDrawer(Gravity.LEFT);
                break;

            case HttpConfigs.RESULT_CEODE_2:

                BeseBean bean = new BeseBean();
                BeseBean beseBean1 = bean.parse(result);
                if (beseBean1.status.equals("1")) {
                    //插入数据成功
                    parent.add(str);
                    List<String> newdata = new ArrayList<String>();
                    mdata.add(newdata);
                    expandAadapter.notifyDataSetChanged();
                    progressDialogUtils.dismiss();
                } else {
                    Toast.makeText(this, beseBean1.message, Toast.LENGTH_SHORT).show();
                    progressDialogUtils.dismiss();
                }
                break;

            case HttpConfigs.RESULT_CEODE_3:

                BeseBean boardbean = new BeseBean();
                BeseBean beandata = boardbean.parse(result);
                if (beandata.status.equals("1")) {
                    //查询数据成功
                    List<BoardInfo> boardData = beandata.getBoardDetail();
                    sqlAdapter = new SQLAdapter(this, boardData);
                    lv_sql1.setAdapter(sqlAdapter);

                } else {
                    Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
                }
                progressDialogUtils.dismiss();
                break;

            case HttpConfigs.RESULT_CEODE_4:
                BeseBean delete = new BeseBean();
                BeseBean deletebean = delete.parse(result);
                if (deletebean.status.equals("1")) {
                    //删除成功
                    expandAadapter.parent.remove(currentpositon);
                    expandAadapter.childdata.remove(currentpositon);
                    expandAadapter.notifyDataSetChanged();
                    progressDialogUtils.dismiss();
                } else {
                    Toast.makeText(this, deletebean.message, Toast.LENGTH_SHORT).show();
                    progressDialogUtils.dismiss();
                }

                break;

        }

    }

    @Override
    public void onFail(String errorInfo, int resultCode) {

        Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
        progressDialogUtils.dismiss();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    public void showWarnDialog() {

        warn_dialog = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_warn, null);
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                warn_dialog.dismiss();
                //向服务器发送网络请求,删除
                String parent_data = expandAadapter.parent.get(currentpositon);
                List<String> child_data = expandAadapter.childdata.get(currentpositon);

                JSONObject param = new JSONObject();
                StringEntity sEntity = null;
                try {
                    param.put("boxname", parent_data);
                    param.put("boarddata", child_data);
                    sEntity = new StringEntity(param.toString(), "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RequestParams requestParams = new RequestParams();
                requestParams.setBodyEntity(sEntity);
                progressDialogUtils = new ProgressDialogUtils(TestActivity.this);
                String url = HttpHostHolder.dropbox();
                XHttpUtil.getXHttpUtilInstance().doPostserver(url, requestParams, TestActivity.this, HttpConfigs.RESULT_CEODE_4);

            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warn_dialog.dismiss();

            }
        });
        warn_dialog.setCancelable(false);
        warn_dialog.setView(view, 0, 0, 0, 0); // 设置 view
        warn_dialog.show();

    }

    /**
     * 按两次退出
     */
    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出爱车app", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}