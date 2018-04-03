package com.taowei.snatchnews.utils;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

/**
 * Created by 杨学强 on 2018/3/28.
 * Email:326772928@qq.com
 */

public class Util {


    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay()
                .getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取屏幕density
     */
    public static float getScreenDensity(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay()
                .getMetrics(outMetrics);
        return outMetrics.density;
    }
    /**
     * 解锁屏幕、强制点亮屏幕
     * <p>
     * <pre>
     * 	标记值						CPU		屏幕				键盘
     * 	PARTIAL_WAKE_LOCK			开启		关闭				关闭
     * 	SCREEN_DIM_WAKE_LOCK		开启		调暗（Dim）		关闭
     * 	SCREEN_BRIGHT_WAKE_LOCK		开启		调亮（Bright）	关闭
     * 	FULL_WAKE_LOCK				开启		调亮（Bright）	调亮（Bright）
     *  </pre>
     */
    public static void acquireWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "SimpleTimer");
            if (!wl.isHeld()) {
                wl.acquire();
                wl.release();
            }
        }
    }

    /**
     * 禁用键盘锁 、解锁键盘
     */
    public static KeyguardManager.KeyguardLock disableKeylock(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock mKeyguardLock = null;
        Log.i("TAG",mKeyguardManager.isKeyguardLocked()+"当前锁屏状态");

        if (mKeyguardManager.isKeyguardLocked()) {
            mKeyguardLock = mKeyguardManager.newKeyguardLock("unLock");

            mKeyguardLock.disableKeyguard();
        }
        return mKeyguardLock;

    }


}
