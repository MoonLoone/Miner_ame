package com.example.minergame.Arcanoid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.minergame.R;

public class ArcanoidActivity extends AppCompatActivity {

    private ImageView imageViewBackground;
    private Canvas canvas;
    private Bitmap extraBitmap;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcanoid);
        imageViewBackground = findViewById(R.id.imageViewBackgroundArcanoid);
        extraBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        canvas = new Canvas(extraBitmap);
        bitmap = ((BitmapDrawable) imageViewBackground.getDrawable()).getBitmap();
        canvas.drawBitmap(bitmap,0,0,null);

    }

    public void onClickRestart(View view) {
    }

    public void createPlates(){

    }

    public void returnToMain(View view) {
    }
}