package com.houyi.notarization;

import android.app.Application;

import com.houyi.notarization.mode.DaoMaster;
import com.houyi.notarization.mode.DaoSession;
import com.houyi.notarization.utils.ApplicationUtils;
import com.houyi.notarization.utils.IdentityHelper;
import com.houyi.notarization.utils.NotaFileHelper;

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
        NotaFileHelper.getInstance().init(this);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"mydemo");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();


//        AsyncTask task = new AsyncTask() {
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                try {
//                    ServiceFactory.init(App.this);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        };
//        task.execute();
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }
}
