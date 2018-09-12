package com.jch.mydemo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

/**
 * @author changhua.jiang
 * @since 2018/9/11 上午11:25
 */

public class BaseActivity extends AppCompatActivity {
    public void toast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> list = getCurrentFragments();
        if(list != null){
            for(Fragment f : list){
                f.onActivityResult(requestCode,resultCode,data);
            }
        }
    }

    public List<Fragment> getCurrentFragments(){
        return null;
    }


}
