package com.houyi.notarization.event;

public class TakePhotoEvent {
    private byte[] data;
    public TakePhotoEvent(byte[] data){
        this.data = data;
    }

    public byte[] getData(){
        return data;
    }
}
