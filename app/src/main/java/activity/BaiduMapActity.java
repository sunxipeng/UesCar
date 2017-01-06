package activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.http.ResponseInfo;
import com.usecar.uescar.R;

import net.HttpConfigs;
import net.HttpHostHolder;
import net.XHttpUtil;

import java.util.ArrayList;
import java.util.List;

import bean.BeseBean;
import bean.MarkDetailMessage;
import scroller.ListviewAdapter;
import scroller.ScreenUtil;
import scroller.ScrollLayout;
import util.ProgressDialogUtils;
import util.SharePreferenceUtils;

/**
 * Created by Administrator on 2017/1/5.
 */
public class BaiduMapActity extends BaseActivity implements View.OnClickListener, XHttpUtil.HttpCallBack {

    private MapView mMapView;
    private ScrollLayout mScrollLayout;
    private LinearLayout text_foot;
    //百度地图mark图标
    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
    public BDLocationListener myListener = new MyLocationListener();

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                mScrollLayout.getBackground().setAlpha(255 - (int) precent);
            }
            if (text_foot.getVisibility() == View.VISIBLE)
                text_foot.setVisibility(View.GONE);
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
                text_foot.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };
    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private ProgressDialogUtils progressDialogUtils;
    private ListView listView;
    private List<MarkDetailMessage> markdata;
    private TextView icname;
    private TextView icnumber;
    private boolean isfirstlocation = true;//是否显示弹窗的标志位
    private AlertDialog linkDialog;
    private String str_number;
    private String str_address;
    private MarkDetailMessage markDetailMessage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_baidumap;
    }

    @Override
    public void initBaidumap() {
        super.initBaidumap();
        SDKInitializer.initialize(getApplicationContext());
    }

    @Override
    protected void initView() {
        mMapView = (MapView) findViewById(R.id.baidumap);
        baiduMap = mMapView.getMap();
        locationClient = new LocationClient(getApplicationContext());
        ImageView iv_track = (ImageView) findViewById(R.id.iv_track);
        iv_track.setOnClickListener(this);
        icname = (TextView) findViewById(R.id.tv_icname);
        icnumber = (TextView) findViewById(R.id.tv_icnumber);
        initContentView();
    }

    private void initContentView() {

        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
        text_foot = (LinearLayout) findViewById(R.id.text_foot);
        listView = (ListView) findViewById(R.id.list_view);
        //listView.setAdapter(new ListviewAdapter(this));

        /**设置 setting*/
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(this) * 0.5));
        mScrollLayout.setExitOffset(ScreenUtil.dip2px(this, 70));
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToExit();

        mScrollLayout.getBackground().setAlpha(0);

        text_foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.setToOpen();
            }
        });
    }

    @Override
    protected void loadData() {

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);
        initLocation();

        //开始定位
        locationClient.start();
        //进行网络请求
        request();
    }

    //加载用户添加的巡更点
    private void request() {

        progressDialogUtils = new ProgressDialogUtils(this);
        String url = HttpHostHolder.getmarkdetail(SharePreferenceUtils.getLognUsername(), SharePreferenceUtils.getLognpassword());
        XHttpUtil.getXHttpUtilInstance().dogetfromserver(url, this, HttpConfigs.RESULT_CEODE_1);
    }


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        locationClient.setLocOption(option);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_track:
                locationClient.requestLocation();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        //停止定位
        locationClient.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private void navigateTo(BDLocation location) {

        markDetailMessage = new MarkDetailMessage();
        markDetailMessage.setLatitude(String.valueOf(location.getLatitude()));
        markDetailMessage.setLongitude(String.valueOf(location.getLongitude()));
        markDetailMessage.setWalk("0");
        Log.d("TestActivity", "经度：：：" + String.valueOf(location.getLatitude()));
        Log.d("TestActivity", "纬度：：：" + String.valueOf(location.getLongitude()));
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions localMarkerOptions = new MarkerOptions().position(latLng).icon(bitmapDescriptor);
        baiduMap.addOverlay(localMarkerOptions);
        MapStatusUpdate mapStatusUpdate1 = MapStatusUpdateFactory.newLatLng(latLng);
        MapStatusUpdate mapStatusUpdate2 = MapStatusUpdateFactory.zoomTo(18.0F);
        baiduMap.setMapStatus(mapStatusUpdate1);
        baiduMap.setMapStatus(mapStatusUpdate2);
        icname.setText(location.getAddrStr());

        if (!isfirstlocation) {
            //显示弹窗
            showDialog();
        }
        isfirstlocation = false;
    }

    private void showDialog() {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        linkDialog = adb.create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_location, null);

        final EditText et_address = (EditText) view.findViewById(R.id.et_address);
        final EditText et_icnumber = (EditText) view.findViewById(R.id.et_icnumber);
        //点击确实的监听
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_address = et_address.getText().toString().trim();
                str_number = et_icnumber.getText().toString().trim();
                if (str_address.equals(null) || str_number.equals(null)) {
                    Toast.makeText(BaiduMapActity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //进行网络请求，插入成功之后，局部刷新界面
                    linkDialog.dismiss();

                    addmarkpositionLoadData();

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

    //进行网络请求
    private void addmarkpositionLoadData() {

        String username = SharePreferenceUtils.getLognUsername();
        String password = SharePreferenceUtils.getLognpassword();

        progressDialogUtils = new ProgressDialogUtils(this);
        String url = HttpHostHolder.addMarkPosition(username, password, str_address, str_number, markDetailMessage);
        XHttpUtil.getXHttpUtilInstance().dogetfromserver(url, this, HttpConfigs.RESULT_CEODE_2);

    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo, int resultCode) {

        String result = responseInfo.result;
        switch (resultCode) {
            case HttpConfigs.RESULT_CEODE_1:

                BeseBean beseBean = new BeseBean();
                BeseBean bean = beseBean.parse(result);
                if (bean.status.equals("1")) {
                    markdata = bean.getMarkDetail();
                    ListviewAdapter listviewAdapter = new ListviewAdapter(this, markdata);
                    listView.setAdapter(listviewAdapter);

                    drawmarkline();
                } else {
                    Toast.makeText(this, bean.message, Toast.LENGTH_SHORT).show();
                }

                break;

            case HttpConfigs.RESULT_CEODE_2:

                BeseBean beseBean1 = new BeseBean();
                BeseBean bean1 = beseBean1.parse(result);
                if (bean1.status.equals("1")) {

                    //添加位置成功
                }
                Toast.makeText(this, bean1.message, Toast.LENGTH_SHORT).show();
                break;

        }
        progressDialogUtils.dismiss();
    }

    List<LatLng> ll_data = new ArrayList<>();
    List<Integer> colorsdata = new ArrayList<>();

    //为mark点连线
    private void drawmarkline() {

        for (MarkDetailMessage markDetailMessage : markdata) {

            LatLng latLng = new LatLng(Double.parseDouble(markDetailMessage.getLatitude())
                    , Double.parseDouble(markDetailMessage.getLongitude()));
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(bitmapDescriptor);
            baiduMap.addOverlay(markerOptions);
            ll_data.add(latLng);
            colorsdata.add(Color.RED);
        }

        PolylineOptions polylineOptions = new PolylineOptions().width(10).colorsValues(colorsdata).points(ll_data);
        this.baiduMap.addOverlay(polylineOptions);

    }

    @Override
    public void onFail(String errorInfo, int resultCode) {
        progressDialogUtils.dismiss();
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location.getLocType() == BDLocation.TypeGpsLocation) {//gps定位
                navigateTo(location);
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {//网络定位
                navigateTo(location);
            } else {
                Toast.makeText(BaiduMapActity.this, "定位失败，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
