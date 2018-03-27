package com.example.qatt.database;

import android.content.Context;

import java.util.List;

import greendao.Scan;
import greendao.ScanDao;

public class ScanRepository {

    //Insert or update
    public static void insertOrUpdate(Context context, Scan attendance) {
        getScanDao(context).insertOrReplace(attendance);
    }

    //Delete all
    public static void clearScan(Context context) {
        getScanDao(context).deleteAll();
    }

    //Delete specific
    public static void deleteScanWithId(Context context, long id) {
        getScanDao(context).delete(getScanForId(context, id));
    }

    //Get all
    public static List<Scan> getAllScans(Context context) {
        return getScanDao(context).loadAll();
    }

    //Get specific
    public static Scan getScanForId(Context context, long id) {
        return getScanDao(context).load(id);
    }

    //Gets database connection
    private static ScanDao getScanDao(Context c){
        return ((DaoApplication) c.getApplicationContext()).getDaoSession().getScanDao();
    }
}
