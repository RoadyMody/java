package com.spaceshooter.spaceshooter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sandipbhattacharya.spaceshooter.R;

public class GameOver extends AppCompatActivity {

    TextView tvPoints;
    TextView tvHighScores;

    int highScore;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        int points = getIntent().getExtras().getInt("points");
        int highScores = getIntent().getExtras().getInt("highScores");
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText("" + points);

        if(points > highScores){
            highScore = points;
        }

        int highScore = getIntent().getExtras().getInt("highScore");
        tvHighScores = findViewById(R.id.tvHighScores);
        tvHighScores.setText(""+ 12);
    }

    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, StartUp.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        finish();
    }
}
