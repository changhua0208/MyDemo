package com.jch.mydemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jch.mydemo.mode.Identity;
import com.jch.mydemo.utils.CurrentIdentityUtils;
import com.jch.mydemo.utils.IdentityHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/9/12 下午3:04
 */

public class FpVerifyActivity extends FpBaseActivity {
    Bitmap currentFp1,currentFp2,identityFp1,identityFp2;
    String currentFpFeature1,currentFpFeature2,identityFpFeature1,identityFpFeature2;

    @BindView(R.id.img_current_fp1) ImageView mImgCurrentFp1;
    @BindView(R.id.img_current_fp2) ImageView mImgCurrentFp2;
    @BindView(R.id.img_identity_fp1) ImageView mImgIdentityFp1;
    @BindView(R.id.img_identity_fp2) ImageView mImgIdentityFp2;
    @BindView(R.id.et_name) TextView mName;
    @BindView(R.id.et_identity_no) TextView mIdentityNo;
    @BindView(R.id.et_birthday) TextView mBirthday;
    @BindView(R.id.et_similarity1) TextView mSimilarity1;
    @BindView(R.id.et_similarity2) TextView mSimilarity2;


    Identity identity;
    int currentIndex = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp_verify);
        ButterKnife.bind(this);

        identity = CurrentIdentityUtils.currentIdentity();
        loadFp();
        initViews();
    }

    private void loadFp() {
        String data = identity.getFp1();
        if(!TextUtils.isEmpty(data)){
            //identityFp1 = BitmapUtils.byteArray2bmp(data);
            int index = getIndexByFpName(identity.getFp1Name());
            currentFp1 = IdentityHelper.getInstance().getFpImageByIndex(identity,index);
            currentFpFeature1 = IdentityHelper.getInstance().getFpFeatureByIndex(identity,index);
        }
        data = identity.getFp2();
        if(!TextUtils.isEmpty(data)){
            //identityFp2 = BitmapUtils.byteArray2bmp(data);
            int index = getIndexByFpName(identity.getFp2Name());
            currentFp2 = IdentityHelper.getInstance().getFpImageByIndex(identity,index);
            currentFpFeature2 = IdentityHelper.getInstance().getFpFeatureByIndex(identity,index);
        }
    }

    public void initViews(){
//        if(identityFp2 != null)
//            mImgIdentityFp2.setImageBitmap(identityFp2);
        if(currentFp2 != null)
            mImgCurrentFp2.setImageBitmap(currentFp2);
        if(currentFp1 != null)
            mImgCurrentFp1.setImageBitmap(currentFp1);
//        if(identityFp1 != null)
//            mImgIdentityFp1.setImageBitmap(identityFp1);

        mName.setText(identity.getName());
        mBirthday.setText(identity.getBirthDay());
        mIdentityNo.setText(identity.getIdentityNo());
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSave(){

    }

    @OnClick(R.id.btn_comparison1)
    public void onComparison1(){
        if(!TextUtils.isEmpty(identity.getFp1()) && !TextUtils.isEmpty(currentFpFeature1)){
            int ret = ssF.fingerComparison(identity.getFp1(),currentFpFeature1);
            mSimilarity1.setText("" + ret);
        }
    }
    @OnClick(R.id.btn_comparison2)
    public void onComparision2(){
        if(!TextUtils.isEmpty(identity.getFp2()) && !TextUtils.isEmpty(currentFpFeature2)){
            int ret = ssF.fingerComparison(identity.getFp2(),currentFpFeature2);
            mSimilarity2.setText("" + ret);
        }
//        currentIndex = 2;
//        mProgressDlg.show();
//        new AsyncTask() {
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                byte[] data1 = (byte[]) objects[0];
//                byte[] data2 = (byte[]) objects[1];
//                ssF.getFingerByteData();
//                String ret1 = ssF.getFingerInfoQuick(1,data1);
//                SystemClock.sleep(500);
//                ssF.getFingerByteData();
//                String ret2 = ssF.getFingerInfoQuick(2,data2);
//                if(!TextUtils.isEmpty(ret1) && !TextUtils.isEmpty(ret2)){
////                byte[] fingerInfo1 = Util.hexStr2ByteArray(ret1);
////                byte[] fingerInfo2 = Util.hexStr2ByteArray(ret2);
//                    return ssF.fingerComparison(ret1,ret2);
//                }
//                return -1;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                mProgressDlg.dismiss();
//                int ret = (int) o;
//                if(currentIndex == 1){
//                    mSimilarity1.setText("" + ret);
//                }
//                else if(currentIndex == 2){
//                    mSimilarity2.setText("" + ret);
//                }
//
//            }
//        }.execute(data1,data2);
    }






    private int getIndexByFpName(String name){
        if(name.startsWith("左手拇指")){
            return 1;
        }
        else if(name.startsWith("左手食指")){
            return 2;
        }
        else if(name.startsWith("左手中指")){
            return 3;
        }
        else if(name.startsWith("左手无名指")){
            return 4;
        }
        else if(name.startsWith("左手小指")){
            return 5;
        }
        else if(name.startsWith("右手拇指")){
            return 6;
        }
        else if(name.startsWith("右手食指")){
            return 7;
        }
        else if(name.startsWith("右手中指")){
            return 8;
        }
        else if(name.startsWith("右手无名指")){
            return 9;
        }
        else if(name.startsWith("右手小指")){
            return 10;
        }
        else{
            return 0;
        }
    }
}
