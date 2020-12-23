package com.chxip.musicview;

import android.content.Context;
import android.view.WindowManager;

/**
 * @ClassName: Util
 * @Description: java类作用描述
 * @Author: chxip
 * @CreateDate: 2020/12/22 6:09 PM
 */
public class Util {
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int dp2px(float dpValue,Context context) {
        float scale =context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
