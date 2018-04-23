package com.example.qatt.database;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.Scan;

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
        daoMaster.dropAllTables(db,true);
        daoMaster.createAllTables(db, true);
        addToDatabase();

    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

    public void addToDatabase(){
        Scan s = new Scan();
        s.setNetID("1nl6");
        s.setAttendance(1);
        s.setScanTime("11:30");
        s.setScanDate("Monday");
        s.setWeek(1);
        ScanRepository.insertOrUpdate(this, s);

    }


    public void dropDatabase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "attendance-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        daoMaster.dropAllTables(db,true);
        daoMaster.createAllTables(db, true);
    }


}
