package activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import zxing.activity.CaptureActivity;

/**
 * Created by sunxipeng on 2016/11/7.
 */
public class TestActivity extends BaseActivity implements View.OnClickListener, XHttpUtil.HttpCallBack, DrawerLayout.DrawerListener {


    //private TextView bt_left;
    //private TextView bt_main;
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
    private List<BoardInfo> boardData;

    //checkbox是否展示的标志位
    private boolean isshow = false;
    //backspac的标志位
    private boolean cancleDelete = false;
    private ImageView delete;
    private ImageView iv_addkey;
    private EditText et_mapper;
    private BoardInfo addkeyboardInfo;
    private int op_position;
    private ImageView iv_setting;
    private ImageView iv_search;
    private ImageView iv_editor;
    private LinearLayout ll_all_operate;

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
        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);
        iv_editor = (ImageView) findViewById(R.id.iv_editor);
        iv_editor.setOnClickListener(this);
        iv_addkey = (ImageView) findViewById(R.id.iv_addkey);
        iv_addkey.setOnClickListener(this);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        iv_setting.setOnClickListener(this);
        ll_all_operate = (LinearLayout) findViewById(R.id.ll_all_operate);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(this);
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

        delete = (ImageView) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        lv_sql1 = (ListView) findViewById(R.id.lv_sql);
        lv_sql1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                BoardInfo boardInfo = (BoardInfo) boardData.get(i);
                if (isshow) {

                    if (boardInfo.isSelected()) {
                        boardInfo.setSelected(false);
                        if (selectData.contains(boardInfo)) {
                            selectData.remove(boardInfo);
                        }
                    } else {
                        boardInfo.setSelected(true);
                        if (selectData.contains(boardInfo)) {
                            selectData.remove(boardInfo);
                        }
                        selectData.add(boardInfo);
                    }

                    sqlAdapter.notifyDataSetChanged();
                }
                //startActivity(new Intent(TestActivity.this, UserDetailActivity.class));
            }
        });

        lv_sql1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                op_position = i;
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
                        .putExtra("operete", operete).putExtra("position", boardData.get(position).get库位()));
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

                    case "refresh_detail":

                        BoardInfo boardInfo = (BoardInfo) intent.getSerializableExtra("operate_result");
                        BoardInfo n_boardInfo = (BoardInfo) boardData.get(op_position);
                        n_boardInfo.set操作(boardInfo.get操作());
                        n_boardInfo.set日期(boardInfo.get日期());
                        n_boardInfo.set时间(boardInfo.get时间());
                        n_boardInfo.set姓名(boardInfo.get姓名());
                        n_boardInfo.set备注(boardInfo.get备注());
                        sqlAdapter.notifyDataSetChanged();
                        break;

                    case "closemainactivity":
                        finish();
                        break;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConfig.REFRESH);
        intentFilter.addAction(CommonConfig.CLOSE);
        intentFilter.addAction(CommonConfig.REFRESH_DETAIL_BOARD);
        intentFilter.addAction(CommonConfig.CLOSEMAINACTIVITY);
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
        String url = HttpHostHolder.getBoardDetail(parentName, childName, SharePreferenceUtils.getLognUsername(), SharePreferenceUtils.getLognpassword());
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
        String url = HttpHostHolder.addBox(str, SharePreferenceUtils.getLognUsername(), SharePreferenceUtils.getLognpassword());
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
            case R.id.iv_search:
                //drawerLayout.openDrawer(Gravity.LEFT);
                break;

            case R.id.iv_editor:
                //删除数据操作
                ll_all_operate.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                this.cancleDelete = true;
                this.isshow = true;
                for (BoardInfo board : boardData) {
                    board.setIsshow(true);
                }
                sqlAdapter.notifyDataSetChanged();
                break;

            case R.id.delete:

                String str1 = SharePreferenceUtils.getLognUsername();
                String str2 = SharePreferenceUtils.getLognpassword();
                JSONObject localJSONObject = new JSONObject();
                try {
                    localJSONObject.put("boardname", this.boxname + "_" + this.boardname);
                    localJSONObject.put("keydata", new Gson().toJson(this.selectData));
                    localJSONObject.put("db_username", str1);
                    localJSONObject.put("db_password", str2);
                    Log.d("TESTACTIVITY", localJSONObject.toString());
                    StringEntity localStringEntity2 = new StringEntity(localJSONObject.toString(), "utf-8");
                    RequestParams localRequestParams = new RequestParams();
                    localRequestParams.setBodyEntity(localStringEntity2);
                    this.progressDialogUtils = new ProgressDialogUtils(this);
                    String str3 = HttpHostHolder.deletekey();
                    XHttpUtil.getXHttpUtilInstance().doPostserver(str3, localRequestParams, this, HttpConfigs.RESULT_CEODE_5);
                    return;
                } catch (Exception localException) {

                    localException.printStackTrace();

                }
                break;


            case R.id.iv_addkey:
                showAddKeyDialog();
                break;

            case R.id.iv_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    //添加钥匙弹窗
    private void showAddKeyDialog() {
        linkDialog = new AlertDialog.Builder(this).create();
        View addKeyview = LayoutInflater.from(this).inflate(R.layout.dialog_new, null);
        final EditText et_editor = (EditText) addKeyview.findViewById(R.id.et_editor);
        et_mapper = ((EditText) addKeyview.findViewById(R.id.et_mapper));
        addKeyview.findViewById(R.id.bt_mapper).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Intent localIntent = new Intent(TestActivity.this, CaptureActivity.class);
                TestActivity.this.startActivityForResult(localIntent, 0);
            }
        });

        addKeyview.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String str_editor = et_editor.getText().toString().trim();
                String str_mapper = et_mapper.getText().toString().trim();
                if ((TextUtils.isEmpty(str_editor)) || (str_mapper.isEmpty())) {
                    Toast.makeText(TestActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String username = SharePreferenceUtils.getLognUsername();
                String password = SharePreferenceUtils.getLognpassword();

                addkeyboardInfo = new BoardInfo();
                addkeyboardInfo.set库位(str_editor);
                addkeyboardInfo.set钥匙编码(str_mapper);
                String str_keydata = new Gson().toJson(addkeyboardInfo);
                JSONObject localJSONObject = new JSONObject();
                try {
                    localJSONObject.put("boardname", boardname);
                    localJSONObject.put("boxname", boxname);
                    localJSONObject.put("db_username", username);
                    localJSONObject.put("db_password", password);
                    localJSONObject.put("keyinfo", str_keydata);
                    StringEntity localStringEntity = new StringEntity(localJSONObject.toString(), "utf-8");
                    RequestParams localRequestParams = new RequestParams();
                    localRequestParams.setBodyEntity(localStringEntity);
                    progressDialogUtils = new ProgressDialogUtils(TestActivity.this);
                    String url = HttpHostHolder.addkey();
                    XHttpUtil.getXHttpUtilInstance().doPostserver(url, localRequestParams, TestActivity.this, HttpConfigs.RESULT_CEODE_6);
                    linkDialog.dismiss();
                } catch (Exception localException) {

                }
            }


        });

        addKeyview.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linkDialog.dismiss();
            }

        });
        linkDialog.setCancelable(false);
        linkDialog.setView(addKeyview, 0, 0, 0, 0);
        linkDialog.show();
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
                    boardData = beandata.getBoardDetail();
                    sqlAdapter = new SQLAdapter(this, boardData);
                    lv_sql1.setAdapter(sqlAdapter);

                } else {
                    Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
                }
                progressDialogUtils.dismiss();
                break;

            case HttpConfigs.RESULT_CEODE_4:
                BeseBean delete1 = new BeseBean();
                BeseBean deletebean = delete1.parse(result);
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

            case HttpConfigs.RESULT_CEODE_5:
                BeseBean localBeseBean2 = new BeseBean().parse(result);
                if (localBeseBean2.status.equals("1")) {
                    this.boardData.removeAll(this.selectData);
                    this.sqlAdapter.notifyDataSetChanged();
                    Toast.makeText(this, localBeseBean2.message, Toast.LENGTH_SHORT).show();
                    this.isshow = false;
                    for (BoardInfo boardInfo : boardData) {
                        boardInfo.setSelected(false);
                        boardInfo.setIsshow(false);
                    }
                    this.sqlAdapter.notifyDataSetChanged();
                    this.selectData.clear();
                    ll_all_operate.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                }
                progressDialogUtils.dismiss();

                break;

            case HttpConfigs.RESULT_CEODE_6:

                BeseBean localBeseBean1 = new BeseBean().parse(result);
                if (localBeseBean1.status.equals("1")) {
                    this.boardData.add(addkeyboardInfo);
                    this.sqlAdapter.notifyDataSetChanged();
                    Toast.makeText(this, localBeseBean1.message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, localBeseBean1.message, Toast.LENGTH_SHORT).show();
                }
                progressDialogUtils.dismiss();
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

    //删除数据条目的临时容器
    private List<BoardInfo> selectData = new ArrayList();

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && cancleDelete) {
            this.isshow = false;
            for (BoardInfo boardinfo : boardData) {
                boardinfo.setSelected(false);
                boardinfo.setIsshow(false);
            }
            this.sqlAdapter.notifyDataSetChanged();
            this.cancleDelete = false;
            this.selectData.clear();
            ll_all_operate.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && !cancleDelete) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK && requestCode == 0) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            et_mapper.setText(scanResult);
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        //侧滑菜单打开
        ll_all_operate.setVisibility(View.GONE);
        iv_setting.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        //侧滑菜单关闭
        ll_all_operate.setVisibility(View.VISIBLE);
        iv_setting.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
