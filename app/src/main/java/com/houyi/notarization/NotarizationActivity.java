package com.houyi.notarization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.OnClick;

/**
 * 案例详情
 * @author changhua.jiang
 * @since 2018/11/5 3:26 PM
 */

public class NotarizationActivity extends BaseNotaActivity{


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notarization);
    }



    @OnClick(R.id.btn_parties)
    public void onParties(){
        Intent intent = new Intent(this,PartiesActivity.class);
        //intent.putExtra("role",1);
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
        Intent intent = new Intent(this,AddTxtResActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_photo)
    public void onPhoto(){

    }

    @OnClick(R.id.btn_pigeonhole)
    public void onPigeonhole(){

    }

}