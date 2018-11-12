package com.houyi.notarization;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.houyi.notarization.mode.Person;
import com.houyi.notarization.utils.BitmapUtils;
import com.houyi.notarization.utils.CurrentIdentityUtils;
import com.houyi.notarization.utils.IdentityHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.aratek.fp.Bione;
import cn.com.aratek.fp.FingerprintImage;
import cn.com.aratek.util.Result;

/**
 * @author changhua.jiang
 * @since 2018/9/12 下午3:07
 */

public class FpCollectionActivity extends FpBaseActivity {

    @BindView(R.id.fp_l_1)
    ImageView mImgFpl1;
    @BindView(R.id.fp_l_2)
    ImageView mImgFpl2;
    @BindView(R.id.fp_l_3)
    ImageView mImgFpl3;
    @BindView(R.id.fp_l_4)
    ImageView mImgFpl4;
    @BindView(R.id.fp_l_5)
    ImageView mImgFpl5;
    @BindView(R.id.fp_r_1)
    ImageView mImgFpr1;
    @BindView(R.id.fp_r_2)
    ImageView mImgFpr2;
    @BindView(R.id.fp_r_3)
    ImageView mImgFpr3;
    @BindView(R.id.fp_r_4)
    ImageView mImgFpr4;
    @BindView(R.id.fp_r_5)
    ImageView mImgFpr5;

    Bitmap[] bmps;
    String[] features;

    int currentIndex;
    private static String TAG = "FpCollectionActivity";


    static final int MSG_COLLECT_FAIL = 11;
    static final int  MSG_IMG_LOW_QUALITY = 12;
    static final int  MSG_COLLECT_SUCCESS = 13;
    static final int MSG_FEATURE_ERROR = 14;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp_collection);
        ButterKnife.bind(this);
        loadFps();
    }

    public void loadFps(){
        bmps = new Bitmap[10];
        features = new String[10];
//        Person identity = CurrentIdentityUtils.currentIdentity();
//        IdentityHelper.getInstance().getFpImage(identity,bmps);
//        for(int i = 1;i<= bmps.length;i++){
//            if(bmps[i - 1] != null)
//                updateFpImgView(i);
//        }
    }

    @OnClick(R.id.fp_l_1)
    public void onFpl1(){
        if(mDeviceOpened){
            currentIndex = 1;
            captureFpImage();
        }
    }

    @OnClick(R.id.fp_l_2)
    public void onFpl2(){
        currentIndex = 2;
        captureFpImage();
    }

    @OnClick(R.id.fp_l_3)
    public void onFpl3(){
        currentIndex = 3;
        captureFpImage();
    }

    @OnClick(R.id.fp_l_4)
    public void onFpl4(){
        currentIndex = 4;
        captureFpImage();
    }

    @OnClick(R.id.fp_l_5)
    public void onFpl5(){
        currentIndex = 5;
        captureFpImage();
    }

    @OnClick(R.id.fp_r_1)
    public void onFpr1(){
        currentIndex = 6;
        captureFpImage();
    }

    @OnClick(R.id.fp_r_2)
    public void onFpr2(){
        currentIndex = 7;
        captureFpImage();
    }

    @OnClick(R.id.fp_r_3)
    public void onFpr3(){
        currentIndex = 8;
        captureFpImage();
    }

    @OnClick(R.id.fp_r_4)
    public void onFpr4(){
        currentIndex = 9;
        captureFpImage();
    }

    @OnClick(R.id.fp_r_5)
    public void onFpr5(){
        currentIndex = 10;
        captureFpImage();
    }

    private void captureFpImage(){
        AsyncTask task = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showWaitDlg();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                mScanner.prepare();
                Result res = mScanner.capture();
                FingerprintImage fi = (FingerprintImage) res.data;
                int quality = 0;
                if (fi != null) {
                    quality = Bione.getFingerprintQuality(fi);
                    Log.i(TAG, "Fingerprint image quality is " + quality);

                }
                if(quality < 50){
                    return null;
                }
                else{
                    Bitmap bmp = BitmapUtils.byteArray2bmp(fi.convert2Bmp());
                    return bmp;
                }

            }

            @Override
            protected void onPostExecute(Object ret) {
                super.onPostExecute(ret);
                if(ret != null){
                    showMsg(R.string.msg_capture_fail);
                }
                else{
                    bmps[currentIndex - 1] = (Bitmap) ret;
                    updateFpImgView(currentIndex);
                }
                dismissWaitDlg();
            }
        };
        task.execute();

    }

    private void updateFpImgView(int index){
        Bitmap bmp = bmps[index - 1];
        switch (index){
            case 1:
                mImgFpl1.setImageBitmap(bmp);
                break;
            case 2:
                mImgFpl2.setImageBitmap(bmp);
                break;
            case 3:
                mImgFpl3.setImageBitmap(bmp);
                break;
            case 4:
                mImgFpl4.setImageBitmap(bmp);
                break;
            case 5:
                mImgFpl5.setImageBitmap(bmp);
                break;
            case 6:
                mImgFpr1.setImageBitmap(bmp);
                break;
            case 7:
                mImgFpr2.setImageBitmap(bmp);
                break;
            case 8:
                mImgFpr3.setImageBitmap(bmp);
                break;
            case 9:
                mImgFpr4.setImageBitmap(bmp);
                break;
            case 10:
                mImgFpr5.setImageBitmap(bmp);
                break;

        }
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSave(){
        new AsyncTask<Object,Object,Object>(){

            @Override
            protected Object doInBackground(Object[] objects) {
                Person identity = CurrentIdentityUtils.currentIdentity();
                for(int i = 1;i<= bmps.length;i++){
                    Bitmap bmp = bmps[i -1];
                    String feature = features[i - 1];
                    if(bmp != null && !TextUtils.isEmpty(feature)) {
                        IdentityHelper.getInstance().saveFp(identity, bmp, i);
                        IdentityHelper.getInstance().saveFpFeature(identity, feature, i);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                mProgressDlg.dismiss();
                showMsg(R.string.msg_save_ok);
            }
        }.execute();
        mProgressDlg.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
