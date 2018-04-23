package com.example.qatt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.qatt.database.DaoApplication;
import com.example.qatt.database.ScanRepository;

import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.ScanDao;

public class DropDatabase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_database);
    }

    public void cancelDropping(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void drop(View view){
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

    }

    public void dropDB(){
        ScanRepository.clearScan(this.getApplicationContext());
        Toast.makeText(this,"Attendance database has been dropped.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
