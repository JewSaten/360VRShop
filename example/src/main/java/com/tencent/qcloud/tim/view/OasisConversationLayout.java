package com.tencent.qcloud.tim.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


import com.gipsyz.panoramaglandroid.R;
import com.gipsyz.panoramaglandroid.event.LoadMessageFinishEvent;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListAdapter;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationProvider;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.interfaces.IConversationAdapter;
import com.tencent.qcloud.tim.uikit.modules.conversation.interfaces.IConversationLayout;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

public class OasisConversationLayout extends RelativeLayout implements IConversationLayout {

    private ConversationListLayout mConversationList;

    public OasisConversationLayout(Context context) {
        super(context);
        init();
    }

    public OasisConversationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OasisConversationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private DataChangeListener mDataChangeListener;

    public void setDataChangeListener(DataChangeListener listener){
        this.mDataChangeListener = listener;
    }

    /**
     * 初始化相关UI元素
     */
    private void init() {
        inflate(getContext(), R.layout.tim_my_conversation_layout, this);
        mConversationList = findViewById(R.id.conversation_list);
    }

    public void initDefault() {
        final IConversationAdapter adapter = new ConversationListAdapter();
        mConversationList.setAdapter(adapter);
        ConversationManagerKit.getInstance().loadConversation(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                adapter.setDataProvider((ConversationProvider) data);
                EventBus.getDefault().post(new LoadMessageFinishEvent());
                if(mDataChangeListener != null)
                    mDataChangeListener.onLoadSuccess((ConversationProvider) data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage("加载消息失败");
                EventBus.getDefault().post(new LoadMessageFinishEvent());
                if(mDataChangeListener != null)
                    mDataChangeListener.onLoadError();
            }
        });
    }

    @Override
    public TitleBarLayout getTitleBar() {
        return null;
    }

    @Override
    public void setParentLayout(Object parent) {

    }

    @Override
    public ConversationListLayout getConversationList() {
        return mConversationList;
    }

    public void addConversationInfo(int position, ConversationInfo info) {
        mConversationList.getAdapter().addItem(position, info);
    }

    public void removeConversationInfo(int position) {
        mConversationList.getAdapter().removeItem(position);
    }

    @Override
    public void setConversationTop(int position, ConversationInfo conversation) {
        ConversationManagerKit.getInstance().setConversationTop(position, conversation);
    }

    @Override
    public void deleteConversation(int position, ConversationInfo conversation) {
        ConversationManagerKit.getInstance().deleteConversation(position, conversation);
        if(mDataChangeListener != null)
            mDataChangeListener.onDataChange();
    }

    public interface DataChangeListener{
        void onLoadSuccess(ConversationProvider data);
        void onLoadError();
        void onDataChange();
    }
}
