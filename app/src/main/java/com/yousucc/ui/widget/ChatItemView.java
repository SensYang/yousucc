package com.yousucc.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yousucc.R;
import com.yousucc.config.GlideRoundTransform;
import com.yousucc.database.tables.ChatMessage;
import com.yousucc.protocol.ApiByHttp;
import com.yousucc.utils.CommonUtils;
import com.yousucc.utils.ToastUtil;
import com.yuntongxun.ecsdk.ECMessage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SensYang on 2016/4/7 0007.
 */
public class ChatItemView extends LinearLayout implements View.OnLongClickListener {
    @Bind(R.id.leftHeadRIV)
    ImageView leftHeadRIV;
    @Bind(R.id.leftNameTV)
    YouSuccTextView leftNameTV;
    @Bind(R.id.leftImageView)
    ImageView leftImageView;
    @Bind(R.id.leftVideoTagIV)
    ImageView leftVideoTagIV;
    @Bind(R.id.leftVideoTimeTV)
    YouSuccTextView leftVideoTimeTV;
    @Bind(R.id.leftVideoFrame)
    FrameLayout leftVideoFrame;
    @Bind(R.id.leftTextView)
    YouSuccTextView leftTextView;
    @Bind(R.id.leftTakeBtn)
    Button leftTakeBtn;
    @Bind(R.id.leftSendcouponBackIV)
    ImageView leftSendcouponBackIV;
    @Bind(R.id.leftSendcouponInfoLL)
    LinearLayout leftSendcouponInfoLL;
    @Bind(R.id.leftBusinessIV)
    ImageView leftBusinessIV;
    @Bind(R.id.leftBusinessTV)
    YouSuccTextView leftBusinessTV;
    @Bind(R.id.leftItemRL)
    RelativeLayout leftItemRL;
    @Bind(R.id.leftContentRL)
    RelativeLayout leftContentRL;

    @Bind(R.id.rightHeadRIV)
    ImageView rightHeadRIV;
    @Bind(R.id.rightNameTV)
    YouSuccTextView rightNameTV;
    @Bind(R.id.rightImageView)
    ImageView rightImageView;
    @Bind(R.id.rightVideoTagIV)
    ImageView rightVideoTagIV;
    @Bind(R.id.rightVideoTimeTV)
    YouSuccTextView rightVideoTimeTV;
    @Bind(R.id.rightVideoFrame)
    FrameLayout rightVideoFrame;
    @Bind(R.id.rightTextView)
    YouSuccTextView rightTextView;
    @Bind(R.id.rightTakeBtn)
    Button rightTakeBtn;
    @Bind(R.id.rightSendcouponBackIV)
    ImageView rightSendcouponBackIV;
    @Bind(R.id.rightSendcouponInfoLL)
    LinearLayout rightSendcouponInfoLL;
    @Bind(R.id.rightBusinessIV)
    ImageView rightBusinessIV;
    @Bind(R.id.rightBusinessTV)
    YouSuccTextView rightBusinessTV;
    @Bind(R.id.rightItemRL)
    RelativeLayout rightItemRL;
    @Bind(R.id.rightContentRL)
    RelativeLayout rightContentRL;
    private GlideRoundTransform roundTransform;
    private ViewHolder viewHolder = new ViewHolder();

    public ChatItemView(Context context) {
        this(context, null);
    }

    public ChatItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (roundTransform == null) {
            roundTransform = new GlideRoundTransform(context);
        }
        inflate(getContext(), R.layout.widget_chat_item, this);
        ButterKnife.bind(this);
        initListener();
    }

    private boolean isLongClick = false;

    private void initListener() {
        int[] ids = {R.id.leftImageView, R.id.leftTakeBtn, R.id.leftSendcouponBackIV, R.id.rightHeadRIV, R.id.rightImageView, R.id.rightTakeBtn, R.id.rightSendcouponBackIV};
        for (int id : ids) {
            findViewById(id).setOnLongClickListener(this);
        }
        setOnLongClickListener(this);
    }

    /**
     * 单条聊天的信息
     */
    private ChatMessage chatMessage;

    /**
     * 初始化数据
     */
    public void initData(ChatMessage chatMessage, String type) {
        this.chatMessage = chatMessage;
        if (chatMessage.getFrom().equalsIgnoreCase(ApiByHttp.getInstance().getSessionId())) {//TODO 自己
            viewHolder.initSelf(true);
            Glide.with(viewHolder.headRIV.getContext())
                    .load(R.drawable.default_head_boy)
                    .crossFade()
                    .centerCrop()
                    .transform(roundTransform)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.headRIV);
        } else {//TODO 不是自己
            viewHolder.initSelf(false);
            Glide.with(viewHolder.headRIV.getContext())
                    .load(R.drawable.default_head_girl)
                    .crossFade()
                    .centerCrop()
                    .transform(roundTransform)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.headRIV);
        }

