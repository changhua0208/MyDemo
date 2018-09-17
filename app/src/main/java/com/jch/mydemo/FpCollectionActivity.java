package com.jch.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.jch.mydemo.mode.Identity;
import com.jch.mydemo.utils.CurrentIdentityUtils;
import com.jch.mydemo.utils.IdentityHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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


    static final int MSG_COLLECT_FAIL = 11;
    static final int  MSG_IMG_LOW_QUALITY = 12;
    static final int  MSG_COLLECT_SUCCESS = 13;
    static final int MSG_FEATURE_ERROR = 14;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_COLLECT_FAIL:
                    if(mProgressDlg.isShowing())
                        mProgressDlg.dismiss();
                    toast("采集失败");
                    break;
                case MSG_IMG_LOW_QUALITY:
                    if(mProgressDlg.isShowing())
                        mProgressDlg.dismiss();
                    toast("图片质量太低，请重新采集");
                    break;
                case MSG_COLLECT_SUCCESS:
                    if(mProgressDlg.isShowing())
                        mProgressDlg.dismiss();
                    updateFpImgView(currentIndex);
                    break;
            }
        }
    };


    Runnable collectFpRun = new Runnable() {
        @Override
        public void run() {
            byte[] fpdata = ssF.getFingerByteData();
            if(fpdata == null){
                handler.sendEmptyMessage(MSG_COLLECT_FAIL);
            }
            else{
                int nRet = ssF.getFingerQuality(fpdata);
                if(nRet >= 60){
                    String feature = ssF.getFingerInfoQuick(1,fpdata);
                    if(TextUtils.isEmpty(feature)){
                        handler.sendEmptyMessage(MSG_FEATURE_ERROR);
                        return;
                    }
                    Bitmap bmp = BitmapFactory.decodeByteArray(fpdata,0,fpdata.length);
                    bmps[currentIndex - 1] = bmp;
                    features[currentIndex - 1] = feature;
                    handler.sendEmptyMessage(MSG_COLLECT_SUCCESS);
                }
                else{
                    handler.sendEmptyMessage(MSG_IMG_LOW_QUALITY);
                }
            }
        }
    };

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
        Identity identity = CurrentIdentityUtils.currentIdentity();
        IdentityHelper.getInstance().getFpImage(identity,bmps);
        for(int i = 1;i<= bmps.length;i++){
            if(bmps[i - 1] != null)
                updateFpImgView(i);
        }
    }

    @OnClick(R.id.fp_l_1)
    public void onFpl1(){
        mProgressDlg.show();
        currentIndex = 1;
        Thread thr = new Thread(collectFpRun);
        thr.start();
    }

    @OnClick(R.id.fp_l_2)
    public void onFpl2(){
        mProgressDlg.show();
        currentIndex = 2;
        Thread thr = new Thread(collectFpRun);
        thr.start();
    }

    @OnClick(R.id.fp_l_3)
    public void onFpl3(){
        mProgressDlg.show();
        currentIndex = 3;
        Thread thr = new Thread(collectFpRun);
        thr.start();
    }

    @OnClick(R.id.fp_l_4)
    public void onFpl4(){
        mProgressDlg.show();
        currentIndex = 4;
        Thread thr = new Thread(collectFpRun);
        thr.start();
    }

    @OnClick(R.id.fp_l_5)
    public void onFpl5(){
        mProgressDlg.show();
        currentIndex = 5;
        Thread thr = new Thread(collectFpRun);
        thr.start();
    }

    @OnClick(R.id.fp_r_1)
    public void onFpr1(){
        mProgressDlg.show();
        currentIndex = 6;
        Thread thr = new Thread(collectFpRun);
        thr.start();
    }

    @OnClick(R.id.fp_r_2)
    public void onFpr2(){
        mProgressDlg.show();
        currentIndex = 7;
        Thread thr = new Thread(collectFpRun);
        thr.start();
    }

    @OnClick(R.id.fp_r_3)
    public void onFpr3(){
        mProgressDlg.show();
        currentIndex = 8;
        Thread thr = new Thread(collectFpRun);
        thr.start();
    }

    @OnClick(R.id.fp_r_4)
    public void onFpr4(){
        mProgressDlg.show();
        currentIndex = 9;
        Thread thr = new Thread(collectFpRun);
        thr.start();
    }

    @OnClick(R.id.fp_r_5)
    public void onFpr5(){
        mProgressDlg.show();
        currentIndex = 10;
        Thread thr = new Thread(collectFpRun);
        thr.start();
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
                Identity identity = CurrentIdentityUtils.currentIdentity();
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
        //ssF.f_poweroff();
    }
}
