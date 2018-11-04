package com.houyi.notarization.thr;

public interface IThreadController {
    public void start();
    public void pause();
    public void resume();
    public void stop();

    public LoopThread.ThreadState getState();
    public LoopThread getCurrentThread();
}
