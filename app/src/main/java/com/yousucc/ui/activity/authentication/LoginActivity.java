package com.yousucc.ui.activity.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yousucc.R;
import com.yousucc.YouSuccApplication;
import com.yousucc.chat.SDKCoreHelper;
import com.yousucc.protocol.ApiByHttp;
import com.yousucc.ui.activity.main.ActivityMain;
import com.yousucc.ui.base.BaseActivity;
import com.yousucc.ui.widget.YouSuccEditText;
import com.yousucc.ui.widget.YouSuccTextView;
import com.yousucc.utils.CommonUtils;
import com.yousucc.utils.ContactsUtil;
import com.yousucc.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SensYang on 2016/3/23 0023.
 */
public class LoginActivity extends BaseActivity {
    public static LoginActivity instance;
    @Bind(R.id.phoneET)
    YouSuccEditText phoneET;
    @Bind(R.id.clearPhone)
    ImageView clearPhone;
    @Bind(R.id.passET)
    YouSuccEditText passET;
    @Bind(R.id.lookPass)
    ImageView lookPass;
    @Bind(R.id.loginBtn)
    Button loginBtn;
    @Bind(R.id.registeTV)
    YouSuccTextView registeTV;
    @Bind(R.id.findPassTV)
    YouSuccTextView findPassTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.clearPhone, R.id.lookPass, R.id.loginBtn, R.id.registeTV, R.id.findPassTV})
    public void onClick(View view) {
        if (CommonUtils.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.clearPhone:
                phoneET.setText("");
                break;
            case R.id.lookPass:
                if (passET.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    passET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    passET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            case R.id.loginBtn:
                ToastUtil.showToast("点击");
                String phone = phoneET.getText().toString();
                String password = passET.getText().toString();
                if (ContactsUtil.isMobileNO(phone)) {
                    if (password != null && password.length() > 5) {
                        //TODO
                        ApiByHttp.getInstance().setSessionId(phone);
                        ApiByHttp.getInstance().setPassword(password);
                        SDKCoreHelper.getInstance().setHandler(getHandler());
                        SDKCoreHelper.getInstance().init();
//                        ApiByHttp.getInstance().login(phone, password, new HttpPostCallback<Status>(true, Status.class) {
//                            @Override
//                            public void resultSuccess(Status result) {
//                                if (result != null) {
//                                    if (result.getStatus() == 200) {
//                                        SDKCoreHelper.getInstance().setHandler(getHandler());
//                                        SDKCoreHelper.getInstance().init();
//                                    } else {
//                                        ToastUtil.showToast(result.getMsg());
//                                    }
//                                } else {
//                                    ToastUtil.showToast("登陆失败");
//                                }
//                                SDKCoreHelper.getInstance().setHandler(getHandler());
//                                SDKCoreHelper.getInstance().init();
//                            }
//
//                            @Override
//                            public void resultFailure() {
//                                //TODO  记得删除
//                                ToastUtil.showToast("登陆失败");
//                            }
//                        });
                    } else {
                        ToastUtil.showToast(R.string.pleaseinputrightpass);
                    }
                } else {
                    ToastUtil.showToast(R.string.pleaseinputrightphone);
                }
                break;
            case R.id.registeTV:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.findPassTV:
                startActivity(new Intent(this, FindPassworldActivity.class));
                break;
        }
    }

    @Override
    protected void handlerPacketMsg(Message msg) {
        switch (msg.what) {
            case SDKCoreHelper.CONNECT_SUCCESS:
                YouSuccApplication.getInstance().initQuPaiAuth();
                startActivity(new Intent(LoginActivity.this, ActivityMain.class));
                break;
        }
    }

}
