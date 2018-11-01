package com.yousucc.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yousucc.R;
import com.yousucc.database.GlobalSharedPreferences;
import com.yousucc.database.PreferencesSetting;
import com.yousucc.ui.adapter.chatbroad.ChatFaceAdapter;
import com.yousucc.ui.adapter.chatbroad.ChatMoreBroadAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SensYang on 2016/4/16 0016.
 *
 * @Link setType(type)
 */
public class ChatMoreBroadView extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.moreVP)
    ViewPager moreVP;
    @Bind(R.id.faceMoreLL)
    LinearLayout faceMoreLL;
    @Bind(R.id.faceMore)
    LinearLayout faceMore;
    /**
     * viewPager当前显示的页面 表情or更多
     */
    private int currentPageIndex = 0;
    /**
     * 表情页索引记录
     */
    private int facePageIndex = 0;
    /**
     * 功能页索引记录
     */
    private int functionPageIndex = 0;
    /**
     * 当前控件的尺寸参数
     */
    private LayoutParams moreControlLayoutParams;
    /**
     * 表情适配器
     */
    private ChatFaceAdapter chatFaceAdapter;
    /**
     * 更多功能适配器
     */
    private ChatMoreBroadAdapter chatMoreBroadAdapter;
    /**
     * 准备显示
     */
    private boolean readyShowing = false;
    /**
     * 键盘是否显示了
     */
    private boolean isKeyBoardShowing = false;
    /**
     * 键盘的默认高度
     */
    private int keyBoardHeight = GlobalSharedPreferences.getInstance().getIntPreferences(PreferencesSetting.SETTING_KEYBOARDHEIGHT.getSettingName(), (Integer) PreferencesSetting.SETTING_KEYBOARDHEIGHT.getDefaultValue());

    public ChatMoreBroadView(Context context) {
        this(context, null);
    }

    public ChatMoreBroadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatMoreBroadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.widget_more_broad, this);
        ButterKnife.bind(this);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        setVisibility(View.GONE);
    }

    public void addMoreFace(int imgResId) {
        ViewHolder viewHolder = new ViewHolder(inflate(getContext(), R.layout.item_broad_face_more, null));
        viewHolder.faceIV.setImageResource(imgResId);
        viewHolder.faceIV.setTag(imgResId);
        moreFaceList.add(viewHolder);
        faceMoreLL.addView(viewHolder.parentView);
    }

    public void removeMoreFace(int imgResId) {

    }

    /**
     * 表情集合
     */
    private ArrayList<ViewHolder> moreFaceList = new ArrayList<>(1);

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (currentPageIndex == 1) {
            functionPageIndex = position;
        } else {
            facePageIndex = position;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 更多表情
     */
    class ViewHolder {
        View parentView;
        @Bind(R.id.faceIV)
        ImageView faceIV;
        @Bind(R.id.faceLine)
        View faceLine;

        public ViewHolder(View v) {
            this.parentView = v;
            ButterKnife.bind(this.parentView);
        }
    }


    /**
     * 初始化聊天类型
     * 该方法必须实现
     */
    public void initType(ChatControlView chatControlView) {
        chatMoreBroadAdapter = new ChatMoreBroadAdapter(getContext(), chatControlView.getChatType());
        chatMoreBroadAdapter.setOnBroadItemClickListener(chatControlView);
        chatFaceAdapter = new ChatFaceAdapter(getContext());
        chatFaceAdapter.setOnFaceClickListener(chatControlView);
        moreVP.setAdapter(chatMoreBroadAdapter);
        moreVP.setOnPageChangeListener(this);
    }

    /**
     * 后去当前面板的索引
     * return 0  表情
     * return 1  更多面板
     */
    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    /**
     * 显示表情
     */
    public void showFaceBroad() {
        currentPageIndex = 0;
        moreVP.setAdapter(chatFaceAdapter);
        moreVP.setCurrentItem(facePageIndex, false);
        if (isKeyBoardShowing)
            readyShowing = true;
        else {
            setVisibility(View.VISIBLE);
            faceMore.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示更多功能
     */
    public void showFunctionBroad() {
        currentPageIndex = 1;
        moreVP.setAdapter(chatMoreBroadAdapter);
        moreVP.setCurrentItem(functionPageIndex, false);
        if (isKeyBoardShowing)
            readyShowing = true;
        else {
            setVisibility(View.VISIBLE);
            faceMore.setVisibility(View.GONE);
        }
    }

    /**
     * 更新面板的高度
     */
    private void initMoreControlHeight() {
        GlobalSharedPreferences.getInstance().setIntPreferences(PreferencesSetting.SETTING_KEYBOARDHEIGHT.getSettingName(), keyBoardHeight);
        if (moreControlLayoutParams != null) {
            moreControlLayoutParams.height = keyBoardHeight;
            setLayoutParams(moreControlLayoutParams);
        }
    }

    public boolean isKeyBoardShowing() {
        return isKeyBoardShowing;
    }

    /**
     * 父控件高度即将改变
     */
    public void onHeightToBigger(boolean isBigger, int height) {
        if (keyBoardHeight != height) {
            keyBoardHeight = height;
            initMoreControlHeight();
        }
        if (!isBigger) {
            setVisibility(View.GONE);
            isKeyBoardShowing = true;
        } else {
            isKeyBoardShowing = false;
            if (readyShowing) {
                setVisibility(View.VISIBLE);
                readyShowing = false;
                if (currentPageIndex == 0) {
                    faceMore.setVisibility(View.VISIBLE);
                } else {
                    faceMore.setVisibility(View.GONE);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onGlobalLayout() {
        if (moreControlLayoutParams == null) {
            moreControlLayoutParams = (LayoutParams) getLayoutParams();
            moreControlLayoutParams.height = keyBoardHeight;
            setLayoutParams(moreControlLayoutParams);
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }
}
