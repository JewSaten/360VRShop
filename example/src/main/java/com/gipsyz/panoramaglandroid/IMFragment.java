package com.gipsyz.panoramaglandroid;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gipsyz.panoramaglandroid.adapter.TimAdapter;
import com.gipsyz.panoramaglandroid.constants.Keys;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;

import java.util.List;

public class IMFragment extends BaseFragment {

    private View rootView;

    private RecyclerView recyclerView;

    private ChatInfo chatInfo;
    private TIMConversation mCurrentConversation;

    private TimAdapter timAdapter;

    private RecyclerView recyclerViewGoods;
    private  View viewBottom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.im_fragment, container, false);
        rootView = view.findViewById(R.id.root_view);
        chatInfo = (ChatInfo) getArguments().getSerializable(Keys.KEY_CHAT_INFO);
        initViews();
        initIM();
        return view;
    }

    View view_editContent;
    EditText edContent;

    @TargetApi(Build.VERSION_CODES.M)
    private void initViews() {
        viewBottom =rootView.findViewById(R.id.view_bottom);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        timAdapter = new TimAdapter();
        recyclerView.setAdapter(timAdapter);

        View view_edit = rootView.findViewById(R.id.view_edit);
        view_editContent = rootView.findViewById(R.id.view_edit_content);

        edContent = rootView.findViewById(R.id.edit_content);

        view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftInput(edContent);
            }
        });

        rootView.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        rootView.findViewById(R.id.tv_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("```````tv_send onClick");
                String content = edContent.getText().toString();
                if(TextUtils.isEmpty(content.trim()))
                    return;
                TIMMessage msg = new TIMMessage();
                //添加文本内容
                TIMTextElem elem = new TIMTextElem();
                elem.setText(content);
                msg.addElement(elem);
                mCurrentConversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
                    @Override
                    public void onError(int code, String desc) {
                        System.out.println("````````sendMessage 发送消息失败 desc="+desc);
                        Toast.makeText(getActivity(), "消息发送失败 msg="+desc, Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).hideSoftInput(rootView);
                        hideEditContent();
                    }

                    @Override
                    public void onSuccess(TIMMessage msg) {
                        System.out.println("````````sendMessage 发送消息成功");
                        timAdapter.addData(msg);
                        if(timAdapter.getItemCount() >3){
                            recyclerView.smoothScrollToPosition(timAdapter.getItemCount() -1);
                        }
                        ((MainActivity) getActivity()).hideSoftInput(rootView);
                        hideEditContent();
                    }
                });
            }
        });

        recyclerViewGoods =rootView.findViewById(R.id.recyclerView);
        recyclerViewGoods.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        GoodsAdapter adapter = new GoodsAdapter();
        recyclerViewGoods.setAdapter(adapter);
        rootView.findViewById(R.id.iv_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewGoods.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                viewBottom.setVisibility(View.GONE);
            }
        });

        KeyboardStateObserver.getKeyboardStateObserver(getActivity()).
                setKeyboardVisibilityListener(new KeyboardStateObserver.OnKeyboardVisibilityListener() {
                    @Override
                    public void onKeyboardShow() {
                        view_editContent.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onKeyboardHide() {
                        hideEditContent();
                    }
                });

    }

    private boolean isSoftShowing() {
        //获取当屏幕内容的高度
        int screenHeight = getActivity().getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight*2/3 > rect.bottom;
    }


    public void setVisibility(int visibility){
        rootView.setVisibility(visibility);
        showNormal();
    }

    public void showNormal(){
        recyclerViewGoods.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        viewBottom.setVisibility(View.VISIBLE);
    }

    public void hideEditContent(){
        view_editContent.setVisibility(View.GONE);
        edContent.setText("");
    }

    private void initIM() {
        FileUtil.initPath(); // 从application移入到这里，原因在于首次装上app
        // ，需要获取一系列权限，如创建文件夹，图片下载需要指定创建好的文件目录，否则会下载本地失败，聊天页面从而获取不到图片、表情
        GroupChatManagerKit.getInstance();
        mCurrentConversation = TIMManager.getInstance().getConversation(chatInfo.getType(),
                chatInfo.getId());

        TUIKit.addIMEventListener(new IMEventListener() {

            @Override
            public void onNewMessages(List<TIMMessage> msgs) {
                super.onNewMessages(msgs);
                for (TIMMessage msg : msgs) {
                    //群组消息
                    if (TIMConversationType.Group == msg.getConversation().getType() && TextUtils.equals(mCurrentConversation.getPeer(), msg.getConversation().getPeer())) {
                        //消息发送者
                        timAdapter.addData(msg);
                        if(timAdapter.getItemCount() >3){
                            recyclerView.smoothScrollToPosition(timAdapter.getItemCount() -1);
                        }
                    }
                }
                System.out.println("`````````````onNewMessages");
            }

        });

    }

    public void showSoftInput(EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }
}
