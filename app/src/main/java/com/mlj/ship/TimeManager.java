package com.mlj.ship;

import java.util.Random;

/*
    玩家战舰移动线程
 */
public class TimeManager implements Runnable {

    private GameShip ship;
    private int speed = 1000;
    private Submarine submarine;
    private GameActivity activity;
    private GameView gameView;

    public TimeManager(GameShip ship, GameActivity activity, Submarine submarine,GameView gameView){
        this.ship=ship;;
        this.submarine = submarine;
        this.activity = activity;
        this.gameView = gameView;
    }

    @Override
    public void run() {
        Random r = new Random();

        while(activity.isRunning()) {
            if(!ship.isRunning()){
                synchronized(GameActivity.subLock) {
                    try {
                        GameActivity.subLock.wait();
                    }
                    catch(Exception e) {
                        e.printStackTrace();
//                        panel.endGame();
                    }
                }
            }
            Submarine sm = new Submarine(activity,ship,gameView);
            gameView.getSubmarineArray().add(sm);
            Thread t = new Thread(sm);
            t.start();

            try {
//				System.out.println("qqq");
                Thread.sleep(this.speed + r.nextInt(this.speed * 3));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

    }

    public int getSpeed(){
        return speed;
    }
    public void setSpeed(int speed){
        this.speed=speed;
    }
}
