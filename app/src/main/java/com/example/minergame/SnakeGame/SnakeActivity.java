package com.example.minergame.SnakeGame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minergame.GameOverActivity;
import com.example.minergame.MainActivity;
import com.example.minergame.R;
import com.example.minergame.miner.minerUsage.MinerActivity;
import com.example.minergame.miner.minerUsage.Plate;

import java.util.concurrent.TimeUnit;

public class SnakeActivity extends AppCompatActivity {

    private static final int backSize =13;
    private static final int onePlateSize = 80;
    private boolean isGameRemains = true;
    private SnakePlate[][] plates = new SnakePlate[backSize][backSize];
    private Canvas canvas;
    private ImageView imageViewBackground;
    private Bitmap extraBitmap;
    private Bitmap bitmap;
    private Bitmap snakeBodyBitmap;
    private SnakeLoop snakeLoop;
    private Thread thread;
    private Bitmap cherryBitmap;
    private int currentXCoord;
    private int currentYCoord;
    private int xCoordPlus;
    private int yCoordPlus;
    private ImageButton[] imageButtons = new ImageButton[4];
    private boolean findCherry;
    private int score;
    private TextView textViewScore;
    private String textScore;
    private Bitmap snakeHeadBitmap;
    private Bitmap[] headStateBitmaps = new Bitmap[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);
        score = 0;
        textViewScore = findViewById(R.id.textViewScore);
        imageButtons[0] = findViewById(R.id.ButtonBottom);
        imageButtons[1] = findViewById(R.id.buttonTop);
        imageButtons[2] = findViewById(R.id.buttonRight);
        imageButtons[3] = findViewById(R.id.ButtonLeft);
        imageViewBackground = findViewById(R.id.imageViewBackground);
        bitmap = ((BitmapDrawable) imageViewBackground.getDrawable()).getBitmap();
        extraBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        snakeHeadBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.snake_head);
        snakeBodyBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.snake_body);
        cherryBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.cherry);
        cherryBitmap = Bitmap.createScaledBitmap(cherryBitmap,onePlateSize,onePlateSize,false);
        snakeBodyBitmap = Bitmap.createScaledBitmap(snakeBodyBitmap,onePlateSize,onePlateSize,false);
        snakeHeadBitmap = Bitmap.createScaledBitmap(snakeHeadBitmap,onePlateSize,onePlateSize,false);
        headStateBitmaps[0] = rotateHead(snakeHeadBitmap,90);
        headStateBitmaps[1] = rotateHead(snakeHeadBitmap,180);
        headStateBitmaps[2] = rotateHead(snakeHeadBitmap,270);
        headStateBitmaps[3] = snakeHeadBitmap;
        snakeHeadBitmap = headStateBitmaps[0];
        canvas = new Canvas(extraBitmap);
        bitmap.setDensity(Bitmap.DENSITY_NONE);
        canvas.drawBitmap(bitmap,0,0,null);
        createPlates();
        createStartSnake();
        startGameThread();
    }


    public void gameOver(){
        snakeLoop.setGameRemains(false);
        Intent intent = new Intent(this, GameOverActivity.class);
        startActivity(intent);
    }

    public void createStartSnake(){
        plates[0][7].setExistTime(1);
        plates[1][7].setExistTime(2);
        plates[2][7].setExistTime(3);
        currentXCoord = 2;
        currentYCoord = 7;
        xCoordPlus = 1;
        yCoordPlus = 0;
        imageButtons[3].setClickable(false);
    }

    public void startGameThread(){
        snakeLoop = new SnakeLoop(backSize,plates);
        thread = new Thread(snakeLoop);
        thread.start();
    }

    public void createPlates(){
        for (int i=0;i<backSize;i++){
            for (int j=0;j<backSize;j++){
                plates[i][j] = new SnakePlate(i*80,j*80,0);
            }
        }
    }

    public void paintField(){
        textScore = "Your score: "+score;
        textViewScore.setText(textScore);
        canvas.drawBitmap(bitmap,0,0,null);
        for (int i=0;i<backSize;i++){
            for (int j=0;j<backSize;j++){
                if(plates[i][j].getExistTime()>0) canvas.drawBitmap(snakeBodyBitmap,plates[i][j].getxCoord(),plates[i][j].getyCoord(),null);
                if (plates[i][j].getIsCherry()) canvas.drawBitmap(cherryBitmap,plates[i][j].getxCoord(),plates[i][j].getyCoord(),null);
            }
        }
        canvas.drawBitmap(snakeHeadBitmap,plates[currentXCoord][currentYCoord].getxCoord(),plates[currentXCoord][currentYCoord].getyCoord(),null);
        imageViewBackground.setImageBitmap(extraBitmap);
    }


    public void onClickRestart(View view) {
        snakeLoop.setGameRemains(false);
        Intent intent = new Intent(SnakeActivity.this, SnakeActivity.class);
        startActivity(intent);
    }

    public void returnToMain(View view) {
        snakeLoop.setGameRemains(false);
        Intent intent = new Intent(SnakeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public Bitmap rotateHead(Bitmap bitmap, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    public void clickBtn(View view) {
        String tag =view.getTag().toString();
        switch (tag){
            case "left":snakeHeadBitmap =headStateBitmaps[2];  imageButtons[2].setClickable(false); xCoordPlus = -1; yCoordPlus=0; imageButtons[0].setClickable(true); imageButtons[1].setClickable(true); break;
            case "bottom":snakeHeadBitmap = headStateBitmaps[1]; imageButtons[1].setClickable(false); xCoordPlus = 0; yCoordPlus=1; imageButtons[2].setClickable(true); imageButtons[3].setClickable(true); break;
            case "right":snakeHeadBitmap = headStateBitmaps[0]; imageButtons[3].setClickable(false); xCoordPlus = 1; yCoordPlus = 0; imageButtons[0].setClickable(true); imageButtons[1].setClickable(true); break;
            case "top":snakeHeadBitmap = headStateBitmaps[3]; imageButtons[0].setClickable(false); xCoordPlus = 0; yCoordPlus=-1; imageButtons[3].setClickable(true); imageButtons[2].setClickable(true); break;
        }
    }


    class SnakeLoop implements Runnable{
        private int size;
        private boolean isCherryNotExist = true;
        private SnakePlate[][] plates;
        private int snakeSize;

        public SnakeLoop(int size, SnakePlate[][] plates) {
            this.size = size;
            this.plates = plates;
            snakeSize =4;
        }

        @Override
        public void run() {
            Log.i("hi","Hello from loop");
            while (isGameRemains) {
                if (isCherryNotExist) createCherry();
                currentXCoord = currentXCoord + xCoordPlus;
                currentYCoord = currentYCoord + yCoordPlus;
                if (currentXCoord == backSize || currentYCoord == -1 || currentYCoord == backSize || currentXCoord == -1 || plates[currentXCoord][currentYCoord].getExistTime() > 0) gameOver();
                else{
                    plates[currentXCoord][currentYCoord].setExistTime(snakeSize);
                    if (plates[currentXCoord][currentYCoord].getIsCherry()) {
                        score = score+10;
                        snakeSize++;
                        findCherry = true;
                        plates[currentXCoord][currentYCoord].setIsCherry(false);
                        isCherryNotExist = true;
                    }
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            if (findCherry && plates[i][j].getExistTime() > 0)
                                plates[i][j].setExistTime(plates[i][j].getExistTime() + 1);
                            if (plates[i][j].getExistTime() > 0) {
                                plates[i][j].setExistTime(plates[i][j].getExistTime() - 1);
                            }
                        }
                    }
                    findCherry = false;
                    paintField();
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            }
        }

        public void setGameRemains(boolean gameRemains) {
            isGameRemains = gameRemains;
        }

        public void createCherry(){
            while (isCherryNotExist){
                int randomCherryCoordX =(int)(Math.random()*backSize-1);
                int randomCherryCoordY =(int)(Math.random()*backSize-1);
                if (plates[randomCherryCoordX][randomCherryCoordY].getExistTime() == 0) {
                    plates[randomCherryCoordX][randomCherryCoordY].setIsCherry(true);
                    isCherryNotExist = false;
                }
            }
        }

    }

}