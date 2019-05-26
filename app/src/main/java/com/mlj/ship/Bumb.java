package com.mlj.ship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import android.view.WindowManager;
/*
    战舰发射的炸弹
*/
public class Bumb implements Runnable{

    //炸弹基本信息
    private GameShip ship;
    private int  beginX;
    private int  beginY;
    private int  width = 5;
    private int  height = 15;
    public  boolean flag = false;
    private Bitmap bitmap;
    private int srcWidth,srcHeight;
    private GameView gameView;

    //生成炸弹
    public Bumb(Context context, GameShip ship,GameView gameView){
        //取得窗口句柄
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        srcWidth = wm.getDefaultDisplay().getWidth();
        srcHeight = wm.getDefaultDisplay().getHeight();
        this.beginX= ship.getBeginX()+100;
        this.beginY = ship.getBeginY()+20;
        this.ship = ship;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.gameView = gameView;
    }

    //绘制炸弹线程
    @Override
    public void run() {
        while (!flag){
            //绘制向下运动
            this.moveDown();
            if(!ship.isRunning()){
                //锁定线程
                synchronized(GameActivity.subLock) {
                    try {
                        //将游戏线程加入等待队列
                        GameActivity.subLock.wait();
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        this.flag = true;
                    }
                }
            }
            try {
                Thread.sleep(10);
            }
            catch(Exception event) {
                event.printStackTrace();
                this.flag = true;
            }
        }
    }

    //绘制炸弹
    public void drawBumb(Canvas canvas) {
        canvas.drawBitmap(bitmap,beginX,beginY,null);
    }

    //计算炸弹下落位置
    public void moveDown(){
        this.beginY += 3;
        if(this.beginY > (srcHeight - this.height / 2)) {
            flag = true;
        }
        gameView.postInvalidate();
    }


    public GameShip getShip() {
        return ship;
    }
    public void setShip(GameShip ship) {
        this.ship = ship;
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
}
