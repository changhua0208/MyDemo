package com.jch.mydemo.utils;

import com.jch.mydemo.App;

/**
 * @author changhua.jiang
 * @since 2018/9/11 上午11:37
 */

public class ApplicationUtils {
    private static App app;
    public static void init(App app){
        ApplicationUtils.app = app;
    }

    public static App getApplication(){
        return app;
    }
}
