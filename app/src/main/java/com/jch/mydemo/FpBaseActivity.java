package com.jch.mydemo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.sdses.fingerJar.SSFingerInterfaceImp;

/**
 * @author changhua.jiang
 * @since 2018/9/14 下午3:21
 */

public class FpBaseActivity extends BaseActivity {
    static final int MSG_USB_CONNECTION = 10;

    protected SSFingerInterfaceImp ssF;
    protected ProgressDialog mProgressDlg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDlg = new ProgressDialog(this);
        Thread thread = new Thread(connectRun);
        thread.start();
        mProgressDlg.show();
    }

    Runnable connectRun = new Runnable() {
        @Override
        public void run() {
            int nRet = -1;
            ssF=new SSFingerInterfaceImp(FpBaseActivity.this);
            nRet = ssF.f_powerOn();
            if(nRet == 0) {
                for (int i = 0; i < 26; i++) {
                    nRet = ssF.SS_USBConnect();
                    if (nRet == 0) {
                        break;
                    }
                    SystemClock.sleep(200);
                }
                Message msg = Message.obtain();
                msg.what = MSG_USB_CONNECTION;
                msg.arg1 = nRet;
                handler.sendMessage(msg);
            }
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_USB_CONNECTION:
                    if(mProgressDlg.isShowing())
                        mProgressDlg.dismiss();
                    if(msg.arg1 != 0){
                        AlertDialog dlg = new AlertDialog.Builder(FpBaseActivity.this)
                                .setTitle(R.string.tip_error)
                                .setMessage(R.string.msg_fp_open_fail)
                                .setCancelable(false)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).show();
                    }
                    break;
            }
        }
    };
}
