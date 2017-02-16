package com.example.shama.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    static ListView listView = null;
    EditText editText;
    static Button sendButton = null;
    ArrayList<String> messages;
    ChatAdapter messageAdapter;
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        final ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(ChatWindow.this); // Step 5 of Lab 5
        db = dbHelper.getWritableDatabase(); // Step 5 of Lab 5

        ListView listView = (ListView) findViewById(R.id.listView);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final Button sendButton = (Button) findViewById(R.id.button3);
        messages = new ArrayList<String>();

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messages.add(editText.getText().toString());

                ContentValues contentValues = new ContentValues(); // Step 6 for Lab 5
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, editText.getText().toString()); // Step 6 for Lab 5
                db.insert(ChatDatabaseHelper.CHAT_TABLE, "", contentValues); // Step 6 for Lab 5- insert takes 3 parameters- tablename,
                //nullColumnHack to check for the null columns, ContentValues object

                messageAdapter.notifyDataSetChanged();
                editText.setText("");

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            db.close();
            dbHelper.close();
        } catch (Exception e) {
        }
    }

    private class ChatAdapter extends ArrayAdapter {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            messages.clear();
            // Step 5 of Lab 5
            Cursor cursor;
            cursor = db.rawQuery(ChatDatabaseHelper.READALL_CHAT_TABLE, null); //it takes 2 parameters- string sql and string[] selectionArgs
            int messageIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);

            // Print an information message about the Cursor
            Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
            // Then use a for loop to print out the name of each column returned by the cursor.
            for (int colIndex = 0; colIndex < cursor.getColumnCount(); colIndex++) {
                Log.i(ACTIVITY_NAME, "Column name of " + colIndex + " = " + cursor.getColumnName(colIndex));
            }

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                messages.add(cursor.getString(messageIndex));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(messageIndex));
                cursor.moveToNext();
            }


            return messages.size();
        }

        @Override
        public String getItem(int position) {

            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null ;
            if(position%2 == 0){
                result = inflater.inflate(R.layout.chat_row_incoming, null);}
            else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);}

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));

            return result;
        }


    }


}
