package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
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

import java.util.List;

import bean.BeseBean;
import bean.BoardInfo;
import util.CommonConfig;
import util.ProgressDialogUtils;

/**
 * Created by sunxipeng on 2016/11/7.
 */
public class ChildAdapter extends BaseAdapter implements XHttpUtil.HttpCallBack {
    private String parent;
    private Context context;
    private List<String> strings;
    private int position;
    private AlertDialog warn_dialog;
    private ProgressDialogUtils progressDialogUtils;

    public ChildAdapter(Context context, List<String> strings, String parent) {

        this.context = context;
        this.strings = strings;
        this.parent = parent;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int i) {
        return strings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View item_view = LayoutInflater.from(context).inflate(R.layout.item, null);

        TextView tv = (TextView) item_view.findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.sendBroadcast(new Intent(CommonConfig.CLOSE).putExtra("parent", parent).putExtra("child", strings.get(i)));
            }
        });
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                position = i;
                showWarnDialog();

                return true;
            }
        });
        tv.setText(strings.get(i));

        return item_view;
    }

    private void delete(String parentName, String childName) {

        progressDialogUtils = new ProgressDialogUtils(context);
        progressDialogUtils.show();
        String url = HttpHostHolder.deletetable(parentName, childName);
        XHttpUtil.getXHttpUtilInstance().dogetfromserver(url, this, HttpConfigs.RESULT_CEODE_2);
    }


    @Override
    public void onSuccess(ResponseInfo<String> responseInfo, int resultCode) {

        String result = responseInfo.result;
        BeseBean beseBean = new BeseBean();
        BeseBean bean = beseBean.parse(result);
        if (bean.status.equals("1")) {
            //删除成功
            strings.remove(position);
            notifyDataSetChanged();

        } else {
            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
        }

        progressDialogUtils.dismiss();
    }

    @Override
    public void onFail(String errorInfo, int resultCode) {
        progressDialogUtils.dismiss();
        Toast.makeText(context, "链接服务器失败", Toast.LENGTH_SHORT).show();
    }

    public void showWarnDialog() {

        warn_dialog = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_warn, null);
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warn_dialog.dismiss();
                delete(parent, strings.get(position));

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
}
