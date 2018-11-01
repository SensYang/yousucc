package com.yousucc.chat;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yousucc.R;
import com.yousucc.YouSuccApplication;
import com.yousucc.config.ConfigNet;
import com.yousucc.protocol.ApiByHttp;
import com.yousucc.utils.ToastUtil;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECNotifyOptions;
import com.yuntongxun.ecsdk.SdkErrorCode;

/**
 * Created by Jorstin on 2015/3/17.
 */
public class SDKCoreHelper implements ECDevice.InitListener, ECDevice.OnECDeviceConnectListener, ECDevice.OnLogoutListener {
    /**登录成功*/
    public static final int CONNECT_SUCCESS = 101;
    /**
     * 消息处理
     */
    private Handler handler;
    /**
     * 单利模式
     */
    private static SDKCoreHelper sInstance;
    /**
     * 状态栏提醒配置
     */
    private ECNotifyOptions mOptions;
    /**
     * 初始化参数
     */
    private ECInitParams mInitParams;
    /**
     * 登陆模式
     */
    private ECInitParams.LoginMode mMode;
    /**
     * 连接状态
     */
    private ECDevice.ECConnectState mConnect;

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 保持单利模式
     */
    private SDKCoreHelper() {
        //状态栏提醒配置
        initNotifyOptions();
    }

