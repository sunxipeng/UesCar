package activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.usecar.uescar.R;

import scroller.ListviewAdapter;
import scroller.ScreenUtil;
import scroller.ScrollLayout;

/**
 * Created by Administrator on 2017/1/5.
 */
public class BaiduMapActity extends BaseActivity implements View.OnClickListener {

    private MapView mMapView;
    private ScrollLayout mScrollLayout;
    private TextView text_foot;

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
        //this.mLocationClient = new LocationClient(getApplicationContext());
        initContentView();

    }

    private void initContentView() {

        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
        text_foot = (TextView) findViewById(R.id.text_foot);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new ListviewAdapter(this));

        /**设置 setting*/
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(this) * 0.5));
        mScrollLayout.setExitOffset(ScreenUtil.dip2px(this, 50));
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

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
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

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location.getLocType() == BDLocation.TypeGpsLocation) {//gps定位


            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {//网络定位


            }
        }
    }
}
