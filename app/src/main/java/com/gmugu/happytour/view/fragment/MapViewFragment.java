package com.gmugu.happytour.view.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.gmugu.happytour.comment.assist.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mugu on 15-12-24.
 */
public class MapViewFragment extends Fragment {

    private BaiduMap mBaiduMap;
    private MapView mMapView;
    // 起点图标覆盖物
    private static MarkerOptions startMarker = null;
    // 终点图标覆盖物
    private static MarkerOptions endMarker = null;
    // 路线覆盖物
    private PolylineOptions polyline = null;
    private boolean isInUploadFragment = true;

    //data
    private static List<LatLng> pointList = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.i(this, "onCreate");

        mMapView = new MapView(getActivity(), new BaiduMapOptions().mapStatus(new MapStatus.Builder().zoom(18).build()));

        // 删除百度地图logo
//        mMapView.removeViewAt(1);
        // 隐藏比例尺控件
        for (int i = 0; i < mMapView.getChildCount(); i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                child.setVisibility(View.GONE);
                break;
            }
        }

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

//        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mMapView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     * 显示实时轨迹
     */
    private void showRealtimeTrack(double latitude, double longitude) {

        if (Math.abs(latitude - 0.0) < 0.000001 && Math.abs(longitude - 0.0) < 0.000001) {
            Log.i(this, "当前查询无轨迹点");
        } else {
            LatLng latLng = new LatLng(latitude, longitude);
            pointList.add(latLng);
            if (isInUploadFragment) {
                // 绘制实时点
                drawRealtimePoint(latLng);
            }
        }
    }

    /**
     * 绘制实时点
     */
    private void drawRealtimePoint(LatLng point) {
        mBaiduMap.clear();

//        realtimeBitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_gcoding);
//
//        overlay = new MarkerOptions().position(point)
//                .icon(realtimeBitmap).zIndex(9).draggable(true);

        if (pointList.size() >= 2 && pointList.size() <= 10000) {
            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(Color.RED).points(pointList);
        } else {
            polyline = null;
        }
        addMarker();

    }

    /**
     * 添加地图覆盖物
     */
    private void addMarker() {

        // 路线覆盖物
        if (null != polyline) {
            mBaiduMap.addOverlay(polyline);
        }

//        // 实时点覆盖物
//        if (null != overlay) {
//            mBaiduMap.addOverlay(overlay);
//        }
    }


    private void showLocationInfo(BDLocation location, double longitude, double latitude) {
        StringBuilder sb = new StringBuilder();
        if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());// 单位：公里每小时
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());
            sb.append("\nheight : ");
            sb.append(location.getAltitude());// 单位：米
            sb.append("\ndirection : ");
            sb.append(location.getDirection());
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            sb.append("\ndescribe : ");
            sb.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            //运营商信息
            sb.append("\noperationers : ");
            sb.append(location.getOperators());
            sb.append("\ndescribe : ");
            sb.append("网络定位成功");
        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");
        } else if (location.getLocType() == BDLocation.TypeServerError) {
            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        Toast.makeText(getActivity(), latitude + "  " + longitude + "\n" + sb + "\n" + pointList.size(), Toast.LENGTH_SHORT).show();
    }

    public void animateToLocation(float radius, double longitude, double latitude) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(radius) // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100)
                .latitude(latitude)
                .longitude(longitude)
                .build();
        mBaiduMap.setMyLocationData(locData);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18f);
        mBaiduMap.animateMapStatus(u);

    }

    public void addPointToTrackAndShow(double longitude, double latitude) {
        showRealtimeTrack(latitude, longitude);
    }

    public void cleanTrack() {
        pointList.clear();
    }
}