package com.houyi.notarization;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.houyi.notarization.fragment.HomeFragment;
import com.houyi.notarization.fragment.IdentifyFragment;
import com.houyi.notarization.fragment.MineFragment;
import com.houyi.notarization.fragment.QueryFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_main)
    ViewPager mViewPagerMain;
    @BindView(R.id.btn_home)
    View mBtnHome;
    @BindView(R.id.btn_identity)
    View mBtnIndentity;
    @BindView(R.id.btn_query)
    View mBtnQuery;
    @BindView(R.id.btn_mine)
    View mBtnMime;

    private List<Fragment> fs;
    private MyFragmentAdapter mAdapter;
    private int currentIndex;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpViews();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            String[] pers = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
            int hasPers = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasPers != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,pers,PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PERMISSION_REQUEST_CODE == requestCode ){
            if(!checkGrantResult(grantResults)){
                finish();
            }
        }
    }

    private boolean checkGrantResult(int[] grantResults){
        if(grantResults != null && grantResults.length > 0){
            boolean ret = true;
            for(int grant : grantResults){

                ret = (grant == PackageManager.PERMISSION_GRANTED) & ret;

            }
            return ret;
        }
        else
            return false;
    }

    private void setUpViews() {
        fs = new ArrayList<>();
        fs.add(new HomeFragment());
        fs.add(new IdentifyFragment());
        fs.add(new QueryFragment());
        fs.add(new MineFragment());

        mAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mViewPagerMain.setAdapter(mAdapter);

        mViewPagerMain.addOnPageChangeListener(this);
        currentIndex = 0;
        mViewPagerMain.setCurrentItem(currentIndex,false);
    }

    @OnClick(R.id.btn_home)
    public void onHome(){
        onNavBtnSelected(0);
    }

    @OnClick(R.id.btn_identity)
    public void onIdentity(){
        onNavBtnSelected(1);
    }

    @OnClick(R.id.btn_query)
    public void onQuery(){
        onNavBtnSelected(2);
    }

    @OnClick(R.id.btn_mine)
    public void onMine(){
        onNavBtnSelected(3);
    }

    private void onNavBtnSelected(int position){
        if(currentIndex == position){
            switch (position){
                case 0:
                    mBtnHome.setSelected(true);
                    mBtnIndentity.setSelected(false);
                    mBtnMime.setSelected(false);
                    mBtnQuery.setSelected(false);
                    break;
                case 1:
                    mBtnHome.setSelected(false);
                    mBtnIndentity.setSelected(true);
                    mBtnMime.setSelected(false);
                    mBtnQuery.setSelected(false);
                    break;
                case 2:
                    mBtnHome.setSelected(false);
                    mBtnIndentity.setSelected(false);
                    mBtnMime.setSelected(false);
                    mBtnQuery.setSelected(true);
                    break;
                case 3:
                    mBtnHome.setSelected(false);
                    mBtnIndentity.setSelected(false);
                    mBtnMime.setSelected(true);
                    mBtnQuery.setSelected(false);
                    break;
            }
        }
        else{
           mViewPagerMain.setCurrentItem(position,true);
        }
    }

    @Override
    public List<Fragment> getCurrentFragments() {
        return fs;
    }

    public void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
        onNavBtnSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyFragmentAdapter extends FragmentPagerAdapter{

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fs.get(position);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
