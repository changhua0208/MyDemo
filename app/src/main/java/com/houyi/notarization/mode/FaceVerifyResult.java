package com.houyi.notarization.mode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Date;

/**
 * @author changhua.jiang
 */

@Entity(indexes = {
        @Index(value = "identityNo DESC", unique = true)
})
public class FaceVerifyResult {
    @Id(autoincrement = true)
    private Long fid;
    private String identityNo;
    private int score;
    private Date verifyTime;

public FaceVerifyResult(Long fid, String identityNo, String currentImage,
        int score, Date verifyTime) {
    this.fid = fid;
    this.identityNo = identityNo;
    this.score = score;
    this.verifyTime = verifyTime;
}

public FaceVerifyResult() {
}

@Generated(hash = 682899738)
public FaceVerifyResult(Long fid, String identityNo, int score,
        Date verifyTime) {
    this.fid = fid;
    this.identityNo = identityNo;
    this.score = score;
    this.verifyTime = verifyTime;
}
public Long getFid() {
    return this.fid;
}
public void setFid(Long fid) {
    this.fid = fid;
}
public String getIdentityNo() {
    return this.identityNo;
}
public void setIdentityNo(String identityNo) {
    this.identityNo = identityNo;
}
public int getScore() {
    return this.score;
}
public void setScore(int score) {
    this.score = score;
}
public Date getVerifyTime() {
    return this.verifyTime;
}
public void setVerifyTime(Date verifyTime) {
    this.verifyTime = verifyTime;
}
}
