package com.example.qatt.database;

import android.content.Context;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
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

    //Get all scans of specific week
    public static List<Scan> getWeekScan(Context c, int week){
        List <Scan> scans = getScanDao(c).queryBuilder()
                .where(ScanDao.Properties.Week.eq(week))
                .list();
        return scans;
    }

    //Find if a student has already been scanned before
    public static boolean studentScanned(Context c, String netID, int week){
        List <Scan> scans = getScanDao(c).queryBuilder()
                .where(ScanDao.Properties.Week.eq(week), ScanDao.Properties.NetID.eq(netID))
                .list();
        return scans.size() > 0;
    }
}
