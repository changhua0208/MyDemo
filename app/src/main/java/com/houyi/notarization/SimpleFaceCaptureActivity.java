package com.houyi.notarization;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.LivenessDetectionFrames;

/**
 * @author changhua.jiang
 * @since 2018/9/18 下午7:00
 */

public class SimpleFaceCaptureActivity extends FaceCaptureMainActivity {
    @Override
    public void onInitializeSucc() {
        super.onInitializeSucc();
        super.startVerification(); // 开始人脸捕获
    }

    @Override
    public void onInitializeFail(Throwable e) {
        super.onInitializeFail(e);
        Log.e(TAG, "无法初始化活体检测...", e);
        Toast.makeText(this, "无法初始化活体检测", Toast.LENGTH_LONG).show();
//        Intent i = new Intent(SampleFaceCaptureActivity.this, SampleResultActivity.class);
//        i.putExtra("is_success", false);
//        handleLivenessFinish(i);

        Intent it = new Intent();
        it.putExtra("is_success",false);
        setResult(Activity.RESULT_OK, it);
        finish();
    }


    @Override
    public void onPrestartSuccess(final LivenessDetectionFrames detectionFrames) {
        super.onPrestartSuccess(detectionFrames);

//        Intent i = new Intent(SampleFaceCaptureActivity.this, SampleResultActivity.class);
//        i.putExtra("package_content", detectionFrames.verificationPackage);
//        i.putExtra("is_success", true);
//        handleLivenessFinish(i);

        Intent it = new Intent();
//        if(detectionFrames.verificationPackage!=null)
        {
            Log.e(TAG, "package_content 长度：" + detectionFrames.verificationPackage.length);
            it.putExtra("package_content", detectionFrames.verificationPackage);
            it.putExtra("is_success", true);
        }
//        else{
//            //Toast.makeText(this, "将原始数据解析成人像数据失败，请重新拍摄", Toast.LENGTH_LONG).show();
//            Toast.makeText(this, "捕获人像失败，请重新拍摄", Toast.LENGTH_LONG).show();
//            it.putExtra("is_success", false);
//        }
        setResult(Activity.RESULT_OK, it);
        finish();//封掉测试长时间捕捉,不能持续捕捉，成功一次就停，
    }

    @Override
    public void onPrestartFail(int result) {
        super.onPrestartFail(result);

        //finish();
        Log.e(TAG, "onPrestartFail 捕获人脸失败");
        Toast.makeText(this, "onPrestartFail 捕获人脸失败", Toast.LENGTH_LONG).show();

        Intent it = new Intent();
        it.putExtra("is_success",false);
        setResult(Activity.RESULT_OK, it);
        finish();
    }
}
