package com.houyi.notarization;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cw.cwsdk.cw;
import com.jch.videolib.util.DialogHelper;

import cn.com.aratek.fp.Bione;
import cn.com.aratek.fp.FingerprintScanner;


/**
 * @author changhua.jiang
 * @since 2018/9/14 下午3:21
 */

public class FpBaseActivity extends BaseActivity {
    private static final int MSG_USB_CONNECTION = 10;
    private static final String FP_DB_PATH = "/sdcard/fp.db";

    protected ProgressDialog mProgressDlg;
    private static String TAG = "FpBaseActivity";
    protected FingerprintScanner mScanner;
    private USBBroadcastReceiver usbBroadcastReceiver;
    protected boolean mDeviceOpened = false;

    class USBBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG,action);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDlg = new ProgressDialog(this);

        usbBroadcastReceiver = new USBBroadcastReceiver();
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(usbBroadcastReceiver, usbDeviceStateFilter);

        cw.FingerPrintAPI().openUSB();
        mScanner = cw.FingerPrintAPI().Scanner(this);
        openDevice();
    }

    private void openDevice() {
        new Thread() {
            @Override
            public void run() {
                synchronized (FpBaseActivity.this) {
                    showWaitDlg();
                    int error;
                    if ((error = mScanner.open()) != FingerprintScanner.RESULT_OK) {
                        Log.i(TAG, "------------" + error);
                        DialogHelper.showOneButtonDialog(FpBaseActivity.this,R.string.msg_fp_open_fail,R.string.ok,true,null);
                    } else {
                        mDeviceOpened = true;
                    }
                    if ((error = Bione.initialize(FpBaseActivity.this, FP_DB_PATH)) != Bione.RESULT_OK) {
                        DialogHelper.showOneButtonDialog(FpBaseActivity.this,R.string.msg_fp_open_fail,R.string.ok,true,null);
                    }
                    Log.i(TAG, "Fingerprint algorithm version: " + Bione.getVersion());
                    dismissWaitDlg();
                }
            }
        }.start();
    }

    public void showWaitDlg(){
        if(!mProgressDlg.isShowing())
            mProgressDlg.show();
    }

    public void dismissWaitDlg(){
        if(mProgressDlg.isShowing())
            mProgressDlg.dismiss();
    }

