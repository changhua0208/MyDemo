package com.houyi.notarization.video.event;

/**
 * Created by zhanglin on 2017/9/11.
 */

public class EventRecordComplete {
    public String videoPath;
    public int duration;

    public EventRecordComplete( String filePath, int duration) {
        this.videoPath = filePath;
        this.duration = duration;
    }
}
