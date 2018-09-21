package com.jch.mydemo.mode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * @author changhua.jiang
 */
@Entity(indexes = {
        @Index(value = "identityNo DESC", unique = true)
})
public class FpVerifyResult {
    @Id(autoincrement = true)
    private Long fid;
    private String identityNo;
    private String fp1;
    private String fp2;
    private int score1;
    private int score2;
    private Date verifyTime;
@Generated(hash = 1226145485)
public FpVerifyResult(Long fid, String identityNo, String fp1, String fp2,
        int score1, int score2, Date verifyTime) {
    this.fid = fid;
    this.identityNo = identityNo;
    this.fp1 = fp1;
    this.fp2 = fp2;
    this.score1 = score1;
    this.score2 = score2;
    this.verifyTime = verifyTime;
}
@Generated(hash = 1737078107)
public FpVerifyResult() {
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
public String getFp1() {
    return this.fp1;
}
public void setFp1(String fp1) {
    this.fp1 = fp1;
}
public String getFp2() {
    return this.fp2;
}
public void setFp2(String fp2) {
    this.fp2 = fp2;
}
public int getScore1() {
    return this.score1;
}
public void setScore1(int score1) {
    this.score1 = score1;
}
public int getScore2() {
    return this.score2;
}
public void setScore2(int score2) {
    this.score2 = score2;
}
public Date getVerifyTime() {
    return this.verifyTime;
}
public void setVerifyTime(Date verifyTime) {
    this.verifyTime = verifyTime;
}
}
