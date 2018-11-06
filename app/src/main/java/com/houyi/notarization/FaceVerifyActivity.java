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
import com.houyi.notarization.verify.ServiceFactory;
import com.houyi.utils.RxBus;
import com.oliveapp.face.liboffline_face_verification.FaceVerifier;
import com.oliveapp.face.liboffline_face_verification.nativecode.FaceFeature;
import com.oliveapp.face.liboffline_face_verification.nativecode.FeatureExtractResultList;
import com.oliveapp.face.liboffline_face_verification.nativecode.FeatureExtractionOption;
import com.oliveapp.face.liboffline_face_verification.nativecode.ImageType;
import com.oliveapp.face.liboffline_face_verification.nativecode.LivenessPackage;
import com.oliveapp.face.liboffline_face_verification.nativecode.VerifyResult;

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
    private FaceVerifier verifier;
    private DaoSession daoSession;
    private ProgressDialog mProgressDlg;
    private FaceVerifyResult faceVerifyRecode;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_verify);
        ButterKnife.bind(this);
        daoSession = ApplicationUtils.getApplication().getDaoSession();
        verifier = ServiceFactory.getInstance().getFaceVerifier();
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
            boolean success = data.getBooleanExtra("is_success",false);
            if(success){
                byte[] encryptedData = data.getByteArrayExtra("package_content");
                final byte[] frame = getFrameFromPackageData(encryptedData);
                bmp1 = BitmapUtils.byteArray2bmp(frame);
                mPhoto.setImageBitmap(bmp1);
            }
        }
    }

    @OnClick(R.id.btn_comparison)
    public void onComparison(){
        mProgressDlg.show();
        AsyncTask<Object,Object,FVR> task = new AsyncTask<Object,Object,FVR>() {
            @Override
            protected FVR doInBackground(Object[] objects) {
                if(bmp1 != null && bmp2 != null) {
                    byte[] feature1 = extractFeature(bmp1, PHOTO_IMAGE);
                    byte[] feature2 = extractFeature(bmp2, IDENTITY_IMAGE);
                    if(feature1 != null && feature2 != null){
                        VerifyResult ret = verifyFeature(feature1, feature2);
                        FVR result = new FVR(ret);
                        ret.delete();
                        return result;
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(FVR verifyResult) {
                //if(mProgressDlg.isShowing())
                mProgressDlg.dismiss();
                if(verifyResult != null){
                    mSimilarity.setText("" + verifyResult.score);
                    Date date = new Date();
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mVerifyTime.setText(format.format(date));
                }
            }
        };
        task.execute();
    }


    private byte[] getFrameFromPackageData(byte[] packageData)
    {
        LivenessPackage pack = new LivenessPackage();
        int rtn = verifier.parsePackageData(packageData, pack); // 解析数据
        if (rtn != 0) return null;

        byte[] data = verifier.getImageInPackage(pack, 0); // 获取人像图片数据
        pack.delete();
        return data;
    }

    public byte[] extractFeature(Bitmap bmp,int picType){
        byte[] data = BitmapUtils.bmp2byteArray(bmp);
        return extractFeature(data,picType);
    }

    public byte[] extractFeature(byte[] picByte, int picType) {
        /****************************************************
         *
         * #  提取图像特征核心代码
         *    1.实例一个FeatureExtractionOption参数对象，并设置参数
         *    2.实例一个FeatureExtractResultList特征结果对象
         *    3.执行FaceVerifier.extractFeatureFromImageContent()方法，传入参数为图像数据，参数对像，特征结果对象
         *    4.在FeatureExtractResultList特征结果对象中，得到FaceFeature图像特征对象
         *
         ****************************************************/

        // 设置option参数
        FeatureExtractionOption opt = new FeatureExtractionOption();
        FeatureExtractResultList res = new FeatureExtractResultList(); // 特征结果类

        try {
            opt.setEnableAutoFlip(false);
            opt.setEnableAutoRotate(false);
            opt.setMaxFacesAllowed(1);
            // 若是捕获到的照片，设置ImageType为IMAGETYPE_LEIZHENGJIANZHAO;若是芯片照，设置ImageType为IMAGETYPE_XINPIANZHAO
            opt.setImageType(picType == PHOTO_IMAGE ? ImageType.IMAGETYPE_LEIZHENGJIANZHAO : ImageType.IMAGETYPE_XINPIANZHAO);
            opt.setIsQueryImage(picType == PHOTO_IMAGE); // 若是捕获到的照片，设置为1;若是芯片照，设置为0

            int rtn = verifier.extractFeatureFromImageContent(picByte, opt, res); // 图片数据，参数，结果
            if(rtn == 0) {
                // 如果特征提取结果为空，对用户进行相关提示
                if (res.isEmpty()) {
                    return null;
                } else {
                    return verifier.serializeFeature(res.get(0).getFeature());
                }
            }
            else{
                return null;
            }
        } finally {
            opt.delete();
            res.delete();
        }
    }

    public VerifyResult verifyFeature(byte[] feature1, byte[] feature2) {
        /*************************************************
         *
         * #  比对核心代码
         *    1.实例化为VerifyResult比对结果类
         *    2.调用faceVerifier.verifyFeature()方法，其参数分别为捕获照图像特征，芯片照图像特征及比对结果对象
         *    3.利用封装的Parcelable对象回传数据
         *
         *************************************************/
        VerifyResult verifyResult = new VerifyResult();
        FaceFeature faceFeature1 = null;
        FaceFeature faceFeature2 = null;
        try {
            faceFeature1 = verifier.deserializeFeature(feature1);
            faceFeature2 = verifier.deserializeFeature(feature2);
            int rtn = verifier.verifyFeature(faceFeature1, faceFeature2, verifyResult);
            if(rtn == 0){
                return verifyResult;
            }
            else{
                return null;
            }

        } finally {
            //verifyResult.delete();
            if (faceFeature1 != null) {
                faceFeature1.delete();
            }
            if (faceFeature2 != null) {
                faceFeature2.delete();
            }
        }
    }

    //class face verify result
    class FVR {
        int score;
        boolean isSamePerson;
        public FVR(VerifyResult result){
            this.score = (int) Math.round(result.getSimilarity());
            this.isSamePerson = result.getIsSamePerson();
        }
    }

}
