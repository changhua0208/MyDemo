package com.houyi.notarization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/11/7 5:33 PM
 */

public class SimpleNotaActivity extends BaseNotaActivity {
    @BindView(R.id.but_add_resource)
    TextView mBtnAdd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        int type = TYPE_TXT;
        if(getIntent() != null){
            type = getIntent().getIntExtra("type",TYPE_TXT);
        }
        int resId = getBtnAddResId(type);
        mBtnAdd.setText(resId);
    }

    private int getBtnAddResId(int type){
        switch (type){
            case TYPE_TXT:
                return R.string.add_txt_res;
            case TYPE_VIDEO:
                return R.string.add_video;
            case TYPE_AUDIO:
                return R.string.add_audio;
            case TYPE_IMG:
                return R.string.add_txt_res;
            default:
                return R.string.add_txt_res;
        }
    }

    @OnClick(R.id.but_add_resource)
    public void onAddResource(){

    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }

    @OnClick(R.id.btn_pigeonhole)
    public void onPigeonhole(){

    }

}
