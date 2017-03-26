package com.example.shama.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Shama on 2017-03-26.
 */
public class MessageFragment extends Fragment {

    Context parent;
    Long id;

    //no matter how you got here, the data is in the getArguments
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        id = bun.getLong("ID");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View gui = inflater.inflate(R.layout.activity_message_details, null);

        // TextView textViewMsg = (TextView)gui.findViewById(R.id.textViewMsg);
        //     textViewMsg.setText("");//

        return gui;


    }


}
