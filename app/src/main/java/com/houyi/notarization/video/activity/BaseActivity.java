package com.houyi.notarization.video.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhanglin on 2017/7/25.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(setContentView());
        ButterKnife.bind(this);
        initUIAndData();
        initFullScreen();
        mSubscriptions = new CompositeSubscription();
        if (getSubscriptions() != null)
            for (Subscription subscription : getSubscriptions()) {
                mSubscriptions.add(subscription);
            }
    }

    public abstract int setContentView();

    public abstract void initUIAndData();


    public abstract List<Subscription> getSubscriptions();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscriptions != null && !mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
            mSubscriptions = null;
        }
    }

    protected void initFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int msgId) {
        Toast.makeText(mContext, msgId, Toast.LENGTH_SHORT).show();
    }
}
