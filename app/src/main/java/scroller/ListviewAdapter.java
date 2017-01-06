package scroller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.usecar.uescar.R;


/**
 * @function listviewadapter
 * @auther: Created by yinglan
 * @time: 16/3/16
 */
public class ListviewAdapter extends BaseAdapter {

    private Context mContext;

    public ListviewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 50;
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
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_listview, null);
        }

        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
