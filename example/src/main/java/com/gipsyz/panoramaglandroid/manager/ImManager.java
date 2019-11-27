package com.gipsyz.panoramaglandroid.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.gipsyz.panoramaglandroid.GLApplication;
import com.tencent.qcloud.tim.thirdpush.ThirdPushTokenMgr;
import com.tencent.qcloud.tim.utils.Constants;

public class ImManager {

    private static final String TAG = "``" + ImManager.class.getSimpleName();
    private static ImManager instance = new ImManager();

    public String regId;
    public boolean isLogin = false;

    private ImManager() {
    }

    public static ImManager getInstance() {
        return instance;
    }

    public void pushXiaoMiTokenToTIM() {
        ThirdPushTokenMgr.getInstance().setThirdPushToken(ImManager.getInstance().regId);
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
    }

    public void pushTokenToTIM(String token) {
        ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
    }

    public void IMLoginSuccess() {
        isLogin = true;
        SharedPreferences shareInfo =
                GLApplication.getInstance().getSharedPreferences(Constants.USERINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putBoolean(Constants.AUTO_LOGIN, true);
        editor.commit();
    }

    public void IMlogout(Context context, boolean autoLogin) {
        SharedPreferences shareInfo = context.getSharedPreferences(Constants.USERINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putBoolean(Constants.AUTO_LOGIN, autoLogin);
        editor.commit();
    }

    /*public void login(LoginResponse resp, IMLoginCallBack callBack) {
        String account = String.valueOf(resp.data.id);
        String userSig = resp.data.imSig;

        TUIKit.login(account, userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                // 设置im的昵称 头像等

                if (resp.data == null || resp.data.shop == null || resp.data.shop.info == null) {
                    IMLoginSuccess();
                    callBack.onSuccess();
                } else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    if (!TextUtils.isEmpty(resp.data.shop.info.logo)) {
                        hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL, resp.data.shop.info.logo);
                    } else {
                        hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL, "");
                    }
                    // 昵称
                    String nickName = resp.data.shop.info.name;
                    hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK, TextUtils.isEmpty(nickName) ? "暂未返回默认昵称" : nickName);
                    // 地区
                    hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_LOCATION, "fz"); // TODO 不加SDK会有个崩溃

                    TIMFriendshipManager.getInstance().modifySelfProfile(hashMap, new TIMCallBack() {

                        @Override
                        public void onSuccess() {
                            IMLoginSuccess();
                            callBack.onSuccess();
                        }

                        @Override
                        public void onError(int i, String s) {
                            callBack.onError(i, "");

                        }
                    });
                }
            }

            @Override
            public void onError(String module, final int code, final String desc) {
                System.out.println("`````````login onError errCode = " + code + ", errInfo = " + desc);
                callBack.onError(code, desc);
            }
        });
    }*/

    public interface IMLoginCallBack {
        void onSuccess();

        void onError(final int code, final String desc);
    }

}
