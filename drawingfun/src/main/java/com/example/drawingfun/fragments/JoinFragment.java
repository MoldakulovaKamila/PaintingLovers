package com.example.drawingfun.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.drawingfun.R;
import com.example.drawingfun.activities.EnteranceActivity;

/**
 * Created by Admin on 14.05.2017.
 */

public class JoinFragment extends Fragment {

    public ListView lv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_right, container);

        lv = (ListView) v.findViewById(R.id.list_of_rooms);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((EnteranceActivity)getActivity()).goTo(1, ((TextView) view.findViewById(R.id.item_data)).getText().toString());
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
