package com.houyi.notarization.mode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;

//公证事项
public class Notarization {
    @Id(autoincrement = true)
    private Long id;
    //申请人身份
    @ToOne(joinProperty = "identityNo")
    private Identity applicant;
    //公证人
    private String notary;
    //公证时间
    private Date createTime;
}
