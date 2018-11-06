package com.houyi.notarization.video.event;

/**
 * Created by zhanglin on 2017/9/11.
 */

public class EventPlayVideo {
    public int type;//1，结束播放   2，js调用关闭  3 播放异常
    public int code;
    public String msg;

    public EventPlayVideo(int type) {
        this.type = type;
    }

    public EventPlayVideo(int type, int code, String msg) {
        this.type = type;
        this.code = code;
        this.msg = msg;
    }
}
