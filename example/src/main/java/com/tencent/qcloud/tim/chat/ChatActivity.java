package com.tencent.qcloud.tim.chat;

import android.os.Bundle;

import com.gipsyz.panoramaglandroid.R;
import com.gipsyz.panoramaglandroid.base.BaseActivity;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.utils.Constants;

public class ChatActivity extends BaseActivity {

    private ChatFragment mChatFragment;
    private ChatInfo mChatInfo;

    @Override
    public int getLayoutId() {
        return R.layout.chat_activity;
    }

    @Override
    public void initView() {
        hideTitle();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
        } else {
            mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
            if (mChatInfo == null) {
                finish();
                return;
            }
            initTitle(mChatInfo.getChatName());
            mChatFragment = new ChatFragment();
            mChatFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.empty_view, mChatFragment).commitAllowingStateLoss();
        }
    }
}
