package com.chxip.musicview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @ClassName: PlayView
 * @Description: java类作用描述
 * @Author: chxip
 * @CreateDate: 2020/12/22 5:54 PM
 */
public class PlayView extends View {
    private static final float NEEDLE_ROTATION_PLAY = 0.0f;//播放时指针的旋转角度
    private static final float NEEDLE_ROTATION_PAUSE = -25.0f;//暂停时指针的旋转角度

    private static final long TIME_UPDATE = 50;
    private MyHandler myHandler = new MyHandler();

    private float discRotation = 0.0f;//唱片图片和中心歌曲海报旋转的角度，
    private static final float DISC_ROTATION_INCREASE = 0.5f;//每次旋转的角度

    private Bitmap discBitmap;//绘制唱片图片
    private Matrix discMatrix = new Matrix();//唱片图片的Matrix对象,此处使用Matrix，方便后面做旋转功能
    private Point discPoint = new Point();//唱片图片的位置
    private Point discCenterPoint = new Point();//唱片图片的旋转中心角度

    private Bitmap coverBitmap;//歌曲封面图片
    private Matrix coverMatrix = new Matrix();//封面图片的Matrix
    private Point coverPoint = new Point();//封面图片的位置

    private Bitmap needleBitmap;//指针图片
    private Matrix needleMatrix = new Matrix();//指针图片的Matrix
    private Point needlePoint = new Point();//指针图片的位置
    private float needleRotation = NEEDLE_ROTATION_PAUSE;//指针旋转的角度，
    private Point needleCenterPoint = new Point();//指针旋转中心角度
    private ValueAnimator needleAnimator;//播放时，指针旋转的属性动画

    private boolean isPlaying = false;//是否正在播放

    public PlayView(Context context) {
        super(context);
        init();
    }

    public PlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initSize();//初始化位置
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //图片旋转
        discMatrix.setRotate(discRotation, discCenterPoint.x, discCenterPoint.y);
        //图片位置
        discMatrix.preTranslate(discPoint.x, discPoint.y);
        //绘制唱片图片
        canvas.drawBitmap(discBitmap, discMatrix, null);

        //图片旋转
        coverMatrix.setRotate(discRotation, discCenterPoint.x, discCenterPoint.y);
        //图片位置
        coverMatrix.preTranslate(coverPoint.x, coverPoint.y);
        //绘制封面图片
        canvas.drawBitmap(coverBitmap, coverMatrix, null);

        //指针图片
        needleMatrix.setRotate(needleRotation, needleCenterPoint.x, needleCenterPoint.y);
        needleMatrix.preTranslate(needlePoint.x, needlePoint.y);
        canvas.drawBitmap(needleBitmap, needleMatrix, null);
    }


    /**
     * 初始化
     */
    private void init() {
        //获取唱片图片Bitmap
        discBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.play_page_disc);
        //转换图片大小
        discBitmap = ImageUtils.resizeImage(discBitmap, (int) (Util.getScreenWidth(getContext()) * 0.75),
                (int) (Util.getScreenWidth(getContext()) * 0.75));

        //获取封面图片Bitmap
        coverBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.cover);
        //转换图片大小
        coverBitmap = ImageUtils.resizeImage(coverBitmap, Util.getScreenWidth(getContext()) / 2,
                Util.getScreenWidth(getContext()) / 2);
        //将图片剪裁为圆形
        coverBitmap = ImageUtils.createCircleImage(coverBitmap);

        //获取指针图片Bitmap
        needleBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.play_page_needle);
        //转换图片大小
        needleBitmap = ImageUtils.resizeImage(needleBitmap, (int) (Util.getScreenWidth(getContext()) * 0.25),
                (int) (Util.getScreenWidth(getContext()) * 0.375));

        //初始化指针旋转动画
        needleAnimator = ValueAnimator.ofFloat(NEEDLE_ROTATION_PAUSE, NEEDLE_ROTATION_PLAY);
        needleAnimator.setDuration(500);
        needleAnimator.addUpdateListener(animatorUpdateListener);
    }

    /**
     * 属性动画变化监听器
     */
    ValueAnimator.AnimatorUpdateListener animatorUpdateListener=new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if(isPlaying){//判断是否正在播放
                needleRotation = (float) valueAnimator.getAnimatedValue();
            }else{
                needleRotation = - ((float) valueAnimator.getAnimatedValue() + 25);
            }
            invalidate();
        }
    };

    /**
     * 初始化位置
     */
    private void initSize() {
        int discOffsetY = needleBitmap.getHeight() / 2 + 20;
        //唱片图片的位置
        discPoint.x = (getWidth() - discBitmap.getWidth()) / 2;
        discPoint.y = discOffsetY;
        //唱片图片的旋转中心角度
        discCenterPoint.x = getWidth() / 2;
        discCenterPoint.y = discOffsetY + discBitmap.getHeight() / 2;

        //封面图片的位置
        coverPoint.x = (getWidth() - coverBitmap.getWidth()) / 2;
        coverPoint.y = discOffsetY + (discBitmap.getHeight() - coverBitmap.getHeight()) / 2;

        //指针图片的位置
        needlePoint.x = (getWidth() - needleBitmap.getWidth()) / 2 +100;
        //指针旋转的角度
        needleCenterPoint.x =  needlePoint.x;


    }

    /**
     * 开始播放
     */
    public void startPlay() {
        if (isPlaying) {
            return;
        }
        isPlaying = true;
        //开始动画
        needleAnimator.start();
        //启动定时器，执行旋转动画
        myHandler.post(rotationRunnable);
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (!isPlaying) {
            return;
        }
        isPlaying = false;
        //开始动画
        needleAnimator.start();
        myHandler.removeCallbacks(rotationRunnable);
    }

    /**
     * 创建一个定时任务，定时改变图片的选择角度
     */
    private Runnable rotationRunnable = new Runnable() {
        @Override
        public void run() {
            discRotation += DISC_ROTATION_INCREASE;
            if (discRotation >= 360) {
                discRotation = 0;
            }
            //通知页面重绘
            invalidate();
            myHandler.postDelayed(this, TIME_UPDATE);
        }
    };

    private class MyHandler extends Handler {

    }
}
