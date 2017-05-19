package com.example.drawingfun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.drawingfun.R;
import com.example.drawingfun.fragments.JoinFragment;
import com.example.drawingfun.fragments.StartFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Admin on 18.05.2017.
 */

public class EnteranceActivity extends AppCompatActivity {
    Fragment left;
    Fragment right;
    ListView listView;


    //ConstraintLayout frame;

    FragmentTransaction ft;

    Socket socket;

    Context that;

    public ArrayList<Map<String,Object>> data;
    public Map<String, Object> m;
    public SimpleAdapter sAdapter;
    String element;

    final String ATTRIBUTE_TEXT = "text";

    String [] from = { ATTRIBUTE_TEXT };
    int [] to = { R.id.item_data };


    public static final String LOG_TAG = "MYLOGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.enterance);

        left = new StartFragment();
        right = new JoinFragment();
        //listView = (ListView) findViewById(R.id.right_ent);

        that = this;

        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.left_ent, left);
        ft.add(R.id.right_ent, right);
        ft.commit();

        data = new ArrayList<>();
        sAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);


        try {
            //socket = IO.socket("http://192.168.100.19:3000");  // ip for lab518
            socket = IO.socket("http://192.168.43.84:3000");  // current ipv4 of Pulp
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socket.emit("connected", "ChynaJake");
                }
            });

            socket.emit("getList", "");

            socket.on("update", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("MyLogs", "Rooms " + args[0].toString());
                    JSONArray array = (JSONArray) args[0];
                    try {
                        for(int i = 0; i < array.length(); i++) {
                            element = array.getString(i);
                            if(!data.contains(array.getString(i))){

                                m = new HashMap<>();
                                m.put(ATTRIBUTE_TEXT, element);
                                data.add(m);

                                Log.d("MyLogs", "From server " + array.getString(i));
                                Log.d("MyLogs", "From server " + data.toString());
                            }else {
                                Log.d("MyLogs", "inside already");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((JoinFragment) getSupportFragmentManager().findFragmentById(R.id.right_ent)).lv.setAdapter(sAdapter);
                                sAdapter.notifyDataSetChanged();
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });


            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //listView.setAdapter(sAdapter);

//        listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    public void goTo(int role,String room) {
        // role 1 - watch
        // role 0 - draw
        if(role == 0) {
            Intent in = new Intent(that, MainActivity.class);
            in.putExtra("room", room);
            startActivity(in);
        }else {
            Intent in = new Intent(that, ObserverActivity.class);
            in.putExtra("room", room);
            startActivity(in);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sAdapter.notifyDataSetChanged();
    }
}

