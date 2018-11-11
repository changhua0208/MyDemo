package com.houyi.notarization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/11/6 3:17 PM
 */

public class AddTxtResourceDlg extends Activity {
    @BindView(R.id.rb_application_form)
    RadioButton mRbApplication;
    @BindView(R.id.rb_talk_record)
    RadioButton mRbTalkRecord;
    @BindView(R.id.rb_notification_form)
    RadioButton mRbNotification;

    private static final int CODE_OPEN_FILE_BROWSER = 102;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_res_dlg);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_add)
    public void onAddResource(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, CODE_OPEN_FILE_BROWSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
