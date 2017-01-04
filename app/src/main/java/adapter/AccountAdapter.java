package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.usecar.uescar.R;

import java.util.List;

import activity.TestActivity;
import bean.User;
import db.UserDao;
import db.UserDaoImpl;
import util.CommonConfig;

/**
 * Created by sunxipeng on 2016/11/7.
 */
public class AccountAdapter extends BaseAdapter {


    private Context context;
    private List<User> mdata;
    private TextView tv_count;

    public AccountAdapter(Context context, List<User> mdata) {

        this.context = context;
        this.mdata = mdata;
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int i) {
        return mdata.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        View account_view = LayoutInflater.from(context).inflate(R.layout.account_item,null);
        TextView tv = (TextView) account_view.findViewById(R.id.tv);
        tv.setText(mdata.get(i).username);
        account_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonConfig.USRNAME = mdata.get(i).username;
                context.sendBroadcast(new Intent(CommonConfig.REFRESH));
            }
        });
        //TextView tv_count = (TextView) account_view.findViewById(R.id.tv_count);
        TextView tv_count_1 = (TextView) account_view.findViewById(R.id.tv_count_1);

        /*UserDaoImpl userDao = new UserDaoImpl(context);

        List<String> paretnt = userDao.getAllCount(mdata.get(i).username);

        if(paretnt.size()>0){
            tv_count.setText(String.valueOf(paretnt.size()));
            tv_count_1.setText(String.valueOf(paretnt.size()));
        }else {
            tv_count.setText("");
            tv_count_1.setText("");
        }*/

        return account_view;
    }
}
