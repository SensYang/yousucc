package com.yousucc.ui.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.amap.api.services.core.PoiItem;
import com.duanqu.qupai.editor.EditorResult;
import com.yousucc.R;
import com.yousucc.chat.IMChattingHelper;
import com.yousucc.database.GlobalSharedPreferences;
import com.yousucc.database.LiteOrmDBUtil;
import com.yousucc.database.PreferencesSetting;
import com.yousucc.database.tables.ChatMessage;
import com.yousucc.protocol.ApiByYuntx;
import com.yousucc.ui.activity.location.LocationSelectActivity;
import com.yousucc.ui.activity.main.ActivityMain;
import com.yousucc.ui.adapter.ChatAdapter;
import com.yousucc.ui.base.slidback.SlidingActivity;
import com.yousucc.ui.widget.ChatControlView;
import com.yousucc.ui.widget.ResizeListenerLayout;
import com.yousucc.ui.widget.TopNavigationBar;
import com.yousucc.utils.Log;
import com.yousucc.utils.QuPaiUtil;
import com.yousucc.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by DELL on 2016/4/3.
 */
public class ChatActivity extends SlidingActivity implements ApiByYuntx.OnSendMessageListener,
        ChatControlView.OnSendMessageListener, IMChattingHelper.OnMessageReportCallback,
        ResizeListenerLayout.OnResizeListener {
    @Bind(R.id.resizeListenerLayout)
    ResizeListenerLayout resizeListenerLayout;
    @Bind(R.id.topNavigationBar)
    TopNavigationBar topNavigationBar;
    @Bind(R.id.chatControlView)
    ChatControlView chatControlView;
    @Bind(R.id.listView)
    ListView listView;
    private String chatwith;
    private ChatAdapter chatAdapter;
    /**
     * 选择图片的返回码
     */
    private static final int REQUEST_IMAGE = 2;
    /**
     * 拍摄视频的返回码
     */
    private static final int REQUEST_VIDEO = 3;
    /**
     * 选择位置的返回码
     */
    private static final int REQUEST_LOACTION = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiByYuntx.getInstance().setOnSendMessageListener(this);
        if (getIntent() == null || (chatwith = getIntent().getStringExtra("chatwith")) == null) {
            finish();
            return;
        }
        ActivityMain.getInstances().addMessageReportCallback(this);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        topNavigationBar.setTitleText(chatwith);
        //设置聊天类型为个人
        chatControlView.setChatType(ChatControlView.ChatType.PEOPLE);
        resizeListenerLayout.addResizeListener(chatControlView);
        resizeListenerLayout.addResizeListener(this);
        initListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalSharedPreferences.getInstance().setStringPreferences(PreferencesSetting.SETTING_CHATWITH.getSettingName(), chatwith);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalSharedPreferences.getInstance().setStringPreferences(PreferencesSetting.SETTING_CHATWITH.getSettingName(), (String) PreferencesSetting.SETTING_CHATWITH.getDefaultValue());
    }

    private void initListener() {
        chatControlView.setSendMessageListener(this);
        chatAdapter = new ChatAdapter("single");
        listView.setAdapter(chatAdapter);
    }

    private void initData() {
        List<ChatMessage> chatMessageList = LiteOrmDBUtil.getInstance().getQueryByWhere(ChatMessage.class, ChatMessage.SESSIONID, new String[]{chatwith});
        if (chatMessageList.size() > 0) {
            chatAdapter.addChatList(chatMessageList);
            chatAdapter.notifyDataSetChanged();
        }
        listView.setSelection(chatAdapter.getCount() - 1);
    }

    /**
     * 发送文本消息
     */
    @Override
    public void sendTextMessage(String text) {
        Log.e("sendTextMessage--->", text);
        ApiByYuntx.getInstance().sendTextMessage(chatwith, text);
    }

    /**
     * 点击了控制面板
     */
    @Override
    public void clickControlItem(int position, String type) {
        switch (position) {
            case 0://图片选择
                startSelectImages();
                break;
            case 1://录制视频
                QuPaiUtil.getInstances(this).startRecoedActivity(this, REQUEST_VIDEO);
                break;
            case 9://位置
                startActivityForResult(new Intent(this, LocationSelectActivity.class), REQUEST_LOACTION);
                break;
        }
        ToastUtil.showToast(position + "<---->" + type);
    }

    /**
     * 选择图片
     */
    private void startSelectImages() {
        Intent intent = new Intent(ChatActivity.this, MultiImageSelectorActivity.class);
        // 显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量 9
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // 选择模式 多选模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择
//                if (selectedImagePathList != null && selectedImagePathList.size() > 0) {
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, selectedImagePathList);
//                }
//        startActivityForResult(intent, REQUEST_IMAGE);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * 附件下载成功提醒
     */
    @Override
    public void onMessageDowloaded(ChatMessage message) {
        if (message.getSessionId().equalsIgnoreCase(chatwith)) {

        }
    }

    /**
     * 收到新消息
     */
    @Override
    public void onPushMessage(List<ChatMessage> chatMessageList) {
        if (chatMessageList.get(0).getSessionId().equalsIgnoreCase(chatwith)) {
            if (chatMessageList != null && chatMessageList.size() > 0) {
                chatAdapter.addChatList(chatMessageList);
                chatAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSendMessageComplete(ChatMessage chatMessage) {
        chatAdapter.addChat(chatMessage);
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProgress(String messageId, float progress) {
        Log.e("onProgress--->", messageId + "<-->" + progress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    for (String imagePath : mSelectPath) {
                        ApiByYuntx.getInstance().sendImageMessage(chatwith, imagePath);
                    }
                    break;
                case REQUEST_VIDEO:
                    EditorResult result = new EditorResult(data);
                    //得到视频path，和缩略图path的数组，返回十张缩略图,和视频时长
                    String path = result.getPath();
                    long duration = result.getDuration();
                    ApiByYuntx.getInstance().sendVideoMessage(chatwith, path, path + ".png", duration);
                    break;
                case REQUEST_LOACTION:
                    PoiItem poiInfo = data.getParcelableExtra("poiInfo");
                    if (poiInfo != null) {
                        ApiByYuntx.getInstance().sendLocationMessage(chatwith, poiInfo.getLatLonPoint(), poiInfo.getTitle(), poiInfo.getPoiId());
//                        StringBuffer buffer = new StringBuffer();
//                        buffer
//                                .append("<--getLatLonPoint-->")
//                                .append(poiInfo.getLatLonPoint().toString()).append("<--getAdCode-->")
//                                .append(poiInfo.getAdCode()).append("<--getAdName-->")
//                                .append(poiInfo.getAdName()).append("<--getBusinessArea-->")
//                                .append(poiInfo.getBusinessArea()).append("<--getCityCode-->")
//                                .append(poiInfo.getCityCode()).append("<--getCityName-->")
//                                .append(poiInfo.getCityName()).append("<--getDirection-->")
//                                .append(poiInfo.getDirection()).append("<--getDistance-->")
//                                .append(poiInfo.getDistance()).append("<--getEmail-->")
//                                .append(poiInfo.getEmail()).append("<--getEnter-->")
//                                .append(poiInfo.getEnter()).append("<--getExit-->")
//                                .append(poiInfo.getExit()).append("<--getIndoorData-->")
//                                .append(poiInfo.getIndoorData()).append("<--getParkingType-->")
//                                .append(poiInfo.getParkingType()).append("<--getPoiId-->")
//                                .append(poiInfo.getPoiId()).append("<--getPostcode-->")
//                                .append(poiInfo.getPostcode()).append("<--getProvinceCode-->")
//                                .append(poiInfo.getProvinceCode()).append("<--getProvinceName-->")
//                                .append(poiInfo.getProvinceName()).append("<--getSnippet-->")
//                                .append(poiInfo.getSnippet()).append("<--getSubPois-->")
//                                .append(poiInfo.getSubPois()).append("<--getTel-->")
//                                .append(poiInfo.getTel()).append("<--getTitle-->")
//                                .append(poiInfo.getTitle()).append("<--getTypeDes-->")
//                                .append(poiInfo.getTypeDes()).append("<--getWebsite-->")
//                                .append(poiInfo.getWebsite())
//                        ;
//                        Log.e("onActivityResult--->", buffer.toString());
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityMain.getInstances().removeMessageReportCallback(this);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 宽高已经改变过的回掉
     */
    @Override
    public void onAfterSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        int bottomIndex = listView.getAdapter().getCount() - 1;
        Log.e("listView--->", "bottomIndex--->" + bottomIndex);
        listView.setSelection(bottomIndex);
    }

    /**
     * 父控件高度即将改变
     */
    @Override
    public void onHeightToBigger(boolean isBigger, int sizeDiff) {
    }

    /**
     * 父控件宽度即将改变
     */
    @Override
    public void onWidthToBigger(boolean isBigger, int sizeDiff) {

    }
}
