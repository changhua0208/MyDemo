package com.jch.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * @since 2018/9/12 下午3:00
 */

public class FaceVerifyActivity extends BaseActivity {
    @BindView(R.id.img_identity_photo)
    ImageView imgIdentityPhoto;
    @BindView(R.id.img_photo)
    ImageView imgPhoto;

    @BindView(R.id.et_name)
    TextView tvName;
    @BindView(R.id.et_birthday)
    TextView tvBirthDay;
    @BindView(R.id.et_identity_no)
    TextView tvIdentityNo;

    @BindView(R.id.et_similarity)
    TextView tvSimilarity;

    private Bitmap bmp1,bmp2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_verify);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        Identity identity = CurrentIdentityUtils.currentIdentity();

        bmp1 = IdentityHelper.getInstance().getPhoto(identity);
        if(bmp1 != null){
            imgPhoto.setImageBitmap(bmp1);
        }
        bmp2 = BitmapFactory.decodeByteArray(identity.getImage(),0,identity.getImage().length);
        if(bmp2 != null){
            imgIdentityPhoto.setImageBitmap(bmp2);
        }
        tvName.setText(identity.getName());
        tvBirthDay.setText(identity.getBirthDay());
        tvIdentityNo.setText(identity.getIdentityNo());
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }
}
