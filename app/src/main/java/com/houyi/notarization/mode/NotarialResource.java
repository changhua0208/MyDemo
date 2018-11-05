package com.houyi.notarization.mode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

//公证材料
@Entity
public class NotarialResource {
    //主鍵
    @Id(autoincrement = true)
    private Long id;
    //公证事项
    private Long nid;
    //本次公证照片
    private String images;
    //本次公证视频
    private String videos;
    //本次公证录音
    private String audio;
    //本次公证文书
    private String document;
    @Generated(hash = 1408451254)
    public NotarialResource(Long id, Long nid, String images, String videos,
            String audio, String document) {
        this.id = id;
        this.nid = nid;
        this.images = images;
        this.videos = videos;
        this.audio = audio;
        this.document = document;
    }
    @Generated(hash = 810128938)
    public NotarialResource() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getNid() {
        return this.nid;
    }
    public void setNid(Long nid) {
        this.nid = nid;
    }
    public String getImages() {
        return this.images;
    }
    public void setImages(String images) {
        this.images = images;
    }
    public String getVideos() {
        return this.videos;
    }
    public void setVideos(String videos) {
        this.videos = videos;
    }
    public String getAudio() {
        return this.audio;
    }
    public void setAudio(String audio) {
        this.audio = audio;
    }
    public String getDocument() {
        return this.document;
    }
    public void setDocument(String document) {
        this.document = document;
    }
}
