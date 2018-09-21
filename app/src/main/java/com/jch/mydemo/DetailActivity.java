package com.jch.mydemo;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jch.mydemo.mode.DaoSession;
import com.jch.mydemo.mode.FaceVerifyResult;
import com.jch.mydemo.mode.FaceVerifyResultDao;
import com.jch.mydemo.mode.FpVerifyResult;
import com.jch.mydemo.mode.FpVerifyResultDao;
import com.jch.mydemo.mode.Identity;
import com.jch.mydemo.utils.ApplicationUtils;
import com.jch.mydemo.utils.BitmapUtils;
import com.jch.mydemo.utils.CurrentIdentityUtils;
import com.jch.mydemo.utils.IdentityHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/9/18 下午3:34
 */

public class DetailActivity extends BaseActivity {

    @BindView(R.id.ll_root_container)
    ViewGroup mRoot;

    ProgressDialog mProgressDlg;

    //身份证
    @BindView(R.id.rl_identity_card_details)
    View mIdentityCard;
    @BindView(R.id.rb_selection1)
    MyRadioButton mSelection1;
    @BindView(R.id.img_identity_front)
    ImageView mIdentityFront;
    @BindView(R.id.img_identity_back)
    ImageView mIdentityBack;

    //认证结果
    @BindView(R.id.ll_verify_info)
    View mVerifyInfo;
    @BindView(R.id.rl_face_verify_info)
    View mFaceVerifyInfo;
    @BindView(R.id.rb_selection2)
    MyRadioButton mSelection2;
    @BindView(R.id.rl_fp_verify_info)
    View mFpVerifyInfo;
    @BindView(R.id.rb_selection3)
    MyRadioButton mSelection3;
    @BindView(R.id.include_face_verify)
    View mIncludeFaceVerify;
    @BindView(R.id.include_fp_verify)
    View mIncludeFpVerify;

    FaceVerifyInfo mFaceVerifyView;
    FpVerifyInfo mFpVerifyView;



    //指纹扫描
    @BindView(R.id.rl_fp_collection_info)
    View mFpCollection;
    @BindView(R.id.rb_selection4)
    MyRadioButton mSelection4;

    FpCollection mFpCollectionViews;

    //现场照片
    @BindView(R.id.rl_photo_info)
    View mPhotoInfo;
    @BindView(R.id.rb_selection5)
    MyRadioButton mSelection5;
    LocalPhoto mLocalPhoto;

    @BindView(R.id.btn_cancel)
    View mCancel;
    @BindView(R.id.btn_save)
    View mSave;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        mProgressDlg = new ProgressDialog(this);

        mFaceVerifyView = new FaceVerifyInfo(mIncludeFaceVerify);
        mFpVerifyView = new FpVerifyInfo(mIncludeFpVerify);
        mFpCollectionViews = new FpCollection(mFpCollection);
        mLocalPhoto = new LocalPhoto(mPhotoInfo);

        Identity identity = CurrentIdentityUtils.currentIdentity();
        Bitmap[] bmps = IdentityHelper.getInstance().getIdentityCardImage(identity);
        if(bmps != null){
            mIdentityCard.setVisibility(View.VISIBLE);
            mIdentityFront.setImageBitmap(bmps[0]);
            mIdentityBack.setImageBitmap(bmps[1]);
        }
        
        
        DaoSession daoSession = ApplicationUtils.getApplication().getDaoSession();
        FaceVerifyResultDao faceVerifyDao = daoSession.getFaceVerifyResultDao();
        List<FaceVerifyResult> faceVerifyResults = faceVerifyDao.queryBuilder()
                .where(FaceVerifyResultDao.Properties.IdentityNo.eq(identity.getIdentityNo()))
                .list();
        if(faceVerifyResults.size() > 0){
            mVerifyInfo.setVisibility(View.VISIBLE);
            mFaceVerifyInfo.setVisibility(View.VISIBLE);
            FaceVerifyResult result = faceVerifyResults.get(0);
            mFaceVerifyView.updateViews(identity,result);
        }

