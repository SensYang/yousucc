package com.yousucc.chat.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.yousucc.chat.IMChattingHelper;
import com.yousucc.chat.SDKCoreHelper;
import com.yousucc.ui.activity.main.ActivityMain;
import com.yousucc.utils.Log;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.booter.ECNotifyReceiver;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

/**
 * SDK通知会分三种提醒方式
 * <p>1、直接通过设置的回调接口（OnChatReceiveListener）Push给应用
 * <p>2、如果应用没有设置回调接口则采用（BroadcastReceiver）广播通知（v5.1.8版本）此时如果应用处于未运行状态则会直接唤醒应用处理
 * <p>3、如果应用未处于运行状态并且不想被唤醒应用运行则采用状态栏通知处理（SDK直接提醒，不会通知应用）,比如调用
 * {@link ECDevice#logout(ECDevice.NotifyMode, ECDevice.OnLogoutListener)}退出接口传入后台接收消息才会有提醒
 *
 * @author 容联•云通讯
 * @version 5.1.8
 * @since 2015-10-23
 */
public class YuntxNotifyReceiver extends ECNotifyReceiver {
    String TAG = "YuntxNotifyReceiver--->";
    public static final String SERVICE_OPT_CODE = "service_opt_code";
    public static final String MESSAGE_SUB_TYPE = "message_type";

    /**
     * 来电
     */
    public static final int EVENT_TYPE_CALL = 1;
    /**
     * 消息推送
     */
    public static final int EVENT_TYPE_MESSAGE = 2;

    /**
     * 创建一个服务Intent
     *
     * @return Intent
     */
    public Intent buildServiceAction(int optCode) {
        Intent notifyIntent = new Intent(getContext(), NotifyService.class);
        notifyIntent.putExtra(SERVICE_OPT_CODE, optCode);
        return notifyIntent;
    }

    /**
     * 创建一个服务Intent
     *
     * @return Intent
     */
    public Intent buildMessageServiceAction(int subOptCode) {
        Intent intent = buildServiceAction(EVENT_TYPE_MESSAGE);
        intent.putExtra(MESSAGE_SUB_TYPE, subOptCode);
        return intent;
    }

    /**
     * 收到消息
     */
    @Override
    public void onReceivedMessage(Context context, ECMessage msg) {
        Log.e(TAG,"onReceivedMessage--->msg-->"+msg.getType());
        Intent intent = buildMessageServiceAction(OPTION_SUB_NORMAL);
        intent.putExtra(EXTRA_MESSAGE, msg);
        context.startService(intent);
    }

    @Override
    public void onCallArrived(Context context, Intent intent) {
        Log.e(TAG,"onCallArrived--->intent-->"+intent);
        Intent serviceAction = buildServiceAction(EVENT_TYPE_CALL);
        serviceAction.putExtras(intent);
        context.startService(serviceAction);
    }

    @Override
    public void onReceiveGroupNoticeMessage(Context context, ECGroupNoticeMessage notice) {
        Log.e(TAG,"onReceiveGroupNoticeMessage--->ECGroupNoticeMessage-->"+notice.getType());
        Intent intent = buildMessageServiceAction(OPTION_SUB_GROUP);
        intent.putExtra(EXTRA_MESSAGE, notice);
        context.startService(intent);
    }

    @Override
    public void onNotificationClicked(Context context, int notifyType, String sender) {
        Log.e(TAG,"onNotificationClicked--->notifyType-->"+notifyType+" sender"+sender);
        Intent intent = new Intent(context, ActivityMain.class);
        intent.putExtra("chatwith", sender);
        context.startActivity(intent);
    }

    @Override
    public void onReceiveMessageNotify(Context context, ECMessageNotify notify) {
        Log.e(TAG,"onReceiveMessageNotify--->notify-->"+notify.getNotifyType());
        Intent intent = buildMessageServiceAction(OPTION_SUB_MESSAGE_NOTIFY);
        intent.putExtra(EXTRA_MESSAGE, notify);
        context.startService(intent);
    }


    public static class NotifyService extends Service {
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        private void receiveImp(Intent intent) {
            Log.e("NotifyService--->","receiveImp--->intent-->"+intent);
            if (intent == null) {
                return;
            }
            int optCode = intent.getIntExtra(SERVICE_OPT_CODE, 0);
            if (optCode == 0) {
                return;
            }
            if (!SDKCoreHelper.isInitialized()) {
                SDKCoreHelper.getInstance().init();
            }
            switch (optCode) {
                case EVENT_TYPE_CALL:
                    Log.e("NotifyService--->","receiveImp--->EVENT_TYPE_CALL-->"+EVENT_TYPE_CALL);
//                    Intent call = new Intent(this, VoIPCallActivity.class);
//                    call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    call.putExtras(intent);
//                    startActivity(call);
                    break;
                case EVENT_TYPE_MESSAGE:
                    Log.e("NotifyService--->","receiveImp--->EVENT_TYPE_MESSAGE-->"+EVENT_TYPE_MESSAGE);
                    dispatchOnReceiveMessage(intent);
                    break;
            }
        }

        // Android Version 2.0以下版本
//        @Override
//        public void onStart(Intent intent, int startId) {
//            super.onStart(intent, startId);
//            Log.e("NotifyService--->", "onStart--->startId-->" + startId);
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ECLAIR) {
//                receiveImp(intent);
//                Log.e("NotifyService--->", "onStart--->");
//            }
//        }

        // Android 2.0以上版本回调/同时会执行onStart方法
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            receiveImp(intent);
            Log.e("NotifyService--->", "onStartCommand--->intent-->" + intent+" flags "+flags+" startId "+startId);
            return super.onStartCommand(intent, flags, startId);
        }

        private void dispatchOnReceiveMessage(Intent intent) {
            Log.e("NotifyService--->","dispatchOnReceiveMessage--->intent-->"+intent);
            if (intent == null) {
                return;
            }
            int subOptCode = intent.getIntExtra(MESSAGE_SUB_TYPE, -1);
            if (subOptCode == -1) {
                return;
            }
            switch (subOptCode) {
                case OPTION_SUB_NORMAL:
                    Log.e("NotifyService--->","dispatchOnReceiveMessage--->OPTION_SUB_NORMAL-->"+OPTION_SUB_NORMAL);
                    ECMessage message = intent.getParcelableExtra(EXTRA_MESSAGE);
                    IMChattingHelper.getInstance().OnReceivedMessage(message);
                    break;
                case OPTION_SUB_GROUP:
                    Log.e("NotifyService--->","dispatchOnReceiveMessage--->OPTION_SUB_GROUP-->"+OPTION_SUB_GROUP);
                    ECGroupNoticeMessage notice = intent.getParcelableExtra(EXTRA_MESSAGE);
                    IMChattingHelper.getInstance().OnReceiveGroupNoticeMessage(notice);
                    break;
                case OPTION_SUB_MESSAGE_NOTIFY:
                    Log.e("NotifyService--->","dispatchOnReceiveMessage--->OPTION_SUB_MESSAGE_NOTIFY-->"+OPTION_SUB_MESSAGE_NOTIFY);
                    ECMessageNotify notify = intent.getParcelableExtra(EXTRA_MESSAGE);
                    IMChattingHelper.getInstance().onReceiveMessageNotify(notify);
                    break;
            }
        }
    }
}
