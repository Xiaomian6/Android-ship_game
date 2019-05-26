package com.mlj.ship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

/*
    游戏界面类：包含控制绘制战舰，潜艇，爆炸，鱼雷，炸弹的接口毁掉，以及整个游戏背景的绘制
 */

public class GameView extends View    {

    //基础信息
    private static final String TAG="mlj";
    private Paint paint;
    private int width,height;
    int[] colors;
    private int score=0;
    private int pass=1;
    private int liveNum=3;
    private GameShip ship = null;
    private ArrayList<Bumb> bumbArray = new ArrayList<Bumb>();
    private ArrayList<Blast> blastArray = new ArrayList<Blast>();
    private ArrayList<Torpedo> torpedoArray = new ArrayList<Torpedo>();
    private ArrayList<Submarine> submarineArray = new ArrayList<Submarine>();
    private ArrayList<Hit> hitArray = new ArrayList<Hit>();

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        paint = new Paint(); //创建Paint
        paint.setAntiAlias(true); //设置抗锯齿效果
        ship = new GameShip(context);
        Log.d(TAG, "GameView2: "+ship.getBeginX());
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "GameView3: ");
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制天空
        Rect say = new Rect(0,0,width,420);
        colors = new int[]{Color.parseColor("#FF5ED6C2"),Color.parseColor("#FFC1EFE8"),Color.WHITE};
        GradientDrawable mDrawable1 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors);
        mDrawable1.setShape(GradientDrawable.RECTANGLE);
        mDrawable1.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawable1.setBounds(say);
        mDrawable1.draw(canvas);
        //绘制海洋
        Rect sea = new Rect(0,420,width,height);
        colors = new int[]{Color.parseColor("#FF3C60D5"),Color.BLUE};
        GradientDrawable mDrawable2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors);
        mDrawable2.setShape(GradientDrawable.RECTANGLE);
        mDrawable2.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawable2.setBounds(sea);
        mDrawable2.draw(canvas);
        //绘制数字
        paint.setColor(Color.YELLOW);
        paint.setTextSize(100f);
        //285-255
        canvas.drawText(String.valueOf(score),305,155,paint);
        canvas.drawText(String.valueOf(pass),675,155,paint);
        canvas.drawText(String.valueOf(liveNum),1050,155,paint);
        //绘制玩家
        ship.drawShip(canvas);
        drawBombNum(canvas);
        /*
            游戏触发事件检测
         */
        //炸弹
        if(!bumbArray.isEmpty()){
            for(int i = 0;i < bumbArray.size();i++){
                if(!bumbArray.get(i).flag){
                    bumbArray.get(i).drawBumb(canvas);
                }
                if(bumbArray.get(i).flag){
                    bumbArray.remove(i);
                }
            }
        }
        //爆炸
        if(!blastArray.isEmpty()) {
            for(int i = 0; i < blastArray.size(); i++)
            {
                if(!blastArray.get(i).isFlag()) {
                    blastArray.get(i).drawBlast(canvas);
                }
                if(blastArray.get(i).isFlag()) {
                    blastArray.remove(i);
                }
            }
        }
        //鱼雷
        if(!torpedoArray.isEmpty()){
            for(int i = 0; i < torpedoArray.size(); i ++) {
                if(!torpedoArray.get(i).isFlag()) {
                    torpedoArray.get(i).drawTorpedo(canvas);
                }
                if(torpedoArray.get(i).isFlag()) {
                    torpedoArray.remove(i);
                }
            }
        }
        //潜艇
        if(!submarineArray.isEmpty()){
            for(int i = 0; i < submarineArray.size(); i ++) {
                if(!submarineArray.get(i).isFlag()) {
                    submarineArray.get(i).drawSubmarine(canvas);
                }
                if(submarineArray.get(i).isFlag()) {
                    submarineArray.remove(i);
                }
            }
        }
        //有效攻击
        if(!hitArray.isEmpty()){
            for(int i = 0; i < hitArray.size(); i ++) {
                if(!hitArray.get(i).isRunning()) {
                    hitArray.get(i).drawHitting(canvas);
                }
                if(hitArray.get(i).isRunning()) {
                    hitArray.remove(i);
                }
            }
        }
    }

    //绘制指定数量炸弹
    public void drawBombNum(Canvas canvas) {
        Bitmap bomb= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bomb);
        for(int i = 0;i < ship.getBombNum();i ++) {
            canvas.drawBitmap(bomb,width/2+100 + 50*i + 70, 75,null);
        }
    }

    public GameShip getShip() {
        return ship;
    }
    public void setShip(GameShip ship) {
        this.ship = ship;
    }
    public ArrayList<Bumb> getBumbArray() {
        return bumbArray;
    }
    public void setBumbArray(ArrayList<Bumb> bumbArray) {
        this.bumbArray = bumbArray;
    }
    public ArrayList<Blast> getBlastArray() {
        return blastArray;
    }
    public void setBlastArray(ArrayList<Blast> blastArray) {
        this.blastArray = blastArray;
    }
    public ArrayList<Torpedo> getTorpedoArray() {
        return torpedoArray;
    }
    public void setTorpedoArray(ArrayList<Torpedo> torpedoArray) {
        this.torpedoArray = torpedoArray;
    }
    public ArrayList<Submarine> getSubmarineArray() {
        return submarineArray;
    }
    public void setSubmarineArray(ArrayList<Submarine> submarineArray) {
        this.submarineArray = submarineArray;
    }
    public ArrayList<Hit> getHitArray() {
        return hitArray;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getPass() {
        return pass;
    }
    public void setPass(int pass) {
        this.pass = pass;
    }
    public void setHitArray(ArrayList<Hit> hitArray) {
        this.hitArray = hitArray;
    }
    public int getLiveNum() {
        return liveNum;
    }
    public void setLiveNum(int liveNum) {
        this.liveNum = liveNum;
    }
}
