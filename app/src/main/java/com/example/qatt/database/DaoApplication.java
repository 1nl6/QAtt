package com.example.qatt.database;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import greendao.DaoMaster;
import greendao.DaoSession;

public class DaoApplication extends Application {
    public DaoSession daoSession;

    public void onCreate(){
        super.onCreate();
        setupDatabase();
    }

    public void setupDatabase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "attendance-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        //daoMaster.dropAllTables(db,true);
        daoMaster.createAllTables(db, true);
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

}
