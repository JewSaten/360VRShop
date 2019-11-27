package com.gipsyz.panoramaglandroid.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gipsyz.panoramaglandroid.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;
    private View rootView;

    public int mPage = START_PAGE;
    public static final int PAGE_SIZE = 20;
    public static final int START_PAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        doBeforeSetContentView(savedInstanceState);
        onCreateContent(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        doAfterSerContentView(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        initView();
        initListener();
        initData();
    }

    protected void doBeforeSetContentView(Bundle savedInstanceState) {

    }

    protected void doAfterSerContentView(Bundle savedInstanceState) {

    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        try {
//            super.onSaveInstanceState(outState);
//            if (outState != null) {
//                outState.remove("android:support:fragments");
//            }
//        } catch (Exception e) {
//            onSaveInstanceStateException(e);
//        }
//    }
//
//    protected void onSaveInstanceStateException(Exception e) {
//
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        try {
//            super.onRestoreInstanceState(savedInstanceState);
//        } catch (Exception e) {
//            onRestoreInstanceStateException(e);
//        }
//    }
//
//    protected void onRestoreInstanceStateException(Exception e) {
//
//    }

    protected void onCreateContent(Bundle savedInstanceState) {
        int tId = getTotalLayoutId();
        if (tId > 0) {
            rootView = LayoutInflater.from(this).inflate(tId, null);
            setContentView(rootView);
            return;
        }
        rootView = LayoutInflater.from(this).inflate(R.layout.act_base, null);
        setContentView(rootView);
        rootView.requestFocus();
        int rid = getLayoutId();
        if (rid > 0) {
            RelativeLayout contentView = rootView.findViewById(R.id.view_content);
            LayoutInflater.from(this).inflate(rid, contentView, true);
        }
        getTitleLeftView().setOnClickListener(v -> finish());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean flag = customDispatchTouchEvent(ev);
        if (flag) {
            return flag;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View view = getCurrentFocus();
            hideSoftInput(view);
        }
        return super.dispatchTouchEvent(ev);
    }

    protected boolean customDispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
        }
        return false;
    }

    private long lastClickTime;

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD <= 200;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onDestroy() {
        // 解绑ButterKnife
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    public void hideSoftInput(View view) {
        if (view != null && view.getWindowToken() != null) {
            InputMethodManager manager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected int getTotalLayoutId() {
        return -1;
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initListener();

    public abstract void initData();

    public void reloadData() {

    }

    ;

    protected Context getContext() {
        return this;
    }

    protected ImageView getTitleLeftView() {
        return (ImageView) findViewById(R.id.iv_left);
    }

    protected ImageView getTitleRightView() {
        return (ImageView) findViewById(R.id.iv_right);
    }

    protected TextView getTitleRightTextView() {
        return (TextView) findViewById(R.id.tv_right);
    }

    protected void hideStatusView() {
        findViewById(R.id.status_view).setVisibility(View.GONE);
    }

    protected void setStatusViewColor(int color) {
        findViewById(R.id.status_view).setBackgroundColor(getResources().getColor(color));
    }

    protected void hideTitle() {
        findViewById(R.id.rl_title).setVisibility(View.GONE);
    }

    protected void initRight(int resId, View.OnClickListener clickListener) {
        ImageView image = getTitleRightView();
        image.setVisibility(View.VISIBLE);
        image.setImageResource(resId);
        image.setOnClickListener(clickListener);
    }

    protected void initRightText(String content, View.OnClickListener clickListener) {
        TextView textView = getTitleRightTextView();
        textView.setVisibility(View.VISIBLE);
        textView.setText(content);
        textView.setOnClickListener(clickListener);
    }

    protected void initLeftClickListener(View.OnClickListener clickListener) {
        getTitleLeftView().setOnClickListener(clickListener);
    }

    protected TextView getTitleView() {
        return (TextView) findViewById(R.id.tv_title);
    }

    protected void initTitle(int resId) {
        getTitleView().setText(resId);
    }

    protected void initTitle(String title) {
        getTitleView().setText(title);
    }

    protected View getRootView() {
        return rootView;
    }

}
