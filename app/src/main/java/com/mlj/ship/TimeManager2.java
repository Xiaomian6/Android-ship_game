package com.mlj.ship;

import android.content.Context;

import java.util.Random;

/*
    默认更新线程
 */
public class TimeManager2 implements Runnable {

    private GameShip ship;
    private Submarine submarine;
    private GameView gameView;
    private GameActivity activity;

    public TimeManager2(GameShip ship, Submarine submarine, GameActivity activity,GameView gameView){
        this.gameView = gameView;
        this.ship=ship;
        this.submarine = submarine;
        this.activity = activity;
    }

    @Override
    public void run() {
        Random r = new Random();
        while(!this.submarine.isFlag())
        {
            if(!ship.isRunning()) {
                synchronized(GameActivity.subLock) {
                    try {
                        GameActivity.subLock.wait();
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        //this.flag = true;
//                        this.panel.endGame();
                    }
                }
            }
            Torpedo torpedo =new Torpedo(activity,submarine,ship,gameView);
            gameView.getTorpedoArray().add(torpedo);
            Thread t = new Thread(torpedo);
            t.start();
            try {
                int time = r.nextInt(4000) + 2000;
                Thread.sleep(time);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
