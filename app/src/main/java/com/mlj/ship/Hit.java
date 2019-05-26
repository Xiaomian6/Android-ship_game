package com.mlj.ship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/*
    绘制爆炸效果
 */
public class Hit implements Runnable {

    //基本信息
    private Bitmap bitmap;
    private int liveTime = 500;
    private int beginX = 0;
    private int beginY = 0;
    private boolean isRunning = false;

    //初始化
    public Hit(int x, int y, Context context) {
        this.beginX = x;
        this.beginY = y;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.k);
    }

    //绘制
    public void drawHitting(Canvas canvas) {
        canvas.drawBitmap(bitmap,beginX,beginY,null);
    }

    //绘制线程
    @Override
    public void run() {
        while(!this.isRunning) {
            try {
                Thread.sleep(this.liveTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.isRunning = true;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}
