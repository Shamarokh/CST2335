package com.example.shama.androidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageDetails extends AppCompatActivity {
    TextView textViewIdNumber, textViewMsg;

    Button btnDelMsg;
    Bundle bun;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_message_details);

        btnDelMsg = (Button) findViewById(R.id.btnDeleteMsg);

        textViewIdNumber = (TextView)findViewById(R.id.textViewIdNumber);
        textViewMsg = (TextView)findViewById(R.id.textViewMsg);

        //Step 3, create fragment onCreation, pass data from Intent Extras to FragmentTransction
        MessageFragment frag = new MessageFragment();
        bun = getIntent().getExtras();
        Log.i("id came from chat", Long.toString(bun.getLong("ID")));

        frag.setArguments( bun );
        // getFragmentManager().beginTransaction().add(R.id.FragmentHolderLayout, frag).commit();

        textViewIdNumber.setText(Long.toString(bun.getLong("ID")));
        textViewMsg.setText(bun.getString("msgTxt"));

        btnDelMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                // getActivity().setResult(int resultCode, Intent data){

                Intent returnIntent = new Intent();
                String str = Long.toString(bun.getLong("ID"));
                returnIntent.putExtra("ID",str);
                setResult(1, returnIntent);
                //    (getActivity().setResult(int resultCode, Intent data));

                finish();

            }
        });

    }












}
