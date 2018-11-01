package com.yousucc.ui.activity.authentication;

import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.yousucc.R;
import com.yousucc.protocol.ApiByHttp;
import com.yousucc.protocol.HttpPostCallback;
import com.yousucc.beans.Status;
import com.yousucc.ui.base.slidback.SlidingActivity;
import com.yousucc.ui.widget.YouSuccButton;
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
public class RegisterActivity extends SlidingActivity {
    @Bind(R.id.phoneET)
    YouSuccEditText phoneET;
    @Bind(R.id.clearPhone)
    ImageView clearPhone;
    @Bind(R.id.codeET)
    YouSuccEditText codeET;
    @Bind(R.id.getCode)
    YouSuccTextView getCode;
    @Bind(R.id.passET)
    YouSuccEditText passET;
    @Bind(R.id.lookPass)
    ImageView lookPass;
    @Bind(R.id.registerBtn)
    YouSuccButton registerBtn;
    @Bind(R.id.agreeCB)
    CheckBox agreeCB;
    @Bind(R.id.registerAgreementTV)
    YouSuccTextView registerAgreementTV;

    private boolean getCodeAble = true;

    private String phone = "";
    private String passworld = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.clearPhone, R.id.getCode, R.id.lookPass, R.id.registerBtn, R.id.registerAgreementTV})
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
                                if (isDestroy) {
                                    return;
                                }
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
                    ApiByHttp.getInstance().registerGetAuthcode(phone, new HttpPostCallback<Status>(true, Status.class) {
                        @Override
                        public void resultSuccess(Status result) {
                            ToastUtil.showToast(result.toString());
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
                ApiByHttp.getInstance().test(new HttpPostCallback<String>(true,String.class){
                    @Override
                    public void resultSuccess(String result) {
                        ToastUtil.showToast(result.toString());
                    }

                    @Override
                    public void resultFailure() {
                        ToastUtil.showToast("resultFailure");
                    }
                });
                break;
            case R.id.registerBtn:
                //TODO 注册
                if (!phone.equalsIgnoreCase(phoneET.getText().toString())) {
                    ToastUtil.showToast(R.string.pleaseinputrightphone);
                    return;
                }
                String authcode = codeET.getText().toString();
                passworld = passET.getText().toString();
                if (authcode.length() > 3) {
                    if (ContactsUtil.isMobileNO(phone)) {
                        if (passworld.length() > 5) {
                            //TODO
                            ApiByHttp.getInstance().registerSetPassworld(authcode, passworld, new HttpPostCallback<Status>(true, Status.class) {
                                @Override
                                public void resultSuccess(Status result) {
                                    if (result.getStatus() == 200) {
                                        //TODO
                                        ToastUtil.showToast("注册成功，登录...");
                                        RegisterActivity.this.finish();
                                    } else {
                                        ToastUtil.showToast(result.getMsg() + "");
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
            case R.id.registerAgreementTV:
                //TODO 跳转协议页面
                ToastUtil.showToast("跳转协议页面");
                break;
        }
    }

    private boolean isDestroy = false;

    @Override
    protected void onDestroy() {
        isDestroy = true;
        super.onDestroy();
    }

    @Override
    protected void handlerPacketMsg(Message msg) {
        switch (msg.what) {
            case 0:
                if (getCode == null) return;
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
