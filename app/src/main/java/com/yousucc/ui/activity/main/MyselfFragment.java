package com.yousucc.ui.activity.main;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.yousucc.R;
import com.yousucc.ui.base.BaseFragment;
import com.yousucc.ui.widget.TopNavigationBar;
import com.yousucc.ui.widget.YouSuccTextView;
import com.yousucc.utils.CommonUtils;
import com.yousucc.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SensYang on 2016/3/27 0027.
 */
public class MyselfFragment extends BaseFragment {

    @Bind(R.id.topNavigationBar)
    TopNavigationBar topNavigationBar;
    @Bind(R.id.headBackIV)
    ImageView headBackIV;
    @Bind(R.id.headRIV)
    ImageView headRIV;
    @Bind(R.id.nameTV)
    YouSuccTextView nameTV;
    @Bind(R.id.infoTV)
    YouSuccTextView infoTV;
    private View parentView;

    @Override
    protected View initView(LayoutInflater inflater) {
        if (parentView == null) {
            parentView = inflater.inflate(R.layout.fragment_main_myself, null, false);
        }
        return parentView;
    }

    @Override
    public void onFirstUserVisible() {
        ButterKnife.bind(this, parentView);
    }

    @OnClick({R.id.headRIV, R.id.collectFL, R.id.energyFL, R.id.boxFL, R.id.cardsFL, R.id.personalityFL, R.id.settingFL, R.id.applyFL, R.id.helpFL, R.id.filesFL})
    public void onClick(View view) {
        if (CommonUtils.isFastDoubleClick()) return;
        ToastUtil.showToast("点击了" + view.getId());
        switch (view.getId()) {
            case R.id.headRIV:
                break;
            case R.id.collectFL:
                break;
            case R.id.energyFL:
                break;
            case R.id.boxFL:
                break;
            case R.id.cardsFL:
                break;
            case R.id.personalityFL:
                break;
            case R.id.settingFL:
                break;
            case R.id.applyFL:
                break;
            case R.id.helpFL:
                break;
            case R.id.filesFL:
                break;
        }
    }
}
