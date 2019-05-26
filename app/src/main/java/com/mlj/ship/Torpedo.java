package com.mlj.ship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/*
 .鱼雷类：定义鱼雷基础信息，及攻击有效检测接口
 */
public class Torpedo implements Runnable {

    //基础信息
    private Bitmap bitmap;
    private int X;
    private int Y;
    private int weight;
    private int height;
    //外部对象
    private Submarine submarine;
    private boolean flag = false;
    private GameShip ship;
    private Blast blast;
    private GameView gameView;
    private GameActivity activity;

    //初始化
    public Torpedo(GameActivity activity, Submarine submarine, GameShip ship,GameView gameView){
        this.submarine = submarine;
        this.ship = ship;
        this.gameView =gameView;
        this.activity = activity;
        blast = new Blast(this.ship.getBeginX(),this.ship.getBeginY(),activity);
        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.torpedo);
        weight = bitmap.getWidth();
        height = bitmap.getHeight();
        X = this.submarine.getX() + weight/ 2;
        Y = this.submarine.getY();
    }

    //绘制
    public void drawTorpedo(Canvas canvas)
    {
        canvas.drawBitmap(bitmap,X,Y,null);
    }

    //向上移动
    public void upMove() {
        this.Y -= 3;
        if(this.Y <= 420) {
            this.hitting();
            this.flag = true;
        }
        if(this.submarine.isFlag()) {
            if(this.Y < 420) {
                this.flag = true;
            }
        }
    }

    //攻击检测
    public void hitting() {
        //有效判定
        if(this.X > (this.ship.getBeginX() - this.weight) && this.X < (this.ship.getBeginX() + this.ship.getWidth()-this.getWeight())) {
            //获取生命值
            int num = gameView.getLiveNum();
            //添加爆炸对象
            Thread t = new Thread(blast);
            gameView.getBlastArray().add(blast);
            t.start();
            //更新生命值
            num --;
            gameView.setLiveNum(num);
            activity.loseGmae();
        }
    }

    //绘制线程
    @Override
    public void run() {
        while(!flag) {
            this.upMove();
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

    private int getWeight() {
        return weight;
    }
    public boolean isFlag(){
        return flag;
    }
    public void setFlag(boolean flag){
        this.flag = flag;
    }
}
