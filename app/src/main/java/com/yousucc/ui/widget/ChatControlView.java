package com.yousucc.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.emojicon.EmojiconEditText;
import com.yousucc.R;
import com.yousucc.ui.adapter.chatbroad.ChatFaceAdapter;
import com.yousucc.ui.adapter.chatbroad.ChatMoreBroadAdapter;
import com.yousucc.utils.IMEUtil;
import com.yousucc.utils.Log;
import com.yousucc.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SensYang on 2016/4/6 0006.
 */
public class ChatControlView extends LinearLayout implements TextWatcher, View.OnTouchListener,
        ChatMoreBroadAdapter.OnBroadItemClickListener, ChatFaceAdapter.OnFaceClickListener,
        View.OnFocusChangeListener, ResizeListenerLayout.OnResizeListener {
    @Bind(R.id.voiceIV)
    ImageView voiceIV;
    @Bind(R.id.emojiconEditText)
    EditText emojiconEditText;
    @Bind(R.id.faceIV)
    ImageView faceIV;
    @Bind(R.id.moreIV)
    ImageView moreIV;
    @Bind(R.id.sendBtn)
    YouSuccButton sendBtn;
    @Bind(R.id.voiceBtn)
    YouSuccButton voiceBtn;
    @Bind(R.id.chatMoreBroadView)
    ChatMoreBroadView chatMoreBroadView;

    /**
     * 取消语音发送
     */
    private boolean isVoiceCancle;
    /**
     * 阅后即焚
     */
    private boolean isFireType = false;

    public ChatControlView(Context context) {
        this(context, null);
    }

    public ChatControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatControlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(getContext(), R.layout.widget_chat_control, this);
        ButterKnife.bind(this);
        initListener();
    }

    /**
     * 设置监听器
     */
    private void initListener() {
        emojiconEditText.setOnFocusChangeListener(this);
        emojiconEditText.addTextChangedListener(this);
        voiceBtn.setOnTouchListener(this);
    }

    /**
     * 关闭阅后即焚状态
     */
    private void closeFireType() {
        isFireType = false;
        setBackgroundColor(Color.WHITE);
        moreIV.setRotation(0);
    }

    /**
     * 开启阅后即焚状态
     */
    private void startFireType() {
        isFireType = true;
        setBackgroundColor(getResources().getColor(R.color.main_red));
        moreIV.setRotation(45f / 360);
    }

    @OnClick({R.id.voiceIV, R.id.faceIV, R.id.moreIV, R.id.sendBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.voiceIV:
                faceIV.setImageResource(R.drawable.chat_icon_face);
                chatMoreBroadView.setVisibility(View.GONE);
                if (voiceBtn.getVisibility() == View.GONE) {
                    IMEUtil.getInstance().hideSoftKeyboard(emojiconEditText);
                    voiceBtn.setVisibility(View.VISIBLE);
                    emojiconEditText.setVisibility(View.GONE);
                    voiceIV.setImageResource(R.drawable.chat_icon_keyboard);
                    sendBtn.setVisibility(View.GONE);
                    moreIV.setVisibility(View.VISIBLE);
                } else {
                    voiceButtonUp();
                    emojiconEditText.requestFocus();
                    IMEUtil.getInstance().showSoftKeyboard(emojiconEditText);
                }
                break;
            case R.id.faceIV:
                emojiconEditText.requestFocus();
                if (chatMoreBroadView.isKeyBoardShowing()) {
                    faceIV.setImageResource(R.drawable.chat_icon_faced);
                    chatMoreBroadView.showFaceBroad();
                    IMEUtil.getInstance().hideSoftKeyboard(emojiconEditText);
                } else {
                    if (chatMoreBroadView.getVisibility() == View.VISIBLE && chatMoreBroadView.getCurrentPageIndex() == 0) {
                        faceIV.setImageResource(R.drawable.chat_icon_face);
                        IMEUtil.getInstance().showSoftKeyboard(emojiconEditText);
                    } else {
                        faceIV.setImageResource(R.drawable.chat_icon_faced);
                        chatMoreBroadView.showFaceBroad();
                    }
                }
                break;
            case R.id.moreIV:
                emojiconEditText.requestFocus();
                if (isFireType) {
                    closeFireType();
                    break;
                }
                faceIV.setImageResource(R.drawable.chat_icon_face);
                voiceButtonUp();
                if (chatMoreBroadView.isKeyBoardShowing()) {
                    chatMoreBroadView.showFunctionBroad();
                    IMEUtil.getInstance().hideSoftKeyboard(emojiconEditText);
                } else {
                    if (chatMoreBroadView.getVisibility() == View.VISIBLE && chatMoreBroadView.getCurrentPageIndex() == 1) {
                        IMEUtil.getInstance().showSoftKeyboard(emojiconEditText);
                    } else {
                        chatMoreBroadView.showFunctionBroad();
                    }
                }
                break;
            case R.id.sendBtn:
                //TODO 发送消息
                String text = emojiconEditText.getText().toString();
                emojiconEditText.setText("");
                if (sendMessageListener != null) {
                    sendMessageListener.sendTextMessage(text);
                }
                break;
        }
    }

    private void voiceButtonUp() {
        voiceBtn.setVisibility(View.GONE);
        emojiconEditText.setVisibility(View.VISIBLE);
        voiceIV.setImageResource(R.drawable.chat_icon_voice);
        String text = emojiconEditText.getText().toString().trim();
        if (text.length() > 0) {
            sendBtn.setVisibility(View.VISIBLE);
            moreIV.setVisibility(View.GONE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.e("beforeTextChanged--->", "" + s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.e("onTextChanged--->", "" + s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.e("afterTextChanged--->", "" + s);
        String text = emojiconEditText.getText().toString().trim();
        if (text.length() > 0) {
            sendBtn.setVisibility(View.VISIBLE);
            moreIV.setVisibility(View.GONE);
        } else {
            moreIV.setVisibility(View.VISIBLE);
            sendBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 发送语音按钮监听
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v != voiceBtn) return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isVoiceCancle = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float X = event.getX();
                float Y = event.getY();
                if (X < 0 || X > v.getWidth() || Y < -3 * v.getHeight()) {
                    //TODO 提示取消
                    isVoiceCancle = true;
                    voiceBtn.setText("松开手指 取消发送");
                } else {
                    voiceBtn.setText("松开 发送");
                    isVoiceCancle = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                voiceBtn.setText("按住 说话");
                if (isVoiceCancle) {
                    //TODO 删除语音取消发送
                } else {
                    //TODO 发送语音
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    /**
     * 输入框插入文字
     */
    private void insertTextToEdit(CharSequence insertText) {
        int index = emojiconEditText.getSelectionStart();//获取光标所在位置
        Editable edit = emojiconEditText.getEditableText();//获取emojiconEditText的文字
        if (index < 0 || index >= edit.length()) {
            edit.append(insertText);
        } else {
            edit.insert(index, insertText);//光标所在位置插入文字
        }
    }

    /**
     * 删除输入框文字
     */
    private void deleteOneTextForEdit() {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        emojiconEditText.dispatchKeyEvent(event);
        ToastUtil.showToast("删除");
//        int index = emojiconEditText.getSelectionStart();//获取光标所在位置
//        Editable edit = emojiconEditText.getEditableText();//获取emojiconEditText的文字
//        if (index > 0) {
//            edit.delete(index - 1, index);//光标所在位置删除一个文字
//        }
    }

    /**
     * 聊天类型
     */
    private ChatType chatType;

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
        chatMoreBroadView.initType(this);
    }

    /**
     * 获取聊天类型
     */
    public ChatType getChatType() {
        return chatType;
    }

    /**
     * 点击了表情
     */
    @Override
    public void onClickFace(int index, CharSequence text) {
        if (text.toString().equalsIgnoreCase("D")) {
            deleteOneTextForEdit();
            return;
        }
        //将表情插入输入框
        insertTextToEdit(text);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.e("onFocusChange--->", "v-->" + v + " hasFocus--->" + hasFocus);
        if (!hasFocus) {
            v.clearFocus();
            IMEUtil.getInstance().hideSoftKeyboard(v);
            post(resetRunnable);
        }
    }

    Runnable resetRunnable = new Runnable() {
        @Override
        public void run() {
            chatMoreBroadView.setVisibility(View.GONE);
            faceIV.setImageResource(R.drawable.chat_icon_face);
        }
    };

    /**
     * 父控件高度即将改变
     */
    @Override
    public void onHeightToBigger(boolean isBigger, int sizeDiff) {
        if (!isBigger) {
            voiceButtonUp();
        }
        chatMoreBroadView.onHeightToBigger(isBigger, sizeDiff);
    }

    /**
     * 宽高已经改变过的回掉
     */
    @Override
    public void onAfterSizeChanged(int width, int height, int oldWidth, int oldHeight) {

    }

    /**
     * 父控件宽度即将改变
     */
    @Override
    public void onWidthToBigger(boolean isBigger, int sizeDiff) {

    }

    /**
     * 聊天类型
     */
    public enum ChatType {
        PEOPLE,
        GROUP,
        BUSINESS;
    }

    /**
     * 点击更多操作按钮
     */
    @Override
    public void onClickBroadItem(int position, String name) {
        if (position == 12 || name.equalsIgnoreCase("阅后即焚")) {
            startFireType();
            return;
        }
        if (sendMessageListener != null) {
            sendMessageListener.clickControlItem(position, name);
        }
    }

    /**
     * 发送消息的监听器
     */
    private OnSendMessageListener sendMessageListener;

    public void setSendMessageListener(OnSendMessageListener sendMessageListener) {
        this.sendMessageListener = sendMessageListener;
    }

    public interface OnSendMessageListener {
        /**
         * 发送文本消息
         */
        void sendTextMessage(String text);

        /**
         * 点击了主面板
         */
        void clickControlItem(int position, String type);
    }
}
