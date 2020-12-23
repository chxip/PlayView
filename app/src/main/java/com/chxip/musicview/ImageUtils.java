package com.chxip.musicview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.WindowManager;

/**
 * @ClassName: ImageUtils
 * @Description: 图像工具类
 * @Author: chxip
 * @CreateDate: 2020/12/22 6:08 PM
 */
public class ImageUtils {


    /**
     * 将图片放大或缩小到指定尺寸
     */
    public static Bitmap resizeImage(Bitmap source, int dstWidth, int dstHeight) {
        if (source == null) {
            return null;
        }

        return Bitmap.createScaledBitmap(source, dstWidth, dstHeight, true);
    }

    /**
     * 将图片剪裁为圆形
     */
    public static Bitmap createCircleImage(Bitmap source) {
        if (source == null) {
            return null;
        }

        int length = Math.min(source.getWidth(), source.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(source.getWidth() / 2, source.getHeight() / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
}
