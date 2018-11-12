package com.houyi.notarization;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.houyi.notarization.event.VerifyEvent;
import com.houyi.notarization.mode.DaoSession;
import com.houyi.notarization.mode.FaceVerifyResult;
import com.houyi.notarization.mode.FaceVerifyResultDao;
import com.houyi.notarization.mode.Person;
import com.houyi.notarization.utils.ApplicationUtils;
import com.houyi.notarization.utils.BitmapUtils;
import com.houyi.notarization.utils.CurrentIdentityUtils;
import com.houyi.notarization.utils.IdentityHelper;
import com.houyi.utils.RxBus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 */

public class FaceVerifyActivity extends BaseActivity {
    //照片
    private static final int PHOTO_IMAGE = 0;
    //证件照
    private static final int IDENTITY_IMAGE = 1;

    private static final int REQUEST_CODE_EXTRACT_FACE = 0x10;

    @BindView(R.id.img_identity_photo)
    ImageView mIdentityPhoto;
    @BindView(R.id.img_photo)
    ImageView mPhoto;

    @BindView(R.id.et_name)
    TextView mName;
    @BindView(R.id.et_birthday)
    TextView mBirthDay;
    @BindView(R.id.et_identity_no)
    TextView mIdentityNo;

    @BindView(R.id.et_similarity)
    TextView mSimilarity;
    @BindView(R.id.et_verify_time)
    TextView mVerifyTime;

    //bmp1 照片 bmp2 身份证照
    private Bitmap bmp1,bmp2;
    private DaoSession daoSession;
    private ProgressDialog mProgressDlg;
    private FaceVerifyResult faceVerifyRecode;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_verify);
        ButterKnife.bind(this);
        daoSession = ApplicationUtils.getApplication().getDaoSession();
        initViews();
    }

    private void initViews() {
        mProgressDlg = new ProgressDialog(this);
        Person identity = CurrentIdentityUtils.currentIdentity();

        FaceVerifyResultDao dao = daoSession.getFaceVerifyResultDao();
        List<FaceVerifyResult> list =  dao.queryBuilder().where(FaceVerifyResultDao.Properties.IdentityNo.eq(identity.getIdentityNo()))
                .list();
        if(list.size() > 0){
            faceVerifyRecode = list.get(0);
        }

        bmp1 = IdentityHelper.getInstance().getVerifiedFace(identity);
        if(bmp1 != null){
            mPhoto.setImageBitmap(bmp1);
        }
        bmp2 = BitmapUtils.byteArray2bmp(identity.getImage());
        if(bmp2 != null){
            mIdentityPhoto.setImageBitmap(bmp2);
        }
        mName.setText(identity.getName());
        mBirthDay.setText(identity.getBirthDay());
        mIdentityNo.setText(identity.getIdentityNo());
//        if(faceVerifyRecode == null){
//            faceVerifyRecode = new FaceVerifyResult();
//            faceVerifyRecode.setIdentityNo(identity.getIdentityNo());
//        }
        if(faceVerifyRecode != null) {
            mSimilarity.setText("" + faceVerifyRecode.getScore());
            Date date = faceVerifyRecode.getVerifyTime();
            if (date != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mVerifyTime.setText(format.format(date));
            }
        }
        else{
            mSimilarity.setText("-1");
        }

    }

    @OnClick(R.id.img_photo)
    public void onExtractFace(){
        Intent intent = new Intent(this,SimpleFaceCaptureActivity.class);
        startActivityForResult(intent,REQUEST_CODE_EXTRACT_FACE);
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSave(){
        if(bmp1 != null && bmp2 != null){
            AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    Person identity = CurrentIdentityUtils.currentIdentity();
                    if(faceVerifyRecode != null){
                        fillResult(identity,faceVerifyRecode);
                        daoSession.getFaceVerifyResultDao().update(faceVerifyRecode);

                    }
                    else{
                        faceVerifyRecode = new FaceVerifyResult();
                        fillResult(identity,faceVerifyRecode);
                        daoSession.getFaceVerifyResultDao().insert(faceVerifyRecode);

                    }
                    IdentityHelper.getInstance().saveVerfiedFace(identity,bmp1);
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    mProgressDlg.dismiss();
                    showMsg(R.string.msg_save_ok);
                    CurrentIdentityUtils.currentIdentity().setComparison("是");
                    RxBus.getInstance().post(new VerifyEvent());
                }
            };
            mProgressDlg.show();
        task.execute();
        }
    }

    private void fillResult(Person identity, FaceVerifyResult result){
        int score = -1;
        try {
            score = Integer.parseInt(mSimilarity.getText().toString());
        }
        catch (Exception e){

        }
        result.setIdentityNo(identity.getIdentityNo());
        result.setScore(score);
        if(TextUtils.isEmpty(mVerifyTime.getText()))
        {
            result.setVerifyTime(new Date());
        }
        else{
            String time = mVerifyTime.getText().toString();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(time);
            } catch (ParseException e) {
               date = new Date();
            }
            result.setVerifyTime(date);
        }
        //result.setVerifyTime(new Date());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_EXTRACT_FACE && resultCode == RESULT_OK){
        }
    }

    @OnClick(R.id.btn_comparison)
    public void onComparison(){
//        mProgressDlg.show();
//        AsyncTask<Object,Object,FVR> task = new AsyncTask<Object,Object,FVR>() {
//            @Override
//            protected FVR doInBackground(Object[] objects) {
//                if(bmp1 != null && bmp2 != null) {
//                    byte[] feature1 = extractFeature(bmp1, PHOTO_IMAGE);
//                    byte[] feature2 = extractFeature(bmp2, IDENTITY_IMAGE);
//                    if(feature1 != null && feature2 != null){
//                        VerifyResult ret = verifyFeature(feature1, feature2);
//                        FVR result = new FVR(ret);
//                        ret.delete();
//                        return result;
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(FVR verifyResult) {
//                //if(mProgressDlg.isShowing())
//                mProgressDlg.dismiss();
//                if(verifyResult != null){
//                    mSimilarity.setText("" + verifyResult.score);
//                    Date date = new Date();
//                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    mVerifyTime.setText(format.format(date));
//                }
//            }
//        };
//        task.execute();
    }
}
