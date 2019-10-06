package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private TextView textViewZero;
    private TextView textViewFirst;
    private TextView textViewSecond;
    private TextView textViewThird;
    private TextView textViewScore;
    private TextView textViewTask;

    private int rightAnswer;
    private int rightAnswerPosition;
    private int max = 30;
    private int min = 5;
    private int countOfQuestions = 0;
    private int countOfRightAnswers = 0;
    private boolean isPositive;
    private boolean gameOver = false;
    private ArrayList<TextView> textViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewZero = findViewById(R.id.textViewOpinion0);
        textViewFirst = findViewById(R.id.textViewOpinion1);
        textViewSecond = findViewById(R.id.textViewOpinion2);
        textViewThird = findViewById(R.id.textViewOpinion3);
        textViewScore = findViewById(R.id.textViewStat);
        textViewTask = findViewById(R.id.textViewTask);

        playNext();
        CountDownTimer timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                textViewTimer.setText(getTime(l));
                if (l < 10000) {
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onFinish() {
                gameOver = true;
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("max", 0);
                if (max < countOfRightAnswers) {
                    preferences.edit().putInt("max", countOfRightAnswers).apply();
                }
                Intent intent = new Intent(MainActivity.this, GameOver.class);
                intent.putExtra("result", countOfRightAnswers);
                startActivity(intent);
            }
        };
        timer.start();
    }

    private void generateQuestion() {
        int a = (int) (Math.random() * (max-min + 1));
        int b = (int) (Math.random() * (max-min + 1));
        rightAnswerPosition = (int) (Math.random() * 4);
        int mark = (int) (Math.random() * 2);
        isPositive = mark == 1;
        if (isPositive) {
            rightAnswer = a + b;
            textViewTask.setText(String.format("%s+%s", a, b));
        } else {
            rightAnswer = a - b;
            textViewTask.setText(String.format("%s-%s", a, b));
        }
    }

    private void generateAnswers() {
        if (textViews.isEmpty()) {
            textViews.add(textViewZero);
            textViews.add(textViewFirst);
            textViews.add(textViewSecond);
            textViews.add(textViewThird);
        }
        ArrayList<Integer> wrongAnswers = new ArrayList<>();

        for (int i = 0; i < textViews.size(); i++) {
            int random;
            int pov = 0;

            int mark = (int) (Math.random() * 2);
            isPositive = mark == 1;

            if (isPositive) {
                random = rightAnswer + (int) (Math.random() * 5);
            } else {
                random = rightAnswer - (int) (Math.random() * 5);
            }

            if (!wrongAnswers.isEmpty()) {
                for (int answer : wrongAnswers) {
                    if (answer == random) {
                        pov++;
                    }
                }
            }

            wrongAnswers.add(random);

            if (random == rightAnswer || pov != 0) {
                i--;
                continue;
            }

            if (i == rightAnswerPosition) {
                textViews.get(i).setText(Integer.toString(rightAnswer));
            } else {
                textViews.get(i).setText(Integer.toString(random));
            }
        }
    }

    private String getTime(long millis) {
        int seconds = (int) millis / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
    }

    private void playNext() {
        generateQuestion();
        generateAnswers();
        textViewScore.setText(String.format("%s/%s", countOfRightAnswers, countOfQuestions));
    }

    public void onClickAnswer(View view) {
        if (!gameOver) {
            TextView textView = (TextView) view;
            int answer = Integer.parseInt(textView.getText().toString());
            if (answer == rightAnswer) {
                countOfRightAnswers++;
            }
            countOfQuestions++;
            playNext();
        }
    }
}
