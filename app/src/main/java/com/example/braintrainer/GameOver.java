package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameOver extends AppCompatActivity {

    private TextView textViewResult;
    private TextView textViewMaxResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        textViewResult = findViewById(R.id.textViewResult);
        textViewMaxResult = findViewById(R.id.textViewMaxResult);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("result")) {
            int result = intent.getIntExtra("result", 0);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int max = preferences.getInt("max", 0);
            String maxScore = String.format("Ваш максимальный результат: %s", max);
            String score = String.format("Ваш результат: %s", result);
            textViewResult.setText(score);
            textViewMaxResult.setText(maxScore);
        }
    }

    public void onClickStartNewGame(View view) {
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
    }
}
