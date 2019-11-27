package com.gipsyz.panoramaglandroid;

import com.gipsyz.panoramaglandroid.constants.Keys;
import com.panoramagl.PLCameraListener;
import com.panoramagl.PLICamera;
import com.panoramagl.PLIPanorama;
import com.panoramagl.PLImage;
import com.panoramagl.PLSphericalPanorama;
import com.panoramagl.PLView;
import com.panoramagl.PLViewListener;
import com.panoramagl.enumerations.PLCameraAnimationType;
import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.structs.PLRect;
import com.panoramagl.utils.PLUtils;
import com.tencent.imsdk.TIMManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;

import java.math.BigDecimal;

public class MainActivity extends PLView {
    /**
     * member variables
     */

    private Spinner mPanoramaTypeSpinner;
    private ZoomControls mZoomControls;
    /**init methods*/

    PLHotspot plHotspot1 = null;
    PLHotspot plHotspot2 = null;
    PLHotspot plHotspot3 = null;

    private IMFragment imFragment;

    private Handler mHandler;

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListener(new PLViewListener() {


        });

        loadPanorama(1);
    }

    /**
     * This event is fired when root content view is created
     *
     * @param contentView current root content view
     * @return root content view that Activity will use
     */
    ViewGroup mainView;
    @Override
    protected View onContentViewCreated(View contentView) {
        mainView = (ViewGroup) getLayoutInflater().inflate(R.layout.playout, null);

        imFragment = new IMFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Keys.KEY_CHAT_INFO,
                getIntent().getSerializableExtra(Keys.KEY_CHAT_INFO));
        imFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_content, imFragment).commitAllowingStateLoss();

        //Add 360 view
        mainView.addView(contentView, 0);
        return super.onContentViewCreated(mainView);
    }

    /**
     * utility methods
     */
    private void setControlsEnabled(final boolean isEnabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mPanoramaTypeSpinner != null) {
                    mPanoramaTypeSpinner.setEnabled(isEnabled);
                    mZoomControls.setIsZoomInEnabled(isEnabled);
                    mZoomControls.setIsZoomOutEnabled(isEnabled);
                }
            }
        });
    }

    @SuppressWarnings("unused")
    private void loadPanorama(int index) {
        try {
            Context context = getApplicationContext();
            PLIPanorama panorama = null;
            setLocked(true);
            switch (index) {
                case 0:
                    break;
                case 1:
                    panorama = new PLSphericalPanorama();
                    ((PLSphericalPanorama) panorama).setImage(new PLImage(PLUtils.getBitmap(context, R.raw.bbb), false));
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
            if (panorama != null) {
                PLHotspot finalPlHotspot = plHotspot1;
                panorama.getCamera().setOnRotateListener(new PLCameraListener() {
                    @Override
                    public void didBeginAnimation(Object sender, PLICamera camera,
                            PLCameraAnimationType type) {

                    }

                    @Override
                    public void didEndAnimation(Object sender, PLICamera camera,
                            PLCameraAnimationType type) {

                    }

                    @Override
                    public void didLookAt(Object sender, PLICamera camera, float pitch, float yaw
                            , boolean animated) {

                    }

                    @Override
                    public void didRotate(Object sender, PLICamera camera, float pitch, float yaw
                            , float roll) {
                        Log.d("tog",
                                "didRotate yaw =" + yaw + "....roll =" + roll + "....pitch=" + "." +
                                        ".." + pitch);
                        PLRect r = plHotspot1.getRect();
                        Log.d("tog rect",
                                "didRotate camera r yaw =" + yaw + "....r roll =" + roll + ".." +
                                        ".pitch =" + pitch);
                        Log.d("tog rect", "didRotate control r leftTop =" + r.leftTop.x + "....r " +
                                "leftTop.x =" + r.leftTop.y);

                        if (Math.abs(plHotspot1.getAth() - yaw) < 30) {
                            Log.d("tog rect", "第一在显示区域中");
                            if ("false".equals(plHotspot1.getOnClick())) {
                                Log.d("tog rect", "第一开启动画start");
                                try {
                                    start(plHotspot1, true, 0.10, 0.03);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.d("tog rect", "第一开启动画end");
                            }
                        } else {
                            if ("true".equals(plHotspot1.getOnClick())) {
                                Log.d("tog rect", "第一关闭动画start");
                                try {
                                    start(plHotspot1, false, 0.10, 0.03);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.d("tog rect", "第一关闭动画");
                            }
                        }


                        if (Math.abs(plHotspot2.getAth() - (yaw)) < 30) {
                            if ("false".equals(plHotspot2.getOnClick())) {
                                try {
                                    start(plHotspot2, true, 0.14, 0.03);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if ("true".equals(plHotspot2.getOnClick())) {
                                try {
                                    start(plHotspot2, false,0.14,0.03);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (Math.abs(plHotspot3.getAth() - (yaw)) < 30) {
                            Log.d("tog rect", "第三在显示区域中");
                            if ("false".equals(plHotspot3.getOnClick())) {
                                try {
                                    start(plHotspot3, true, 0.14, 0.03);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if ("true".equals(plHotspot3.getOnClick())) {
                                try {
                                    start(plHotspot3, false, 0.14, 0.03);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void didFov(Object sender, PLICamera camera, float fov,
                            boolean animated) {

                    }

                    @Override
                    public void didReset(Object sender, PLICamera camera) {

                    }
                });

                float sacneWidth  = 2500 - 2048;
                float sacneHeight  = 1250 - 1024;

                float hot1Width = 165;
                float hot1Height= 46;
                float hot1X = 568  - 50;
                float hot1Y =623 -118;
                float ath_x = BigDecimal.valueOf(((hot1X / 2048) - 0.5) * 360).floatValue();
                float atv_y = BigDecimal.valueOf(((hot1Y / 1024) - 0.5) *  360).floatValue();

                float hot2Width = 237;
                float hot2Height= 46;
                float hot2X =  1042 - (237 / 2)+12;
                float hot2Y = 759-308;
                float ath2_x = BigDecimal.valueOf(((hot2X / 2048) - 0.5) * 360).floatValue();
                float atv2_y =  BigDecimal.valueOf(((hot2Y / 1024) - 0.5) *  360 ).floatValue();

                float hot3Width = 237;
                float hot3Height= 46;
                float hot3X = 1765  - ((hot3Width ) )+8;
                float hot3Y = 602 - 93;
                float ath3_x = BigDecimal.valueOf(((hot3X / 2048) - 0.5) * 360).floatValue();
                float atv3_y = BigDecimal.valueOf(((hot3Y / 1024) - 0.5) *  360 ).floatValue();

                PLImage image1 = new PLImage(PLUtils.getBitmap(context, R.raw.hot1), false);
                plHotspot1 = new PLHotspot(1, image1, atv_y, ath_x, 0.10f, 0.03f);
                plHotspot1.setOnClick("false");
                panorama.addHotspot(plHotspot1);


                PLImage image2 = new PLImage(PLUtils.getBitmap(context, R.raw.hot1), false);
                plHotspot2 = new PLHotspot(1, image2, atv2_y, ath2_x, 0.14f, 0.03f);
                plHotspot2.setOnClick("true");
                panorama.addHotspot(plHotspot2);

                PLImage image3 = new PLImage(PLUtils.getBitmap(context, R.raw.hot1), false);
                plHotspot3 = new PLHotspot(1, image3, atv3_y, ath3_x, 0.14f, 0.03f);
                plHotspot3.setOnClick("false");
                panorama.addHotspot(plHotspot3);

                plHotspot1.setAlpha(0);
                plHotspot3.setAlpha(0);
                reset();
                panorama.getCamera().setPitchMax(360f);
                panorama.getCamera().setPitchMin(-360f);
                panorama.getCamera().setRollMax(360);
                panorama.getCamera().setRollMin(-360);
                panorama.getCamera().setPitchEnabled(true);
                panorama.getCamera().setRollEnabled(true);
                panorama.getCamera().lookAt(0f, 0f);
                setPanorama(panorama);
            }
            setInertiaEnabled(true);
            setLocked(false);
        } catch (Throwable e) {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View view = getCurrentFocus();
            hideSoftInput(view);
        }
        return super.dispatchTouchEvent(ev);
    }

    private int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private void start(PLHotspot host, boolean isVisible) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_MOVE) {
            hideSoftInput(mainView);
            imFragment.setVisibility(View.GONE);
        } else if (action == MotionEvent.ACTION_UP) {
            imFragment.setVisibility(View.VISIBLE);
        }
        return super.onTouchEvent(event);
    }

    public void hideSoftInput(View view) {
        if (view != null && view.getWindowToken() != null) {
            InputMethodManager manager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void start(final PLHotspot host, boolean isVisible,final double width, final double height) throws InterruptedException {
        if(isVisible){
            host.setOnClick("true");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i < 20; i ++){
                        Log.d("tog rect", "+++++ start");
                        if(i == 0){
                            host.setWidth(0.0001f);
                            host.setHeight(0.0001f);
                            host.setAlpha(1);
                        }
                        Log.d("tog rect", "+++++ start alpha ="+host.getAlpha());
                        host.setWidth((float) (host.getWidth() + (width * 1 / 20)));
//                        System.out.println("```````````host.getWidth() + (width * 1 / 20)="+host.getWidth() + (width * 1 / 20));
                        host.setHeight((float) (host.getHeight() + (height * 0.05)));
                        if(i == 19){
                            host.setWidth((float) width);
//                            System.out.println("`````````19 width="+width);
                            host.setHeight((float) height);
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            host.setOnClick("false");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i < 20; i++){
                        Log.d("tog rect", "---- start");
                        if(i == 0){
                            host.setWidth((float) width);
                            host.setHeight((float) height);
                        }
                        host.setWidth((float) (host.getWidth() - (width* 0.05)));
                        host.setHeight((float) (host.getHeight() - (height  * 0.05)));
                        if(i == 19){
                            host.setWidth(0.0001f);
                            host.setHeight(0.0001f);
                            Log.d("tog rect", "第一关闭动画start");
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        TIMManager.getInstance().logout(null);
        super.onDestroy();

    }
}
