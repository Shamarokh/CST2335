package com.example.shama.androidlabs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;



public class TestToolbar extends AppCompatActivity {
    private static final String LOG = "TestToolbar";
    String snackbarMessage = "You selected item 1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu );
        return true;
    }

    // Lab 8-6: The last part is to respond to one of the items being selected. Write the method
    // public boolean onOptionsItemSelected(MenuItem mi).
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.Camera:
                Log.d(LOG, "Camera menu selected.");
                // Lab 8-8: Set the case statements for your first menu item to display a Snackbar.
                Snackbar.make(findViewById(R.id.toolbar), snackbarMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.Chat:
                Log.d(LOG, "Chat menu selected.");
                // Lab 8-9: you must add your own String resources for the Title, the Positive Button text,
                // and the Negative button text. The title should be “Do you want to go back?” and
                // if the user selects the positive button, then finish the current activity.
                // If they select the negative button then do nothing.
                // Set this to be the code for selecting on the second toolbar item.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("cancel");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //onBackPressed();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.Music:
                Log.d(LOG, "Music menu selected.");
                // Lab 8-10: Use the custom layout given in the example, but download another free icon
                // from the website mentioned in step 4.
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.new_message);

                dialogBuilder.setPositiveButton(R.string.change_message, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        snackbarMessage = edt.getText().toString();
                        Snackbar.make(findViewById(android.R.id.content), "Message is changed.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                });
                dialogBuilder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                     //   onBackPressed();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
                break;
            // Lab 8-7: make it show a Toast saying “Version 1.0, by Your name”,
            // with your actual name in the string.
            case R.id.menu_about:
                Toast.makeText(TestToolbar.this, "Version 1.0, by Shama", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
