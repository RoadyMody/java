package com.spaceshooter.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sandipbhattacharya.spaceshooter.R;

public class Smoke {
    Bitmap smoke[] = new Bitmap[9];
    int smokeFrame;
    int eX, eY;

    public Smoke(Context context, int eX, int eY) {
        smoke[0] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.smoke);
        smoke[1] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.smoke);
        smoke[2] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.smoke);
        smoke[3] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.smoke);
        smoke[4] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.smoke);
        smoke[5] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.smoke);
        smoke[6] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.smoke);
        smoke[7] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.smoke);
        smoke[8] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.smoke);
        smokeFrame = 0;
        this.eX = eX;
        this.eY = eY;
    }

    public Bitmap getSmoke(int smokeFrame){
        return smoke[smokeFrame];
    }
}
