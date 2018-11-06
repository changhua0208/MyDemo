package com.houyi.notarization;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.houyi.notarization.event.TakePhotoEvent;
import com.houyi.utils.RxBus;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/22.
 */
public class CameraActivity extends AppCompatActivity {
    @BindView(R.id.sv_camera_surfaceview)
    SurfaceView mSV;
    @BindView(R.id.btn_take_photo)
    View mTakePhoto;
    @BindView(R.id.ll_opt_btns)
    View mOptBtns;

    private Camera camera;
    private byte[] data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        mSV.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //打开照相机
                camera = Camera.open();
                camera.setDisplayOrientation(180);
                //给照相机设置参数
                Camera.Parameters parameters=camera.getParameters();
                List<Camera.Size> list = parameters.getSupportedPreviewSizes();
                parameters.setPreviewSize(list.get(list.size() -1).width,list.get(list.size() -1).height);
                //List<Camera.Size> list2 = parameters.getSupportedPictureSizes();
                //该pad支持4000*3000的，太大了，
                parameters.setPictureSize(1280,720);
                List<String> focusModes = parameters.getSupportedFocusModes();
                if(focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                } else
                if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                //设置保存格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                //设置质量
                parameters.setJpegQuality(100);
                //给照相机设置参数
                camera.setParameters(parameters);
                //将照相机捕捉的画面展示到SurfaceView
                try {
                    camera.setPreviewDisplay(mSV.getHolder());
                    //开启预览
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    @OnClick(R.id.img_ok)
    public void onImgOK(){
        if(data != null){
            TakePhotoEvent event = new TakePhotoEvent(data);
            RxBus.getInstance().post(event);
            finish();
        }
    }

    @OnClick(R.id.img_cancel)
    public void onCancel(){
        finish();
    }

    @OnClick(R.id.img_back)
    public void onBack(){
        mTakePhoto.setVisibility(View.VISIBLE);
        mOptBtns.setVisibility(View.GONE);
        camera.startPreview();
    }


    //拍照
    @OnClick(R.id.btn_take_photo)
    public void takePhoto(View view){
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //将字节数组
                //Bitmap bitmap= BitmapUtils.byteArray2bmp(data);
                //File dir = getExternalFilesDir("photo");
                mTakePhoto.setVisibility(View.GONE);
                mOptBtns.setVisibility(View.VISIBLE);
                CameraActivity.this.data = data;
                camera.stopPreview();

//                try {
//                    FileOutputStream fileOutputStream=new FileOutputStream("/mnt/sdcard/DCIM/camera/LuoMei\"+System.currentTimeMillis()+\".png");
//                    bitmap.compress(Bitmap.CompressFormat.PNG,85,fileOutputStream);
//                    camera.stopPreview();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        //EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.release();
    }
}
