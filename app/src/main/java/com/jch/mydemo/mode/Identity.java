package com.jch.mydemo.mode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 身份
 * @author changhua.jiang
 * @since 2018/9/10 下午7:32
 */
@Entity(indexes = {
        @Index(value = "identityNo DESC", unique = true)
})
public class Identity  {
   @Id(autoincrement = true)
   private Long id;
   private String identityNo;
   private String name;
   private String address;
   private String sex;
   private String items;
   private String comparison;
   private String year;
   private String month;
   private String day;

   @Transient
   private byte[] image;
   private String nation;
   private String issuinAuthority;
   private String fp1Name;
   private String fp2Name;
   @Transient
   private String fp1;
   @Transient
   private String fp2;
   private String beginTime;
   private String endTime;



   @Generated(hash = 733722077)
   public Identity(Long id, String identityNo, String name, String address,
           String sex, String items, String comparison, String year, String month,
           String day, String nation, String issuinAuthority, String fp1Name,
           String fp2Name, String beginTime, String endTime) {
       this.id = id;
       this.identityNo = identityNo;
       this.name = name;
       this.address = address;
       this.sex = sex;
       this.items = items;
       this.comparison = comparison;
       this.year = year;
       this.month = month;
       this.day = day;
       this.nation = nation;
       this.issuinAuthority = issuinAuthority;
       this.fp1Name = fp1Name;
       this.fp2Name = fp2Name;
       this.beginTime = beginTime;
       this.endTime = endTime;
   }

   @Generated(hash = 103828829)
   public Identity() {
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getItems() {
      return items;
   }

   public void setItems(String items) {
      this.items = items;
   }

   public String getComparison() {
      return comparison;
   }

   public void setComparison(String comparison) {
      this.comparison = comparison;
   }

   public String getIdentityNo() {
       return this.identityNo;
   }

   public void setIdentityNo(String identityNo) {
       this.identityNo = identityNo;
   }

   public String getSex() {
       return this.sex;
   }

   public void setSex(String sex) {
       this.sex = sex;
   }

   public byte[] getImage() {
      return image;
   }

   public void setImage(byte[] image) {
      this.image = image;
   }

   public String getNation() {
      return nation;
   }

   public void setNation(String nation) {
      this.nation = nation;
   }

   public String getIssuinAuthority() {
      return issuinAuthority;
   }

   public void setIssuinAuthority(String issuinAuthority) {
      this.issuinAuthority = issuinAuthority;
   }

   public String getFp1() {
      return fp1;
   }

   public void setFp1(String fp1) {
      this.fp1 = fp1;
   }

   public String getFp2() {
      return fp2;
   }

   public void setFp2(String fp2) {
      this.fp2 = fp2;
   }

   public String getYear() {
      return year;
   }

   public void setYear(String year) {
      this.year = year;
   }

   public String getMonth() {
      return month;
   }

   public void setMonth(String month) {
      this.month = month;
   }

   public String getDay() {
      return day;
   }

   public void setDay(String day) {
      this.day = day;
   }

   public String getFp1Name() {
       return this.fp1Name;
   }

   public void setFp1Name(String fp1Name) {
       this.fp1Name = fp1Name;
   }

   public String getFp2Name() {
       return this.fp2Name;
   }

   public void setFp2Name(String fp2Name) {
       this.fp2Name = fp2Name;
   }

   public String getBirthDay(){
      return getYear() + "年" + getMonth() + "月" + getDay() + "日";
   }

   @Override
   public boolean equals(Object obj) {
      if(obj != null && obj instanceof  Identity){
         Identity i = (Identity) obj;
         if(getIdentityNo().equals(i.getIdentityNo())){
            return true;
         }
      }
      return false;
   }

   public String getBeginTime() {
       return this.beginTime;
   }

   public void setBeginTime(String beginTime) {
       this.beginTime = beginTime;
   }

   public String getEndTime() {
       return this.endTime;
   }

   public void setEndTime(String endTime) {
       this.endTime = endTime;
   }

}
