package com.example.drawingfun.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.drawingfun.R;

/**
 * Created by Admin on 08.05.2017.
 */

public class ObserverView extends View {
    Context context;
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    private float brushSize, lastBrushSize;
    private boolean erase=false;


    public ObserverView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
        this.context = context;
    }
    private void setupDrawing(){
//get drawing area setup for interaction
        brushSize = getResources().getInteger(R.integer.small_size);
        lastBrushSize = brushSize;
        drawPath = new Path();
        drawPaint = new Paint();
        paintColor = Color.parseColor("#FF0000FF");
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
//view given size
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
//draw view
    }


    public void drawFunction(int type, float x, float y) {
        if(type == 0) {
            drawPath.moveTo(x, y);
        }else if(type == 1) {
            drawPath.lineTo(x, y);
        }else if(type ==2) {
            drawCanvas.drawPath(drawPath, drawPaint);
            drawPath.reset();
        }else{
            Log.d("MyLogs", "We are inside else of type");
        }

        invalidate();
    }

    public void setColor(int newColor) {
        invalidate();
        paintColor = newColor;
        drawPaint.setColor(paintColor);
        Log.d("Color",paintColor+" ");
//set color
    }

    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);

//update size
    }
    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }

    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setErase(boolean isErase){
//set erase true or false
        erase=isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }



}
