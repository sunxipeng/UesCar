package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.usecar.uescar.R;

import java.util.List;
import java.util.Map;

/**
 * Created by sunxipeng on 2016/10/29.
 */
public class MyExpendableListAdapter extends BaseExpandableListAdapter {


    private Map<Integer, List<String>> childMap;
    private List<String> groupTitle;
    private Context context;

    public MyExpendableListAdapter(Context context, List<String> groupTitle, Map<Integer, List<String>> childMap) {

        this.context = context;
        this.groupTitle = groupTitle;
        this.childMap = childMap;
    }

    @Override
    public int getGroupCount() {
        return groupTitle.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childMap.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupTitle.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childMap.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        View view_item = LayoutInflater.from(context).inflate(R.layout.parent_item,null);
        TextView tv = (TextView) view_item.findViewById(R.id.tv);
        tv.setText(groupTitle.get(i));

        return view_item;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        View view_item = LayoutInflater.from(context).inflate(R.layout.item,null);
        TextView tv = (TextView) view_item.findViewById(R.id.tv);
        if(i1==childMap.get(i).size()-1){

            tv.setTextColor(Color.parseColor("#0000FF"));
        }

        String str = childMap.get(i).get(i1);
        tv.setText(str);
        return view_item;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
