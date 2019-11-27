package com.tencent.qcloud.tim.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.gipsyz.panoramaglandroid.MainActivity;
import com.gipsyz.panoramaglandroid.R;
import com.gipsyz.panoramaglandroid.base.BaseActivity;
import com.gipsyz.panoramaglandroid.constants.Keys;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.qcloud.tim.signature.GenerateTestUserSig;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.utils.DemoLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Demo的登录Activity
 * 用户名可以是任意非空字符，但是前提需要按照下面文档修改代码里的 SDKAPPID 与 PRIVATEKEY
 * https://github.com/tencentyun/TIMSDK/tree/master/Android
 * <p>
 */

public class LoginForDevActivity extends BaseActivity {

    private static final String TAG = LoginForDevActivity.class.getSimpleName();
    private static final int REQ_PERMISSION_CODE = 0x100;

    private SurfaceView surfaceView;

    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;

    private Button mLoginView;
    private EditText mUserAccount;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected int getTotalLayoutId() {
        return R.layout.login_for_dev_layout;
    }

    @Override
    protected void doBeforeSetContentView(Bundle savedInstanceState) {
        super.doBeforeSetContentView(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private boolean isReleased = false;

    @Override
    public void initView() {
        mLoginView = findViewById(R.id.login_btn);
        mUserAccount = findViewById(R.id.login_user);

        surfaceView = findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(3);
        surfaceView.setFocusable(true);
        surfaceView.setFocusableInTouchMode(true);
        surfaceView.requestFocus();

        // 设置播放时打开屏幕
        // surfaceHolder.setKeepScreenOn(true);

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mediaPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
        });

        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.video);
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    mediaPlayer.start();
                    isReleased = true;
                }
            });

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private MediaPlayer.OnVideoSizeChangedListener listener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        }
    };

    @Override
    public void initListener() {
        mLoginView.setOnClickListener(view -> {
            if(!checkPermission(this)){
                return;
            }
            String userSig = GenerateTestUserSig.genTestUserSig(mUserAccount.getText().toString());
            TUIKit.login(mUserAccount.getText().toString(), userSig, new IUIKitCallBack() {
                @Override
                public void onError(String module, final int code, final String desc) {
                    runOnUiThread(() -> ToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc));
                    System.out.println("``````````````登录失败");
                }

                @Override
                public void onSuccess(Object data) {
                    //获取我的群聊
                    TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {

                        @Override
                        public void onError(int i, String s) {
                            ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
                            System.out.println("``````````````getGroupList onError");
                        }

                        @Override
                        public void onSuccess(List<TIMGroupBaseInfo> infos) {
                            System.out.println("`````````登录成功");
                            if (infos.size() == 0) {
                                TUIKitLog.i(TAG, "getFriendGroups success but no data");
                                Toast.makeText(LoginForDevActivity.this, "当前账号还未加入当前店铺的直播", Toast.LENGTH_SHORT).show();
                                System.out.println("``````````````当前账号还未加入当前店铺的直播");
                                return;
                            }
                            ContactItemBean contact = new ContactItemBean();
                            contact.covertTIMGroupBaseInfo(infos.get(0));
                            ChatInfo chatInfo = new ChatInfo();
                            chatInfo.setType(TIMConversationType.Group);
                            String chatName = contact.getId();
                            if (!TextUtils.isEmpty(contact.getRemark())) {
                                chatName = contact.getRemark();
                            } else if (!TextUtils.isEmpty(contact.getNickname())) {
                                chatName = contact.getNickname();
                            }
                            chatInfo.setChatName(chatName);
                            chatInfo.setId(contact.getId());

                            Intent intent = new Intent(LoginForDevActivity.this, MainActivity.class);
                            intent.putExtra(Keys.KEY_CHAT_INFO, chatInfo);
                            startActivity(intent);
                            finish();
                        }
                    });


                }
            });
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    //权限检查
    public static boolean checkPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(TUIKit.getAppContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(TUIKit.getAppContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                String[] permissionsArray = permissions.toArray(new String[1]);
                ActivityCompat.requestPermissions(activity,
                        permissionsArray,
                        REQ_PERMISSION_CODE);
                return false;
            }
        }

        return true;
    }

    /**
     * 系统请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSION_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.toastLongMessage("未全部授权，部分功能可能无法使用！");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReleased) {
            initView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
