package com.houyi.notarization.utils;

import android.database.Cursor;

import com.houyi.notarization.mode.DaoSession;
import com.houyi.notarization.mode.Notarization;
import com.houyi.notarization.mode.NotarizationDao;

import java.util.List;

/**
 * @author changhua.jiang
 * @since 2018/11/5 5:54 PM
 */

public class NotaDaoHelper {
    //创建一个新的nota
    public static Notarization createNota(){

        DaoSession daoSession = getDaoSession();
        Cursor cursor = daoSession.getDatabase().rawQuery("SELECT MAX(nid) from Notarization", null);
        Long nid = 1L;
        while(cursor.moveToNext()){
            nid = cursor.getLong(0);
        }
        cursor.close();
        Notarization nota = new Notarization();
        nota.setNid(nid + 1);
        nota.setFaceVerify(-1);
        nota.setFp1Verify(-1);
        nota.setFp2Verify(-1);
        return nota;
    }

    public static long  insertNota(Notarization nota){
        if(nota == null)
            throw new NullPointerException("nota null");
        DaoSession daoSession = getDaoSession();
//        if (nota.getPerson() != null){
//            try {
//                daoSession.insert(nota.getPerson());
//            }
//            catch (Exception e){
//
//            }
//        }
        return daoSession.insert(nota);
    }

    public static List<Notarization> selectAllNota(){
        NotarizationDao dao = getDaoSession().getNotarizationDao();
        return dao.loadAll();
    }

    public static List<Notarization> selectNota(String pid,long nid){
        NotarizationDao dao = getDaoSession().getNotarizationDao();
        List<Notarization> list = dao.queryBuilder().where(NotarizationDao.Properties.IdentityNo.eq(pid),
                NotarizationDao.Properties.Nid.eq(nid)
                ).list();
        return list;
    }

    public static List<Notarization> selectNota(int role){
        NotarizationDao dao = getDaoSession().getNotarizationDao();
        return dao.queryBuilder().where(NotarizationDao.Properties.Role.eq(role)).list();
    }

    public static List<Notarization> selectNota(Long nid){
        NotarizationDao dao = getDaoSession().getNotarizationDao();
        return dao.queryBuilder().where(NotarizationDao.Properties.Nid.eq(nid)).list();
    }

    public static void removeNota(Notarization nota){
        DaoSession daoSession = getDaoSession();
        daoSession.delete(nota);
    }

    public static DaoSession getDaoSession(){
        DaoSession daoSession = ApplicationUtils.getApplication().getDaoSession();
        return daoSession;
    }
}
