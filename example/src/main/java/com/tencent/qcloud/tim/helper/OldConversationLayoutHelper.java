package com.tencent.qcloud.tim.helper;


import com.gipsyz.panoramaglandroid.R;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;

public class OldConversationLayoutHelper {

    public static void initTitle(ConversationLayout layout) {
        TitleBarLayout titleBarLayout = layout.getTitleBar();
        titleBarLayout.getLeftIcon().setImageResource(R.mipmap.ic_clock);
        titleBarLayout.setTitle("两周前的消息", TitleBarLayout.POSITION.LEFT);
    }

    public static void customizeConversation(ConversationLayout layout) {

        ConversationListLayout listLayout =  layout.getConversationList();

        listLayout.setItemTopTextSize(14); // 设置adapter item中top文字大小
        listLayout.setItemBottomTextSize(12);// 设置adapter item中bottom文字大小
        listLayout.setItemDateTextSize(10);// 设置adapter item中timeline文字大小
        listLayout.enableItemRoundIcon(true);// 设置adapter item头像是否显示圆角，默认是方形
        listLayout.disableItemUnreadDot(true);// 设置adapter item是否不显示未读红点，默认显示
    }
}
