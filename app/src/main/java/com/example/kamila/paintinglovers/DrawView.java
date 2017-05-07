package com.example.kamila.paintinglovers;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by kamila on 4/28/17.
 */

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder surfaceHolder;
    Thread thread;
    int x_touch;
    int y_touch;
    Path path ;
    Paint paint = new Paint();

    int right;
    int left;
    int bottom;
    int top;
    public DrawView(Context context){
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Canvas canvas = surfaceHolder.lockCanvas();

                    if (canvas == null) {
                        Log.d("My", "canvasisnull");
                        return;
                    }
                    long startTime = System.currentTimeMillis();
                    update();
                    draw(canvas);
                    long drawtime = System.currentTimeMillis() - startTime;
                    float fps = 1000 / (drawtime == 0 ? 1 : drawtime);
                    Log.d("My", fps + " ");
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction() ;
//        x_touch = (int) event.getX() ;
//        y_touch = (int) event.getY() ;
//
//            switch (event.getAction()){
//                case  MotionEvent.ACTION_UP:
//                    right = x_touch+30;
//                case MotionEvent.ACTION_DOWN:
//                    bottom= y_touch+30;
//
//                case MotionEvent.ACTION_MOVE:
//
//            }

            return super.onTouchEvent(event);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public  void update(){

    }
    public  void draw(Canvas canvas){
        canvas.drawColor(Color.CYAN);
        paint = new Paint();
        path= new Path();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        path.moveTo(500,500);
        path.lineTo(700,700);

        Log.d("My", path.toString());
        Log.d("My", paint.toString());
        canvas.drawPath(path,paint);


    }
}
