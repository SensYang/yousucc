package com.yousucc.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.yousucc.YouSuccApplication;

public class LocationUtil {
    private static GeocodeSearch geocodeSearch;
    private static AMapLocationClient mlocationClient;
    private static AMapLocationClientOption mLocationOption;
    private static PoiItem poiItem;
    private static LocationReverseGeocode mLocationReverseGeocode = null;
    private static BroadcastLocationInfoListener broadcastLocationInfoListener = null;
    private static AMapLocation amapLocation;

    public interface LocationReverseGeocode {
        void onGetAddrResult(RegeocodeAddress result);
    }

    public interface BroadcastLocationInfoListener {
        void getBroadcastLocationInfo(PoiItem item1);
    }

    private static AMapLocationListener listener;

    public static void setLocationListener(AMapLocationListener locationListener) {
        LocationUtil.listener = locationListener;
    }

    /**
     * 定位监听
     */
    public static AMapLocationListener locationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    if (amapLocation.getLatitude() != 0 && amapLocation.getLongitude() != 0) {
                        //定位成功回调信息，设置相关消息
//                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                        amapLocation.getLatitude();//获取纬度
//                        amapLocation.getLongitude();//获取经度
//                        amapLocation.getAccuracy();//获取精度信息
//                        amapLocation.getTime();//定位时间
//                        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                        amapLocation.getCountry();//国家信息
//                        amapLocation.getProvince();//省信息
//                        amapLocation.getCity();//城市信息
//                        amapLocation.getDistrict();//城区信息
//                        amapLocation.getStreet();//街道信息
//                        amapLocation.getStreetNum();//街道门牌号信息
//                        amapLocation.getCityCode();//城市编码
//                        amapLocation.getAdCode();//地区编码
                        LocationUtil.amapLocation = amapLocation;
                        if (listener != null) {
                            listener.onLocationChanged(amapLocation);
                        }
                    }
                } else {
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    /**
     * 开始获取位置信息
     */
    public static void startLocation() {
        if (null == mlocationClient) {
            //初始化定位
            mlocationClient = new AMapLocationClient(YouSuccApplication.getInstance().getApplicationContext());
            setLocationOption();
        }
        //设置定位回调监听
        mlocationClient.setLocationListener(locationListener);

        if (mlocationClient.isStarted()) {
            mlocationClient.stopLocation();
        }
        mlocationClient.startLocation();
    }

    /**
     * 设置获取位置信息的相关参数
     */
    private static void setLocationOption() {
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60 * 1000);
        mLocationOption.setKillProcess(true);
        //给定位客户端对象设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
    }

    /**
     * 结束位置信息获取
     */
    public static void stopLocation() {
        if (mlocationClient.isStarted()) {
            mlocationClient.stopLocation();
        }
    }

    /**
     * 获取位置信息的经纬度对象
     */
    public static AMapLocation getLocation() {
        return amapLocation;
    }

    /**
     * 获取当前位置的城市信息
     */
    public static PoiItem getmPoiItem() {
        return poiItem;
    }

    /**
     * 根据经纬度反解出当前的位置信息（注册、更改资料位置信息的时候有使用到）
     *
     * @param listener
     */
    public static void getAddr(LocationReverseGeocode listener) {
        mLocationReverseGeocode = listener;
        geocodeSearch = new GeocodeSearch(YouSuccApplication.getInstance().getApplicationContext());
        geocodeSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener() {

            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int errorCode) {
                if (null != regeocodeResult && errorCode == 0) {
                    mLocationReverseGeocode.onGetAddrResult(regeocodeResult.getRegeocodeAddress());
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int errorCode) {

            }
        });
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(new LatLonPoint(amapLocation == null ? 0 : amapLocation.getLatitude(), amapLocation == null ? 0 : amapLocation.getLongitude()),
                1000, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
    }

    /**
     * 发广播界面手机登入获取当前位置信息
     *
     * @param listener
     */
    public static void getBroadcastLocation(BroadcastLocationInfoListener listener) {
        broadcastLocationInfoListener = listener;
        geocodeSearch = new GeocodeSearch(YouSuccApplication.getInstance().getApplicationContext());
        geocodeSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener() {

            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int errorCode) {
                if (null != regeocodeResult && errorCode == 0) {
                    RegeocodeAddress regecodeAddress = regeocodeResult.getRegeocodeAddress();
                    poiItem = new PoiItem("firstPoi", new LatLonPoint(amapLocation == null ? 0 : amapLocation.getLatitude(), amapLocation == null ? 0 : amapLocation.getLongitude()),
                            regecodeAddress.getCity(), regecodeAddress.getProvince() + regecodeAddress.getCity());

                    broadcastLocationInfoListener.getBroadcastLocationInfo(regecodeAddress.getPois().get(0));
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int errorCode) {

            }
        });
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(new LatLonPoint(amapLocation == null ? 0 : amapLocation.getLatitude(), amapLocation == null ? 0 : amapLocation.getLongitude()),
                1000, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
    }

}