        FpVerifyResultDao fpVerifyDao = daoSession.getFpVerifyResultDao();
        List<FpVerifyResult> fpVerifyResults = fpVerifyDao.queryBuilder()
                .where(FpVerifyResultDao.Properties.IdentityNo.eq(identity.getIdentityNo()))
                .list();
        if(fpVerifyResults.size() > 0){
            mVerifyInfo.setVisibility(View.VISIBLE);
            mFpVerifyInfo.setVisibility(View.VISIBLE);
            FpVerifyResult result = fpVerifyResults.get(0);
            mFpVerifyView.updateViews(identity,result);
        }

        boolean hasFp = false;
        Bitmap[] fps = new Bitmap[10];
        IdentityHelper.getInstance().getFpImage(identity,fps);
        for(int i = 0;i < 10;i++){
            Bitmap fp = fps[i];
            if(fp != null){
                hasFp = true;
                mFpCollectionViews.updateViewByIndex(fp,i);
            }
        }
        if(hasFp){
            mFpCollection.setVisibility(View.VISIBLE);
        }

        Bitmap photo = IdentityHelper.getInstance().getPhoto(identity);
        if(photo != null){
            mPhotoInfo.setVisibility(View.VISIBLE);
            mLocalPhoto.updateViews(photo);
        }
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }


    @OnClick({R.id.rb_selection1,R.id.rb_selection2,R.id.rb_selection3,R.id.rb_selection4,R.id.rb_selection5})
    public void onSelection(View view){
        RadioButton r = (MyRadioButton)view;
        r.setSelected(!r.isSelected());

    }


    @OnClick(R.id.btn_save)
    public void onSave(){
        mProgressDlg.show();
        hideViews();
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                SystemClock.sleep(200);
                Bitmap bmpRoot = BitmapUtils.getBitmapFromView(mRoot);
                MediaStore.Images.Media.insertImage(getContentResolver(), bmpRoot, "title", "description");
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                mProgressDlg.dismiss();
                showMsg(R.string.msg_save_ok);
                mCancel.setVisibility(View.VISIBLE);
                mSave.setVisibility(View.VISIBLE);
            }
        };
        task.execute();

    }

    private void hideViews(){
        if(mSelection1.isSelected()){
            mIdentityCard.setVisibility(View.GONE);
            mSelection1.setVisibility(View.GONE);
        }
        else{
            mSelection1.setVisibility(View.INVISIBLE);
        }

        if(mSelection2.isSelected()){
            mFaceVerifyInfo.setVisibility(View.GONE);
            mSelection2.setVisibility(View.GONE);
        }
        else{
            mSelection2.setVisibility(View.INVISIBLE);
        }
        if(mSelection3.isSelected()){
            mFpVerifyInfo.setVisibility(View.GONE);
            mSelection3.setVisibility(View.GONE);
        }
        else{
            mSelection3.setVisibility(View.INVISIBLE);
        }

        if(mFaceVerifyInfo.getVisibility() == View.GONE && mFpVerifyInfo.getVisibility() == View.GONE){
            mVerifyInfo.setVisibility(View.GONE);
        }

        if(mSelection4.isSelected()){
            mFpCollection.setVisibility(View.GONE);
            mSelection4.setVisibility(View.GONE);
        }
        else{
            mSelection4.setVisibility(View.INVISIBLE);
        }

        if(mSelection5.isSelected()){
            mPhotoInfo.setVisibility(View.GONE);
            mSelection4.setVisibility(View.GONE);
        }
        else{
            mSelection4.setVisibility(View.INVISIBLE);
        }

        mCancel.setVisibility(View.GONE);
        mSave.setVisibility(View.GONE);

        mRoot.requestLayout();

    }

    class FaceVerifyInfo{
        @BindView(R.id.img_photo)
        ImageView mFacePhoto;
        @BindView(R.id.img_identity_photo)
        ImageView mIdentityPhoto;
        @BindView(R.id.et_name)
        TextView mName;
        @BindView(R.id.et_birthday)
        TextView mBirthday;
        @BindView(R.id.et_identity_no)
        TextView mIdentityNo;
        @BindView(R.id.et_verify_time)
        TextView mVerifyTime;
        @BindView(R.id.et_similarity)
        TextView mSimilarity;

        FaceVerifyInfo(View view){
            ButterKnife.bind(this,view);
        }

        public void updateViews(Identity identity,FaceVerifyResult result){
            mName.setText(identity.getName());
            mBirthday.setText(identity.getBirthDay());
            mIdentityNo.setText(identity.getIdentityNo());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mVerifyTime.setText(format.format(result.getVerifyTime()));
            mSimilarity.setText(String.valueOf(result.getScore()));
            Bitmap bmp = BitmapUtils.byteArray2bmp(identity.getImage());
            mIdentityPhoto.setImageBitmap(bmp);
            Bitmap bmp2 = IdentityHelper.getInstance().getVerifiedFace(identity);
            mFacePhoto.setImageBitmap(bmp2);
        }
    }

    class FpVerifyInfo{
        @BindView(R.id.img_current_fp1)
        ImageView mFp1;
        @BindView(R.id.img_current_fp2)
        ImageView mFp2;
        @BindView(R.id.et_name)
        TextView mName;
        @BindView(R.id.et_birthday)
        TextView mBirthday;
        @BindView(R.id.et_identity_no)
        TextView mIdentityNo;
        @BindView(R.id.et_verify_time)
        TextView mVerifyTime;
        @BindView(R.id.et_similarity1)
        TextView mSimilarity1;
        @BindView(R.id.et_similarity2)
        TextView mSimilarity2;

        FpVerifyInfo(View view){
            ButterKnife.bind(this,view);
        }

        public void updateViews(Identity identity,FpVerifyResult result){
            mName.setText(identity.getName());
            mBirthday.setText(identity.getBirthDay());
            mIdentityNo.setText(identity.getIdentityNo());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mVerifyTime.setText(format.format(result.getVerifyTime()));
            mSimilarity1.setText(String.valueOf(result.getScore1()));
            mSimilarity2.setText(String.valueOf(result.getScore2()));
        }
    }

    class FpCollection{
        @BindView(R.id.fp_l_1)
        ImageView mFpl1;
        @BindView(R.id.fp_l_2)
        ImageView mFpl2;
        @BindView(R.id.fp_l_3)
        ImageView mFpl3;
        @BindView(R.id.fp_l_4)
        ImageView mFpl4;
        @BindView(R.id.fp_l_5)
        ImageView mFpl5;
        @BindView(R.id.fp_r_1)
        ImageView mFpr1;
        @BindView(R.id.fp_r_2)
        ImageView mFpr2;
        @BindView(R.id.fp_r_3)
        ImageView mFpr3;
        @BindView(R.id.fp_r_4)
        ImageView mFpr4;
        @BindView(R.id.fp_r_5)
        ImageView mFpr5;

        public FpCollection(View view){
            ButterKnife.bind(this,view);
        }

        public void updateViews(Identity identity){
            Bitmap[] fps = new Bitmap[10];
            IdentityHelper.getInstance().getFpImage(identity,fps);
            for(int i = 0;i < 10;i++){
                Bitmap tmp = fps[i];
                if(tmp != null){
                    ImageView fpImage = findViewsByIndex(i);
                    fpImage.setImageBitmap(tmp);
                }
            }
        }

        public void updateViewByIndex(Bitmap bmp,int i){
            ImageView fpImage = findViewsByIndex(i);
            fpImage.setImageBitmap(bmp);
        }

        private ImageView findViewsByIndex(int i){
            switch (i){
                case 0:
                    return mFpl1;
                case 1:
                    return mFpl2;
                case 2:
                    return mFpl3;
                case 3:
                    return mFpl4;
                case 4:
                    return mFpl5;
                case 5:
                    return mFpr1;
                case 6:
                    return mFpr2;
                case 7:
                    return mFpr3;
                case 8:
                    return mFpr4;
                case 9:
                    return mFpr5;
                default:
                    return null;

            }
        }

    }

    class LocalPhoto{
        @BindView(R.id.img_photo)
        ImageView mPhoto;

        public LocalPhoto(View view){
            ButterKnife.bind(this,view);
        }

        public void updateViews(Bitmap bmp){
            mPhoto.setImageBitmap(bmp);
        }
    }
}
