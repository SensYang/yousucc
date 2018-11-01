package com.yousucc.chat;

import android.text.TextUtils;

import com.yousucc.database.GlobalSharedPreferences;
import com.yousucc.database.LiteOrmDBUtil;
import com.yousucc.database.PreferencesSetting;
import com.yousucc.database.tables.ChatMessage;
import com.yousucc.utils.DemoUtils;
import com.yousucc.utils.FileAccessor;
import com.yousucc.utils.Log;
import com.yousucc.utils.ToastUtil;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECMessageDeleteNotify;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.ECVideoMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SensYang on 2016/4/5 0005.
 */
public class IMChattingHelper implements OnChatReceiveListener, ECChatManager.OnDownloadMessageListener {
    private static IMChattingHelper instance;
    /**
     * 消息发送报告
     */
    private OnMessageReportCallback mOnMessageReportCallback;
    String TAG = "IMChattingHelper--->";
    /**
     * 云通讯SDK聊天功能接口
     */
    private ECChatManager mChatManager;

    private IMChattingHelper() {
        mChatManager = ECDevice.getECChatManager();
    }

    public static IMChattingHelper getInstance() {
        if (instance == null) {
            instance = new IMChattingHelper();
        }
        return instance;
    }

    /**
     * 收到消息提醒
     */
    @Override
    public void onReceiveMessageNotify(ECMessageNotify ecMessageNotify) {
        if (ecMessageNotify.getNotifyType() == ECMessageNotify.NotifyType.DELETE) {  //通过if判断代表当前类型是删除阅后即焚的通知
            ECMessageDeleteNotify deleteMsg = (ECMessageDeleteNotify) ecMessageNotify;
            //TODO 处理接收方删除阅后即焚消息的通知、比如删除本地图片等
        }
        Log.e(TAG, "onReceiveMessageNotify--->ecMessageNotify" + ecMessageNotify);
    }

    /**
     * 收到群组通知消息（有人加入、退出...）
     * 可以根据ECGroupNoticeMessage.ECGroupMessageType类型区分不同消息类型
     */
    @Override
    public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage ecGroupNoticeMessage) {
        Log.e(TAG, "onOfflineMessageCount--->ecGroupNoticeMessage" + ecGroupNoticeMessage);
    }

    /**
     * 登陆成功之后SDK回调该接口通知账号离线消息数
     * 获取自己的云通讯服务端的离线条数
     */
    @Override
    public void onOfflineMessageCount(int count) {
        offlineCount = count;
        Log.e(TAG, "onOfflineMessageCount--->count" + count);
        /**
         注册SDK的参数需要设置如下才能收到该回调
         ECInitParams.setOnChatReceiveListener(new OnChatReceiveListener());
         count参数标识当前账号的离线消息数
         */
    }

    /**
     * 离线消息条数
     */
    private int offlineCount;

    /**
     * 获取离线消息的数量
     */
    @Override
    public int onGetOfflineMessage() {
        Log.e(TAG, "onGetOfflineMessage--->" + offlineCount);
        /*
         注册SDK的参数需要设置如下才能收到该回调
         ECInitParams.setOnChatReceiveListener(new OnChatReceiveListener());
         建议根据onHistoryMessageCount(int count)设置接收的离线消息数
         消息数 ECDevice.SYNC_OFFLINE_MSG_ALL:全部获取 0:不获取
         */
        return offlineCount;
    }

    /**
     * 获取到离线消息
     */
    @Override
    public void onReceiveOfflineMessage(List<ECMessage> list) {
        Log.e(TAG, "onReceiveOfflineMessage--->list" + list.toString());
    }

    /**
     * SDK通知应用离线消息拉取完成
     */
    @Override
    public void onReceiveOfflineMessageCompletion() {
        //TODO 应用可以在此做类似于Loading框的关闭，Notification通知等等
        Log.e(TAG, "onReceiveOfflineMessageCompletion--->");
    }

    /**
     * SDK通知应用当前账号的个人信息版本号
     */
    @Override
    public void onServicePersonVersion(int version) {
        Log.e(TAG, "onServicePersonVersion--->version" + version);
    }

    /**
     * 收到客服消息
     */
    @Override
    public void onReceiveDeskMessage(ECMessage ecMessage) {
        Log.e(TAG, "onReceiveDeskMessage--->ecMessage" + ecMessage.getType());
    }
    /** @deprecated */
    @Override//过时的方法
    public void onSoftVersion(String s, int i) {
        Log.e(TAG, "onSoftVersion--->s" + s + "i" + i);
    }

    /**
     * 收到新消息
     */
    @Override
    public void OnReceivedMessage(ECMessage ecMessage) {
        Log.e(TAG, "onReceiveMessageNotify--->ecMessage" + ecMessage.getType());
        postReceiveMessage(ecMessage, !ecMessage.isNotify());
    }
