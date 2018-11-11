package com.houyi.notarization;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.houyi.notarization.event.TakePhotoEvent;
import com.houyi.notarization.mode.Notarization;
import com.houyi.notarization.utils.BitmapUtils;
import com.houyi.notarization.utils.CurrentNotaUtils;
import com.houyi.notarization.utils.NotaFileHelper;
import com.houyi.notarization.video.activity.VideoPlayActivity;
import com.houyi.notarization.video.activity.VideoRecorderActivity;
import com.houyi.notarization.video.event.EventRecordComplete;
import com.houyi.utils.RxBus;
import com.jch.videolib.util.DialogHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import rx.Observer;

/**
 * @author changhua.jiang
 * @since 2018/11/6 10:26 PM
 */

public class AddMediaActivity extends SimpleNotaActivity implements AdapterView.OnItemClickListener, View.OnLongClickListener {
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_AUDIO = 2;
    public static final int TYPE_IMG = 3;

    private static final int REQUEST_CODE_AUDIO = 103;

    @BindView(R.id.gv_res_list)
    GridView mGrid;
    GridAdapter mAdapter;
    private int mediaType = TYPE_VIDEO;
    private Bitmap mBitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_media_resource);
        if(getIntent() != null){
            mediaType = getIntent().getIntExtra("type",TYPE_VIDEO);
            if(mediaType == TYPE_AUDIO){
                mBitmap = BitmapUtils.getBitmapFromResource(this,R.drawable.audio);
            }
        }
        setUpObserver();
        setUpViews();
    }

    private void setUpViews(){
        Notarization nota = CurrentNotaUtils.currentNota();
        File[] files = null;

        switch (mediaType){
            case TYPE_VIDEO:
                files = NotaFileHelper.getInstance().listVideoFiles(nota);
                break;
            case TYPE_AUDIO:
                files = NotaFileHelper.getInstance().listAudioFiles(nota);
                break;
            case TYPE_IMG:
                files = NotaFileHelper.getInstance().listImgFiles(nota);
                break;

        }

        //NotaFileHelper.getInstance().listVideoFiles(nota);
        mAdapter = new GridAdapter();
        mAdapter.addResourceNoRefresh(files);
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(this);
        mGrid.setOnLongClickListener(this);
    }


    private void setUpObserver(){
        RxBus.getInstance().toObserverable(EventRecordComplete.class)
                .subscribe(new Observer<EventRecordComplete>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(EventRecordComplete eventRecordComplete) {
                        String path = eventRecordComplete.videoPath;
                        Notarization nota = CurrentNotaUtils.currentNota();
                        NotaFileHelper.getInstance().saveThumbBitmap(nota,path);
                        mAdapter.addResource(new File(path));
                    }
                });
        RxBus.getInstance().toObserverable(TakePhotoEvent.class)
                .subscribe(new Observer<TakePhotoEvent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TakePhotoEvent takePhotoEvent) {
                        byte[] data = takePhotoEvent.getData();
                        Bitmap bmp = BitmapUtils.byteArray2bmp(data);
                        Notarization nota = CurrentNotaUtils.currentNota();
                        NotaFileHelper.getInstance().saveImage(nota,bmp);
                        bmp.recycle();
                    }
                });

    }

    @Override
    public void onAddResource() {
        Notarization nota = CurrentNotaUtils.currentNota();
        switch (mediaType){
            case TYPE_VIDEO:
                String videoPath = null;
                try {
                    videoPath = NotaFileHelper.getInstance().createNewVideoFile(nota);
                    VideoRecorderActivity.actionLaunch(this,6000,videoPath);
                } catch (Exception e) {
                    DialogHelper.showOneButtonDialog(this,R.string.msg_error,R.string.ok,true,null);
                }

                break;
            case TYPE_AUDIO:
                int color =  getResources().getColor(R.color.bg_blue);
                try {
                    String audioPath = NotaFileHelper.getInstance().createNewAudioFile(nota);
                    AndroidAudioRecorder.with(this)
                            // Required
                            .setFilePath(audioPath)
                            .setColor(color)
                            .setRequestCode(REQUEST_CODE_AUDIO)

                            // Optional
                            .setSource(AudioSource.MIC)
                            .setChannel(AudioChannel.STEREO)
                            .setSampleRate(AudioSampleRate.HZ_48000)
                            .setAutoStart(true)
                            .setKeepDisplayOn(true)

                            // Start recording
                            .record();
                } catch (IOException e) {
                    DialogHelper.showOneButtonDialog(this,R.string.msg_error,R.string.ok,true,null);
                }
                break;
            case TYPE_IMG:
                Intent intent = new Intent(this,CameraActivity.class);
                startActivity(intent);
                break;

        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File f =  mAdapter.getItem(position);
        VideoPlayActivity.actionLaunch(this,f.getAbsolutePath());
    }

    @Override
    public boolean onLongClick(View v) {
        DialogHelper.showTwoButtonDialog(this,R.string.msg_delete,R.string.ok,R.string.cancel,true, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        },
        null);
        return false;
    }

    class GridAdapter extends BaseAdapter{
        private List<File> resList = new ArrayList<>();

        @Override
        public int getCount() {
            return resList.size();
        }

        @Override
        public File getItem(int position) {
            return resList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(AddMediaActivity.this).inflate(R.layout.grid_item_video,null);
                VideoViewHolder holder = new VideoViewHolder(convertView);
                convertView.setTag(holder);
            }
            VideoViewHolder holder = (VideoViewHolder) convertView.getTag();
            File videoFile = resList.get(position);
            String videoName = videoFile.getName();
//            int index = videoName.lastIndexOf('.');
//            if(index > 0){
//                String pngName = videoName.substring(0,index) + ".png";
//                Bitmap bmp = BitmapUtils.getBitmapFromFile(new File(videoFile.getParent(),pngName));
            Bitmap bmp = getImageByFile(videoFile);
            if(bmp != null) {
                holder.thumbImg.setImageBitmap(bmp);
            }
                holder.tvName.setText(videoName);
//            }
            return convertView;

        }

        public void addResourceNoRefresh(File[] files){
            if(files != null){
                for(File f : files){
                    this.resList.add(f);
                }
            }
        }

        public void addResource(File file){
            this.resList.add(file);
            notifyDataSetChanged();
        }
    }

    class VideoViewHolder {
        View itemView;
        @BindView(R.id.img_thumb)
        ImageView thumbImg;
        @BindView(R.id.tv_video_name)
        TextView tvName;
        VideoViewHolder(View itemView){
            this.itemView = itemView;
            ButterKnife.bind(this,itemView);
        }
    }

    public Bitmap getImageByFile(File file){
        Bitmap bmp = null;
        switch (mediaType){
            case TYPE_VIDEO:
                String videoName = file.getName();
                int index = videoName.lastIndexOf('.');
                if(index > 0) {
                    String pngName = videoName.substring(0, index) + ".png";
                    bmp = BitmapUtils.getBitmapFromFile(new File(file.getParent(), pngName));
                }
                break;
            case TYPE_AUDIO:
                bmp = mBitmap;
                break;
            case TYPE_IMG:
                String audioName = file.getName();
                int index1 = audioName.lastIndexOf('.');
                if(index1 > 0) {
                    String pngName = audioName.substring(0, index1) + ".thumb.png";
                    bmp = BitmapUtils.getBitmapFromFile(new File(file.getParent(), pngName));
                }

                break;
        }
        return bmp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBitmap != null){
            mBitmap.recycle();
        }
    }
}
