package edu.rosehulman.keinslc.jersey;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView mPlayerNameTextView;
    private TextView mPlayerNumberTextView;
    private ImageView mImageView;
    private Jersey mJersey;

    // For the saved preferences
    private final static String PREFS = "PREFS";
    private static final String KEY_JERSEY_NAME = "KEY_JERSEY_NAME";
    private static final String KEY_JERSEY_NUMBER = "KEY_JERSEY_NUMBER";
    private static final String KEY_JERSEY_IS_RED = "KEY_JERSEY_IS_RED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Capturing Views and initializing the jersey
        mPlayerNameTextView = (TextView) findViewById(R.id.playerNameTextView);
        mPlayerNumberTextView = (TextView) findViewById(R.id.playerNumberTextView);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mJersey = new Jersey();

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String name = prefs.getString(KEY_JERSEY_NAME, getString(R.string.default_jersey_name));
        mJersey.setName(name);
        int num = prefs.getInt(KEY_JERSEY_NUMBER, 17);
        mJersey.setNumber(num);
        boolean isRed = prefs.getBoolean(KEY_JERSEY_IS_RED, true);
        mJersey.setIsRed(isRed);
        showCurrentJersey();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editJersy();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_JERSEY_NAME, mJersey.getName());
        editor.putInt(KEY_JERSEY_NUMBER, mJersey.getNumber());
        editor.putBoolean(KEY_JERSEY_IS_RED,mJersey.getIsRed());
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_context_reset:
                final Jersey temp = mJersey;
                mJersey = new Jersey();
                showCurrentJersey();
                // Capture the coordinator layout, had to create the ID in activity_main
                View coordinator = findViewById(R.id.coordinator_layout);
                // Creates the snackbar using the .make
                Snackbar snackbar = Snackbar.make(coordinator, R.string.confirmation_dialog_message, Snackbar.LENGTH_LONG);
                // Sets the action on the snackbar
                snackbar.setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Restore the deleted item
                        mJersey = temp;
                        showCurrentJersey();
                    }
                });
                snackbar.show();
                return true;
            case R.id.action_settings:
                // load the language
                startActivityForResult(new Intent(Settings.ACTION_LOCALE_SETTINGS), 0);
                showCurrentJersey();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editJersy() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit, null, false);
        builder.setView(view);

        final EditText playerNameEditText = (EditText) view.findViewById(R.id.nameEditText);
        final EditText playerNumberEditText = (EditText) view.findViewById(R.id.playerNumberEditText);
        final Switch isRedSwitch = (Switch) view.findViewById(R.id.edit_switch);

        playerNameEditText.setText(mJersey.getName());
        playerNumberEditText.setText(mJersey.getNumber() + "");
        isRedSwitch.setChecked(mJersey.getIsRed());

        // Do nothing if they hit cancel
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mJersey.setName(playerNameEditText.getText().toString());
                String numString = playerNumberEditText.getText().toString();
                if(numString.isEmpty()){
                    numString = "0";
                }
                mJersey.setNumber(Integer.parseInt(numString));
                mJersey.setIsRed(isRedSwitch.isChecked());
                showCurrentJersey();
            }
        });
        builder.create().show();


    }

    private void showCurrentJersey() {
        mPlayerNumberTextView.setText(mJersey.getNumber() + "");
        mPlayerNameTextView.setText(mJersey.getName());
        if (mJersey.getIsRed()) {
            mImageView.setImageResource(R.drawable.red_jersey);
        } else {
            mImageView.setImageResource(R.drawable.blue_jersey);
        }
    }

}
