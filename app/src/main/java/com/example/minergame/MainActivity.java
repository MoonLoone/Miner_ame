package com.example.minergame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.minergame.Arcanoid.ArcanoidActivity;
import com.example.minergame.SnakeGame.SnakeActivity;
import com.example.minergame.miner.minerUsage.MinerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickMinerButton(View view) {
        Intent minerIntent = new Intent(this, MinerActivity.class);
        startActivity(minerIntent);
    }

    public void onClickArcanoid(View view) {
        Intent arcanoidIntent = new Intent(this, ArcanoidActivity.class);
        startActivity(arcanoidIntent);
    }

    public void onClickSnake(View view) {
        Intent snakeIntent = new Intent(this, SnakeActivity.class);
        startActivity(snakeIntent);
    }
}