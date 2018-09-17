package com.jch.mydemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import com.jch.mydemo.event.TakePhotoEvent;
import com.jch.mydemo.mode.Identity;
import com.jch.mydemo.utils.BitmapUtils;
import com.jch.mydemo.utils.CurrentIdentityUtils;
import com.jch.mydemo.utils.IdentityHelper;
import com.jch.mydemo.utils.RxBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * @author changhua.jiang
 * @since 2018/9/12 下午3:09
 */

public class PhotoActivity extends BaseActivity {
    private File mImageFile;
    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final String TAG = "PhotoActivity";

    @BindView(R.id.img_photo)
    ImageView mImgPhoto;
    Bitmap bmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        RxBus.getInstance().toObserverable(TakePhotoEvent.class).subscribe(new Observer<TakePhotoEvent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TakePhotoEvent takePhotoEvent) {
                Bitmap bmp1 = BitmapUtils.byteArray2bmp(takePhotoEvent.getData());
                bmp = BitmapUtils.rotate(bmp1,180);
                mImgPhoto.setImageBitmap(bmp);
                bmp1.recycle();
            }
        });
    }

    @OnClick(R.id.btn_take_photo)
    public void onTakePhoto(){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        mImageFile = this.createNewFile();
//        final Uri uri;
//        if (Build.VERSION.SDK_INT > 23) {
//            uri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", mImageFile);
//        } else {
//            uri = Uri.fromFile(mImageFile);
//        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        Intent intent = new Intent(this,CameraActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_save)
    public void onSave(){
        //TODO copy
        Identity identity = CurrentIdentityUtils.currentIdentity();
        boolean success = IdentityHelper.getInstance().savePhoto(identity,mImageFile);
        if(success){
            showMsg(R.string.msg_save_ok);
        }
        else{
            showMsg(R.string.msg_save_fail);
        }

    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(){
        finish();
    }

    private File createNewFile() {
        File path = getDir("pictures", Context.MODE_PRIVATE);
        String fileName = System.currentTimeMillis() + ".jpg";
        return new File(path, fileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_TAKE_PHOTO == requestCode && resultCode == RESULT_OK)
        {
            //Log.e(TAG,mImageFile.getAbsolutePath());
            Bitmap bmp = BitmapFactory.decodeFile(mImageFile.getAbsolutePath());
            mImgPhoto.setImageBitmap(bmp);
        }
    }
}
