package com.houyi.notarization;

import android.widget.TextView;

import com.houyi.notarization.mode.Notarization;
import com.houyi.notarization.mode.Person;
import com.houyi.notarization.utils.CurrentNotaUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author changhua.jiang
 * @since 2018/11/6 12:03 PM
 */

public class BaseNotaActivity extends BaseActivity {
    public static final int TYPE_TXT = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_AUDIO = 3;
    public static final int TYPE_IMG = 4;

    @BindView(R.id.tv_person_name)
    TextView mName;
    @BindView(R.id.tv_notarization)
    TextView mNotaritation;
    @BindView(R.id.tv_notary)
    TextView mNotary;
    @BindView(R.id.tv_create_time)
    TextView mCreateTime;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews(){
        Notarization nota = CurrentNotaUtils.currentNota();
        Person person = nota.getPerson();
        mName.setText(person.getName());
        mNotaritation.setText(nota.getNotarization());
        mNotary.setText(nota.getNotary());
        Date date = nota.getCreateTime();
        DateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");
        mCreateTime.setText(fmt.format(date));
    }

}
