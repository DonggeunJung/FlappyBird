package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements JGameLib.GameEvent {
    JGameLib gameLib = null;
    JGameLib.Card gameBackground;
    JGameLib.Card cardBird;
    ArrayList<JGameLib.Card> hurdles = new ArrayList();
    double hurdleDistance = 270;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameLib = findViewById(R.id.gameLib);
        initGame();
    }

    @Override
    protected void onDestroy() {
        if(gameLib != null)
            gameLib.clearMemory();
        super.onDestroy();
    }

    void initGame() {
        gameLib.listener(this);
        gameLib.setScreenGrid(100, 140);
        gameBackground = gameLib.addCard(R.drawable.scroll_back_woods);
        gameBackground.sourceRect(0, 0, 30, 100);
        cardBird = gameLib.addCard(R.drawable.sprite_bird01, 10, 30, 10, 12);
        cardBird.addImage(R.drawable.sprite_bird02);
        cardBird.checkCollision();
        addHurdle(100, 0, 15, 30);
        addHurdle(140, 100, 15, 40);
        addHurdle(200, 0, 15, 60);
        addHurdle(200, 110, 15, 30);
        addHurdle(260, 0, 15, 40);
        addHurdle(260, 90, 15, 50);
        newGame();
    }

    void addHurdle(double l, double t, double w, double h) {
        JGameLib.Card hurdle = gameLib.addCardColor(Color.rgb(255,153,51), l, t, w, h);
        hurdle.edge(Color.rgb(255,204,153), 3);
        hurdle.checkCollision();
        hurdles.add(hurdle);
    }

    void newGame() {
        gameBackground.sourceRect(0, 0, 30, 100);
        cardBird.move(10, 30);
        hurdles.get(0).move(100,0);
        hurdles.get(1).move(140,100);
        hurdles.get(2).move(200,0);
        hurdles.get(3).move(200,110);
        hurdles.get(4).move(260,0);
        hurdles.get(5).move(260,90);
    }

    void backScroll() {
        gameBackground.sourceRect(0, 0, 30, 100);
        gameBackground.sourceRectIng(100, 0, 6);
    }

    void birdFly() {
        cardBird.imageChanging(0.5);
        cardBird.movingSpeed(10,127, 1);
    }

    void hurdleScroll() {
        for(JGameLib.Card hurdle : hurdles) {
            hurdle.moveGap(-hurdleDistance, 0, 6);
        }
    }

    // User Event start ====================================

    public void onBtnRestart(View v) {
        newGame();
        backScroll();
        birdFly();
        hurdleScroll();
    }

    public void onBtnJump(View v) {
        cardBird.moveGap(0, -10);
        birdFly();
    }

    // User Event end ====================================

    // Game Event start ====================================

    @Override
    public void onGameWorkEnded(JGameLib.Card card, JGameLib.WorkType workType) {
        switch(workType) {
            case SOURCE_RECT: {
                if(card == gameBackground) {
                    backScroll();
                }
                break;
            }
            case IMAGE_CHANGE: {
                if(card == cardBird) {
                    birdFly();
                }
                break;
            }
            case MOVE: {
                if(card == cardBird) {
                    cardBird.stopImageChanging();
                } else {
                    card.moveGap(hurdleDistance, 0);
                    card.moveGap(-hurdleDistance, 0, 8);
                }
                break;
            }
        }
    }

    @Override
    public void onGameTouchEvent(JGameLib.Card card, int action, float blockX, float blockY) {}

    @Override
    public void onGameSensor(int sensorType, float x, float y, float z) {}

    @Override
    public void onGameCollision(JGameLib.Card card1, JGameLib.Card card2) {
        if(card1 == cardBird) {
            gameLib.stopAllWork();
            gameLib.popupDialog(null, "Oops! Try again", "Close");
        }
    }

    @Override
    public void onGameTimer(int what) {}

    // Game Event end ====================================

}