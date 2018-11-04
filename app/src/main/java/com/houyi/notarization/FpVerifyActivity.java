package com.houyi.notarization;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.houyi.notarization.mode.DaoSession;
import com.houyi.notarization.mode.FpVerifyResult;
import com.houyi.notarization.mode.FpVerifyResultDao;
import com.houyi.notarization.mode.Identity;
import com.houyi.notarization.utils.ApplicationUtils;
import com.houyi.notarization.utils.CurrentIdentityUtils;
import com.houyi.notarization.utils.IdentityHelper;

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
 * @since 2018/9/12 下午3:04
 */

public class FpVerifyActivity extends FpBaseActivity {

    Bitmap currentFp1,currentFp2;
    String currentFpFeature1,currentFpFeature2;

    @BindView(R.id.img_current_fp1)
    ImageView mImgCurrentFp1;
    @BindView(R.id.img_current_fp2)
    ImageView mImgCurrentFp2;
    @BindView(R.id.img_identity_fp1)
    ImageView mImgIdentityFp1;
    @BindView(R.id.img_identity_fp2)
    ImageView mImgIdentityFp2;
    @BindView(R.id.et_name)
    TextView mName;
    @BindView(R.id.et_identity_no)
    TextView mIdentityNo;
    @BindView(R.id.et_birthday)
    TextView mBirthday;
    @BindView(R.id.et_similarity1)
    TextView mSimilarity1;
    @BindView(R.id.et_similarity2)
    TextView mSimilarity2;
    @BindView(R.id.et_verify_time)
    TextView mVerifyTime;

    private FpVerifyResult verifyResult;
    int currentIndex1 = -1;
    int currentIndex2 = -1;

    Identity identity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp_verify);
        ButterKnife.bind(this);
        identity = CurrentIdentityUtils.currentIdentity();
        loadVerifyRecode();
        loadFp();
        initViews();
    }

    private void loadVerifyRecode() {
        DaoSession daoSession = ApplicationUtils.getApplication().getDaoSession();
        FpVerifyResultDao dao = daoSession.getFpVerifyResultDao();
        List<FpVerifyResult> ret = dao.queryBuilder().where(FpVerifyResultDao.Properties.IdentityNo.eq(identity.getIdentityNo()))
                .list();
        if(ret != null && ret.size() > 0){
            verifyResult = ret.get(0);
        }
    }

    private void loadFp() {
        String data = identity.getFp1();
        if(!TextUtils.isEmpty(data)){
            int index = getIndexByFpName(identity.getFp1Name());
            this.currentIndex1 = index;
            currentFp1 = IdentityHelper.getInstance().getFpImageByIndex(identity,index);
            currentFpFeature1 = IdentityHelper.getInstance().getFpFeatureByIndex(identity,index);
        }
        data = identity.getFp2();
        if(!TextUtils.isEmpty(data)){
            int index = getIndexByFpName(identity.getFp2Name());
            this.currentIndex2 = index;
            currentFp2 = IdentityHelper.getInstance().getFpImageByIndex(identity,index);
            currentFpFeature2 = IdentityHelper.getInstance().getFpFeatureByIndex(identity,index);
        }
    }

    public void initViews(){
        if(currentFp2 != null)
            mImgCurrentFp2.setImageBitmap(currentFp2);
        if(currentFp1 != null)
            mImgCurrentFp1.setImageBitmap(currentFp1);

        mName.setText(identity.getName());
        mBirthday.setText(identity.getBirthDay());
        mIdentityNo.setText(identity.getIdentityNo());
        if(verifyResult != null){
            mSimilarity1.setText("" + verifyResult.getScore1());
            mSimilarity2.setText("" +verifyResult.getScore2());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = verifyResult.getVerifyTime();
            if(date != null)
                mVerifyTime.setText(format.format(date));
        }
        else {
            mSimilarity1.setText("-1");
            mSimilarity2.setText("-1");
        }

    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSave(){

        FpVerifyResultDao dao = ApplicationUtils.getApplication().getDaoSession().getFpVerifyResultDao();
        if(verifyResult == null) {
            verifyResult = new FpVerifyResult();
            fillResult(identity,verifyResult);
            dao.insert(verifyResult);
            showMsg(R.string.msg_save_ok);
        }
        else{
            fillResult(identity,verifyResult);
            dao.update(verifyResult);
            showMsg(R.string.msg_save_ok);
            //initViews();
        }
    }

    private void fillResult(Identity identity,FpVerifyResult result){
        int score1 = -1;
        int score2 = -1;
        try {
            score1 = Integer.parseInt(mSimilarity1.getText().toString());
        }
        catch (Exception e){

        }

        try {
            score2 = Integer.parseInt(mSimilarity2.getText().toString());
        }
        catch (Exception e){

        }
        result.setIdentityNo(identity.getIdentityNo());
        result.setFp1("" + currentIndex1);
        result.setFp2("" + currentIndex2);
        result.setScore1(score1);
        result.setScore2(score2);
        if(TextUtils.isEmpty(mVerifyTime.getText())) {
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
    }

    @OnClick(R.id.btn_comparison1)
    public void onComparison1(){
        if(!TextUtils.isEmpty(identity.getFp1()) && !TextUtils.isEmpty(currentFpFeature1)){
            int ret = ssF.fingerComparison(identity.getFp1(),currentFpFeature1);
            mSimilarity1.setText("" + ret);
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mVerifyTime.setText(format.format(date));
        }
    }
    @OnClick(R.id.btn_comparison2)
    public void onComparision2(){
        if(!TextUtils.isEmpty(identity.getFp2()) && !TextUtils.isEmpty(currentFpFeature2)){
            int ret = ssF.fingerComparison(identity.getFp2(),currentFpFeature2);
            mSimilarity2.setText("" + ret);
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mVerifyTime.setText(format.format(date));
        }
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
