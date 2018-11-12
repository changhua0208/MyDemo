package com.houyi.notarization;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cw.cwsdk.u8API.idcard.AsyncParseSFZ;
import com.cw.cwsdk.u8API.idcard.ParseSFZAPI;
import com.houyi.notarization.mode.Notarization;
import com.houyi.notarization.mode.Person;
import com.houyi.notarization.thr.IThreadController;
import com.houyi.notarization.thr.LoopThread;
import com.houyi.notarization.utils.CurrentIdentityUtils;
import com.houyi.notarization.utils.IdentityHelper;
import com.houyi.notarization.utils.NotaDaoHelper;
import com.houyi.notarization.utils.PersonDaoHelper;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/9/10 下午7:55
 */

public class NewNotaActivity extends BaseActivity {
    private static final String TAG = "NewNotaActivity";
    private static final int MSG_CLEAR_INFO = 0xf1;
    private static final int MSG_READ_CARD_FAIL = 0xf2;
    private static final int MSG_SELECT_CARD_FAIL = 0xf3;
    private static final int MSG_READ_CARD_SUCCESS = 0xf4;




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
//    @BindView(R.id.btn_comparison)
//    View mBtnComparision;
    @BindView(R.id.img_identity_photo)
    ImageView mImage;
    @BindView(R.id.tv_notary)
    EditText mNotary;
    int role = 0;

//    private Person identity;
    private Notarization nota;
    private SoundPool mSoundPool;
    private int mSuccessSoundID,mErrorID;

    private IThreadController threadController;

    //头像
    private Bitmap headBmp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newnota);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent != null){
            role = intent.getIntExtra("role",0);
        }
        if(role == 1){
            mNotary.setVisibility(View.GONE);
        }
        loadPromptSoundFile();

        MyThread thr = new MyThread(this,100);
        threadController = thr.getThreadController();
        threadController.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if(threadController != null && threadController.getState() == LoopThread.ThreadState.RUNNING)
//            threadController.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(threadController != null && threadController.getState() == LoopThread.ThreadState.PAUSED){
//            threadController.resume();
//        }
    }

    @OnClick(R.id.btn_save)
    public void onSave(){
        //for test
        if(nota == null){
            nota = NotaDaoHelper.createNota();
            Person person = new Person();
            person.setName(mName.getText().toString());
            person.setAddress(mAddr.getText().toString());
            person.setYear("1999");
            person.setMonth("12");
            person.setDay("11");
            person.setIdentityNo("362330198602082838");
            PersonDaoHelper.insertPerson(person);

            nota.setIdentityNo(person.getIdentityNo());
            nota.setCreateTime(new Date());
            nota.setNotarization(mItems.getText().toString());
            nota.setNotary(mNotary.getText().toString());

        }
        //test end

        if(nota != null) {
            NotaDaoHelper.insertNota(nota);
            if(nota.getPerson() != null && headBmp != null) {
                IdentityHelper.getInstance().saveHeadImage(nota.getPerson(), headBmp);
                IdentityHelper.getInstance().saveIdentityFpFeature(nota.getPerson());
            }
            Intent data = new Intent();
            data.putExtra("identityNo", nota.getIdentityNo());
            data.putExtra("nid",nota.getNid());
            setResult(RESULT_OK, data);
            finish();
        }

    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }

    @OnClick(R.id.btn_comparison)
    public void onComparison(){
//        if(nota == null){
//            toast("请刷身份证！");
//            return ;
//        }
        Intent intent = new Intent(this,ComparisonActivity.class);
        startActivity(intent);
    }

    private void loadPromptSoundFile() {
        mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        mSuccessSoundID = mSoundPool.load(this, R.raw.success, 1);
        mErrorID=mSoundPool.load(this, R.raw.error, 1);
    }
    private void PlayPromptSoundFile(int nSoundID) {
        mSoundPool.play(nSoundID, 1, 1, 0, 0, 1);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_CLEAR_INFO:
                    clearInfo();
                    break;
                case MSG_READ_CARD_FAIL:
                    toast("读卡失败");
                    break;
                case MSG_SELECT_CARD_FAIL:
                    toast("选卡失败");
                    break;
                case MSG_READ_CARD_SUCCESS:
                    autoFillInfo();
                    break;
            }
        }
    };

    class MyThread extends LoopThread implements AsyncParseSFZ.OnReadSFZListener {
        AsyncParseSFZ parser;
        MyThread(Context context, long timeslice){
            super(timeslice);
            parser = AsyncParseSFZ.getInstance(context);
            parser.setOnReadSFZListener(this);
        }

        @Override
        protected void onProcess() {
            parser.readCardID();
        }

        @Override
        protected void onStop() {
            parser.setOnReadCardIDListener(null);
        }

        @Override
        public void onReadSuccess(ParseSFZAPI.People people) {
            Message msg = Message.obtain();
            msg.what = MSG_READ_CARD_SUCCESS;
            msg.obj = people;
            handler.sendMessage(msg);

        }

        @Override
        public void onReadFail(int i) {
            Message msg = Message.obtain();
            msg.what = MSG_READ_CARD_FAIL;
            handler.sendMessage(msg);
        }
    }
//
//    public void clearInfo(){
//        mName.setText("");
//        mAddr.setText("");
//        mBirthday.setText("");
//        mSex.setText("");
//        mImage.setImageBitmap(null);
//    }
//
    public void autoFillInfo(){
        //id2Result
        Notarization notarization = NotaDaoHelper.createNota();
        notarization.setPerson();
        per = new Person();
        identity.setName(id2Result[1]);
        identity.setSex(id2Result[2]);
        identity.setNation(id2Result[3]);
        identity.setYear(id2Result[4]);
        identity.setMonth(id2Result[5]);
        identity.setDay(id2Result[6]);
        identity.setAddress(id2Result[7]);

        identity.setIdentityNo(id2Result[8]);
        //identity.setIssuinAuthority(id2Result[9]);
        identity.setBeginTime(id2Result[10]);
        identity.setEndTime(id2Result[11]);
        identity.setYear(id2Result[4]);
        identity.setMonth(id2Result[5]);
        identity.setDay(id2Result[6]);
        identity.setAddress(id2Result[7]);
        identity.setImage(SSUtil.hexStringToByte(id2Result[12]));
        if(id2Result[14].equals("0")){
            identity.setFp1Name(id2Result[15]);
            identity.setFp2Name(id2Result[16]);
            identity.setFp1(id2Result[17]);
            identity.setFp2(id2Result[18]);
        }
        identity.setComparison("否");

        CurrentIdentityUtils.save(identity);

        mName.setText(identity.getName());
        mAddr.setText(identity.getAddress());
        mBirthday.setText(identity.getBirthDay());
        mSex.setText(identity.getSex());
        byte[] imgData = identity.getImage();
        headBmp = BitmapFactory.decodeByteArray(imgData,0,imgData.length);
        mImage.setImageBitmap(headBmp);
    }
//
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        threadController.stop();
    }
}
