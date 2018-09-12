package com.jch.mydemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;

/**
 * @author changhua.jiang
 * @since 2018/9/12 下午3:04
 */

public class FpVerifyActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp_verify);
        ButterKnife.bind(this);
    }
}
