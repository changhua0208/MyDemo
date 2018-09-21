package com.jch.mydemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 */

public class PhotoActivity extends BaseActivity {
    private static final String TAG = "PhotoActivity";

    @BindView(R.id.img_photo)
    ImageView mImgPhoto;
    ProgressDialog mProgressDlg;

    Bitmap bmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        initViews();

        RxBus.getInstance().toObserverable(TakePhotoEvent.class).subscribe(new Observer<TakePhotoEvent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final TakePhotoEvent takePhotoEvent) {
                Bitmap bmp1 = BitmapUtils.byteArray2bmp(takePhotoEvent.getData());
                bmp = BitmapUtils.rotate(bmp1,180);
                bmp1.recycle();
                mImgPhoto.setImageBitmap(bmp);

            }
        });
    }

    private void initViews() {
        mProgressDlg = new ProgressDialog(this);

        Identity identity = CurrentIdentityUtils.currentIdentity();
        Bitmap bmp = IdentityHelper.getInstance().getPhoto(identity);
        if(bmp != null){
            mImgPhoto.setImageBitmap(bmp);
        }
    }

    @OnClick(R.id.btn_take_photo)
    public void onTakePhoto(){
        Intent intent = new Intent(this,CameraActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_save)
    public void onSave(){
        if(bmp != null) {
            mProgressDlg.show();
            AsyncTask<Object,Object,Boolean> task = new AsyncTask<Object,Object,Boolean>() {
                @Override
                protected Boolean doInBackground(Object[] objects) {
                    Identity identity = CurrentIdentityUtils.currentIdentity();
                    boolean success = IdentityHelper.getInstance().savePhoto(identity, bmp);
                    return success;
                }

                @Override
                protected void onPostExecute(Boolean success) {
                    mProgressDlg.dismiss();
                    if (success) {
                        showMsg(R.string.msg_save_ok);
                    } else {
                        showMsg(R.string.msg_save_fail);
                    }
                }
            };
            task.execute();
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
}
