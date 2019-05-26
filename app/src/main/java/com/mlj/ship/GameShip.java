package com.mlj.ship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.WindowManager;


/*
    战舰类：定义战舰的各种属性，及绘制战舰的接口.
 */
public class GameShip {

    //战舰基本属性
    private static final String TAG="mlj";
    //初始位置和高度，宽度
    private int beginX ;
    private int beginY ;
    private int width ;
    private int height;
    //运行标志
    private boolean isRunning = false;
    private int scrwidth,scrheight;

    private int bombNum  = 5;  //炸弹数量
    private Bitmap bitmap;  //图片对象

    //初始化
    public GameShip(Context context){
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        scrwidth = wm.getDefaultDisplay().getWidth();
        scrheight = wm.getDefaultDisplay().getHeight();
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        beginX = (scrwidth - getWidth()) / 2;
        beginY =  420 - getHeight();
    }

    //绘制
    public void drawShip(Canvas canvas) {
        canvas.drawBitmap(bitmap,beginX,beginY,null);
    }

    //右移
    public void moveRight() {
        if(isRunning){
            this.beginX +=20;
        }
        if((this.beginX + this.width) > scrwidth){
            this.beginX = scrwidth - this.width;
        }
    }
    //左移
    public void moveLeft() {
        if(isRunning){
            this.beginX -= 20;
        }
        if(beginX < 0){
            beginX = 0;
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBombNum() {
        return bombNum;
    }

    public void setBombNum(int bombNum) {
        this.bombNum = bombNum;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public int getBeginX() {
        return beginX;
    }

    public void setBeginX(int beginX) {
        this.beginX = beginX;
    }

    public int getBeginY() {
        return beginY;
    }

    public void setBeginY(int beginY) {
        this.beginY = beginY;
    }
}
