package com.gipsyz.panoramaglandroid.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gipsyz.panoramaglandroid.R;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;

public class TimAdapter extends BaseQuickAdapter<TIMMessage, BaseViewHolder> {

    public TimAdapter() {
        super(R.layout.item_tim_chat, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, TIMMessage item) {
        TextView tvContent = helper.getView(R.id.tv_content);
        TIMElem timElem = item.getElement(0);
        if(timElem.getType() == TIMElemType.Text){
            TIMTextElem textElem = (TIMTextElem)timElem;
            if(item.isSelf()){
                tvContent.setText(Html.fromHtml("<font color='#FEA062'>"+"我："+"</font>"+textElem.getText()));
            } else {
                tvContent.setText(Html.fromHtml("<font color='#FF59F0'>"+item.getSender()+"："+"</font>"+textElem.getText()));
            }

        }

    }

}
