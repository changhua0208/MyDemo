package com.houyi.notarization.video.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.blibee.videolib.callback.IVideoRecordCallback;
import com.blibee.videolib.recorder.RecordManager;
import com.blibee.videolib.util.DialogHelper;
import com.blibee.videolib.util.PermissionUtil;
import com.houyi.notarization.R;
import com.houyi.notarization.video.VideoConstants;
import com.houyi.notarization.video.event.EventJSCloseRecord;
import com.houyi.notarization.video.event.EventRecordCancel;
import com.houyi.notarization.video.event.EventRecordComplete;
import com.houyi.utils.RxBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by zhanglin on 2017/9/7.
 */
public class VideoRecorderActivity extends BaseActivity implements IVideoRecordCallback {
    private boolean isLightOpen = false;
    @BindView(R.id.recorder_start)
    ImageView btnStart;
    @BindView(R.id.recorder_stop)
    ImageView btnStop;
    @BindView(R.id.video_view)
    SurfaceView mVideoView;
    @BindView(R.id.chronometer)
    Chronometer chronometer;
    //@BindView(R.id.switch_btn)
    //Button btn_switch;
    @BindView(R.id.open_light)
    ImageView ivOpenLight;

    public static void actionLaunch(Context context, int maxDuration) {
        context.startActivity(new Intent(context, VideoRecorderActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("maxDuration", maxDuration));
    }

    @Override
    public int setContentView() {
        return R.layout.act_recorder;
    }

    @Override
    public void initUIAndData() {
        if (!PermissionUtil.hasAudioPermission()) {
            showPermissionDialog(R.string.audio_permission_refuse);
            return;
        } else if (!PermissionUtil.hasCameraPermission()) {
            showPermissionDialog(R.string.camera_permission_refuse);
            return;
        }
        int maxDuration = getIntent().getIntExtra("maxDuration", VideoConstants.DEFAULT_MAX_DURATION);
        RecordManager.getInstance().setRecordWidth(VideoConstants.RECORD_WITH, VideoConstants.RECORD_HEIGHT);//设置录制视频的宽
        RecordManager.getInstance().setEncodingBitRate(VideoConstants.ENCODING_BIT_RATE);
        RecordManager.getInstance().setSurfaceView(mContext, mVideoView, maxDuration, this);

    }

    private void showPermissionDialog(int strId) {
        DialogHelper.showOneButtonDialog(mContext, mContext.getString(strId), mContext.getString(R.string.know_that), false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    @OnClick({R.id.switch_btn, R.id.recorder_start, R.id.recorder_stop, R.id.iv_close, R.id.open_light})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_btn:
                RecordManager.getInstance().switchCamera();
                break;
            case R.id.recorder_start:
                startRecorder();
                break;
            case R.id.recorder_stop:
                RecordManager.getInstance().stopRecord(true);
                break;
            case R.id.iv_close:
                if (RecordManager.getInstance().isRecording()) {
                    showCancelDialog();
                } else {
                    finish();
                }
                break;
            case R.id.open_light:
                isLightOpen = !isLightOpen;
                RecordManager.getInstance().turnLight(isLightOpen);
                ivOpenLight.setImageResource(!isLightOpen ? R.mipmap.open_light : R.mipmap.close_light);
                break;
            default:
                break;
        }
    }

    private void startRecorder() {
        RecordManager.getInstance().startRecording();
        showToast(R.string.start_recording);
        btnStart.setEnabled(false);
        chronometer.setVisibility(View.VISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        btnStart.setVisibility(View.INVISIBLE);
        btnStop.setVisibility(View.VISIBLE);
        btnStop.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecordManager.getInstance().onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        RecordManager.getInstance().onPause();
    }


    @Override
    public void onRecordStop(String videoPath, int duration, boolean isSilence) {
        if (isSilence) {
            showToast(getString(R.string.onpause_record_finish));
            RxBus.getInstance().post(new EventRecordComplete(videoPath, duration));
            finish();
            return;
        }
        if (duration <= 1) {
            showToast(R.string.record_time_too_short);
            finish();
            return;
        }
        chronometer.stop();
        btnStop.setEnabled(false);
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.INVISIBLE);
        showStopDialog(videoPath, duration);
    }

    @Override
    public void onRecordError(int code, String msg) {
        RxBus.getInstance().post(new EventRecordCancel(code, msg));
        showToast(msg);
        finish();
    }

    @Override
    public List<Subscription> getSubscriptions() {
        return Arrays.asList(RxBus.getInstance().toObserverable(EventJSCloseRecord.class)
                .subscribe(new Action1<EventJSCloseRecord>() {
                    @Override
                    public void call(EventJSCloseRecord event) {
                        RecordManager.getInstance().stopRecord(false);
                        finish();
                    }
                }));
    }

    public void showStopDialog(final String videoPath, final int duration) {
        DialogHelper.showTwoButtonDialog(mContext, mContext.getString(R.string.either_save_video), mContext.getString(R.string.save),
                mContext.getString(R.string.reRecord), false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RxBus.getInstance().post(new EventRecordComplete(videoPath, duration));
                        finish();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RecordManager.getInstance().deleteFile();
                        startRecorder();
                    }
                });
    }

    public void showCancelDialog() {
        DialogHelper.showTwoButtonDialog(mContext, mContext.getString(R.string.sure_to_cancel), mContext.getString(R.string.ok),
                mContext.getString(R.string.discard), false, null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RecordManager.getInstance().stopRecord(false);
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        RecordManager.getInstance().onDestory();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (RecordManager.getInstance().isRecording()) {
            showCancelDialog();
        } else {
            super.onBackPressed();
        }
    }
}
