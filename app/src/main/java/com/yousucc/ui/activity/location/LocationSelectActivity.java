package com.yousucc.ui.activity.location;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.yousucc.R;
import com.yousucc.ui.base.slidback.SlidingActivity;
import com.yousucc.utils.LocationUtil;
import com.yousucc.utils.Log;
import com.yousucc.utils.StringUtil;
import com.yousucc.utils.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("InflateParams")
public class LocationSelectActivity extends SlidingActivity implements AMap.OnCameraChangeListener, AdapterView.OnItemClickListener, GeocodeSearch.OnGeocodeSearchListener, LocationSource, AMapLocationListener {
    @Bind(R.id.mapView)
    MapView mapView;
    @Bind(R.id.locationTitleTV)
    TextView locationTitleTV;
    @Bind(R.id.locationSubTitleTV)
    TextView locationSubTitleTV;
    @Bind(R.id.locationTextLL)
    LinearLayout locationTextLL;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.emptyTV)
    TextView emptyTV;
    @Bind(R.id.searchLL)
    LinearLayout searchLL;
    @Bind(R.id.progressBarLL)
    LinearLayout progressBarLL;

    private View tmp_view = null;
    private View map_view;
    private POIAdapter poiAdapter;

    // 控制
    private AMap aMap;
    private UiSettings uiSettings;
    //搜索
    private GeocodeSearch geocoderSearch;

    private boolean cameraChanging = false;
    private boolean needRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);

        initData();
        initListener();
    }

    protected void initData() {
        listView.setEmptyView(emptyTV);
        poiAdapter = new POIAdapter(this);
        listView.setAdapter(poiAdapter);

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);
        //设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14.0f));

        uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    private void initListener() {
        listView.setOnItemClickListener(this);
        aMap.setOnCameraChangeListener(this);
        LocationUtil.setLocationListener(this);
    }

    @OnClick({R.id.backLocationIB, R.id.rightBtn, R.id.leftBtn})
    public void onClick(View view) {
        Log.e("onClick--->", view.toString());
        switch (view.getId()) {
            case R.id.backLocationIB:
                if (LocationUtil.getLocation() != null)
                    if (LocationUtil.getLocation().getLatitude() != 0 && LocationUtil.getLocation().getLongitude() != 0) {
                        aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(LocationUtil.getLocation().getLatitude(), LocationUtil.getLocation().getLongitude())), 500, null);
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(14.0f));
                    }
                break;
            case R.id.rightBtn:
                PoiItem poiInfo = (PoiItem) listView.getItemAtPosition(listView.getCheckedItemPosition());
                if (null == poiInfo) {
                    ToastUtil.showToast("请选择一个位置");
                } else {
                    if (!StringUtil.isEmpty(poiInfo.getTitle())) {
                        Intent intent = new Intent();
                        intent.putExtra("poiInfo", poiInfo);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtil.showToast("当前位置无法准确定位");
                    }
                }
                break;
            case R.id.leftBtn:
                finish();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e("onSaveInstanceState--->", outState.toString());
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        Log.e("onResume--->", "onResume");
        mapView.onResume();
        super.onResume();
        LocationUtil.startLocation();
    }

    @Override
    protected void onPause() {
        Log.e("onPause--->", "onPause");
        mapView.onPause();
        super.onPause();
        LocationUtil.stopLocation();
    }

    @Override
    protected void onDestroy() {
        Log.e("onDestroy--->", "onDestroy");
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int errorCode) {
        Log.e("onGeocodeSearched--->", geocodeResult.toString() + "  errorCode--->" + errorCode);
        if (null != geocodeResult && errorCode == 0) {
            List<GeocodeAddress> regecodeAddress = geocodeResult.getGeocodeAddressList();
            StringBuffer buffer = new StringBuffer();
            for (GeocodeAddress geocodeAddress : regecodeAddress) {
                buffer.append(geocodeAddress.getFormatAddress() + "" + geocodeAddress.getLatLonPoint().toString() + "\n");
            }
            Log.e("onGeocodeSearched--->",buffer.toString());
        } else {
            locationTextLL.setVisibility(View.GONE);
            progressBarLL.setVisibility(View.VISIBLE);
            searchLL.setVisibility(View.GONE);
            ToastUtil.showToast("真木有数据啦");
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int errorCode) {
        Log.e("onRegeocodeSearched--->", regeocodeResult.toString() + "  errorCode--->" + errorCode);
        if (null != regeocodeResult && errorCode == 1000) {
            RegeocodeAddress regecodeAddress = regeocodeResult.getRegeocodeAddress();
            locationTitleTV.setText(regecodeAddress.getPois().get(0).getTitle());
            locationSubTitleTV.setText(StringUtil.isEmpty(regecodeAddress.getPois().get(0).getSnippet()) ? regecodeAddress.getPois().get(0).getTitle() : regecodeAddress.getPois().get(0).getSnippet());

            locationTextLL.setVisibility(View.VISIBLE);
            locationTitleTV.setVisibility(View.VISIBLE);
            locationSubTitleTV.setVisibility(View.VISIBLE);
            if (tmp_view != null) {
                tmp_view.setVisibility(View.GONE);
            }
            tmp_view = null;
            poiAdapter.flag = 0;
            poiAdapter.setItemList(regecodeAddress.getPois());

            progressBarLL.setVisibility(View.GONE);
            searchLL.setVisibility(View.VISIBLE);
            listView.setItemChecked(0, true);
            listView.smoothScrollToPosition(0);
            listView.setSelection(0);
        } else {
            locationTextLL.setVisibility(View.GONE);
            progressBarLL.setVisibility(View.VISIBLE);
            searchLL.setVisibility(View.GONE);
            ToastUtil.showToast("木有数据啦");
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        needRefresh = false;
        if (tmp_view == null) {
            tmp_view = poiAdapter.tmpView;
        }

        map_view = view.findViewById(R.id.mapIV);
        if (tmp_view != null) {
            tmp_view.setVisibility(View.GONE);
        }
        map_view.setVisibility(View.VISIBLE);
        tmp_view = map_view;

        PoiItem poiInfo = (PoiItem) parent.getItemAtPosition(position);
        Log.e("onItemClick--->", poiInfo.toString());
        if (null != poiInfo) {
            aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(poiInfo.getLatLonPoint().getLatitude(), poiInfo.getLatLonPoint().getLongitude())), 500, null);

            locationTextLL.setVisibility(View.VISIBLE);
            locationTitleTV.setVisibility(View.VISIBLE);
            locationSubTitleTV.setVisibility(View.VISIBLE);
            locationTitleTV.setText(poiInfo.getTitle());
            locationSubTitleTV.setText(StringUtil.isEmpty(poiInfo.getSnippet()) ? poiInfo.getTitle() : poiInfo.getSnippet());
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Log.e("onCameraChangeFinish--->", cameraPosition.toString());
        try {
            if (needRefresh) {
                searchLL.setVisibility(View.GONE);
                progressBarLL.setVisibility(View.VISIBLE);
                LatLng changedLatLng = cameraPosition.target;
                //latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
                RegeocodeQuery regeocodeQuery = new RegeocodeQuery(new LatLonPoint(changedLatLng.latitude, changedLatLng.longitude), 3000, GeocodeSearch.AMAP);
                Log.e("getFromLocationAsyn--->",changedLatLng.latitude+"<-->"+ changedLatLng.longitude+"");
                geocoderSearch.getFromLocationAsyn(regeocodeQuery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            needRefresh = true;
            cameraChanging = false;
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.e("onCameraChange--->", cameraPosition.toString());
        if (!cameraChanging && needRefresh) {
            locationTextLL.setVisibility(View.GONE);
        }
        cameraChanging = true;
    }

    OnLocationChangedListener onLocationChangedListener;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (onLocationChangedListener != null)
            onLocationChangedListener.onLocationChanged(LocationUtil.getLocation());
    }
}