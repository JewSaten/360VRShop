package com.tencent.qcloud.tim.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gipsyz.panoramaglandroid.R;
import com.tencent.qcloud.tim.helper.ChatLayoutHelper;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.utils.Constants;


public class ChatFragment extends BaseFragment {

    private View mBaseView;
    private ChatLayout mChatLayout;
    private ChatInfo mChatInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
        if (mChatInfo == null) {
            return null;
        }
        mBaseView = inflater.inflate(R.layout.chat_fragment, container, false);
        initView();
        return mBaseView;
    }

    private void initView() {
        //从布局文件中获取聊天面板组件
        mChatLayout = mBaseView.findViewById(R.id.chat_layout);

        //单聊组件的默认UI和交互初始化
        mChatLayout.initDefault();

        // TODO 通过api设置ChatLayout各种属性的样例
        ChatLayoutHelper.customizeChatLayout(getActivity(), mChatLayout);

        /*
         * 需要聊天的基本信息
         */
        mChatLayout.setChatInfo(mChatInfo);

        //获取单聊面板的标题栏
//        mTitleBar = mChatLayout.getTitleBar();

        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
//        mTitleBar.setOnLeftClickListener(view -> getActivity().finish());
//        if (mChatInfo.getType() == TIMConversationType.C2C) {
//            mTitleBar.setOnRightClickListener(v -> {
//                Intent intent = new Intent(GLApplication.getInstance(), FriendProfileActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, mChatInfo);
//                GLApplication.getInstance().startActivity(intent);
//            });
//        }

        mChatLayout.getMessageLayout().setOnItemClickListener(new MessageLayout.OnItemClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //因为adapter中第一条为加载条目，位置需减1
                mChatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
//                if (null == messageInfo || null == messageInfo.getTIMMessage()) {
//                    return;
//                }
//                ChatInfo info = new ChatInfo();
//                info.setId(messageInfo.getTIMMessage().getSender());
//                Intent intent = new Intent(GLApplication.getInstance(), FriendProfileActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, info);
//                GLApplication.getInstance().startActivity(intent);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mChatLayout.exitChat();
    }

}
