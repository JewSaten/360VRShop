package com.tencent.qcloud.tim.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gipsyz.panoramaglandroid.GLApplication;
import com.gipsyz.panoramaglandroid.R;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.FriendProfileLayout;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.utils.Constants;

public class FriendProfileActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_friend_profile_activity);
        FriendProfileLayout layout = findViewById(R.id.friend_profile);

        layout.initData(getIntent().getSerializableExtra(TUIKitConstants.ProfileType.CONTENT));
        layout.setOnButtonClickListener(new FriendProfileLayout.OnButtonClickListener() {
            @Override
            public void onStartConversationClick(ContactItemBean info) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(TIMConversationType.C2C);
                chatInfo.setId(info.getId());
                String chatName = info.getId();
                if (!TextUtils.isEmpty(info.getRemark())) {
                    chatName = info.getRemark();
                } else if (!TextUtils.isEmpty(info.getNickname())) {
                    chatName = info.getNickname();
                }
                chatInfo.setChatName(chatName);
//                Intent intent = new Intent(GLApplication.getInstance(), ChatActivity.class);
//                intent.putExtra(Constants.CHAT_INFO, chatInfo);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                GLApplication.getInstance().startActivity(intent);
            }

            @Override
            public void onDeleteFriendClick(String id) {
//                Intent intent = new Intent(GLApplication.getInstance(), MessageListActivity.class);
//                startActivity(intent);
            }
        });
    }

}
