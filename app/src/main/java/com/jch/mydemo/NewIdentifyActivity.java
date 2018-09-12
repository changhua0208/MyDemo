package com.jch.mydemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.jch.mydemo.mode.DaoSession;
import com.jch.mydemo.mode.Identity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/9/10 下午7:55
 */

public class NewIdentifyActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    EditText mName;
    @BindView(R.id.tv_addr)
    EditText mAddr;
    @BindView(R.id.tv_birthday)
    EditText mBirthday;
    @BindView(R.id.tv_sex)
    EditText mSex;
    @BindView(R.id.tv_items)
    EditText mItems;
    @BindView(R.id.btn_comparison)
    Button mBtnComparision;

    private Identity identity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newidentity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_save)
    public void onSave(){
        String addr = mAddr.getText().toString();
        String name = mName.getText().toString();
        String birthday = mBirthday.getText().toString();
        String sex = mSex.getText().toString();
        String items = mItems.getText().toString();
        if(TextUtils.isEmpty(addr)
                || TextUtils.isEmpty(birthday)
                || TextUtils.isEmpty(sex)
                || TextUtils.isEmpty(items)
                || TextUtils.isEmpty(name))
        {
            this.toast("所有属性都不能空");
            return;
        }
        if(this.identity == null){
            this.identity = new Identity();
        }
        this.identity.setAddress(addr);
        this.identity.setName(name);
        this.identity.setItems(items);
        this.identity.setComparison("否");

        //TODO
        this.identity.setIdentityNo("" + SystemClock.currentThreadTimeMillis());
        this.identity.setSex(sex);
        DaoSession session = ((App)getApplication()).getDaoSession();
        session.insert(this.identity);

        Intent data = new Intent();
        data.putExtra("identityNo",identity.getIdentityNo());
        setResult(RESULT_OK,data);
        finish();
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }

    @OnClick(R.id.btn_comparison)
    public void onComparison(){
        Intent intent = new Intent(this,ComparisonActivity.class);
        startActivity(intent);
    }
}
