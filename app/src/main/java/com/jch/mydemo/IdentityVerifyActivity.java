package com.jch.mydemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/9/12 下午2:51
 */

public class IdentityVerifyActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indentity_verify);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }
}
