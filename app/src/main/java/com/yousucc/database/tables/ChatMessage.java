package com.yousucc.database.tables;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;
import com.yousucc.database.LiteOrmDBUtil;
import com.yousucc.database.tables.base.OrmTableBase;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECLocationMessageBody;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVideoMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;

import java.util.List;

/**
 * Created by SensYang on 2016/4/9 0009.
 */
@Table("chatmessage")
public class ChatMessage extends OrmTableBase {
    // 消息ID
    public static final String MESSAGE_ID = "messageId";
    //    // 消息类型
//    public static final String MESSAGE_TYPE = "messageType";
//    // 消息发送者昵称
//    public static final String NICKNAME = "nickName";
//    // 会话ID
//    public static final String CONVERSATION_ID = "conversationId";
    // 消息创建者
    public static final String FROM = "_from";
    //会话ID 如果是群组消息则为群组ID、 其他为消息创建者
    public static final String SESSIONID = "sessionId";
    //
//    // 是否已读
//    public static final String READ_STATUS = "hasRead";
//    // 文本
//    public static final String BODY = "text";
//    // 消息方向 send 发送 receive 接收 draft 草稿
//    public static final String DIRECTION = "direction";
//    // 发送状态 -1发送失败 0发送成功 1发送中 2接收成功（默认为0 接收的消息）
//    public static final String SEND_STATUS = "state";
//    // 服务器时间 毫秒
//    public static final String CREATE_DATE = "createTime";
//    // 入库本地时间 毫秒
//    public static final String RECEIVE_DATE = "receiveTime";
//    // 用户自定义数据
//    public static final String USER_DATA = "userData";
//    // 下载路径
//    public static final String REMOTE_URL = "remoteUrl";
//    // 文件本地路径
//    public static final String LOACL_PATH = "localPath";
//    // 语音时间
//    public static final String DURATION = "duration";
//    //消息版本
//    public static final String VERSION = "version";
//    //图片详情
//    public static final String IMAGEINFO = "imageInfo";

    @Column("_from")
    private String from;
    private String messageId;
    private String messageType;
    private int conversationId;
    private String sessionId;
    private String nickName;
    private boolean hasRead;
    private String text;
    private String direction;
    private String state;
    private long createTime;
    private long receiveTime;
    private String userData;
    private String remoteUrl;
    private String localPath;
    private long duration;
    private int version;

    private String thumbnailUrl;
    private int totalLen;
    private boolean isGif;

    private double latitude;
    private double longitude;
    private String locationTitle;
    private String locationPoi;

    public ChatMessage() {
    }

