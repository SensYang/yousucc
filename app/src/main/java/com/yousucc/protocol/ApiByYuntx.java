package com.yousucc.protocol;

import com.amap.api.services.core.LatLonPoint;
import com.yousucc.database.LiteOrmDBUtil;
import com.yousucc.database.tables.ChatMessage;
import com.yousucc.utils.FileUtils;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECLocationMessageBody;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVideoMessageBody;

/**
 * Created by SensYang on 2016/4/7 0007.
 */
public class ApiByYuntx implements ECChatManager.OnSendMessageListener {
    private static ApiByYuntx instance;
    private ECChatManager chatManager;
    private OnSendMessageListener onSendMessageListener;

    public void setOnSendMessageListener(OnSendMessageListener onSendMessageListener) {
        this.onSendMessageListener = onSendMessageListener;
    }

    private ApiByYuntx() {
        chatManager = ECDevice.getECChatManager();
    }

    public static ApiByYuntx getInstance() {
        if (instance == null) {
            instance = new ApiByYuntx();
        }
        return instance;
    }

    /**
     * 发送文本消息
     */
    public void sendTextMessage(String toId, String textContent) {
        // 创建一个文本消息体，并添加到消息对象中
        ECTextMessageBody messageBody = new ECTextMessageBody(textContent);
        ECMessage msg = getMessage(ECMessage.Type.TXT, toId);
        msg.setBody(messageBody);
        sendMessage(msg);
    }

    /**
     * 发送图片消息
     */
    public void sendImageMessage(String toId, String localUrl) {
        // 或者创建一个图片消息体 并且设置附件包体（其实图片也是相当于附件）
        // 比如我们发送SD卡里面的一张Tony_2015.jpg图片
        ECImageMessageBody messageBody = new ECImageMessageBody();
        String fileName = localUrl.substring(localUrl.lastIndexOf('/'));
        String fileExt = localUrl.substring(localUrl.lastIndexOf('.'));
        // 设置附件名
        messageBody.setFileName(fileName);
        // 设置附件扩展名
        messageBody.setFileExt(fileExt);
        // 设置附件本地路径
        messageBody.setLocalUrl(localUrl);
        messageBody.setLength(FileUtils.decodeFileLength(localUrl));
        ECMessage msg = getMessage(ECMessage.Type.IMAGE, toId);
        msg.setBody(messageBody);
        sendMessage(msg);
    }

    /**
     * 发送视频消息
     */
    public void sendVideoMessage(String toId, String localUrl, String localThumb, long duration) {
        String fileName = localUrl.substring(localUrl.lastIndexOf('/'));
        String fileExt = localUrl.substring(localUrl.lastIndexOf('.'));
        // 创建一个文本消息体，并添加到消息对象中
        ECMessage msg = getMessage(ECMessage.Type.VIDEO, toId);
        ECVideoMessageBody messageBody = new ECVideoMessageBody();
        // 设置附件名
        messageBody.setFileName(fileName);
        // 设置附件扩展名
        messageBody.setFileExt(fileExt);
        // 设置附件本地路径
        messageBody.setLocalUrl(localUrl);
        //设置缩略图本地路径
        messageBody.setLocalThumb(localThumb);
        //设置时长
        messageBody.setDuration(duration);
        //设置文件大小
        messageBody.setLength(FileUtils.decodeFileLength(localUrl));
        msg.setBody(messageBody);
        sendMessage(msg);
    }
    /**
     * 发送位置消息
     */
    public void sendLocationMessage(String toId, LatLonPoint point, String title, String poiId) {
        // 创建一个文本消息体，并添加到消息对象中
        ECLocationMessageBody messageBody = new ECLocationMessageBody(point.getLatitude(),point.getLongitude());
        messageBody.setTitle(title);
        messageBody.setPoi(poiId);
        messageBody.setHasPoi(true);
        ECMessage msg = getMessage(ECMessage.Type.LOCATION, toId);
        msg.setBody(messageBody);
        sendMessage(msg);
    }

    /**
     * 获取消息框
     */
    private ECMessage getMessage(ECMessage.Type type, String toId) {
        ECMessage msg = ECMessage.createECMessage(type);
        //设置消息的属性：发出者，接受者，发送时间等
        msg.setFrom(ApiByHttp.getInstance().getSessionId());
        msg.setMsgTime(System.currentTimeMillis());
        // 设置消息接收者
        msg.setTo(toId);
        msg.setSessionId(toId);
        // 设置消息发送类型（发送或者接收）
        msg.setDirection(ECMessage.Direction.SEND);
        return msg;
    }

    /**
     * 发送消息
     */
    private void sendMessage(ECMessage msg) {
        new SendMessageThread(msg).start();
    }

    /**
     * 发送消息线程
     */
    class SendMessageThread extends Thread {
        ECMessage msg;

        SendMessageThread(ECMessage msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            chatManager.sendMessage(msg, ApiByYuntx.getInstance());
        }
    }

    @Override
    public void onSendMessageComplete(ECError error, ECMessage message) {
        if (message == null) return;
        ChatMessage chatMessage = new ChatMessage(message);
        LiteOrmDBUtil.getInstance().insert(chatMessage);
        if (onSendMessageListener != null) {
            onSendMessageListener.onSendMessageComplete(chatMessage);
        }
    }

    @Override
    public void onProgress(String msgId, int totalByte, int progressByte) {
        if (onSendMessageListener != null) {
            onSendMessageListener.onProgress(msgId, progressByte * 1f / totalByte);
        }
    }

    public interface OnSendMessageListener {
        /**
         * 消息发送成功
         */
        void onSendMessageComplete(ChatMessage chatMessage);

        /**
         * 文件发送进度
         */
        void onProgress(String messageId, float progress);
    }
}
