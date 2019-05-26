package com.mlj.ship;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/*
  游戏窗口
 */

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="mlj";
    private TextView gameText,exitText,scoreText,liveText,passText;
    private Button moveLeft,moveRight,shoot;
    private GameView gameView;
    private ArrayList<Bumb> bumbArray = new ArrayList<Bumb>();
    private boolean isRunning = false;
    public static final Object subLock = new Object();
    private Timer timer,timer1;
    private TimeManager tm1;
    private Submarine submarine;
    private int hitX = 0;
    private int hitY = 0;

    //生成
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_view);
        findView();
        init();
        gameText.setOnClickListener(this);
        exitText.setOnClickListener(this);
        moveLeft.setOnClickListener(this);
        moveRight.setOnClickListener(this);
        shoot.setOnClickListener(this);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                addBomb();
            }
        }, 2000, 2000);
        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                expoled();
            }
        },0,10);
    }
    //获得窗口
    private void findView(){
        gameText = (TextView)findViewById(R.id.pause_text);
        exitText = (TextView)findViewById(R.id.exit_text);
        moveLeft = (Button)findViewById(R.id.left_btn);
        moveRight = (Button)findViewById(R.id.right_btn);
        shoot = (Button)findViewById(R.id.shoot_btn);
        gameView = (GameView)findViewById(R.id.game_view);
    }
    //初始化
    private void init(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    //炸弹回复
    public void addBomb(){
        int bombnum = gameView.getShip().getBombNum();
        if(bombnum < 5 && bombnum >= 0){
            bombnum ++;
            gameView.getShip().setBombNum(bombnum);
            gameView.postInvalidate();
        }
        if(bombnum > 5){
            gameView.getShip().setBombNum(5);
        }
        if (bombnum < 0){
            gameView.getShip().setBombNum(0);
        }
    }
    //按键事件检测
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pause_text:
                if(gameText.getText().equals("开始")){
                    startGame();
                }else{
                    gameView.getShip().setRunning(false);
                    showPauseDialog();
                }
                break;
            case R.id.exit_text:
                System.exit(0);
                break;
            case R.id.left_btn:
                gameView.getShip().moveLeft();
                gameView.invalidate();
                break;
            case R.id.right_btn:
                gameView.getShip().moveRight();
                gameView.invalidate();
                break;
            case R.id.shoot_btn:
                int bombNun = gameView.getShip().getBombNum();
                if(isRunning){
                    if( bombNun> 0 && bombNun <= 5) {
                        Bumb bumb = new Bumb(GameActivity.this,gameView.getShip(),gameView);
                        bumbArray.add(bumb);
                        gameView.setBumbArray(bumbArray);
                        Thread t1 =  new Thread(bumb);
                        t1.start();
                        bombNun -- ;
                        gameView.getShip().setBombNum(bombNun);
                    }
                }
                break;
        }
    }
    //游戏运行
    private void startGame(){
        //初始未点击开始按钮状态
        if(isRunning){
            isRunning = false;
            gameView.setLiveNum(3);
            gameView.setPass(1);
            gameView.setScore(0);
            tm1.setSpeed(1000);
            gameView.getShip().setBombNum(5);
            isRunning = true;
            gameView.getShip().setRunning(true);
            for(int i = 0; i < gameView.getBumbArray().size(); i++)
            {
                ((Bumb) gameView.getBumbArray().get(i)).flag = true;
            }
            for(int i = 0; i <  gameView.getSubmarineArray().size(); i++)
            {
                ((Submarine)gameView.getSubmarineArray().get(i)).setFlag(true);
            }
            for(int i = 0; i <gameView.getTorpedoArray().size(); i++)
            {
                ((Torpedo)gameView.getTorpedoArray().get(i)).setFlag(true);
            }
        }
        //开始游戏
        if(!isRunning){
            gameText.setText("暂停");
            gameView.getShip().setRunning(true);
            isRunning = true;
            tm1 = new TimeManager(gameView.getShip(),GameActivity.this,submarine,gameView);
            Thread t = new Thread(tm1);
            t.start();
            for(int i = 0; i < gameView.getBumbArray().size(); i++)
            {
                gameView.getBumbArray().get(i).flag = true;
                gameView.getBumbArray().remove(i);
            }
            for(int i = 0; i < gameView.getSubmarineArray().size(); i++)
            {
                gameView.getSubmarineArray().get(i).setFlag(true);
                gameView.getSubmarineArray().remove(i);
            }
            for(int i = 0; i < gameView.getTorpedoArray().size(); i++)
            {
                gameView.getTorpedoArray().get(i).setFlag(true);
                gameView.getTorpedoArray().remove(i);
            }
        }
    }
    //绘制游戏规则界面
    private void showRuleDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        View view = View.inflate(GameActivity.this, R.layout.ruledialog, null);

        Button ok = (Button) view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPauseDialog();
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }
    //绘制暂停界面
    private void showPauseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        View view = View.inflate(GameActivity.this, R.layout.pausedialog, null);
        Button rule = (Button)view.findViewById(R.id.rule);
        Button contiueBtn = (Button) view.findViewById(R.id.contiue);
        rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRuleDialog();
                dialog.dismiss();
            }
        });
        contiueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameText.setText("暂停");
                gameView.getShip().setRunning(true);
                synchronized(GameActivity.subLock) {
                    GameActivity.subLock.notifyAll();
                }
                dialog.dismiss();
            }
        });
        Button again = (Button)view.findViewById(R.id.about);
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
                synchronized(GameActivity.subLock) {
                    GameActivity.subLock.notifyAll();
                }
                dialog.dismiss();
                gameView.invalidate();
            }
        });
        Button exit =(Button)view.findViewById(R.id.tuichu);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        dialog.setView(view);
        dialog.show();
    }
    //游戏失败判断
    public void loseGmae() {
        if(gameView.getLiveNum() == 0)
        {
            gameView.getShip().setRunning(false);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showdeathDialog();
                }
            });
        }
    }
    //绘制失败界面
    private void showdeathDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        View view = View.inflate(GameActivity.this, R.layout.deathdialog, null);
        Button button = (Button)view.findViewById(R.id.againBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
                synchronized(GameActivity.subLock) {
                    GameActivity.subLock.notifyAll();
                }
                dialog.dismiss();
                gameView.invalidate();
            }
        });
        Button button1 = (Button)view.findViewById(R.id.exitgame);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        dialog.setView(view);
        dialog.show();
    }
    //爆炸点判断
    public void expoled(){
        if(!gameView.getSubmarineArray().isEmpty() && !gameView.getBumbArray().isEmpty()){
            for (int i = 0; i < gameView.getBumbArray().size(); i++) {
                if(!gameView.getBumbArray().get(i).flag){
                    for (int j = 0; j < gameView.getSubmarineArray().size(); j++) {
                        if(!gameView.getSubmarineArray().get(j).isFlag()){
                            int by =((Bumb)gameView.getBumbArray().get(i)).getBeginY();
                            int syStart = ((Submarine)gameView.getSubmarineArray().get(j)).getY() - ((Bumb)gameView.getBumbArray().get(i)).getHeight();
                            int syEnd   = ((Submarine)gameView.getSubmarineArray().get(j)).getY() + ((Submarine)gameView.getSubmarineArray().get(j)).getHeight();

                            int bx = ((Bumb)gameView.getBumbArray().get(i)).getBeginX();

                            int sxStart = ((Submarine)gameView.getSubmarineArray().get(j)).getX() - ((Bumb)gameView.getBumbArray().get(i)).getWidth();
                            int sxEnd = ((Submarine)gameView.getSubmarineArray().get(j)).getX() + ((Submarine)gameView.getSubmarineArray().get(j)).getWeight();
                            if( by >= syStart && by <= syEnd && bx >= sxStart && bx <= sxEnd)
                            {
                                gameView.getBumbArray().get(i).flag = true;
                                gameView.getSubmarineArray().get(j).setFlag(true);
                                this.hitX   = gameView.getBumbArray().get(i).getBeginX();
                                this.hitY   = gameView.getBumbArray().get(i).getBeginY();
                                Hit hit = new Hit(this.hitX,this.hitY,this);
                                this.gameView.getHitArray().add(hit);
                                Thread  t = new Thread(hit);
                                t.start();
                                gameView.setScore(gameView.getScore()+10);
                                this.addPass(gameView.getScore());
                            }
                        }
                    }
                }
            }
        }
    }
    //记录等级
    public void addPass(int score) {
        if(score < 10) {
            gameView.setPass(1);
        }
        else if(score >=10 && score < 50) {
            gameView.setPass(2);
            this.tm1.setSpeed(800);
        }
        else if(score >= 50 && score <100) {
            gameView.setPass(3);
            this.tm1.setSpeed(700);
        }
        else if(score >= 100 && score <200) {
            gameView.setPass(4);
            this.tm1 .setSpeed(600);
        }else if(score >=200 ) {
            gameView.setPass(5);
            this.tm1 .setSpeed(500);
        }

    }

    public boolean isRunning() {
        return isRunning;
    }
    public void setRunning(boolean running) {
        isRunning = running;
    }
}