//        ImageLoader.getInstance().displayImage(chatMessage.getImage(), viewHolder.headRIV, CoreSetting.headOptions565);
        if (type.equalsIgnoreCase("single")) {
            viewHolder.nameTV.setVisibility(View.GONE);
        } else {
            viewHolder.nameTV.setVisibility(View.VISIBLE);
            viewHolder.nameTV.setText(chatMessage.getNickName());
        }
        switch (ECMessage.Type.valueOf(chatMessage.getMessageType())) {
            case TXT://文本消息
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.videoFrame.setVisibility(View.GONE);
                viewHolder.textView.setVisibility(View.VISIBLE);
                viewHolder.takeBtn.setVisibility(View.GONE);
                viewHolder.sendcouponBackIV.setVisibility(View.GONE);
                viewHolder.sendcouponInfoLL.setVisibility(View.GONE);

                viewHolder.textView.setText(chatMessage.getText());
                break;
            case VOICE://语音消息
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.videoFrame.setVisibility(View.GONE);
                viewHolder.textView.setVisibility(View.GONE);
                viewHolder.takeBtn.setVisibility(View.GONE);
                viewHolder.sendcouponBackIV.setVisibility(View.GONE);
                viewHolder.sendcouponInfoLL.setVisibility(View.GONE);
                break;
            case VIDEO://视频消息
            {
                viewHolder.imageView.setVisibility(View.VISIBLE);
                viewHolder.videoFrame.setVisibility(View.VISIBLE);
                viewHolder.textView.setVisibility(View.GONE);
                viewHolder.takeBtn.setVisibility(View.GONE);
                viewHolder.sendcouponBackIV.setVisibility(View.GONE);
                viewHolder.sendcouponInfoLL.setVisibility(View.GONE);

                String url = chatMessage.getThumbnailUrl();
                if (url != null) {
                    viewHolder.imageView.setTag(R.id.image_url, url);
                    Glide.with(viewHolder.imageView.getContext())
                            .load(url)
                            .placeholder(R.drawable.default_img)
                            .crossFade()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(viewHolder.imageView);
                }
            }
            break;
            case IMAGE://图片消息
            {
                viewHolder.imageView.setVisibility(View.VISIBLE);
                viewHolder.videoFrame.setVisibility(View.GONE);
                viewHolder.textView.setVisibility(View.GONE);
                viewHolder.takeBtn.setVisibility(View.GONE);
                viewHolder.sendcouponBackIV.setVisibility(View.GONE);
                viewHolder.sendcouponInfoLL.setVisibility(View.GONE);

                String url = chatMessage.getThumbnailUrl();
                if (url == null) url = chatMessage.getLocalPath();
                if (url == null) url = chatMessage.getRemoteUrl();
                if (url != null) {
                    viewHolder.imageView.setTag(R.id.image_url, url);
                    Glide.with(viewHolder.imageView.getContext())
                            .load(url)
                            .placeholder(R.drawable.default_img)
                            .crossFade()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(viewHolder.imageView);
                }
            }
            break;
            case LOCATION:

                break;
            case FILE:

                break;
            case CALL:

                break;
            case NONE:

                break;
            default:
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.videoFrame.setVisibility(View.GONE);
                viewHolder.textView.setVisibility(View.GONE);
                viewHolder.takeBtn.setVisibility(View.GONE);
                viewHolder.sendcouponBackIV.setVisibility(View.GONE);
                viewHolder.sendcouponInfoLL.setVisibility(View.GONE);

                break;
        }
