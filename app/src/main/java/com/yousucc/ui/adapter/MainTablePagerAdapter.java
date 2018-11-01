package com.yousucc.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.yousucc.ui.activity.main.ContactsFragment;
import com.yousucc.ui.activity.main.ConversationFragment;
import com.yousucc.ui.activity.main.DiscoverFragment;
import com.yousucc.ui.activity.main.MyselfFragment;
import com.yousucc.ui.activity.main.RedpaperFragment;
import com.yousucc.ui.widget.MainTableView;
import com.yousucc.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2016/3/27 0027.
 */
public class MainTablePagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener, MainTableView.MainTableClickListener {
    private MainTableView mainTableView;
    private List<Fragment> fragmentList;
    private ViewPager viewPager;

    public MainTablePagerAdapter(FragmentActivity fa, ViewPager viewPager, MainTableView mainTableView) {
        super(fa.getSupportFragmentManager());
        this.mainTableView = mainTableView;
        this.mainTableView.setOnMainTableClickListener(this);
        fragmentList = new ArrayList<>();
        //TODO 红包城
        fragmentList.add(new RedpaperFragment());
        fragmentList.add(new ConversationFragment());
        fragmentList.add(new ContactsFragment());
        fragmentList.add(new DiscoverFragment());
        fragmentList.add(new MyselfFragment());
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);
        this.viewPager.setOnPageChangeListener(this);
        this.viewPager.setOffscreenPageLimit(4);
        this.viewPager.setCurrentItem(1, false);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mainTableView.doTranslate(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        mainTableView.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTableClick(int tableAt) {
        this.viewPager.setCurrentItem(tableAt, false);
        ToastUtil.showToast(tableAt + "");
    }
}
