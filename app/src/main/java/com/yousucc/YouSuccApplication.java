package com.yousucc;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.duanqu.qupai.upload.AuthService;
import com.duanqu.qupai.upload.QupaiAuthListener;
import com.yousucc.config.QupaiContant;
import com.yousucc.protocol.ApiByHttp;
import com.yousucc.utils.FileAccessor;
import com.yousucc.utils.Log;

/**
 * Created by Jorstin on 2015/3/17.
 */
public class YouSuccApplication extends Application {
    private static YouSuccApplication instance;
    private Typeface typeFace;

    public Typeface getTypeFace() {
        if (typeFace == null)
            typeFace = Typeface.createFromAsset(getAssets(), "fonts/PingFang Regular.ttf");
        return typeFace;
    }

    /**
     * 单例，返回一个实例
     *
     * @return
     */
    public static YouSuccApplication getInstance() {
        if (instance == null) {
            Log.w("[yousuccApplication] instance is null.");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化应用文件夹
        FileAccessor.initFileAccess();

//        //初始化图片下载器
//        initImageLoader();
        //使用volley做为Glide的引擎
//        Glide.get(this).register(GlideUrl.class, InputStream.class, new VolleyUrlLoader.Factory(VolleyRequest.getRequestQueue()));

//        //错误收集
//        CrashHandler.getInstance().init(this);
//        //腾讯错误日志统计
//        CrashReport.initCrashReport(getApplicationContext(), "900023057", false);
    }

    //趣拍初始化
    public void initQuPaiAuth() {
        //趣拍鉴权
        initAuth(getApplicationContext(), QupaiContant.appkey, QupaiContant.appsecret, ApiByHttp.getInstance().getSessionId());
    }

    /**
     * 鉴权 建议只调用一次,在Application调用。在demo里面为了测试调用了多次 得到accessToken
     *
     * @param context
     * @param appKey    appkey
     * @param appsecret appsecret
     * @param space     space
     */
    private void initAuth(Context context, String appKey, String appsecret, String space) {
        AuthService service = AuthService.getInstance();
        service.setQupaiAuthListener(new QupaiAuthListener() {
            @Override
            public void onAuthError(int errorCode, String message) {
                Log.e("qupaiAuth--->", "ErrorCode" + errorCode + "message" + message);
            }

            @Override
            public void onAuthComplte(int responseCode, String responseMessage) {
                Log.e("qupaiAuth--->", "趣拍鉴权成功--->"+responseMessage);
                QupaiContant.accessToken = responseMessage;
            }
        });
        service.startAuth(context, appKey, appsecret, space);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
