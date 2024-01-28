package com.example.pingpong;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.View;
import android.os.Handler;
import androidx.annotation.NonNull;

import java.util.Random;


public class GameView extends View {
    Context context;
    float ballX,ballY;
    Velocity velocity = new Velocity(25,32);
    Handler handler;
    final long UODATE=30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float textSize = 120;
    float paddleX,paddleY;
    float oldX,oldPaddleX;
    int points = 30;
    int life = 3;
    Bitmap ball,paddle;
    int dWidth,dHeight;
    MediaPlayer mHit,mMiss;
    Random random;
    SharedPreferences sharedPreferences;
    Boolean audioState;


    public GameView(Context context){
        super(context);
        this.context = context;
        ball = BitmapFactory.decodeResource(getResources(),R.drawable.ball);
        paddle = BitmapFactory.decodeResource(getResources(),R.drawable.paddle);
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        mHit = MediaPlayer.create(context,R.raw.hit);
        mMiss = MediaPlayer.create(context,R.raw.miss);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
        healthPaint.setColor(Color.GREEN);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        random = new Random();
        ballX = random.nextInt(dWidth);
        paddleY = (dHeight * 4)/5;
        paddleX = dWidth/2 - paddle.getWidth();
        sharedPreferences = context.getSharedPreferences("my_pref",0);
        audioState = sharedPreferences.getBoolean("audioState",true);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
    }
}
