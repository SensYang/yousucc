package com.yousucc.ui.activity.main;

import android.view.LayoutInflater;
import android.view.View;

import com.yousucc.R;
import com.yousucc.ui.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by SensYang on 2016/3/27 0027.
 */
public class RedpaperFragment extends BaseFragment {
    private View parentView;

    @Override
    protected View initView(LayoutInflater inflater) {
        if (parentView == null) {
            parentView = inflater.inflate(R.layout.fragment_main_redpaper, null, false);
        }
        return parentView;
    }

    @Override
    public void onFirstUserVisible() {
        ButterKnife.bind(this, parentView);
    }
}
