package com.example.drawingfun.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.drawingfun.R;
import com.example.drawingfun.views.ObserverView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Admin on 08.05.2017.
 */

public class ObserverActivity extends AppCompatActivity {
    public ObserverView drawView;

    JSONObject job;

    Socket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.observer);
        drawView = (ObserverView)findViewById(R.id.observe);




//        saveBtn = (ImageButton)findViewById(R.id.save_btn);
//        saveBtn.setOnClickListener(this);
        try {
            socket = IO.socket("https://lit-ravine-37919.herokuapp.com/");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socket.emit("connected", "ChynaJake");
                }
            });

            socket.on("meconnected", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MyLogs", "meConnected" + args[0].toString());
                }
            });

            socket.on("data_painted", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    job = (JSONObject) args[0];
                    Log.d("MyLogs", "OA Painted " + job.toString());
                    try {
                        Log.d("MyLogs", "OA x " + job.get("x"));
                        Log.d("MyLogs", "OA y " + job.get("y"));
                        Log.d("MyLogs", "OA type " + job.get("type"));
                        Log.d("MyLogs", "OA color " + job.get("color"));
                        Log.d("MyLogs", "OA size " + job.get("size"));
                        Log.d("MyLogs", "OA lastSize " + job.get("lastSize"));
                        Log.d("MyLogs", "OA erase " + job.get("erase"));
                        Log.d("MyLogs1", "OA x " + job.get("x"));
                        Log.d("MyLogs1", "OA y " + job.get("y"));
                        Log.d("MyLogs1", "OA type " + job.get("type"));
                        Log.d("MyLogs1", "OA color " + job.get("color"));
                        Log.d("MyLogs1", "OA size " + job.get("size"));
                        Log.d("MyLogs1", "OA lastSize " + job.get("lastSize"));
                        Log.d("MyLogs1", "OA erase " + job.get("erase"));
//                        float x_coor = (Float)job.get("x");
//                        float y_coor = (Float)job.get("y");
//                        int type = (Integer)job.get("type");
//                        int color = (Integer)job.get("color");
//                        float b_size = (Float)job.get("size");
//                        float b_lastSize = (Float)job.get("lastSize");
//                        boolean erase = (Boolean)job.get("erase");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //stuff that updates ui
                                try {
                                    if(!job.get("color").equals(null)){
                                        drawView.setColor((Integer)job.get("color"));
                                    }

                                if(!job.get("lastSize").equals(null)){
                                    drawView.setLastBrushSize((Integer)job.get("lastSize"));
                                }
                                paintSetBrushSize((Integer)job.get("size") ,(Boolean)job.get("erase"));
                                drawView.drawFunction((Integer)job.get("type"), Float.valueOf(job.get("x").toString()), Float.valueOf(job.get("y").toString()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    Log.d("MyLogs", "Painted " + args.length);
                }
            });
            socket.on("painter", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MyLogs", args[0].toString());
                }
            });

            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    public void paintSetBrushSize(float brush, boolean clean) {
        if(!clean) {
            drawView.setBrushSize(brush);
            drawView.setLastBrushSize(brush);
            drawView.setErase(false);
        }else {
            drawView.setErase(true);
            drawView.setBrushSize(brush);
        }
    }

//    @Override
//    public void onClick(View view) {
//        if(view.getId()==R.id.draw_btn){
//            //draw button clicked
//            final Dialog brushDialog = new Dialog(this);
//            brushDialog.setTitle("Brush size:");
//            brushDialog.setContentView(R.layout.brush_chooser);
//            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
//            smallBtn.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setBrushSize(smallBrush);
//                    drawView.setLastBrushSize(smallBrush);
//                    drawView.setErase(false);
//                    brushDialog.dismiss();
//                }
//            });
//            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
//            mediumBtn.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setBrushSize(mediumBrush);
//                    drawView.setLastBrushSize(mediumBrush);
//                    drawView.setErase(false);
//                    brushDialog.dismiss();
//                }
//            });
//
//            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
//            largeBtn.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setBrushSize(largeBrush);
//                    drawView.setLastBrushSize(largeBrush);
//                    drawView.setErase(false);
//                    brushDialog.dismiss();
//                }
//            });
//
//            brushDialog.show();
//        }else if(view.getId()==R.id.erase_btn){
//            //switch to erase - choose size
//            final Dialog brushDialog = new Dialog(this);
//            brushDialog.setTitle("Eraser size:");
//            brushDialog.setContentView(R.layout.brush_chooser);
//
//            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
//            smallBtn.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(true);
//                    drawView.setBrushSize(smallBrush);
//                    drawView.setErase(false);
//                    brushDialog.dismiss();
//                }
//            });
//            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
//            mediumBtn.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(true);
//                    drawView.setBrushSize(mediumBrush);
//                    drawView.setErase(false);
//                    brushDialog.dismiss();
//                }
//            });
//            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
//            largeBtn.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(true);
//                    drawView.setBrushSize(largeBrush);
//                    drawView.setErase(false);
//                    brushDialog.dismiss();
//                }
//            });
//
//            brushDialog.show();
//        }else if(view.getId()==R.id.new_btn){
//            //new button
//        }else if(view.getId()==R.id.save_btn){
//            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
//            saveDialog.setTitle("Save drawing");
//            saveDialog.setMessage("Save drawing to device Gallery?");
//            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    //save drawing
//                    drawView.setDrawingCacheEnabled(true);
//                    String imgSaved = MediaStore.Images.Media.insertImage(
//                            getContentResolver(), drawView.getDrawingCache(),
//                            UUID.randomUUID().toString()+".png", "drawing");
//                    if(imgSaved!=null){
//                        Toast savedToast = Toast.makeText(getApplicationContext(),
//                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
//                        savedToast.show();
//                    }
//                    else{
//                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
//                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
//                        unsavedToast.show();
//                    }
//
//                    drawView.destroyDrawingCache();
//                }
//            });
//            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    dialog.cancel();
//                }
//            });
//            saveDialog.show();
//            //save drawing
//        }
//    }
}

