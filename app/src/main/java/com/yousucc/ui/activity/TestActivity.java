package com.yousucc.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yousucc.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SensYang on 2016/4/19 0019.
 */
public class TestActivity extends Activity {

    @Bind(R.id.button0)
    Button button0;
//    @Bind(R.id.button1)
//    Button button1;
//    @Bind(R.id.button2)
//    Button button2;
//    @Bind(R.id.button3)
//    Button button3;
//    @Bind(R.id.button4)
//    Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

//    @OnClick({R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4})
//    public void doClick(View view) {
//        Toast.makeText(view.getContext(), view.getId() + "", Toast.LENGTH_SHORT).show();
//        switch (view.getId()) {
//            case R.id.button0:
//                break;
//            case R.id.button1:
//                break;
//            case R.id.button2:
//                break;
//            case R.id.button3:
//                break;
//            case R.id.button4:
//                break;
//        }
//    }

    @OnClick(R.id.button0)
    public void onclick(View view){
        Toast.makeText(view.getContext(), "sssssssssss", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
