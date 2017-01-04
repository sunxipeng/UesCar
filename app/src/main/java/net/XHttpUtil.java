package net;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by sunxipeng on 2016/12/13.
 */
public class XHttpUtil {

    public static final String TAG = "XHttpUtil";
    private static XHttpUtil XHttpUtilInstance = null;
    private HttpUtils mHttpUtil = null;

    private XHttpUtil() {

        mHttpUtil = new HttpUtils(HttpConfigs.TIME_OUT, HttpConfigs.USER_AFENT);
        //设置默认请求的缓存时间
        mHttpUtil.configDefaultHttpCacheExpiry(0);
    }

    public static XHttpUtil getXHttpUtilInstance() {

        if (XHttpUtilInstance == null) {
            XHttpUtilInstance = new XHttpUtil();
        }
        return XHttpUtilInstance;
    }


    public interface HttpCallBack {

        public void onSuccess(ResponseInfo<String> responseInfo, int resultCode);

        public void onFail(String errorInfo, int resultCode);
    }

    public void dogetfromserver(String url, final HttpCallBack callBack, final int resultCode) {


        this.mHttpUtil.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {


            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                callBack.onSuccess(responseInfo, resultCode);

            }

            @Override
            public void onFailure(HttpException e, String s) {

                callBack.onFail(s, resultCode);

            }
        });

    }


    public void doPostserver(String url,RequestParams params,final HttpCallBack callBack,final int resultCode){

        this.mHttpUtil.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                callBack.onSuccess(responseInfo, resultCode);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                callBack.onFail(s, resultCode);

            }
        });
    }
}
