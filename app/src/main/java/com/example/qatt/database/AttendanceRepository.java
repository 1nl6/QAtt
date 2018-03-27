package com.example.qatt.database;

import android.content.Context;

import java.util.List;

import greendao.Attendance;
import greendao.AttendanceDao;


public class AttendanceRepository {
    //Insert or update attendance
    public static void insertOrUpdate(Context context, Attendance attendance) {
        getAttendanceDao(context).insertOrReplace(attendance);
    }

    //Delete all attendance
    public static void clearAttendence(Context context) {
        getAttendanceDao(context).deleteAll();
    }

    //Delete specific attendance
    public static void deleteAttendanceWithId(Context context, long id) {
        getAttendanceDao(context).delete(getAttendanceForId(context, id));
    }

    //Get all attendance
    public static List<Attendance> getAllAttendances(Context context) {
        return getAttendanceDao(context).loadAll();
    }

    //Get specific attendance
    public static Attendance getAttendanceForId(Context context, long id) {
        return getAttendanceDao(context).load(id);
    }

    //Gets database connection for attendance object
    private static AttendanceDao getAttendanceDao(Context c){
        return ((DaoApplication) c.getApplicationContext()).getDaoSession().getAttendanceDao();
    }
}
