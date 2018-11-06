package com.houyi.notarization.video.event;

/**
 * Created by zhanglin on 2017/9/11.
 */

public class EventRecordCancel {
    public int code;
    public String msg;

    public EventRecordCancel(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
