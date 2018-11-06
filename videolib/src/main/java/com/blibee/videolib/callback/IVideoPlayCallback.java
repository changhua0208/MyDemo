package com.blibee.videolib.callback;

/**
 * Created by zhanglin on 2017/9/14.
 */

public interface IVideoPlayCallback {
    void onPlayError();

    void onAutoComplete();

    void onPrepared();
}