//        switch (chatMessage.getMessageType()) {
//            /**文本消息*/
//            case StaticType.MESSAGE_TYPE_TEXT:
//                viewHolder.imageView.setVisibility(View.GONE);
//                viewHolder.videoFrame.setVisibility(View.GONE);
//                viewHolder.textView.setVisibility(View.VISIBLE);
//                viewHolder.takeBtn.setVisibility(View.GONE);
//                viewHolder.sendcouponBackIV.setVisibility(View.GONE);
//                viewHolder.sendcouponInfoLL.setVisibility(View.GONE);
//
//                viewHolder.textView.setText(chatMessage.getMessageText());
//
//                break;
//            /**语音消息*/
//            case StaticType.MESSAGE_TYPE_VOICE:
//                viewHolder.imageView.setVisibility(View.GONE);
//                viewHolder.videoFrame.setVisibility(View.GONE);
//                viewHolder.textView.setVisibility(View.GONE);
//                viewHolder.takeBtn.setVisibility(View.GONE);
//                viewHolder.sendcouponBackIV.setVisibility(View.GONE);
//                viewHolder.sendcouponInfoLL.setVisibility(View.GONE);
//
//                //TODO
//                break;
//            /**图片消息*/
//            case StaticType.MESSAGE_TYPE_IMAGE:
//                viewHolder.imageView.setVisibility(View.VISIBLE);
//                viewHolder.videoFrame.setVisibility(View.GONE);
//                viewHolder.textView.setVisibility(View.GONE);
//                viewHolder.takeBtn.setVisibility(View.GONE);
//                viewHolder.sendcouponBackIV.setVisibility(View.GONE);
//                viewHolder.sendcouponInfoLL.setVisibility(View.GONE);
//
//                viewHolder.imageView.setTag(R.id.image_url, chatMessage.getMessageUrl());
//                ImageLoader.getInstance().displayImage(chatMessage.getMessageUrl(), viewHolder.imageView, CoreSetting.options565, imageLoadingListener);
//                break;
//            /**视频消息*/
//            case StaticType.MESSAGE_TYPE_VIDEO:
//                viewHolder.imageView.setVisibility(View.VISIBLE);
//                viewHolder.videoFrame.setVisibility(View.VISIBLE);
//                viewHolder.textView.setVisibility(View.GONE);
//                viewHolder.takeBtn.setVisibility(View.GONE);
//                viewHolder.sendcouponBackIV.setVisibility(View.GONE);
//                viewHolder.sendcouponInfoLL.setVisibility(View.GONE);
//
//                viewHolder.imageView.setTag(R.id.image_url, chatMessage.getMessageUrl());
//                ImageLoader.getInstance().displayImage(chatMessage.getMessageUrl(), viewHolder.imageView, CoreSetting.options565, imageLoadingListener);
//                break;
//            /**优惠券消息*/
//            case StaticType.MESSAGE_TYPE_COUPON:
//                viewHolder.imageView.setVisibility(View.GONE);
//                viewHolder.videoFrame.setVisibility(View.GONE);
//                viewHolder.textView.setVisibility(View.GONE);
//                viewHolder.takeBtn.setVisibility(View.GONE);
//                viewHolder.sendcouponBackIV.setVisibility(View.VISIBLE);
//                viewHolder.sendcouponInfoLL.setVisibility(View.VISIBLE);
//
//                viewHolder.businessTV.setText(chatMessage.getBusinessInfo());
//                viewHolder.businessIV.setTag(R.id.image_url, chatMessage.getBusinessUrl());
//                ImageLoader.getInstance().displayImage(chatMessage.getBusinessUrl(), viewHolder.businessIV, CoreSetting.options565, imageLoadingListener);
//                break;
//            /**领取优惠券消息*/
//            case StaticType.MESSAGE_TYPE_TAKE:
//                viewHolder.imageView.setVisibility(View.GONE);
//                viewHolder.videoFrame.setVisibility(View.GONE);
//                viewHolder.textView.setVisibility(View.GONE);
//                viewHolder.takeBtn.setVisibility(View.VISIBLE);
//                viewHolder.sendcouponBackIV.setVisibility(View.GONE);
//                viewHolder.sendcouponInfoLL.setVisibility(View.GONE);
//
//                break;
//            default:
//                viewHolder.imageView.setVisibility(View.GONE);
//                viewHolder.videoFrame.setVisibility(View.GONE);
//                viewHolder.textView.setVisibility(View.GONE);
//                viewHolder.takeBtn.setVisibility(View.GONE);
//                viewHolder.sendcouponBackIV.setVisibility(View.GONE);
//                viewHolder.sendcouponInfoLL.setVisibility(View.GONE);
//
//                break;
//        }
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.leftHeadRIV, R.id.leftImageView, R.id.leftTakeBtn, R.id.leftSendcouponBackIV, R.id.rightHeadRIV, R.id.rightImageView, R.id.rightTakeBtn, R.id.rightSendcouponBackIV})
    public void onClick(View view) {
        if (isLongClick) {
            isLongClick = false;
            return;
        }
        if (CommonUtils.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.leftHeadRIV:
            case R.id.rightHeadRIV://TODO 点击头像进入资料页
                ToastUtil.showToast("进入资料页");
                break;
            case R.id.leftImageView:
            case R.id.rightImageView://TODO 点击图片或视频 进入预览或播放
                if (chatMessage.getMessageType().equalsIgnoreCase(ECMessage.Type.IMAGE.name())) {
                    ToastUtil.showToast("预览图片");
                } else if (chatMessage.getMessageType().equalsIgnoreCase(ECMessage.Type.VIDEO.name())) {
                    ToastUtil.showToast("观看视频");
                }
                break;
            case R.id.leftTakeBtn:
            case R.id.rightTakeBtn://TODO 点击领取成功按钮
                ToastUtil.showToast("进入优惠券详情");
                break;
            case R.id.leftSendcouponBackIV:
            case R.id.rightSendcouponBackIV://TODO 点击优惠价
                ToastUtil.showToast("领取优惠券");
                break;
        }
    }

    /**
     * 长按事件
     */
    @Override
    public boolean onLongClick(View view) {
        isLongClick = true;
        //TODO 长按显示更多操作
        ToastUtil.showToast("显示更多操作");
        return false;
    }


    class ViewHolder {
        ImageView headRIV;
        YouSuccTextView nameTV;
        ImageView imageView;
        ImageView videoTagIV;
        YouSuccTextView videoTimeTV;
        FrameLayout videoFrame;
        YouSuccTextView textView;
        Button takeBtn;
        ImageView sendcouponBackIV;
        LinearLayout sendcouponInfoLL;
        ImageView businessIV;
        YouSuccTextView businessTV;
        RelativeLayout contentRL;

        public void initSelf(boolean isSelf) {
            if (isSelf) {
                headRIV = rightHeadRIV;
                nameTV = rightNameTV;
                imageView = rightImageView;
                videoTagIV = rightVideoTagIV;
                videoTimeTV = rightVideoTimeTV;
                videoFrame = rightVideoFrame;
                textView = rightTextView;
                takeBtn = rightTakeBtn;
                sendcouponBackIV = rightSendcouponBackIV;
                sendcouponInfoLL = rightSendcouponInfoLL;
                businessIV = rightBusinessIV;
                businessTV = rightBusinessTV;
                contentRL = rightContentRL;
                leftItemRL.setVisibility(View.GONE);
                rightItemRL.setVisibility(View.VISIBLE);
            } else {//TODO 不是自己
                headRIV = leftHeadRIV;
                nameTV = leftNameTV;
                imageView = leftImageView;
                videoTagIV = leftVideoTagIV;
                videoTimeTV = leftVideoTimeTV;
                videoFrame = leftVideoFrame;
                textView = leftTextView;
                takeBtn = leftTakeBtn;
                sendcouponBackIV = leftSendcouponBackIV;
                sendcouponInfoLL = leftSendcouponInfoLL;
                businessIV = leftBusinessIV;
                businessTV = leftBusinessTV;
                contentRL = leftContentRL;
                rightItemRL.setVisibility(View.GONE);
                leftItemRL.setVisibility(View.VISIBLE);
            }
        }
    }
}
