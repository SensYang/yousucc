package com.yousucc.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yousucc.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SensYang on 2016/3/26 0026.
 */
public class MainTableView extends RelativeLayout {
    @Bind(R.id.tableOne)
    FrameLayout tableOne;
    @Bind(R.id.tableTwo)
    FrameLayout tableTwo;
    @Bind(R.id.tableThree)
    FrameLayout tableThree;
    @Bind(R.id.tableFour)
    FrameLayout tableFour;
    private int currentId = 0;
    @Bind({R.id.tableZeroDown, R.id.tableZeroUp})
    List<ImageView> tableZeroChilds;
    @Bind({R.id.tableOneDown, R.id.tableOneUp})
    List<ImageView> tableOneChilds;
    @Bind({R.id.tableTwoDown, R.id.tableTwoUp})
    List<ImageView> tableTwoChilds;
    @Bind({R.id.tableThreeDown, R.id.tableThreeUp})
    List<ImageView> tableThreeChilds;
    @Bind({R.id.tableFourDown, R.id.tableFourUp})
    List<ImageView> tableFourChilds;
    List<List<ImageView>> tables;

    public MainTableView(Context context) {
        this(context, null);
    }

    public MainTableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainTableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(getContext(), R.layout.widget_main_table, this);
        ButterKnife.bind(this);
        tables = new ArrayList<>(4);
        tableOne.setTag(1);
        tableTwo.setTag(2);
        tableThree.setTag(3);
        tableFour.setTag(4);
        tables.add(tableZeroChilds);
        tables.add(tableOneChilds);
        tables.add(tableTwoChilds);
        tables.add(tableThreeChilds);
        tables.add(tableFourChilds);
    }

    @OnClick({R.id.tableOne, R.id.tableTwo, R.id.tableThree, R.id.tableFour})
    public void onClick(View view) {
//        if (CommonUtils.isFastDoubleClick()) return;
        int clickAt = (Integer) view.getTag();
        if (currentId == clickAt) return;
        if (mainTableClickListener != null) {
            mainTableClickListener.onTableClick(clickAt);
        }
    }

    /**
     * 页面切换
     */
    public void onPageSelected(int position) {
        if (currentId == position) return;
        currentId = position;
        for (int i = 0; i < tables.size(); i++) {
            tables.get(i).get(0).setAlpha(1f);
            tables.get(i).get(1).setAlpha(0f);
        }
        tables.get(currentId).get(0).setAlpha(0f);
        tables.get(currentId).get(1).setAlpha(1f);
    }

    /**
     * 移动
     *
     * @param position 下一个位置
     * @param distance 移动距离
     */
    public void doTranslate(int position, float distance) {
//        Log.e("position--->" + position + "<---distance--->" + distance);
        if (position == currentId) {
            //滑动方向<---  0->1
            if (currentId != tables.size() - 1) {
                tables.get(currentId + 1).get(0).setAlpha(1 - distance);
                tables.get(currentId + 1).get(1).setAlpha(distance);
            }
            tables.get(currentId).get(0).setAlpha(distance);
            tables.get(currentId).get(1).setAlpha(1 - distance);
        } else {
            //滑动方向--->  1->0
            if (currentId != 0) {
                tables.get(currentId - 1).get(0).setAlpha(distance);
                tables.get(currentId - 1).get(1).setAlpha(1 - distance);
            }
            tables.get(currentId).get(0).setAlpha(1 - distance);
            tables.get(currentId).get(1).setAlpha(distance);
        }
    }

    private MainTableClickListener mainTableClickListener;

    /**
     * 设置点击监听器
     */
    public void setOnMainTableClickListener(MainTableClickListener mainTableClickListener) {
        this.mainTableClickListener = mainTableClickListener;
    }

    public interface MainTableClickListener {
        void onTableClick(int tableAt);
    }

}
