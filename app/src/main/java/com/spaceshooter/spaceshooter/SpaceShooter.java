package com.spaceshooter.spaceshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.sandipbhattacharya.spaceshooter.R;

import java.util.ArrayList;
import java.util.Random;

public class SpaceShooter extends View {
    Context context;
    Bitmap background, lifeImage;
    Handler handler;
    long UPDATE_MILLIS = 30;
    static int screenWidth, screenHeight;
    int points = 0;

    int stage = 0;
    int life = 3;
    Paint scorePaint;
    int TEXT_SIZE = 80;
    boolean paused = false;
    Dog dog;
    Enemy enemy;
    Random random;
    ArrayList<Shot> enemyShots, ourShots;
    Smoke smoke;
    ArrayList<Smoke> smokes;
    boolean enemyShotAction = false;
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
           invalidate();
        }
    };


    public SpaceShooter(Context context) {
        super(context);
        this.context = context;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        random = new Random();
        enemyShots = new ArrayList<>();
        ourShots = new ArrayList<>();
        smokes = new ArrayList<>();
        dog = new Dog(context);
        enemy = new Enemy(context);
        handler = new Handler();
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background3);
        lifeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        scorePaint = new Paint();
        scorePaint.setColor(Color.RED);
        scorePaint.setTextSize(TEXT_SIZE);
        scorePaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw background, Points and life on Canvas
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawText("Points: " + points, 0, TEXT_SIZE, scorePaint);
        canvas.drawText("Stages: " + stage, 550, TEXT_SIZE, scorePaint);

        for(int i=life; i>=1; i--){
            canvas.drawBitmap(lifeImage, screenWidth - lifeImage.getWidth() * i, 0, null);
        }
        // When life becomes 0, stop game and launch GameOver Activity with points
        if(life == 0){
            paused = true;
            handler = null;
            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
        // Move enemy
        enemy.ex += enemy.enemyVelocity;
        // If enemy collides with right wall, reverse enemyVelocity
        if(enemy.ex + enemy.getEnemyWidth() >= screenWidth){
            enemy.enemyVelocity *= -1;
        }
        // If enemy collides with left wall, again reverse enemyVelocity
        if(enemy.ex <=0){
            enemy.enemyVelocity *= -1;
        }
        // Till enemyShotAction is false, enemy should fire shots from random travelled distance
        if(enemyShotAction == false){
            if(enemy.ex >= 200 + random.nextInt(400)){
                Shot enemyShot = new Shot(context, enemy.ex + enemy.getEnemyWidth() / 2, enemy.ey );
                enemyShots.add(enemyShot);
                // We're making enemyShotAction to true so that enemy can take a short at a time
                enemyShotAction = true;
            }
            if(enemy.ex >= 400 + random.nextInt(800)){
                Shot enemyShot = new Shot(context, enemy.ex + enemy.getEnemyWidth() / 2, enemy.ey );
                enemyShots.add(enemyShot);
                // We're making enemyShotAction to true so that enemy can take a short at a time
                enemyShotAction = true;
            }
            else{
                Shot enemyShot = new Shot(context, enemy.ex + enemy.getEnemyWidth() / 2, enemy.ey );
                enemyShots.add(enemyShot);
                // We're making enemyShotAction to true so that enemy can take a short at a time
                enemyShotAction = true;
            }
        }
        // Draw the enemy Spaceship
        canvas.drawBitmap(enemy.getEnemy(), enemy.ex, enemy.ey, null);
        // Draw our spaceship between the left and right edge of the screen
        if(dog.ox > screenWidth - dog.getDogWidth()){
            dog.ox = screenWidth - dog.getDogWidth();
        }else if(dog.ox < 0){
            dog.ox = 0;
        }
        // Draw our Spaceship
        canvas.drawBitmap(dog.getDog(), dog.ox, dog.oy, null);
        // Draw the enemy shot downwards our spaceship and if it's being hit, decrement life, remove
        // the shot object from enemyShots ArrayList and show an smoke.
        // Else if, it goes away through the bottom edge of the screen also remove
        // the shot object from enemyShots.
        // When there is no enemyShots no the screen, change enemyShotAction to false, so that enemy
        // can shot.
        for(int i=0; i < enemyShots.size(); i++){
            enemyShots.get(i).shy += 15;
            canvas.drawBitmap(enemyShots.get(i).getShot(), enemyShots.get(i).shx, enemyShots.get(i).shy, null);
            if((enemyShots.get(i).shx >= dog.ox)
                && enemyShots.get(i).shx <= dog.ox + dog.getDogWidth()
                && enemyShots.get(i).shy >= dog.oy
                && enemyShots.get(i).shy <= screenHeight){
                life--;
                enemyShots.remove(i);
                smoke = new Smoke(context, dog.ox, dog.oy);
                smokes.add(smoke);
            }else if(enemyShots.get(i).shy >= screenHeight){
                enemyShots.remove(i);
            }
            if(enemyShots.size() < 1){
                enemyShotAction = false;
            }
        }
        // Draw our spaceship shots towards the enemy. If there is a collision between our shot and enemy
        // spaceship, increment points, remove the shot from ourShots and create a new smoke object.
        // Else if, our shot goes away through the top edge of the screen also remove
        // the shot object from enemyShots ArrayList.
        for(int i=0; i < ourShots.size(); i++){
            ourShots.get(i).shy -= 15;
            canvas.drawBitmap(ourShots.get(i).getShot(), ourShots.get(i).shx, ourShots.get(i).shy, null);
            if((ourShots.get(i).shx >= enemy.ex)
               && ourShots.get(i).shx <= enemy.ex + enemy.getEnemyWidth()
               && ourShots.get(i).shy <= enemy.getEnemyWidth()
               && ourShots.get(i).shy >= enemy.ey){
                points++;
                ourShots.remove(i);
                smoke = new Smoke(context, enemy.ex, enemy.ey);
                smokes.add(smoke);
            }else if(ourShots.get(i).shy <=0){
                ourShots.remove(i);
            }
        }




        switch (points) {
            case 10:
                background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background5);
                enemy.enemyVelocity *= 1.05;
                stage = 2;
                break;
            case 20:
                background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background5);
                stage = 3;
                break;

            case 30:
                background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background5);
                enemy.enemyVelocity *= 1.05;

                stage = 5;
                break;

            case 40:
                stage = 6;
                break;

            case 50:
                stage = 7;
                break;
            default:
                // Handle the case where points is neither 2 nor 3
        }


        // Do the smoke
        for(int i=0; i < smokes.size(); i++){
            canvas.drawBitmap(smokes.get(i).getSmoke(smokes.get(i).smokeFrame), smokes.get(i).eX, smokes.get(i).eY, null);
            smokes.get(i).smokeFrame++;
            if(smokes.get(i).smokeFrame > 8){
                smokes.remove(i);
            }
        }
        // If not paused, we’ll call the postDelayed() method on handler object which will cause the
        // run method inside Runnable to be executed after 30 milliseconds, that is the value inside
        // UPDATE_MILLIS.
        if(!paused)
            handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int)event.getX();
        // When event.getAction() is MotionEvent.ACTION_UP, if ourShots arraylist size < 1,
        // create a new Shot.
        // This way we restrict ourselves of making just one shot at a time, on the screen.
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(ourShots.size() < 1){
                Shot ourShot = new Shot(context, dog.ox + dog.getDogWidth() / 2, dog.oy);
                ourShots.add(ourShot);
            }
        }
        // When event.getAction() is MotionEvent.ACTION_DOWN, control dog
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            dog.ox = touchX;
        }
        // When event.getAction() is MotionEvent.ACTION_MOVE, control dog
        // along with the touch.
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            dog.ox = touchX;
        }
        // Returning true in an onTouchEvent() tells Android system that you already handled
        // the touch event and no further handling is required.
        return true;
    }
}
