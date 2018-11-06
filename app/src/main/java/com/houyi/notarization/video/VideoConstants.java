package com.houyi.notarization.video;

/**
 * Created by zhanglin on 2017/9/12.
 */

public interface VideoConstants {
    int VIDEO_PLAY_NORMAL = 1;
    int VIDEO_PLAY_JS_FINISH = 2;
    int VIDEO_PLAY_EXCEPTION = 3;

    int CODE_404 = 404;//本地文件不存在
    int CODE_405 = 405;//本地文件损坏
    int CODE_406 = 406;//url 链接异常
    int CODE_500 = 500;
    int CODE_100 = 100;

    int ENCODING_BIT_RATE = 1500000;//设置比特率，可不设，
    int RECORD_WITH = 1280;//设置录制视频的宽  可不设，默认1280
    int RECORD_HEIGHT = 720;//设置录制视频的宽  可不设，默认720
    int DEFAULT_MAX_DURATION = 60000;//录制最大时长，默认60s
}
