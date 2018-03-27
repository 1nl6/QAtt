package greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Student;
import greendao.Attendance;
import greendao.Scan;

import greendao.StudentDao;
import greendao.AttendanceDao;
import greendao.ScanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig studentDaoConfig;
    private final DaoConfig attendanceDaoConfig;
    private final DaoConfig scanDaoConfig;

    private final StudentDao studentDao;
    private final AttendanceDao attendanceDao;
    private final ScanDao scanDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        studentDaoConfig = daoConfigMap.get(StudentDao.class).clone();
        studentDaoConfig.initIdentityScope(type);

        attendanceDaoConfig = daoConfigMap.get(AttendanceDao.class).clone();
        attendanceDaoConfig.initIdentityScope(type);

        scanDaoConfig = daoConfigMap.get(ScanDao.class).clone();
        scanDaoConfig.initIdentityScope(type);

        studentDao = new StudentDao(studentDaoConfig, this);
        attendanceDao = new AttendanceDao(attendanceDaoConfig, this);
        scanDao = new ScanDao(scanDaoConfig, this);

        registerDao(Student.class, studentDao);
        registerDao(Attendance.class, attendanceDao);
        registerDao(Scan.class, scanDao);
    }
    
    public void clear() {
        studentDaoConfig.getIdentityScope().clear();
        attendanceDaoConfig.getIdentityScope().clear();
        scanDaoConfig.getIdentityScope().clear();
    }

    public StudentDao getStudentDao() {
        return studentDao;
    }

    public AttendanceDao getAttendanceDao() {
        return attendanceDao;
    }

    public ScanDao getScanDao() {
        return scanDao;
    }

}
