package com.example.qatt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
        ScanRepository.clearScan(this.getApplicationContext());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
}
