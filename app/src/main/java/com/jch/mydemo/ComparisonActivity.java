package com.jch.mydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 认证对比页面
 * @author changhua.jiang
 */

public class ComparisonActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparision);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_identity1)
    public void onIdentity1(){
        Intent intent = new Intent(this,IdentityVerifyActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_identity2)
    public void onIdentity2(){
        Intent intent = new Intent(this,FaceVerifyActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_identity3)
    public void onIdentity3(){
        Intent intent = new Intent(this,FpVerifyActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_fp_collection)
    public void onFpCollection(){
        Intent intent = new Intent(this,FpCollectionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_take_photo)
    public void onTakePhoto(){
        Intent intent = new Intent(this,PhotoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }

}
