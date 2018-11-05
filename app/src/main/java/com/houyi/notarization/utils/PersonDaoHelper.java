package com.houyi.notarization.utils;

import com.houyi.notarization.mode.DaoSession;
import com.houyi.notarization.mode.Person;

/**
 * @author changhua.jiang
 * @since 2018/11/5 8:15 PM
 */

public class PersonDaoHelper {
    public static void insertPerson(Person person){
        try {
            DaoSession daoSession = ApplicationUtils.getApplication().getDaoSession();
            daoSession.insert(person);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
