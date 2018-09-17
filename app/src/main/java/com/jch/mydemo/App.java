package com.jch.mydemo;

import android.app.Application;

import com.jch.mydemo.mode.DaoMaster;
import com.jch.mydemo.mode.DaoSession;
import com.jch.mydemo.utils.ApplicationUtils;
import com.jch.mydemo.utils.IdentityHelper;

import org.greenrobot.greendao.database.Database;

/**
 * @author changhua.jiang
 * @since 2018/9/10 下午7:43
 */

public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationUtils.init(this);
        IdentityHelper.getInstance().init(this);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"mydemo");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }
}
