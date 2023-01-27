package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Mosaic.GameEvent {
    Mosaic mosaic = null;
    Mosaic.Card gameBackground;
    Mosaic.Card cardBird;
    ArrayList<Mosaic.Card> hurdles = new ArrayList();
    double hurdleDistance = 270;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mosaic = findViewById(R.id.mosaic);
        initGame();
    }

    @Override
    protected void onDestroy() {
        if(mosaic != null)
            mosaic.clearMemory();
        super.onDestroy();
    }

    void initGame() {
        mosaic.listener(this);
        mosaic.setScreenGrid(100, 140);
        gameBackground = mosaic.addCard(R.drawable.flappybird_back);
        gameBackground.sourceRect(0, 0, 30, 100);
        cardBird = mosaic.addCard(R.drawable.sprite_bird01, 10, 30, 10, 12);
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
        Mosaic.Card hurdle = mosaic.addCardColor(Color.rgb(255,153,51), l, t, w, h);
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
        for(Mosaic.Card hurdle : hurdles) {
            hurdle.movingGap(-hurdleDistance, 0, 6);
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
    public void onGameWorkEnded(Mosaic.Card card, Mosaic.WorkType workType) {
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
                    card.movingGap(-hurdleDistance, 0, 8);
                }
                break;
            }
        }
    }

    @Override
    public void onGameTouchEvent(Mosaic.Card card, int action, float x, float y, MotionEvent event) {}

    @Override
    public void onGameSensor(int sensorType, float x, float y, float z) {}

    @Override
    public void onGameCollision(Mosaic.Card card1, Mosaic.Card card2) {
        if(card1 == cardBird) {
            mosaic.stopAllWork();
            mosaic.popupDialog(null, "Oops! Try again", "Close");
        }
    }

    @Override
    public void onGameTimer() {}

    // Game Event end ====================================

}