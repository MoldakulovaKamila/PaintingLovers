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
import android.view.MotionEvent;
import android.view.View;

import com.example.drawingfun.R;
import com.example.drawingfun.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kamila on 5/7/17.
 */

public class DrawingView extends View {
    Context context;
    private JSONObject elements = new JSONObject();
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


    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
        this.context = context;
    }
    private void setupDrawing(){
//get drawing area setup for interaction
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;
        drawPath = new Path();
        drawPaint = new Paint();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        try {
            elements.put("x", touchX);
            elements.put("y", touchY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                try {
                    elements.put("type", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                try {
                    elements.put("type", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                try {
                    elements.put("type", 2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                return false;
        }
        ((MainActivity)context).sendData(elements);
        invalidate();
        return true;
//detect user touch
    }

    public void setColor(String newColor) {
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
        Log.d("Color",paintColor+" ");
        try {
            elements.put("color", paintColor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//set color
    }

    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
        try {
            elements.put("size", brushSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//update size
    }
    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
        try {
            elements.put("lastSize", lastBrushSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }
    public void setErase(boolean isErase){
//set erase true or false
        erase=isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
        try {
            elements.put("erase", erase);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public JSONObject getElements(){
        return elements;
    }



}
