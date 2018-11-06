package com.houyi.notarization.mode;

import com.houyi.notarization.utils.VerifyConst;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
@Entity
public class Notarization {
    @Id(autoincrement = true)
    private Long id;
    private Long nid;
    private String identityNo;
    @ToOne(joinProperty = "identityNo")
    private Person person;
    private int role;// 0,1
    private String notarization;
    private int fp1Verify;
    private int fp2Verify;
    private Date fp1VerifyTime;
    private Date fp2VerifyTime;
    private int faceVerify;
    private Date faceVerifyTime;

    //公证人
    private String notary;
    //公证时间
    private Date createTime;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2061033714)
    private transient NotarizationDao myDao;

    @Generated(hash = 1752492272)
    public Notarization(Long id, Long nid, String identityNo, int role,
            String notarization, int fp1Verify, int fp2Verify, Date fp1VerifyTime,
            Date fp2VerifyTime, int faceVerify, Date faceVerifyTime, String notary,
            Date createTime) {
        this.id = id;
        this.nid = nid;
        this.identityNo = identityNo;
        this.role = role;
        this.notarization = notarization;
        this.fp1Verify = fp1Verify;
        this.fp2Verify = fp2Verify;
        this.fp1VerifyTime = fp1VerifyTime;
        this.fp2VerifyTime = fp2VerifyTime;
        this.faceVerify = faceVerify;
        this.faceVerifyTime = faceVerifyTime;
        this.notary = notary;
        this.createTime = createTime;
    }
    @Generated(hash = 928317750)
    public Notarization() {
    }
    @Generated(hash = 1979224670)
    private transient String person__resolvedKey;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getNid() {
        return this.nid;
    }
    public void setNid(Long nid) {
        this.nid = nid;
    }
    public int getRole() {
        return this.role;
    }
    public void setRole(int role) {
        this.role = role;
    }
    public int getFp1Verify() {
        return this.fp1Verify;
    }
    public void setFp1Verify(int fp1Verify) {
        this.fp1Verify = fp1Verify;
    }
    public int getFp2Verify() {
        return this.fp2Verify;
    }
    public void setFp2Verify(int fp2Verify) {
        this.fp2Verify = fp2Verify;
    }
    public int getFaceVerify() {
        return this.faceVerify;
    }
    public void setFaceVerify(int faceVerify) {
        this.faceVerify = faceVerify;
    }
    public String getNotary() {
        return this.notary;
    }
    public void setNotary(String notary) {
        this.notary = notary;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getIdentityNo() {
        return this.identityNo;
    }
    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }
    public String getNotarization() {
        return this.notarization;
    }
    public void setNotarization(String notarization) {
        this.notarization = notarization;
    }
    public Date getFp1VerifyTime() {
        return this.fp1VerifyTime;
    }
    public void setFp1VerifyTime(Date fp1VerifyTime) {
        this.fp1VerifyTime = fp1VerifyTime;
    }
    public Date getFp2VerifyTime() {
        return this.fp2VerifyTime;
    }
    public void setFp2VerifyTime(Date fp2VerifyTime) {
        this.fp2VerifyTime = fp2VerifyTime;
    }
    public Date getFaceVerifyTime() {
        return this.faceVerifyTime;
    }
    public void setFaceVerifyTime(Date faceVerifyTime) {
        this.faceVerifyTime = faceVerifyTime;
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 339359153)
    public Person getPerson() {
        String __key = this.identityNo;
        if (person__resolvedKey == null || person__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PersonDao targetDao = daoSession.getPersonDao();
            Person personNew = targetDao.load(__key);
            synchronized (this) {
                person = personNew;
                person__resolvedKey = __key;
            }
        }
        return person;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 554982177)
    public void setPerson(Person person) {
        synchronized (this) {
            this.person = person;
            identityNo = person == null ? null : person.getIdentityNo();
            person__resolvedKey = identityNo;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    public boolean hasCompared(){
        if(this.fp1Verify > VerifyConst.FP_VERIFY_THRESHOLD || this.fp2Verify > VerifyConst.FP_VERIFY_THRESHOLD || this.faceVerify > VerifyConst.FACE_VERIFY_THRESHOLD){
            return true;
        }
        else{
            return false;
        }
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1894342372)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNotarizationDao() : null;
    }

}