//    private class FingerprintTask extends AsyncTask<String, Integer, Void> {
//        private boolean mIsDone = false;
//
//        @Override
//        protected void onPreExecute() {
//            showWaitDlg();
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//            long startTime, captureTime = -1, extractTime = -1, generalizeTime = -1, verifyTime = -1;
//            FingerprintImage fi = null;
//            byte[] fpFeat = null, fpTemp = null;
//            Result res;
//
//            do {
//                if (params[0].equals("show") || params[0].equals("enroll") || params[0].equals("verify") || params[0].equals("identify")) {
//                    int capRetry = 0;
//                    mScanner.prepare();
//                    do {
//                        startTime = System.currentTimeMillis();
//                        res = mScanner.capture();
//                        captureTime = System.currentTimeMillis() - startTime;
//                        fi = (FingerprintImage) res.data;
//                        int quality;
//                        if (fi != null) {
//                            quality = Bione.getFingerprintQuality(fi);
//                            Log.i(TAG, "Fingerprint image quality is " + quality);
//                            if (quality < 50 && capRetry < 3 && !isCancelled()) {
//                                capRetry++;
//                                continue;
//                            }
//                        }
//
//                        if (res.error != FingerprintScanner.NO_FINGER || isCancelled()) {
//                            break;
//                        }
//
//                    } while (true);
//                    mScanner.finish();
//
//                    if (isCancelled()) {
//                        break;
//                    }
//
//                    if (res.error != FingerprintScanner.RESULT_OK) {
//                        DialogHelper.showOneButtonDialog(FpBaseActivity.this,R.string.msg_capture_fail,R.string.ok,true,null);
//                        break;
//                    }
//
//                    //updateFingerprintImage(fi);
//                }
//
//                if (params[0].equals("show")) {
//                    showInformation(getString(R.string.capture_image_success), null);
//                } else if (params[0].equals("enroll")) {
//                    showProgressDialog(getString(R.string.loading), getString(R.string.enrolling));
//                } else if (params[0].equals("verify")) {
//                    showProgressDialog(getString(R.string.loading), getString(R.string.verifying));
//                } else if (params[0].equals("identify")) {
//                    showProgressDialog(getString(R.string.loading), getString(R.string.identifying));
//                }
//
//                if (params[0].equals("enroll") || params[0].equals("verify") || params[0].equals("identify")) {
//                    startTime = System.currentTimeMillis();
//                    res = Bione.extractFeature(fi);
//                    extractTime = System.currentTimeMillis() - startTime;
//                    if (res.error != Bione.RESULT_OK) {
//                        showError(getString(R.string.enroll_failed_because_of_extract_feature), getFingerprintErrorString(res.error));
//                        break;
//                    }
//                    fpFeat = (byte[]) res.data;
//                }
//
//                if (params[0].equals("enroll")) {//注册
//                    startTime = System.currentTimeMillis();
//                    res = Bione.makeTemplate(fpFeat, fpFeat, fpFeat);
//                    generalizeTime = System.currentTimeMillis() - startTime;
//                    if (res.error != Bione.RESULT_OK) {
//                        showError(getString(R.string.enroll_failed_because_of_make_template), getFingerprintErrorString(res.error));
//                        break;
//                    }
//                    fpTemp = (byte[]) res.data;
//
//                    int id = Bione.getFreeID();
//                    if (id < 0) {
//                        showError(getString(R.string.enroll_failed_because_of_get_id), getFingerprintErrorString(id));
//                        break;
//                    }
//                    int ret = Bione.enroll(id, fpTemp);
//                    if (ret != Bione.RESULT_OK) {
//                        showError(getString(R.string.enroll_failed_because_of_error), getFingerprintErrorString(ret));
//                        break;
//                    }
//                    mId = id;
//                    showInformation(getString(R.string.enroll_success), getString(R.string.enrolled_id, id));
//                } else if (params[0].equals("verify")) {//比对
//                    startTime = System.currentTimeMillis();
//                    res = Bione.verify(mId, fpFeat);
//                    verifyTime = System.currentTimeMillis() - startTime;
//                    if (res.error != Bione.RESULT_OK) {
//                        showError(getString(R.string.verify_failed_because_of_error), getFingerprintErrorString(res.error));
//                        break;
//                    }
//                    if ((Boolean) res.data) {
//                        showInformation(getString(R.string.fingerprint_match), getString(R.string.fingerprint_similarity, res.arg1));
//                    } else {
//                        showError(getString(R.string.fingerprint_not_match), getString(R.string.fingerprint_similarity, res.arg1));
//                    }
//                } else if (params[0].equals("identify")) {
//                    startTime = System.currentTimeMillis();
//                    int id = Bione.identify(fpFeat);
//                    verifyTime = System.currentTimeMillis() - startTime;
//                    if (id < 0) {
//                        showError(getString(R.string.identify_failed_because_of_error), getFingerprintErrorString(id));
//                        break;
//                    }
//                    showInformation(getString(R.string.identify_match), getString(R.string.matched_id, id));
//                }
//            } while (false);
//
//            updateSingerTestText(captureTime, extractTime, generalizeTime, verifyTime);
//            enableControl(true);
//            dismissProgressDialog();
//            mIsDone = true;
//            return null;
//        }
//
//        /**
//         * 当我们的异步任务执行完之后，就会将结果返回给这个方法，这个方法也是在UI Thread当中调用的，我们可以将返回的结果显示在UI控件上
//         *
//         * @param result
//         */
//        @Override
//        protected void onPostExecute(Void result) {
//        }
//
//        /**
//         * 这个方法也是在UI Thread当中执行的，我们在异步任务执行的时候，有时候需要将执行的进度返回给我们的UI界面
//         *
//         * @param values
//         */
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//        }
//
//        @Override
//        protected void onCancelled() {
//        }
//
//        public void waitForDone() {
//            while (!mIsDone) {
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    private void closeDevice() {
//        new Thread() {
//            @Override
//            public void run() {
//                synchronized (FpBaseActivity.this) {
//                    showWaitDlg();
//                    int error;
//                    if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED) {
//                        mTask.cancel(false);
//                        mTask.waitForDone();
//                    }
//                    if ((error = mScanner.close()) != FingerprintScanner.RESULT_OK) {
//                        showError(getString(R.string.fingerprint_device_close_failed), getFingerprintErrorString(error));
//                    } else {
//                        showInformation(getString(R.string.fingerprint_device_close_success), null);
//                    }
//                    if ((error = Bione.exit()) != Bione.RESULT_OK) {
//                        showError(getString(R.string.algorithm_cleanup_failed), getFingerprintErrorString(error));
//                    }
//                    mDeviceOpened = false;
//                    dismissProgressDialog();
//                }
//            }
//        }.start();
//    }

}