    public ChatMessage(ECMessage message) {
        boolean hasRead = false;
        ECMessage.Direction direction = message.getDirection();
        //类型是发送的或者是草稿 就 设为已读
        if (direction == ECMessage.Direction.SEND || direction == ECMessage.Direction.DRAFT) {
            hasRead = true;
        }
        int conversationId = -1;
        Conversation conversation;
        List<Conversation> conversationList = LiteOrmDBUtil.getInstance().getQueryByWhere(Conversation.class, "sessionId", new String[]{message.getSessionId()});
        if (conversationList.size() > 0) {
            conversationId = conversationList.get(0).getId();
        }
        if (conversationId == -1) {
            conversation = new Conversation();
        } else {
            conversation = conversationList.get(0);
        }
            /*
            private String distance;
            */
        switch (message.getType()) {
            case VIDEO:
                this.setDuration(((ECVideoMessageBody) message.getBody()).getDuration());
                conversation.setInfo("[视频]");
                this.setLocalPath(((ECVideoMessageBody) message.getBody()).getLocalUrl());
                this.setRemoteUrl(((ECVideoMessageBody) message.getBody()).getRemoteUrl());
                this.setThumbnailUrl(((ECVideoMessageBody) message.getBody()).getThumbnailUrl());
                break;
            case FILE:
                conversation.setInfo("[文件]");
                this.setLocalPath(((ECFileMessageBody) message.getBody()).getLocalUrl());
                this.setRemoteUrl(((ECFileMessageBody) message.getBody()).getRemoteUrl());
                break;
            case VOICE:
                conversation.setInfo("[语音]");
                this.setLocalPath(((ECVoiceMessageBody) message.getBody()).getLocalUrl());
                this.setRemoteUrl(((ECVoiceMessageBody) message.getBody()).getRemoteUrl());
                break;
            case IMAGE:
                conversation.setInfo("[图片]");
                String LocalUrl = ((ECImageMessageBody) message.getBody()).getLocalUrl();
                if (LocalUrl != null)
                    this.setLocalPath("file://" + LocalUrl);
                this.setRemoteUrl(((ECImageMessageBody) message.getBody()).getRemoteUrl());
                this.setThumbnailUrl(((ECImageMessageBody) message.getBody()).getThumbnailFileUrl());
                break;
            case TXT:
                String text = ((ECTextMessageBody) message.getBody()).getMessage();
                this.setText(text);
                conversation.setInfo(text.length() > 20 ? text.substring(0, 20) + "..." : text);
                break;
            case CALL:

                break;
            case LOCATION:
                this.setLatitude(((ECLocationMessageBody) message.getBody()).getLatitude());
                this.setLongitude(((ECLocationMessageBody) message.getBody()).getLongitude());
                this.setLocationPoi(((ECLocationMessageBody) message.getBody()).getPoi());
                break;
            case NONE: {//TODO 扩展消息类型

            }
            break;
        }
        conversation.setName(message.getNickName());
        conversation.setUnreadCount(conversation.getUnreadCount() + 1);
        conversation.setFrom(message.getForm());
        conversation.setSessionId(message.getSessionId());
        conversation.setTime(System.currentTimeMillis());
        String[] headers = null;
        if (conversation.getFrom().toUpperCase().startsWith("G")) {
            conversation.setConversationType("group");
            headers = new String[3];
            headers[0] = "http://v1.qzone.cc/avatar/201508/10/16/22/55c85f3ba930f213.jpg%21200x200.jpg";
            headers[0] = "http://img.tfdccn.com/2011/10/75_201110240802463B6OS.jpg";
            headers[0] = "http://img5.duitang.com/uploads/item/201508/05/20150805000408_5RiXH.thumb.224_0.jpeg";
        } else {
            conversation.setConversationType("person");
            headers = new String[1];
            headers[0] = "http://img5.duitang.com/uploads/item/201508/05/20150805000408_5RiXH.thumb.224_0.jpeg";
        }
        //TODO 还有更多扩展类型
        //TODO TEMP
        conversation.setHeadUrls(headers);
        if (conversationId == -1) {
            LiteOrmDBUtil.getInstance().insert(conversation);
            conversationList = LiteOrmDBUtil.getInstance().getQueryByWhere(Conversation.class, "sessionId", new String[]{message.getSessionId()});
            if (conversationList.size() > 0) {
                conversationId = conversationList.get(0).getId();
            }
        } else {
            LiteOrmDBUtil.getInstance().update(conversation);
        }
        this.setNickName(message.getNickName());
        this.setConversationId(conversationId);
        this.setCreateTime(message.getMsgTime());
        this.setDirection(direction.name());
        //视频
        this.setHasRead(hasRead);
        this.setMessageId(message.getMsgId());
        this.setMessageType(message.getType().name());
        this.setFrom(message.getForm());
        this.setSessionId(message.getSessionId());
        this.setReceiveTime(System.currentTimeMillis());
        this.setState(message.getMsgStatus().name());
        this.setUserData(message.getUserData());
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(int totalLen) {
        this.totalLen = totalLen;
    }

    public boolean isGif() {
        return isGif;
    }

    public void setIsGif(boolean isGif) {
        this.isGif = isGif;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public String getLocationPoi() {
        return locationPoi;
    }

    public void setLocationPoi(String locationPoi) {
        this.locationPoi = locationPoi;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "from='" + from + '\'' +
                ", messageId='" + messageId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", conversationId=" + conversationId +
                ", sessionId='" + sessionId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", hasRead=" + hasRead +
                ", text='" + text + '\'' +
                ", direction='" + direction + '\'' +
                ", state='" + state + '\'' +
                ", createTime=" + createTime +
                ", receiveTime=" + receiveTime +
                ", userData='" + userData + '\'' +
                ", remoteUrl='" + remoteUrl + '\'' +
                ", localPath='" + localPath + '\'' +
                ", duration=" + duration +
                ", version=" + version +
                '}';
    }
}
