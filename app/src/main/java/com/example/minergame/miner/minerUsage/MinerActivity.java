package com.example.minergame.miner.minerUsage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minergame.GameOverActivity;
import com.example.minergame.MainActivity;
import com.example.minergame.R;
import com.example.minergame.WinActivity;

public class MinerActivity extends AppCompatActivity {

    private ImageView imageViewField;

    private static final int onePlateSize = 80;
    private static final int fontSizePX = 2000;
    private int linePlateCount;
    private Plate[][] plates;
    private Bitmap bitmap;
    private int mineCount;
    private int flagCount;
    private Canvas canvas;
    private Bitmap vspomBitmap;
    private Bitmap[] bitmapCollection = new Bitmap[12];
    private int unCheckedPlateCount;
    private boolean usingFlag;
    private TextView textViewFlags;
    private ImageButton imageButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miner);
        usingFlag = false;
        textViewFlags = findViewById(R.id.textViewScore);
        imageButton = findViewById(R.id.imageButton);
        bitmapCollection[0] = BitmapFactory.decodeResource(getResources(),R.drawable.checked_plate);
        bitmapCollection[1] = BitmapFactory.decodeResource(getResources(),R.drawable.one);
        bitmapCollection[2] = BitmapFactory.decodeResource(getResources(),R.drawable.two);
        bitmapCollection[3] = BitmapFactory.decodeResource(getResources(),R.drawable.three);
        bitmapCollection[4] = BitmapFactory.decodeResource(getResources(),R.drawable.four);
        bitmapCollection[5] = BitmapFactory.decodeResource(getResources(),R.drawable.five);
        bitmapCollection[6] = BitmapFactory.decodeResource(getResources(),R.drawable.six);
        bitmapCollection[7] = BitmapFactory.decodeResource(getResources(),R.drawable.seven);
        bitmapCollection[8] = BitmapFactory.decodeResource(getResources(),R.drawable.eight);
        bitmapCollection[9] = BitmapFactory.decodeResource(getResources(),R.drawable.mine);
        bitmapCollection[10] = BitmapFactory.decodeResource(getResources(),R.drawable.flag);
        bitmapCollection[11] = BitmapFactory.decodeResource(getResources(),R.drawable.plate);
        for (int i=0;i<12;i++){
            bitmapCollection[i] = Bitmap.createScaledBitmap(bitmapCollection[i],onePlateSize,onePlateSize,false);
        }
        linePlateCount = fontSizePX / onePlateSize;
        plates = new Plate[linePlateCount][linePlateCount];
        mineCount = linePlateCount * linePlateCount / 10;
        flagCount = mineCount;
        unCheckedPlateCount = linePlateCount*linePlateCount;
        imageViewField = findViewById(R.id.imageViewMinerField);
        bitmap = ((BitmapDrawable) imageViewField.getDrawable()).getBitmap();
        makePlates();
        randomCreateMine();
        calculateMineCount();
        vspomBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        canvas = new Canvas(vspomBitmap);
        bitmap.setDensity(Bitmap.DENSITY_NONE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        String flagCountText = "Осталось флагов: "+flagCount;
        textViewFlags.setText(flagCountText);
        imageViewField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                    int touchXCoord = (int) motionEvent.getX() / 80;
                    int touchYCoord = (int) motionEvent.getY() / 80;
                    checkPlate(touchXCoord, touchYCoord);
                    return false;
                }
        });

    }



    private void makePlates(){
        for (int i=0;i<linePlateCount;i++){
            for (int j=0;j<linePlateCount;j++){
                plates[i][j] = new Plate(i*onePlateSize,j*onePlateSize);
            }
        }
    }

    private void randomCreateMine(){
        for (int i=0;i<mineCount;i++) {
            int xRand = (int) (Math.random() * (linePlateCount-1));
            int yRand = (int) (Math.random() * (linePlateCount-1));
            plates[xRand][yRand].setMine(true);
        }
    }

    private void calculateMineCount(){
        for (int i=0;i<linePlateCount;i++) {
            for (int j = 0; j < linePlateCount; j++) {
                int mineCount = 0;
                if (i!=linePlateCount-1 && plates[i+1][j].isMine()) mineCount++;
                if (j!=linePlateCount-1 && plates[i][j+1].isMine()) mineCount++;
                if (i!=0 && plates[i-1][j].isMine()) mineCount++;
                if (j!=0 && plates[i][j-1].isMine()) mineCount++;
                if (i!=0 && j!=0 && plates[i-1][j-1].isMine()) mineCount++;
                if (i!=0 && j!=linePlateCount-1 && plates[i-1][j+1].isMine()) mineCount++;
                if (i!=linePlateCount-1 && j!=linePlateCount-1 && plates[i+1][j+1].isMine()) mineCount++;
                if (i!=linePlateCount-1 && j!=0 && plates[i+1][j-1].isMine()) mineCount++;
                if (plates[i][j].isMine()) plates[i][j].setMineCount(-1);
                else plates[i][j].setMineCount(mineCount);
            }
        }
    }

    private boolean checkPlate(int xCoord,int yCoord){
        if (plates[xCoord][yCoord].isFlag()){
            flagCount++;
            unCheckedPlateCount++;
            plates[xCoord][yCoord].setFlag(false);
            plates[xCoord][yCoord].setChecked(false);
            canvas.drawBitmap(bitmapCollection[11], plates[xCoord][yCoord].getxCoord(), plates[xCoord][yCoord].getyCoord(), null);
            imageViewField.setImageBitmap(vspomBitmap);
            return true;
        }
        if (plates[xCoord][yCoord].isChecked()){
            return true;
        }
        if (plates[xCoord][yCoord].getMineCount() == 0){
            if (usingFlag) putFlag(xCoord,yCoord);
            else {
                isEmpty(xCoord, yCoord);
                imageViewField.setImageBitmap(vspomBitmap);
            }
        }

        if (plates[xCoord][yCoord].getMineCount() > 0){
            if (usingFlag) putFlag(xCoord,yCoord);
            else {
                canvas.drawBitmap(bitmapCollection[plates[xCoord][yCoord].getMineCount()], plates[xCoord][yCoord].getxCoord(), plates[xCoord][yCoord].getyCoord(), null);
                unCheckedPlateCount--;
                imageViewField.setImageBitmap(vspomBitmap);
                plates[xCoord][yCoord].setChecked(true);
            }
        }

        if (plates[xCoord][yCoord].getMineCount() == -1){
            if (usingFlag) putFlag(xCoord,yCoord);
            else {
                canvas.drawBitmap(bitmapCollection[9], plates[xCoord][yCoord].getxCoord(), plates[xCoord][yCoord].getyCoord(), null);
                imageViewField.setImageBitmap(vspomBitmap);
                Intent intentGameOver = new Intent(MinerActivity.this, GameOverActivity.class);
                startActivity(intentGameOver);
            }
        }
        if (unCheckedPlateCount == 0){
            Intent winIntent = new Intent(MinerActivity.this, WinActivity.class);
            startActivity(winIntent);
        }
        return false;
    }

    public boolean isEmpty(int xCoord,int yCoord){
        if (plates[xCoord][yCoord].isChecked()) return false;
        unCheckedPlateCount--;
        canvas.drawBitmap(bitmapCollection[plates[xCoord][yCoord].getMineCount()],plates[xCoord][yCoord].getxCoord(),plates[xCoord][yCoord].getyCoord(),null);
        plates[xCoord][yCoord].setChecked(true);

        if (plates[xCoord][yCoord].getMineCount()>0) return true;

        if (xCoord != linePlateCount-1 && yCoord!= linePlateCount-1){
            isEmpty(xCoord+1,yCoord);
            isEmpty(xCoord+1,yCoord+1);
            isEmpty(xCoord,yCoord+1);
        }
        if (xCoord != 0 && yCoord!= 0){
            isEmpty(xCoord-1,yCoord);
            isEmpty(xCoord-1,yCoord-1);
            isEmpty(xCoord,yCoord-1);
        }
        if (xCoord!=0 && yCoord!= linePlateCount-1) isEmpty(xCoord-1,yCoord+1);
        if (xCoord!= linePlateCount-1 && yCoord!= 0) isEmpty(xCoord+1,yCoord-1);
        return true;
    }

    public void putFlag(int xCoord,int yCoord){
        plates[xCoord][yCoord].setFlag(true);
        unCheckedPlateCount--;
        flagCount--;
        canvas.drawBitmap(bitmapCollection[10], plates[xCoord][yCoord].getxCoord(), plates[xCoord][yCoord].getyCoord(), null);
        imageViewField.setImageBitmap(vspomBitmap);
        String flagCountText = "Осталось флагов: "+flagCount;
        textViewFlags.setText(flagCountText);
    }

    public void onClickFlag(View view) {
        if(usingFlag){
            imageButton.setImageResource(R.drawable.flag_btn);
            usingFlag = false;
        }
            else{
                imageButton.setImageResource(R.drawable.flag_btn_used);
                usingFlag = true;
        }
    }

    public void onClickRestart(View view) {
        Intent intent = new Intent(MinerActivity.this,MinerActivity.class);
        startActivity(intent);
    }

    public void returnToMain(View view) {
        Intent intent = new Intent(MinerActivity.this, MainActivity.class);
        startActivity(intent);
    }



}