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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import android.content.Intent;

import android.widget.AdapterView;


public class ChatWindow extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    static ListView listView = null;
    EditText editText;
    static Button sendButton = null;
    ArrayList<String> messages;
    ChatAdapter messageAdapter;
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;

    // lab 7
    protected boolean flag_FrameLayoutExsist=false;
    String str2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_window);
        //Lab7 - Check if FrameLayout has been uploaded
        flag_FrameLayoutExsist = (findViewById(R.id.FragmentHolderLayout) != null); //find out if this is a phone or tablet

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
        messages.clear();
        // Step 5 of Lab 5

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


        //Lab7 - Add onItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {
                Bundle bun = new Bundle();
                cursor.moveToPosition(position);
                bun.putLong("ID", getItemId(position) );
                bun.putString("msgTxt",str2);
                if(!flag_FrameLayoutExsist) {
                    //    MessageFragment frag = new MessageFragment();

                    //  frag.setArguments(bun);

                    //   getFragmentManager().beginTransaction().add(R.id.FragmentHolderLayout, frag).commit();
                    //}
                    //step 3 if a phone, transition to empty Activity that has FrameLayout
                    //else //isPhone
                    // {
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(bun);
                    startActivityForResult(intent, 5);
                }
            }

        });
    }
//end of oncreate()
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();


    }



    private class ChatAdapter extends ArrayAdapter {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {


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

            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));

            return result;
        }


    }

    //For lab7
    public long getItemId(int position) {
        Log.i("this is the position", String.valueOf(position));

        cursor.moveToPosition(position);
        Log.i("c moved to position", String.valueOf(position));

        int id_columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID);
        String str = cursor.getString(id_columnIndex);
        Log.i("c read this item", str);

        id_columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        str2 = cursor.getString(id_columnIndex);

        int value = Integer.parseInt(str);


        return value;
    }

        //onActivityResult of lab7
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
            final SQLiteDatabase db = dbHelper.getWritableDatabase();
            Log.i("onActivityResult","");

            if (resultCode == 1) {
                String id = returnedIntent.getStringExtra("ID");
                //   int id_columnIndex = cursor.getColumnIndex(ChatDatabaseHelper.ID_COLUMN);
                String str = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                Log.i("the msg",str);
                // int position = messageAdapter.getPosition(str);
                Log.i("position is",String.valueOf(messages.indexOf(str)) );

                messages.remove(messages.indexOf(str));
                db.delete(ChatDatabaseHelper.CHAT_TABLE, ChatDatabaseHelper.KEY_ID +"=?", new String[]{id});
                // messageAdapter.remove(messageAdapter.getItem(position));
                messageAdapter.notifyDataSetChanged();
                //  db.delete(ChatDatabaseHelper.databaseName, ChatDatabaseHelper.ID_COLUMN +"=?", new String[]{id});


            }
        }


    public void onDestroy() {

        super.onDestroy();
        dbHelper.close();

    }

}
