package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.ResponseInfo;
import com.usecar.uescar.R;

import net.HttpConfigs;
import net.HttpHostHolder;
import net.XHttpUtil;

import java.util.List;

import bean.BeseBean;
import db.UserDaoImpl;
import util.CommonConfig;
import util.ListViewParamsUtils;
import util.ProgressDialogUtils;
import view.CustomListView;

/**
 * Created by sunxipeng on 2016/11/7.
 */
public class ExpandAadapter extends BaseExpandableListAdapter implements XHttpUtil.HttpCallBack {


    public List<List<String>> childdata;
    public List<String> parent;
    private Context context;
    private AlertDialog linkDialog;
    private String parent_test;
    private List<String> child;
    private ChildAdapter childAdapter;
    private String str;
    private ProgressDialogUtils progressDialogUtils;

    public ExpandAadapter(Context context, List<String> parent, List<List<String>> childdata) {

        this.parent = parent;
        this.context = context;
        this.childdata = childdata;
    }

    @Override
    public int getGroupCount() {
        return parent.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return parent.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childdata.get(i).get(i1);
    }

    //子条目的位置id
    int position = 0;

    @Override
    public long getGroupId(int i) {

        return i;
    }


    @Override
    public long getChildId(int i, int i1) {
        position = i;
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        View parent_view = LayoutInflater.from(context).inflate(R.layout.parent_item, null);

        parent_view.setTag(R.layout.parent_item, i);
        TextView tv = (TextView) parent_view.findViewById(R.id.tv);
        tv.setText(parent.get(i));
        return parent_view;
    }

    @Override
    public View getChildView(final int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        String test = "" + i;
        View footview = LayoutInflater.from(context).inflate(R.layout.footview, null);
        View child_view = LayoutInflater.from(context).inflate(R.layout.child_list_item, null);
        CustomListView listview = (CustomListView) child_view.findViewById(R.id.listview);
        listview.addFooterView(footview);
        childAdapter = new ChildAdapter(context, childdata.get(i), parent.get(i));
        listview.setAdapter(childAdapter);
        ListViewParamsUtils.setListViewHeightBasedOnChildren(listview);
        footview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                parent_test = parent.get(position);
                child = childdata.get(position);
                showDialog();
            }
        });

        return child_view;
    }

    private void showDialog() {

        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        linkDialog = adb.create();

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_new, null);

        final EditText et_editor = (EditText) view.findViewById(R.id.et_editor);
        //点击确实的监听
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str = et_editor.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(context, "输入不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //进行网络请求，添加新的表
                    //添加数据成功之后

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

    private void addData() {

        progressDialogUtils = new ProgressDialogUtils(context);
        progressDialogUtils.show();
        String url = HttpHostHolder.addTbale(parent_test, str);
        XHttpUtil.getXHttpUtilInstance().dogetfromserver(url, this, HttpConfigs.RESULT_CEODE_1);
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo, int resultCode) {

        String result = responseInfo.result;
        BeseBean beseBean = new BeseBean();
        BeseBean bean = beseBean.parse(result);
        if (bean.status.equals("1")) {

            //插入数据创建表成功
            child.add(str);
            childAdapter.notifyDataSetChanged();

        } else {

            Toast.makeText(context, "创建推拉板数据失败", Toast.LENGTH_SHORT).show();
        }

        progressDialogUtils.dismiss();

    }

    @Override
    public void onFail(String errorInfo, int resultCode) {
        progressDialogUtils.dismiss();
        Toast.makeText(context, "链接服务器失败，请稍后重试", Toast.LENGTH_SHORT).show();
    }
}
