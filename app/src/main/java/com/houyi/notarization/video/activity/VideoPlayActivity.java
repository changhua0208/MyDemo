package com.houyi.notarization.video.activity;

import android.content.Context;
import android.content.Intent;

import com.blibee.videolib.callback.IVideoPlayCallback;
import com.blibee.videolib.player.CustomVideo;
import com.blibee.videolib.recorder.RecordManager;
import com.houyi.notarization.R;
import com.houyi.notarization.video.VideoConstants;
import com.houyi.notarization.video.event.EventPlayVideo;
import com.houyi.utils.RxBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by zhanglin on 2017/9/7.
 */
public class VideoPlayActivity extends BaseActivity implements IVideoPlayCallback {
    @BindView(R.id.video_player)
    CustomVideo videoPlayer;
    private String path;

    public static void actionLaunch(Context context, String path) {
        context.startActivity(new Intent(context, VideoPlayActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("path", path));
    }

    @Override
    public int setContentView() {
        return R.layout.act_player;
    }

    @Override
    public void initUIAndData() {
        path = getIntent().getStringExtra("path");
        videoPlayer.setUp(path, false, "");
        videoPlayer.setIVideoPlayCallback(this);
        videoPlayer.startPlayVideo();
//        videoPlayer.addRotateMirror();//是否添加旋转镜像按钮
        videoPlayer.addRotatePicture();//是否添加旋转画面按钮
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    public List<Subscription> getSubscriptions() {
        return Arrays.asList(RxBus.getInstance().toObserverable(EventPlayVideo.class)
                .subscribe(new Action1<EventPlayVideo>() {
                    @Override
                    public void call(EventPlayVideo event) {
                        if (event.type == VideoConstants.VIDEO_PLAY_JS_FINISH) {
                            videoPlayer.onDestroy();
                            finish();
                        }
                    }
                }));
    }

    @Override
    public void onPlayError() {
        RxBus.getInstance().post(new EventPlayVideo(VideoConstants.VIDEO_PLAY_EXCEPTION, path.startsWith("http") ? VideoConstants.CODE_406 : VideoConstants.CODE_405, getString(R.string.play_execption)));
    }

    @Override
    public void onAutoComplete() {
        RxBus.getInstance().post(new EventPlayVideo(VideoConstants.VIDEO_PLAY_NORMAL));
    }

    @Override
    public void onPrepared() {

    }

    @Override
    public void onBackPressed() {
        if (videoPlayer.onBackPressed()) return;
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        if (RecordManager.getInstance().isPlaying()) {
            RxBus.getInstance().post(new EventPlayVideo(VideoConstants.VIDEO_PLAY_EXCEPTION, VideoConstants.CODE_100, getString(R.string.play_cancel)));
            RecordManager.getInstance().setPlaying(false);
        }
        videoPlayer.onDestroy();
        super.onDestroy();
    }
}
