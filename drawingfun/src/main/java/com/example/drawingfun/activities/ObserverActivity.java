package com.example.drawingfun.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.drawingfun.R;
import com.example.drawingfun.views.ObserverView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DecimalFormat;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.drawingfun.R.id.room;

/**
 * Created by Admin on 08.05.2017.
 */

public class ObserverActivity extends AppCompatActivity {
    public ObserverView drawView;

    JSONObject job;

    Socket socket;
    Context that;

    public float initBrush;

    String LOG_TAG = "MyLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "OA onCreate");
        that = this;

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.observer);
        drawView = (ObserverView)findViewById(R.id.observe);

        initBrush = getResources().getInteger(R.integer.medium_size);


//        saveBtn = (ImageButton)findViewById(R.id.save_btn);
//        saveBtn.setOnClickListener(this);
        try {
            //socket = IO.socket("http://192.168.100.19:3000");  // ip for lab518
            //socket = IO.socket("http://192.168.43.84:3000");  // current ipv4 of Pulp
            socket = IO.socket("http://192.168.100.5:3000");  // current ipv4 of homeWifi
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socket.emit("connected", "ChynaJake");
                }
            });
            String room = getIntent().getStringExtra("room");
            JSONObject c = new JSONObject();
            Log.d("MyLogs", "ObserverACtivity " + room);
            c.put("room", room);
            socket.emit("enter", c);

            socket.on("entered", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if((Boolean) args[0]){
                        Log.d(LOG_TAG, "Entered room" + args[0].toString());
                    }else {
                        try {
                            deleteRoom();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(that, "Some problems with server occured", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(that, EnteranceActivity.class));
                    }
                }
            });

            socket.on("drawing", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    job = (JSONObject) args[0];
                    Log.d("MyLogs", "OA Painted " + job.toString());
                    try {
                        Log.d("MyLogs1", "OA x " + job.get("x"));
                        Log.d("MyLogs1", "OA y " + job.get("y"));
                        Log.d("MyLogs1", "OA type " + job.get("type"));
                        Log.d("MyLogs1", "OA color " + job.get("color"));
                        Log.d("MyLogs1", "OA size " + job.get("size"));
                        Log.d("MyLogs1", "OA lastSize " + job.get("lastSize"));
                        Log.d("MyLogs1", "OA erase " + job.get("erase"));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //stuff that updates ui
                                try {
                                    if(!job.get("color").equals(null)){
                                        drawView.setColor((Integer)job.get("color"));
                                    }else {
                                        drawView.setColor(Color.parseColor("#FF660000"));
                                    }

                                    if(!job.get("lastSize").equals(null)){
                                        drawView.setLastBrushSize((Integer)job.get("lastSize"));
                                    }else {
                                        drawView.setLastBrushSize(initBrush);
                                    }
                                    paintSetBrushSize((Float)job.get("size") ,(Boolean)job.get("erase"));
                                    paintSetBrushSize(Float.valueOf(new DecimalFormat("#").format((Double)job.get("size"))), (Boolean)job.get("erase"));
                                    drawView.drawFunction((Integer)job.get("type"), Float.valueOf(job.get("x").toString()), Float.valueOf(job.get("y").toString()));
                                } catch (Exception e) {
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

            socket.on("deleting", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if((Boolean) args[0]){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(that, "Sorry, your room have been deleted", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(that, EnteranceActivity.class));
                            }
                        });

                    }
                }
            });

            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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

    private void deleteRoom() throws JSONException {
        JSONObject b = new JSONObject();
        b.put("type", 1);
        b.put("room", room);
        if(socket.connected()) {
            socket.emit("delete", b);
            socket.on("left", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if((Boolean) args[0]){
                        Toast.makeText(that, "You have successfully left the room", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(that, EnteranceActivity.class));
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "OA onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "OA onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "OA onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "OA onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "OA onDestroy");
        try {
            deleteRoom();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.close();
    }
}

