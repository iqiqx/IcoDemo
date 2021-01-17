package com.xx.iocdemo;

import android.util.Log;
import android.view.View;
import android.widget.Button;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @ViewInject(R.id.btn)
    Button btn1;
    @ViewInject(R.id.btn2)
    Button btn2;

    @Override
    protected void initView() {
        super.initView();

        btn1.setText("按钮一");
        btn2.setText("按钮二");
        btn2.setBackgroundResource(R.color.colorAccent);
    }

    @OnClick({R.id.btn, R.id.btn2})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn:
                Log.d(TAG, "click: >>>>>>>>>>>>>>>>点击了按钮1");
                break;
            case R.id.btn2:
                Log.d(TAG, "click: >>>>>>>>>>>>>>>>点击了按钮2");
                break;
        }
    }
}