package com.example.qatt.database;

import android.content.Context;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import greendao.Student;
import greendao.StudentDao;


public class StudentRepository {
    //Gets database connection
    private static StudentDao getStudentDao(Context c){
        return ((DaoApplication) c.getApplicationContext()).getDaoSession().getStudentDao();
    }

    //Get all students
    public static List<Student> getAllStudents(Context context) {
        return getStudentDao(context).loadAll();
    }

    //Get specific student with primary key
    public static Student getStudentForId(Context context, long id) {
        return getStudentDao(context).load(id);
    }

    //Get specific student with netID
    public static Student getStudent(Context c, String netID){
        QueryBuilder<Student> student = getStudentDao(c).queryBuilder().where(StudentDao.Properties.NetID.eq(netID)).limit(1);
        return student.unique();
    }

    //Insert or update
    public static void insertOrUpdate(Context context, Student student) {
        getStudentDao(context).insertOrReplace(student);
    }

    //Delete all students
    public static void clearStudent(Context context) {
        getStudentDao(context).deleteAll();
    }

    //Delete specific student
    public static void deleteStudentWithId(Context context, long id) {
        getStudentDao(context).delete(getStudentForId(context, id));
    }
}
