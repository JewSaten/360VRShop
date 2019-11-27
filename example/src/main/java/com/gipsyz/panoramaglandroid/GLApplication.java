package com.gipsyz.panoramaglandroid;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.tencent.imsdk.TIMBackgroundParam;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.session.SessionWrapper;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.helper.ConfigHelper;
import com.tencent.qcloud.tim.signature.GenerateTestUserSig;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.utils.DemoLog;

import java.util.List;

public class GLApplication extends Application {

    private static GLApplication mInstance;

    public static GLApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        if (SessionWrapper.isMainProcess(getApplicationContext())) {
            TUIKit.init(getApplicationContext(), GenerateTestUserSig.SDKAPPID, new ConfigHelper().getConfigs());
        }
    }

}
