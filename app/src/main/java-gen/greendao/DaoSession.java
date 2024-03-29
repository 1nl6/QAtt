package greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Scan;

import greendao.ScanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig scanDaoConfig;

    private final ScanDao scanDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        scanDaoConfig = daoConfigMap.get(ScanDao.class).clone();
        scanDaoConfig.initIdentityScope(type);

        scanDao = new ScanDao(scanDaoConfig, this);

        registerDao(Scan.class, scanDao);
    }
    
    public void clear() {
        scanDaoConfig.getIdentityScope().clear();
    }

    public ScanDao getScanDao() {
        return scanDao;
    }

}