/*
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setConversationId(conversationId);
        chatMessage.setCreateTime(message.getMsgTime());
        chatMessage.setDirection(direction.name());
        //视频
        chatMessage.setDuration(((ECVideoMessageBody)message.getBody()).getDuration());
        chatMessage.setHasRead(hasRead);
        chatMessage.setLocalPath(((ECFileMessageBody) message.getBody()).getLocalUrl());
        chatMessage.setMessageId(message.getMsgId());
        chatMessage.setMessageType(message.getType().name());
        chatMessage.setRemoteUrl(((ECFileMessageBody) message.getBody()).getRemoteUrl());
        chatMessage.setSender(message.getForm());
        chatMessage.setReceiveTime(System.currentTimeMillis());
        chatMessage.setState(message.getMsgStatus().name());
        chatMessage.setText(((ECTextMessageBody) message.getBody()).getMessage());
        chatMessage.setUserData(message.getUserData());
* */

    private static HashMap<String, SyncMsgEntry> syncMessage = new HashMap<>();

    public class SyncMsgEntry {
        // 是否是第一次初始化同步消息
        boolean showNotice = false;
        boolean thumbnail = false;

        // 重试下载次数
        private int retryCount = 1;
        ECMessage msg;

        public SyncMsgEntry(boolean showNotice, boolean thumbnail, ECMessage message) {
            this.showNotice = showNotice;
            this.msg = message;
            this.thumbnail = thumbnail;
        }

        public void increase() {
            retryCount++;
        }

        public boolean isRetryLimit() {
            return retryCount >= 3;
        }
    }

    /**
     * 处理接收消息
     *
     * @param message
     * @param showNotice
     */
    private synchronized void postReceiveMessage(ECMessage message, boolean showNotice) {
        // 接收到的IM消息，根据IM消息类型做不同的处理
        // IM消息类型：ECMessage.Type
        if (message.isMultimediaBody()) {
            ECFileMessageBody body = (ECFileMessageBody) message.getBody();
            FileAccessor.initFileAccess();
            if (message.getType() != ECMessage.Type.CALL) {
                if (!TextUtils.isEmpty(body.getRemoteUrl())) {
                    boolean thumbnail = false;
                    boolean doDownload = false;
                    String fileExt = DemoUtils.getExtensionName(body.getRemoteUrl());
                    if (message.getType() == ECMessage.Type.VOICE) {
                        body.setLocalUrl(new File(FileAccessor.getVoicePathName(), DemoUtils.md5(String.valueOf(System.currentTimeMillis())) + ".amr").getAbsolutePath());
                        doDownload = true;
                    } else if (message.getType() == ECMessage.Type.IMAGE) {
//                        ECImageMessageBody imageBody = (ECImageMessageBody) body;
//                        thumbnail = !TextUtils.isEmpty(imageBody.getThumbnailFileUrl());
//                        imageBody.setLocalUrl(new File(FileAccessor.getImagePathName(), DemoUtils.md5(thumbnail ? imageBody.getThumbnailFileUrl() : imageBody.getRemoteUrl()) + "." + fileExt).getAbsolutePath());
                    } else if (message.getType() == ECMessage.Type.VIDEO) {
                        ECVideoMessageBody videoBody = (ECVideoMessageBody) body;
                        thumbnail = !TextUtils.isEmpty(videoBody.getThumbnailUrl());
                        StringBuilder builder = new StringBuilder(videoBody.getFileName());
                        builder.append("_thum.png");
                        body.setLocalUrl(new File(FileAccessor.getFilePathName(), builder.toString()).getAbsolutePath());
                        doDownload = true;
                    } else {
                        body.setLocalUrl(new File(FileAccessor.getFilePathName(), DemoUtils.md5(String.valueOf(System.currentTimeMillis())) + "." + fileExt).getAbsolutePath());
                        doDownload = true;
                    }

                    if (syncMessage != null) {
                        syncMessage.put(message.getMsgId(), new SyncMsgEntry(showNotice, thumbnail, message));
                    }
                    if (doDownload && mChatManager != null) {
                        if (!thumbnail) {
//                            mChatManager.downloadThumbnailMessage(message, this);
//                        } else {
                            mChatManager.downloadMediaMessage(message, this);
                        }
                    }
                    if (TextUtils.isEmpty(body.getFileName()) && !TextUtils.isEmpty(body.getRemoteUrl())) {
                        body.setFileName(FileAccessor.getFileName(body.getRemoteUrl()));
                    }
                    if (message.getType() == ECMessage.Type.IMAGE && message.getDirection() == ECMessage.Direction.RECEIVE) {
                        message.setUserData(message.getUserData());
                    } else {
                        message.setUserData("fileName=" + body.getFileName());
                    }
                }
            } else {
                Log.e(TAG, "ECMessage fileUrl: null");
            }
        }

        ChatMessage chatMessage = new ChatMessage(message);
        LiteOrmDBUtil.getInstance().insert(chatMessage);

        if (mOnMessageReportCallback != null) {
            ArrayList<ChatMessage> msgs = new ArrayList<>();
            msgs.add(chatMessage);
            mOnMessageReportCallback.onPushMessage(msgs);
        }

        // 是否状态栏提示
        if (showNotice) {
            showNotification(message);
        }
    }

    private static void showNotification(ECMessage message) {
        if (checkNeedNotification(message.getSessionId())) {
            ToastUtil.showToast("状态栏提醒");
//            ECNotificationManager.getInstance().forceCancelNotification();
//            String lastMsg = "";
//            if (message.getType() == ECMessage.Type.TXT) {
//                lastMsg = ((ECTextMessageBody) message.getBody()).getMessage();
//            }
//            ECContacts contact = ContactSqlManager.getContact(message.getForm());
//            if (contact == null) {
//                return;
//            }
//            ECNotificationManager.getInstance().showCustomNewMessageNotification(CCPAppManager.getContext(), lastMsg, contact.getNickname(), message.getSessionId(), message.getType().ordinal());
        }
    }

    /**
     * 是否需要状态栏通知
     *
     * @param contactId
     */
    public static boolean checkNeedNotification(String contactId) {
        String currentChattingContactId = GlobalSharedPreferences.getInstance().getStringPreferences(PreferencesSetting.SETTING_CHATWITH.getSettingName(), (String) PreferencesSetting.SETTING_CHATWITH.getDefaultValue());
        if (contactId == null) {
            return true;
        }
        // 当前聊天
        if (contactId.equals(currentChattingContactId)) {
            return false;
        }
//        // 群组免打扰
//        if (contactId.toUpperCase().startsWith("G")) {
//            return GroupSqlManager.isGroupNotify(contactId);
//        }
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 通知附件消息下载完成
     */
    @Override
    public void onDownloadMessageComplete(ECError error, ECMessage message) {
        Log.e(TAG, "onDownloadMessageComplete--->error" + error + "message" + message.getType());
        if (error.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
            if (message == null)
                return;
            // 处理发送文件IM消息的时候进度回调
            postDowloadMessageResult(message);

//            if (message.getType() == ECMessage.Type.VIDEO && mOnMessageReportCallback != null && message.getDirection() == ECMessage.Direction.RECEIVE && mOnMessageReportCallback instanceof ChatActivity) {
//
//                ((ChatActivity) mOnMessageReportCallback).dismissPostingDialog();
//            }

        } else {
            // 重试下载3次
            SyncMsgEntry remove = syncMessage.remove(message.getMsgId());
            if (remove == null) return;
            Log.e(TAG, "[onDownloadMessageComplete] download fail , retry ：" + remove.retryCount);
            retryDownload(remove);
        }
    }

    /**
     * 重试下载3次
     *
     * @param entry
     */
    private void retryDownload(SyncMsgEntry entry) {
        if (entry == null || entry.msg == null || entry.isRetryLimit()) {
            return;
        }
        entry.increase();
        // download ..
        if (mChatManager != null) {
            if (entry.thumbnail) {
                mChatManager.downloadThumbnailMessage(entry.msg, this);
            } else {
                mChatManager.downloadMediaMessage(entry.msg, this);
            }
        }
        syncMessage.put(entry.msg.getMsgId(), entry);
    }

    private synchronized void postDowloadMessageResult(ECMessage message) {
        if (message == null) {
            return;
        }
        ChatMessage chatMessage;
        List<ChatMessage> chatMessageList = LiteOrmDBUtil.getInstance().getQueryByWhere(ChatMessage.class, ChatMessage.MESSAGE_ID, new String[]{message.getMsgId()});
        if (chatMessageList == null || chatMessageList.size() < 1) {
            chatMessage = new ChatMessage(message);
        } else {
            chatMessage = chatMessageList.get(0);
        }
        if (chatMessage.getMessageType().equalsIgnoreCase(ECMessage.Type.VOICE.name())) {
            ECVoiceMessageBody voiceBody = (ECVoiceMessageBody) message.getBody();
            chatMessage.setDuration(DemoUtils.calculateVoiceTime(voiceBody.getLocalUrl()));
        }
        LiteOrmDBUtil.getInstance().update(chatMessage);
        if (mOnMessageReportCallback != null) {
            mOnMessageReportCallback.onMessageDowloaded(chatMessage);
        }
        boolean showNotice = true;
        SyncMsgEntry remove = syncMessage.remove(message.getMsgId());
        if (remove != null) {
            showNotice = remove.showNotice;
            if (mOnMessageReportCallback != null && remove.msg != null) {
                if (remove.msg.getMsgId().equalsIgnoreCase(chatMessage.getMessageId())) {
                    ArrayList<ChatMessage> msgs = new ArrayList<>();
                    msgs.add(chatMessage);
                    mOnMessageReportCallback.onPushMessage(msgs);
                }
            }
        }
        if (showNotice)
            showNotification(message);
    }

    /**
     * 附件消息下载进度
     */
    @Override
    public void onProgress(String msgId, int totalByte, int progressByte) {
        Log.e(TAG, "onProgress--->msgId" + msgId + "totalByte" + totalByte + "progressByte" + progressByte);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置消息监听器
     */
    public static void setOnMessageReportCallback(OnMessageReportCallback callback) {
        getInstance().mOnMessageReportCallback = callback;
    }

    /**
     * 信息发送与接收的回调接口
     */
    public interface OnMessageReportCallback {
        /**
         * 附件下载成功
         */
        void onMessageDowloaded(ChatMessage message);

        /**
         * 收到新消息
         */
        void onPushMessage(List<ChatMessage> chatMessageList);
    }
}