    /**
     * 获取实例
     */
    public static SDKCoreHelper getInstance() {
        if (sInstance == null) {
            sInstance = new SDKCoreHelper();
        }
        return sInstance;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * 初始化
     */
    public void init() {
        init(ECInitParams.LoginMode.FORCE_LOGIN);
    }

    /**
     * 初始化
     *
     * @param mode 登陆模式 默认强制登录
     */
    public void init(ECInitParams.LoginMode mode) {
        sInstance.mContext = YouSuccApplication.getInstance().getApplicationContext();
        sInstance.mMode = mode;
        // 判断SDK是否已经初始化，没有初始化则先初始化SDK
        if (!ECDevice.isInitialized()) {
            sInstance.mConnect = ECDevice.ECConnectState.CONNECTING;
            ECDevice.initial(sInstance.mContext, sInstance);
            Log.e("SDKCoreHelper--->", "tototoInitialize");
            return;
        }
        Log.e("SDKCoreHelper--->", "Initialize");
        // 若已经初始化成功，直接进行注册
        getInstance().onInitialized();
    }

    /**
     * 状态栏提醒配置
     */
    private void initNotifyOptions() {
        if (mOptions == null) {
            mOptions = new ECNotifyOptions();
        }
        // 设置新消息是否提醒
        mOptions.setNewMsgNotify(true);
        // 设置状态栏通知图标
        mOptions.setSmallIcon(R.drawable.ic_launcher);
        // 设置是否启用勿扰模式（不会声音/震动提醒）
        mOptions.setSilenceEnable(false);
        // 设置勿扰模式时间段（开始小时/开始分钟-结束小时/结束分钟）
        // 小时采用24小时制
        // 如果设置勿扰模式不启用，则设置勿扰时间段无效
        // 当前设置晚上11点到第二天早上8点之间不提醒
        mOptions.setSilenceTime(23, 0, 8, 0);
        // 设置是否震动提醒(如果处于免打扰模式则设置无效，没有震动)
        mOptions.enableShake(true);
        // 设置是否声音提醒(如果处于免打扰模式则设置无效，没有声音)
        mOptions.enableSound(true);
    }

    /**
     * YuntxSDK初始化成功
     * 开始注册
     */
    @Override
    public void onInitialized() {
        Log.e("SDKCoreHelper--->","onInitialized");
        // 设置消息提醒配置
        ECDevice.setNotifyOptions(mOptions);
//        // 设置接收VoIP来电事件通知Intent
//        // 呼入界面activity、开发者需修改该类
//        Intent intent = new Intent(getInstance().mContext, VoIPCallActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getInstance().mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        ECDevice.setPendingIntent(pendingIntent);
//
//
//        // 设置VOIP 自定义铃声路径
//        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
//        if (setupManager != null) {
//            // 目前支持下面三种路径查找方式
//            // 1、如果是assets目录则设置为前缀[assets://]
//            setupManager.setInComingRingUrl(true, "assets://phonering.mp3");
//            setupManager.setOutGoingRingUrl(true, "assets://phonering.mp3");
//            setupManager.setBusyRingTone(true, "assets://playend.mp3");
//            // 2、如果是raw目录则设置为前缀[raw://]
//            // 3、如果是SDCard目录则设置为前缀[file://]
//        }
//
//        if (ECDevice.getECMeetingManager() != null) {
//            ECDevice.getECMeetingManager().setOnMeetingListener(MeetingMsgReceiver.getInstance());
//        }

        if (mInitParams == null) {
            mInitParams = ECInitParams.createParams();
        }
        mInitParams.reset();
        // 如：VoIP账号/手机号码/..
        mInitParams.setUserid(ApiByHttp.getInstance().getSessionId());
        mInitParams.setAppKey(ConfigNet.YUNTX_APPKEY);
        mInitParams.setToken(ConfigNet.YUNTX_APPTOKEN);
        mInitParams.setMode(getInstance().mMode);
        Log.e("SDKCoreHelper--->", ConfigNet.YUNTX_APPKEY+"验证信息"+ ConfigNet.YUNTX_APPTOKEN+"<--->"+getInstance().mMode);
        // 如果有密码（VoIP密码，对应的登陆验证模式是）
        // ECInitParams.LoginAuthType.PASSWORD_AUTH
//        if (!TextUtils.isEmpty(ApiByHttp.getInstance().getPassword())) {
//            mInitParams.setPwd(ApiByHttp.getInstance().getPassword());
//        }

        // 设置登陆验证模式（是否验证密码/如VoIP方式登陆）
        mInitParams.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        // 设置SDK注册结果回调通知，当第一次初始化注册成功或者失败会通过该引用回调
        // 通知应用SDK注册状态
        // 当网络断开导致SDK断开连接或者重连成功也会通过该设置回调
        ECDevice.setOnChatReceiveListener(IMChattingHelper.getInstance());
        ECDevice.setOnDeviceConnectListener(this);
        if (mInitParams.validate()) {
            Log.e("SDKCoreHelper--->", "login");
            ECDevice.login(mInitParams);
        }
        Log.e("SDKCoreHelper--->", "endInitialized");
    }

    /**
     * SDK 初始化失败,可能有如下原因造成
     * 1、可能SDK已经处于初始化状态
     * 2、SDK所声明必要的权限未在清单文件（AndroidManifest.xml）里配置、或者未配置服务属性android:exported="false";
     * 3、当前手机设备系统版本低于ECSDK所支持的最低版本（当前ECSDK支持Android Build.VERSION.SDK_INT 以及以上版本）
     */
    @Override
    public void onError(Exception excepition) {
        Log.e("onConnectState--->", excepition.toString());
    }
    /** @deprecated */
    @Override
    public void onConnect() {
    }
    /** @deprecated */
    @Override
    public void onDisconnect(ECError ecError) {
    }

    @Override
    public void onConnectState(ECDevice.ECConnectState ecConnectState, ECError ecError) {
        Log.e("onConnectState--->", "链接状态--->"+ecConnectState+ecError.toString());
        if (ecConnectState == ECDevice.ECConnectState.CONNECT_FAILED) {
            if (ecError.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                //账号异地登陆
                ToastUtil.showToast("账号异地登陆");
            } else {
                //连接状态失败
                ToastUtil.showToast("登陆失败");
            }
            return;
        } else if (ecConnectState == ECDevice.ECConnectState.CONNECT_SUCCESS) {
            // 登陆成功
            if (handler != null) {
                handler.sendEmptyMessage(CONNECT_SUCCESS);
            }
        }
    }

    @Override
    public void onLogout() {

    }

    /**
     * 判断服务是否自动重启
     *
     * @return 是否自动重启
     */
    public static boolean isInitialized() {
        return ECDevice.isInitialized();
    }
}
