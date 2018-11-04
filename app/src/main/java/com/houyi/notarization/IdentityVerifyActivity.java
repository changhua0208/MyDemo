package com.houyi.notarization;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.houyi.notarization.mode.Identity;
import com.houyi.notarization.utils.BitmapUtils;
import com.houyi.notarization.utils.CurrentIdentityUtils;
import com.houyi.notarization.utils.IdentityHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/9/12 下午2:51
 */

public class IdentityVerifyActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.tv_address)
    TextView mAddress ;
    @BindView(R.id.tv_year)
    TextView mYear;
    @BindView(R.id.tv_sex)
    TextView mSex;
    @BindView(R.id.tv_month)
    TextView mMonth;
    @BindView(R.id.tv_day)
    TextView mDay;
    @BindView(R.id.tv_nation)
    TextView mNation;
    @BindView(R.id.tv_issuin_authority)
    TextView mAuthority;
    @BindView(R.id.tv_identity_no)
    TextView mIdentityNo;
    @BindView(R.id.tv_time_limit)
    TextView mTimeLimit;
    @BindView(R.id.img_head)
    ImageView mHead;
    @BindView(R.id.al_identity_front)
    View mIdentityFront;
    @BindView(R.id.al_identity_back)
    View mIdentityBack;

    ProgressDialog mPd;

    //private static final int MSG_SAVE_FINISH = 100;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indentity_verify);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews(){
        mPd = new ProgressDialog(this);

        Identity identity = CurrentIdentityUtils.currentIdentity();
        mName.setText(identity.getName());
        mAddress.setText(identity.getAddress());
        mSex.setText(identity.getSex());
        mNation.setText(identity.getNation());
        mYear.setText(identity.getYear());
        mMonth.setText(identity.getMonth());
        mDay.setText(identity.getDay());
        mIdentityNo.setText(identity.getIdentityNo());
        Bitmap head = BitmapUtils.byteArray2bmp(identity.getImage());
        mHead.setImageBitmap(head);

        mAuthority.setText(identity.getIssuinAuthority());
        try {
            mTimeLimit.setText(format(identity.getBeginTime()) + "-" + format(identity.getEndTime()));
        }
        catch (Exception e){

        }
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSave(){
        mPd.show();
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Identity identity = CurrentIdentityUtils.currentIdentity();
                IdentityHelper.getInstance().saveIdentityView(identity,mIdentityFront,mIdentityBack);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(mPd.isShowing())
                    mPd.dismiss();
                showMsg(R.string.msg_save_ok);
            }
        }.execute();

    }

    private String format(String time){
        if(time.split(".").length == 3){
            return time;
        }
        else{
            String year = time.substring(0,4);
            String month = time.substring(4,6);
            String day = time.substring(6,8);
            return year + "." + month + "." + day;
        }
    }
}
