package com.yousucc.ui.activity.authentication;

import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.view.View;

import com.yousucc.R;
import com.yousucc.protocol.ApiByHttp;
import com.yousucc.protocol.HttpPostCallback;
import com.yousucc.beans.Status;
import com.yousucc.ui.base.slidback.SlidingActivity;
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
public class FindPassworldActivity extends SlidingActivity {
    @Bind(R.id.phoneET)
    YouSuccEditText phoneET;
    @Bind(R.id.codeET)
    YouSuccEditText codeET;
    @Bind(R.id.getCode)
    YouSuccTextView getCode;
    @Bind(R.id.passET)
    YouSuccEditText passET;
    private boolean getCodeAble = true;
    private String phone;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.clearPhone, R.id.getCode, R.id.lookPass, R.id.loginBtn, R.id.accountLoginTV})
    public void onClick(View view) {
        if (CommonUtils.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.clearPhone:
                phoneET.setText("");
                break;
            case R.id.getCode:
                //TODO 获取验证码
                if (!getCodeAble) return;
                phone = phoneET.getText().toString();
                if (ContactsUtil.isMobileNO(phone)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 60; i++) {
                                getHandler().sendMessage(getHandler().obtainMessage(0, i));
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            getHandler().sendMessage(getHandler().obtainMessage(0, 60));
                        }
                    }).start();
                    //TODO 获取验证码
                    ApiByHttp.getInstance().findGetAuthcode(phone, new HttpPostCallback<Status>(true,Status.class) {
                        @Override
                        public void resultSuccess(Status result) {
                            if (result.getStatus() != 200) {
                                ToastUtil.showToast(result.getMsg());
                            }
                        }

                        @Override
                        public void resultFailure() {
                            ToastUtil.showToast("resultFailure");
                        }
                    });
                } else {
                    ToastUtil.showToast(R.string.pleaseinputrightphone);
                }
                break;
            case R.id.lookPass:
                if (passET.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    passET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    passET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            case R.id.loginBtn:
                //TODO 登录
                if (!phone.equalsIgnoreCase(phoneET.getText().toString())) {
                    ToastUtil.showToast(R.string.pleaseinputrightphone);
                    return;
                }
                String authcode = codeET.getText().toString();
                password = passET.getText().toString();
                if (authcode.length() > 3) {
                    if (ContactsUtil.isMobileNO(phone)) {
                        if (password.length() > 5) {
                            //TODO
                            ToastUtil.showToast("注册");
                            ApiByHttp.getInstance().findSetPassworld(authcode, password, new HttpPostCallback<Status>(true, Status.class) {
                                @Override
                                public void resultSuccess(Status result) {
                                    if (result.getStatus() == 200) {
                                        //TODO
                                        ToastUtil.showToast("找回密码成功，登录...");
                                        FindPassworldActivity.this.finish();
                                    } else {
                                        ToastUtil.showToast(result.getMsg());
                                    }
                                }

                                @Override
                                public void resultFailure() {
                                    ToastUtil.showToast("resultFailure");
                                }
                            });
                        } else {
                            ToastUtil.showToast(R.string.pleaseinputrightpass);
                        }
                    } else {
                        ToastUtil.showToast(R.string.pleaseinputrightphone);
                    }
                } else {
                    ToastUtil.showToast(R.string.pleaseinputrightcode);
                }

                break;
            case R.id.accountLoginTV:
                finish();
                break;
        }
    }

    @Override
    protected void handlerPacketMsg(Message msg) {
        switch (msg.what) {
            case 0:
                int time = (Integer) msg.obj;
                if (time < 60) {
                    getCodeAble = false;
                    getCode.setClickable(false);
                    getCode.setText((60 - time) + "s");
                } else {
                    getCodeAble = true;
                    getCode.setClickable(true);
                    getCode.setText("发送验证码");
                }
                break;
        }
    }

}
