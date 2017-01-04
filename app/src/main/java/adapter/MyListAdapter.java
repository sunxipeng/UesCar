package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.usecar.uescar.R;

import java.util.List;

/**
 * Created by sunxipeng on 2016/11/7.
 */
public class MyListAdapter extends BaseAdapter {
    private List<String> parent;
    private Context context;

    public MyListAdapter(Context context,List<String> parent) {

        this.context = context;
        this.parent = parent;
    }

    @Override
    public int getCount() {
        return parent.size();
    }

    @Override
    public Object getItem(int i) {
        return parent.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.item,null);
    }
}
