package bean;


import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunxipeng on 2016/12/13.
 */
public class BeseBean implements Serializable {


    public String status;
    public String message;
    public String content;

    public BeseBean parse(String jsonString) {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            status = jsonObject.optString("status");
            message = jsonObject.optString("message");
            content = jsonObject.optString("content");
            return this;
        } catch (Exception e) {

        }

        return null;
    }

    public List<BoxMessageBean> getContent() {

        Gson gson = new Gson();
        List<BoxMessageBean> obj = gson.fromJson(content.toString(), new TypeToken<List<BoxMessageBean>>() {
        }.getType());

        return obj;

    }


    public List<BoardInfo> getBoardDetail() {

        Gson gson = new Gson();
        List<BoardInfo> obj = gson.fromJson(content.toString(), new TypeToken<List<BoardInfo>>() {
        }.getType());

        return obj;
    }

    public List<MarkDetailMessage> getMarkDetail() {

        Gson gson = new Gson();
        List<MarkDetailMessage> obj = gson.fromJson(content.toString(), new TypeToken<List<MarkDetailMessage>>() {
        }.getType());

        return obj;
    }

}
