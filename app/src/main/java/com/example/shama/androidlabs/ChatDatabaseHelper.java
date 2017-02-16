package com.example.shama.androidlabs;

/**
 * Created by Shama on 2017-02-14.
 */



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ChatDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase";   // Database name
    private static final int VERSION_NUM = 8;   // Version Number

    public static final String CHAT_TABLE = "table_chat"; // Table Name
    public static final String KEY_ID = "id";
    public static final String KEY_MESSAGE = "message";

    private static final String ACTIVITY_NAME = "ChatDatabaseHelper"; // Step 7 for Lab 5

    public static final String CREATE_CHAT_TABLE = "CREATE TABLE " + CHAT_TABLE
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MESSAGE + " TEXT NOT NULL)";

    public static final String DROP_CHAT_TABLE = "DROP TABLE IF EXISTS " + CHAT_TABLE;

    public static final String READALL_CHAT_TABLE = "SELECT " + KEY_ID + ", " + KEY_MESSAGE
            + " FROM " + CHAT_TABLE; // step 5 for lab 5



    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_CHAT_TABLE);
        Log.i(ACTIVITY_NAME, "Calling onCreate"); // Step 7 for Lab 5
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DROP_CHAT_TABLE);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion); // Step 7 for Lab 5
    }

}
