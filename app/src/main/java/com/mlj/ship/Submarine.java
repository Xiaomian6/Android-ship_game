package com.mlj.ship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.WindowManager;
import java.util.Random;

/*
    潜艇类：定义潜艇的各种基本属性，以及刷新肌接口和绘制接口
 */
public class Submarine implements Runnable{

    //基本信息
    private int X;
    private int Y;
    private int m;  //方向:0代表向左 1代表向右
    private int weight = 65;
    private int height = 20;
    //玩家角色
    private GameShip ship;
    //运行标记
    private boolean flag = false;
    //皮肤
    private Bitmap bitmap;
    private int srcWidth,srcHeight;
    //窗口
    private GameView gameView;

    //初始化
    public Submarine(GameActivity activity, GameShip ship,GameView gameView) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        srcWidth = wm.getDefaultDisplay().getWidth();
        srcHeight = wm.getDefaultDisplay().getHeight();
        this.ship = ship;
        this.gameView = gameView;
        //随机刷新
        this.m = (int) (Math.random() * 2);
        if (this.m == 0) {
            Random r = new Random();
            int num = r.nextInt(3);
            if (num == 0) {
                bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.submarine1);
            } else if (num == 1) {
                bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.submarine2);
            } else if (num == 2) {
                bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.submarine8);
            }
        }
        if(this.m == 1) {
            Random r1 = new Random();
            int  num = r1.nextInt(4);
            if(num == 0 ) {
                bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.submarine3);
            }
            else if(num == 1 ) {
                bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.submarine4);
            }
            else if(num == 2 ) {
                bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.submarine6);
            }
            else if(num == 3 ) {
                bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.submarine7);
            }
        }

        this.weight =  bitmap.getWidth();
        this.height =  bitmap.getHeight();

        if(m == 0) {
            this.X = srcWidth - this.weight;
        }
        if(m == 1) {
            this.X = 0;
        }

        Random ry = new Random();
        int y1 = ry.nextInt(srcHeight) + 420;

        while((y1+this.getHeight())  >= srcHeight)
        {
            y1 = ry.nextInt(srcHeight) + 420;

        }
        this.Y = y1;
        TimeManager2 t2 = new TimeManager2(ship,Submarine.this,activity,gameView);
        Thread t = new Thread(t2);
        t.start();
    }

    //绘制
    public void drawSubmarine(Canvas canvas)
    {
        canvas.drawBitmap(bitmap,X,Y,null);
    }

    //左移
    public void moveLeft() {
        this.X -= 4;
        gameView.postInvalidate();
        if(this.X < 0) {
            this.flag = true;
        }
    }
    //右移
    public void moveright() {
        this.X += 4;
        gameView.postInvalidate();
        if(this.X > srcWidth) {
            this.flag = true;
        }
    }

    //绘制线程
    @Override
    public void run() {
        while(!flag) {
            if(this.m == 0) {
                this.moveLeft();
            }
            if(this.m == 1) {
                this.moveright();
            }
            if(!ship.isRunning()) {
                synchronized(GameActivity.subLock) {
                    try {
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
            catch(Exception e) {
                e.printStackTrace();
                this.flag = true;
            }
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

    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
