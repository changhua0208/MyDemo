package com.houyi.notarization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.houyi.notarization.mode.Notarization;
import com.houyi.notarization.mode.Person;
import com.houyi.notarization.utils.CurrentNotaUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/11/5 3:26 PM
 */

public class NotarizationActivity extends BaseActivity{
    @BindView(R.id.tv_person_name)
    TextView mName;
    @BindView(R.id.tv_notarization)
    TextView mNotaritation;
    @BindView(R.id.tv_notary)
    TextView mNotary;
    @BindView(R.id.tv_create_time)
    TextView mCreateTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notarization);
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

    @OnClick(R.id.btn_parties)
    public void onParties(){
        Intent intent = new Intent(this,PartiesActivity.class);
        intent.putExtra("role",1);
        startActivity(intent);
    }

    @OnClick(R.id.btn_video)
    public void onVideo(){

    }

    @OnClick(R.id.btn_audio)
    public void onAudio(){

    }

    @OnClick(R.id.btn_txt_res)
    public void onTxtRes(){

    }

    @OnClick(R.id.btn_photo)
    public void onPhoto(){

    }

    @OnClick(R.id.btn_pigeonhole)
    public void onPigeonhole(){

    }

}