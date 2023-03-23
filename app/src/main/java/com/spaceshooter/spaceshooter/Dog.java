package com.spaceshooter.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sandipbhattacharya.spaceshooter.R;

import java.util.Random;

public class Dog {
    Context context;
    Bitmap dog;
    int ox, oy;
    Random random;

    public Dog(Context context) {
        this.context = context;
        dog = BitmapFactory.decodeResource(context.getResources(), R.drawable.dog);
        random = new Random();
        ox = random.nextInt(SpaceShooter.screenWidth);
        oy = SpaceShooter.screenHeight - dog.getHeight();
    }

    public Bitmap getDog(){
        return dog;
    }

    int getDogWidth(){
        return dog.getWidth();
    }
}
