package com.mlj.ship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
/*
     潜艇发射的鱼雷击中战舰后爆炸的效果对象,原理：绘制多个从小到大然后从大到小的圆显示爆炸效果
 */
public class Blast implements Runnable
{
    //基础坐标信息
    private int X;
    private int Y;
    private boolean flag = false;
    private Bitmap bitmap;
    private int beginX   =  0; //
    private int beginY   =  0;

    //生成鱼雷
    public Blast(int x, int y, Context context)
    {
        this.X = x;
        this.Y = y;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.k);
    }


    //绘制鱼雷

    public void drawBlast(Canvas canvas)
    {
        canvas.drawBitmap(bitmap,X,Y,null);
    }



    public void run()
    {
        while(!this.flag)
        {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.flag = true;
        }

    }



    public int getX() {
        return X;
    }



    public void setX(int x) {
        X = x;
    }



    public int getY() {
        return Y;
    }



    public void setY(int y) {
        Y = y;
    }



    public boolean isFlag() {
        return flag;
    }



    public void setFlag(boolean flag) {
        this.flag = flag;
    }


}
