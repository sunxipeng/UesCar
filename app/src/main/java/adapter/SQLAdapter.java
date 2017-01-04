package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.usecar.uescar.R;

import java.util.List;

import activity.TestActivity;
import bean.BoardInfo;
import util.CommonConfig;

/**
 * Created by sunxipeng on 2016/11/8.
 */
public class SQLAdapter extends BaseAdapter {
    public List<BoardInfo> boardData;
    private Context context;


    public SQLAdapter(Context context, List<BoardInfo> boardData) {

        this.context = context;
        this.boardData = boardData;
    }

    @Override
    public int getCount() {
        return boardData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.sql_item, null);

            viewHolder.iv_board_car = (ImageView) view.findViewById(R.id.iv_board_car);
            viewHolder.tv_name_key = (TextView) view.findViewById(R.id.tv_name_key);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);

            viewHolder.tv_code_key = (TextView) view.findViewById(R.id.tv_code_key);
            viewHolder.tv_code = (TextView) view.findViewById(R.id.tv_code);
            viewHolder.tv_oper_key = (TextView) view.findViewById(R.id.tv_oper_key);
            viewHolder.tv_oper = (TextView) view.findViewById(R.id.tv_oper);

            viewHolder.tv_position_key = (TextView) view.findViewById(R.id.tv_position_key);
            viewHolder.tv_position = (TextView) view.findViewById(R.id.tv_position);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_position.setText(boardData.get(i).get库位());

        return view;
    }

    class ViewHolder {

        private ImageView iv_board_car;
        private TextView tv_name_key;
        private TextView tv_name;

        private TextView tv_code_key;
        private TextView tv_code;

        private TextView tv_oper_key;
        private TextView tv_oper;

        private TextView tv_position_key;
        private TextView tv_position;
    }
}
