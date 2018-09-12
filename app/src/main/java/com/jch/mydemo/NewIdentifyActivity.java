package com.jch.mydemo;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.jch.mydemo.mode.DaoSession;
import com.jch.mydemo.mode.Identity;
import com.jch.mydemo.thr.IThreadController;
import com.jch.mydemo.thr.LoopThread;
import com.sdses.id2CardInterface.ID2CardInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/9/10 下午7:55
 */

public class NewIdentifyActivity extends BaseActivity {
    private static final String TAG = "NewIdentifyActivity";

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
    private SoundPool mSoundPool;
    private int mSuccessSoundID,mErrorID;

    private ID2CardInterface id2Handle=null;
    private IThreadController threadController;
    private volatile String[] id2Result;

    private int openRet=0,closeRet=0;

    private static final int MSG_CLEAR_INFO = 1;
    private static final int MSG_SELECT_CARD_FAIL = 2;
    private static final int MSG_READ_CARD_FAIL = 3;
    private static final int MSG_READ_CARD_SUCCESS = 4;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newidentity);
        ButterKnife.bind(this);

        id2Handle=new ID2CardInterface();
        openRet=id2Handle.openReadCard();
        if(openRet==1){
            MyThread thr = new MyThread(200);
            threadController = thr.getThreadController();
            threadController.start();
        }
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
                    autoFillInfo();
                    toast("读卡失败");
                    break;
                case MSG_SELECT_CARD_FAIL:
                    toast("选卡失败");
                    break;
                case MSG_READ_CARD_SUCCESS:
                    break;
            }
        }
    };

    class MyThread extends LoopThread{
        MyThread(long timeslice){
            super(timeslice);
        }

        @Override
        protected void onProcess() {
            if(id2Handle.selectCard()){
                handler.sendEmptyMessage(MSG_CLEAR_INFO);
                if(id2Handle.searchID2Card()){
                    id2Result=id2Handle.readCardInfoNew();
                    if(id2Result[0].equalsIgnoreCase("0")){
                        PlayPromptSoundFile(mSuccessSoundID);
                        handler.sendEmptyMessage(MSG_READ_CARD_SUCCESS);
                    }else{
                        PlayPromptSoundFile(mErrorID);
                        handler.sendEmptyMessage(MSG_READ_CARD_FAIL);
                    }
                }else{
                    handler.sendEmptyMessage(MSG_SELECT_CARD_FAIL);
                }
            }
        }

        @Override
        protected void onStop() {
            id2Handle.closeReadCard();
        }
    }

    public void clearInfo(){
        mName.setText("");
        mAddr.setText("");
        mBirthday.setText("");
        mSex.setText("");
    }

    public void autoFillInfo(){
        //id2Result
        identity = new Identity();
        identity.setName(id2Result[1]);
        identity.setSex(id2Result[2]);
        identity.setNation(id2Result[3]);
        identity.setYear(id2Result[4]);
        identity.setMonth(id2Result[5]);
        identity.setDay(id2Result[6]);
        identity.setAddress(id2Result[7]);

        identity.setIdentityNo(id2Result[8]);
        identity.setIssuinAuthority(id2Result[9]);
        //identity.setNation(id2Result[10]);
        identity.setYear(id2Result[4]);
        identity.setMonth(id2Result[5]);
        identity.setDay(id2Result[6]);
        identity.setAddress(id2Result[7]);

//        mName.setText("");
//        mAddr.setText("");
//        mBirthday.setText("");
//        mSex.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadController.stop();
    }
}
