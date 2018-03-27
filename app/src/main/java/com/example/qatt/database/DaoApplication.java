package com.example.qatt.database;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.Scan;
import greendao.Student;

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
        addStudent();
        addScan();
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

    public void addStudent(){
        Student s = new Student();
        s.setName("Test Person");
        s.setNetID("1we1");
        StudentRepository.insertOrUpdate(this, s);
    }

    public void addScan(){
        Scan s = new Scan();
        s.setNetID("11we2");
        s.setAttendance(1);
        s.setScanDate("Monday");
        s.setScanTime("12:30");
        s.setWeek(3);
        ScanRepository.insertOrUpdate(this, s);

        Scan c = new Scan();
        c.setNetID("4hk4");
        c.setAttendance(1);
        c.setScanDate("Tuesday");
        c.setScanTime("11:30");
        c.setWeek(2);
        ScanRepository.insertOrUpdate(this, c);
    }

}
