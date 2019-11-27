package com.gipsyz.panoramaglandroid;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


public class GoodsAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder>{
    static final String[] ITEMS_TITLE = {"小心意系列爱心18K金彩金钻石手镯",
            "金蜻蜓18K金彩金吊坠","一鹿有你彩金钻石鹿角项链",
            "时尚18K金南洋珍珠项链套链吊坠",
            "小心意Tri-Light系列18K金彩金项链","VaNa  18K金·海洋之心 "};
    static final int[] ITEMS_RES= {R.mipmap.icon1,R.mipmap.icon2,R.mipmap.icon3,R.mipmap.icon4,R.mipmap.icon5,R.mipmap.icon6};
    static List<GoodsBean> data = new ArrayList<>();

    static {
        for(int i=0;i<6;i++){
            GoodsBean bean = new GoodsBean();
            bean.resId = ITEMS_RES[i];
            bean.title = ITEMS_TITLE[i];
            data.add(bean);
        }
    }

    public GoodsAdapter() {
        super(R.layout.item_goods,data);
    }



    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        if (helper.getAdapterPosition() == 0)
            helper.getView(R.id.view_divide).setVisibility(View.VISIBLE);
        else
            helper.getView(R.id.view_divide).setVisibility(View.GONE);

        TextView tvOrigin = helper.getView(R.id.tv_origin);

        tvOrigin.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        helper.setText(R.id.tv_title,item.title);
        helper.setImageResource(R.id.iv_icon,item.resId);

        AmountView amountView = helper.getView(R.id.view_amount);
        amountView.setGoods_storage(100);
        amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {

            }
        });
    }
}
