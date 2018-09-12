package com.jch.mydemo.thr;

public abstract class LoopThread implements Runnable{
    private Object lock = new Object();
    private ThreadState state = ThreadState.NEW;
    private Thread mThread;
    private long timeslice = 10;

    private IThreadController controller = new IThreadController() {
        @Override
        public synchronized void start() {
            if(getState() == ThreadState.NEW){
                mThread.start();
            }
            else{
                throw new RuntimeException("thread is started yet");
            }
        }

        @Override
        public synchronized void pause() {
            if(getState() == ThreadState.RUNNING){
                setState(ThreadState.PAUSED);
            }
            else{
                throw new RuntimeException("thread is not on running state");
            }
        }

        @Override
        public synchronized void resume() {
            if(getState() == ThreadState.PAUSED){
                setState(ThreadState.RUNNING);
                synchronized (lock) {
                    lock.notify();
                }
            }
            else {
                throw new RuntimeException("thread is not on paused state");
            }

        }

        @Override
        public synchronized void stop() {
            ThreadState state = getState();
            switch (state){
                case RUNNING:
                    setState(ThreadState.STOPED);
                    break;
                case PAUSED:
                    resume();
                    setState(ThreadState.STOPED);
                    break;
                case STOPED:
                    break;
                case NEW:
                    throw new RuntimeException("thread not start yet");
            }

        }

        @Override
        public synchronized ThreadState getState() {
            return LoopThread.this.getState();
        }

        @Override
        public LoopThread getCurrentThread() {
            return LoopThread.this;
        }
    };

    public LoopThread(){
        mThread = new Thread(this,"LoopThread");
    }

    public LoopThread(long timeslice){
        this();
        this.timeslice = timeslice;

    }


    protected abstract void onProcess();

    protected void onStart(){

    }

    protected void onPause(){

    }
    protected void onResume(){

    }

    protected void onStop(){

    }

    public IThreadController getThreadController(){
        return this.controller;
    }

    public void setState(ThreadState state){
        this.state = state;
    }

    public ThreadState getState(){
        return this.state;
    }

    public final void run(){
        onStart();
        setState(ThreadState.RUNNING);
        ThreadState state = getState();
        while (state != ThreadState.STOPED){
            state = getState();
            onProcess();
            if(state == ThreadState.PAUSED){
                synchronized (lock) {
                    try {
                        onPause();
                        lock.wait();
                        onResume();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(timeslice);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        onStop();
    }

    public String getName(){
        return mThread.getName() + "-" + mThread.getId() ;
    }

    public enum ThreadState{
        NEW,RUNNING,PAUSED,STOPED
    }

}