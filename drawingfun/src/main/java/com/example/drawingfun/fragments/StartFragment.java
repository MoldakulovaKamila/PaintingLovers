package com.example.drawingfun.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drawingfun.R;
import com.example.drawingfun.activities.EnteranceActivity;

import static com.example.drawingfun.R.id.room;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    Button btn;
    EditText et;

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_left, container, false);
        btn = (Button) v.findViewById(R.id.create_room);
        et = (EditText) v.findViewById(room);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et.getText().toString();
                if(!text.equals("")) {
                    ((EnteranceActivity) getActivity()).goTo(0, text);
                }
                else {
                    Toast.makeText(getActivity(), "You haven't entered the room", Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }

}
