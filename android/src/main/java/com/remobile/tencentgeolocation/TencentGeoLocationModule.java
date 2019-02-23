package com.remobile.tencentgeolocation;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import static android.os.Looper.getMainLooper;

public class TencentGeoLocationModule extends ReactContextBaseJavaModule implements TencentLocationListener {
    private final ReactApplicationContext reactContext;
    private TencentLocationManager mLocationManager;

    public TencentGeoLocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        mLocationManager = TencentLocationManager.getInstance(reactContext);
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
    }

    @Override
    public String getName() {
        return "TencentGeolocation";
    }

    @ReactMethod
    public void stopSerialLocation() {
        mLocationManager.removeUpdates(this);
    }

    @ReactMethod
    public void startSingleLocation() {
        // 创建定位请求
        TencentLocationRequest request = TencentLocationRequest.create()
                .setInterval(2*1000) // 设置定位周期
                .setAllowCache(false)
                .setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_NAME); // 设置定位level
        // 开始定位
        mLocationManager.requestLocationUpdates(request, this, getMainLooper());
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        // ignore
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error,
                                  String reason) {
        String msg = null;
        if (error == TencentLocation.ERROR_OK) {
            // 定位成功
            WritableMap map = Arguments.createMap();
            map.putDouble("latitude", location.getLatitude() );
            map.putDouble("longitude", location.getLongitude() );
            map.putString("name", location.getName());
            map.putString("address", location.getAddress());
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("TencentGeolocation", map);
        } else {
            // 定位失败
            WritableMap map = Arguments.createMap();
            map.putInt("errcode", error);
            map.putString("reason", reason);
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("TencentGeolocation", map);
        }
        this.stopSerialLocation();
    }
}
