package com.example.qatt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qatt.database.DaoApplication;
import com.example.qatt.database.ScanRepository;

import java.util.Random;

import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.ScanDao;

public class DropDatabase extends AppCompatActivity {

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
    public static final int CONFIRMATION_LENGTH = 6;
    String confirmString;
    EditText confirmInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_database);
        setup();

        confirmInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    matchStrings();
                    return true;
                }
                return false;
            }
        });
    }

    public void setup(){
        //Generate random string for confirmation
        confirmString = random();
        TextView confirmText = findViewById(R.id.confirmText);
        confirmText.setText(confirmString);

        //EditTExt
        confirmInput = (EditText) findViewById(R.id.confirmInput);
        confirmInput.setText("");

    }


    public static String random() {
        Random random=new Random();
        StringBuilder sb=new StringBuilder(CONFIRMATION_LENGTH);
        for(int i=0;i<CONFIRMATION_LENGTH;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


    public void cancelDropping(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void drop(View view){
        matchStrings();
    }

    public void matchStrings(){
        if(confirmInput.getText().toString().equals(confirmString)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You'll lose all you scan records!")
                    .setTitle("Drop attendance database?");
            builder.setPositiveButton("Drop", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dropDB();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else{
            Toast.makeText(this,"Input did not match.", Toast.LENGTH_SHORT).show();
            setup();
        }
    }

    public void dropDB(){
        ScanRepository.clearScan(this.getApplicationContext());
        Toast.makeText(this,"Attendance database has been dropped.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
