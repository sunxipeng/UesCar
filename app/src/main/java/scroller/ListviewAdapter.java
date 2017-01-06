package scroller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.usecar.uescar.R;

import java.util.List;

import bean.MarkDetailMessage;


/**
 * @function listviewadapter
 * @auther: Created by yinglan
 * @time: 16/3/16
 */
public class ListviewAdapter extends BaseAdapter {

    private List<MarkDetailMessage> data;
    private Context mContext;

    public ListviewAdapter(Context context, List<MarkDetailMessage> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MarkDetailMessage markDetailMessage = data.get(position);
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_listview, null);
            TextView a_name = (TextView) convertView.findViewById(R.id.a_name);
            TextView number = (TextView) convertView.findViewById(R.id.number);
            a_name.setText(markDetailMessage.getName());
            number.setText(markDetailMessage.getIcnumber());
        }

        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
