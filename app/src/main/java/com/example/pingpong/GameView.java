package com.example.pingpong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    final long UPDATE=30;
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
        canvas.drawColor(Color.BLACK);
        ballX += velocity.getX();
        ballY += velocity.getY();
        if(ballX <= 0 || ballX >= dWidth-ball.getWidth()){
            velocity.setX(velocity.getX() * -1);
        }
        if(ballY <= 0 ){
            velocity.setY(velocity.getY() * -1);
        }
        if(ballY > paddleY + paddle.getHeight()){
            ballX = 1 + random.nextInt(dWidth - ball.getWidth() -1 );
            ballY = 0;
            if(mMiss != null && audioState){
                mMiss.start();
            }
            velocity.setX(xVelocity());
            velocity.setY(32);
            life--;
            if(life == 0){
                Intent intent = new Intent(context,GameOver.class);
                intent.putExtra("points",points);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        }
        if(((ballX + ball.getWidth()) >= paddleX)
        && (ballX <= paddleX + paddle.getWidth())
        && (ballY + ball.getHeight() >= paddleY)
        && (ballY + ball.getHeight() <= paddleY + paddle.getHeight())){
            if(mHit != null && audioState){
                mHit.start();
            }
            velocity.setX(velocity.getX() + 1);
            velocity.setY(velocity.getY() + 1);
            points++;
            canvas.drawBitmap(ball,ballX,ballY,null);
            canvas.drawBitmap(paddle,paddleX,paddleY,null);
            canvas.drawText(""+points,20,textSize,textPaint);
            if(life == 2){
                healthPaint.setColor((Color.YELLOW));
            }
            else if(life == 1){
                healthPaint.setColor((Color.RED));
            }
            canvas.drawRect(dWidth-200, 30, dWidth-200 + 60 * life , 80, healthPaint);
            handler.postDelayed(runnable , UPDATE);
        }


    }

    private int xVelocity() {
        int[] values = {-35, -30, -25, 25, 30, 35};
        int index = random.nextInt(6);
        return values[index];
    }
}
