package com.jch.mydemo.mode;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Identity implements Parcelable {
   @Id(autoincrement = true)
   private Long id;
   private String identityNo;
   private String name;
   private String address;
   private String sex;
   private String items;
   private String comparison;

   @Transient
   private String year;
   @Transient
   private String month;
   @Transient
   private String day;

   @Transient
   private byte[] image;
   @Transient
   private String nation;
   @Transient
   private String issuinAuthority;
   @Transient
   private byte[] fp1;
   @Transient
   private byte[] fp2;



   @Generated(hash = 1109470565)
   public Identity(Long id, String identityNo, String name, String address,
           String sex, String items, String comparison) {
       this.id = id;
       this.identityNo = identityNo;
       this.name = name;
       this.address = address;
       this.sex = sex;
       this.items = items;
       this.comparison = comparison;
   }

   @Generated(hash = 103828829)
   public Identity() {
   }

   protected Identity(Parcel in) {
      if (in.readByte() == 0) {
         id = null;
      } else {
         id = in.readLong();
      }
      identityNo = in.readString();
      name = in.readString();
      address = in.readString();
      sex = in.readString();
      items = in.readString();
      comparison = in.readString();
      in.readByteArray(image);
      nation = in.readString();
      issuinAuthority = in.readString();
      in.readByteArray(fp1);
      in.readByteArray(fp2);

   }

   public static final Creator<Identity> CREATOR = new Creator<Identity>() {
      @Override
      public Identity createFromParcel(Parcel in) {
         return new Identity(in);
      }

      @Override
      public Identity[] newArray(int size) {
         return new Identity[size];
      }
   };

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

   @Override
   public int describeContents() {
      return 0;
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

   public byte[] getFp1() {
      return fp1;
   }

   public void setFp1(byte[] fp1) {
      this.fp1 = fp1;
   }

   public byte[] getFp2() {
      return fp2;
   }

   public void setFp2(byte[] fp2) {
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

   @Override
   public void writeToParcel(Parcel dest, int flags) {

      if (id == null) {
         dest.writeByte((byte) 0);
      } else {
         dest.writeByte((byte) 1);
         dest.writeLong(id);
      }
      dest.writeString(identityNo);
      dest.writeString(name);
      dest.writeString(address);
      dest.writeString(sex);
      dest.writeString(items);
      dest.writeString(comparison);
      dest.writeByteArray(image);
      dest.writeString(nation);
      dest.writeString(issuinAuthority);
      dest.writeByteArray(fp1);
      dest.writeByteArray(fp2);
   }

}
