package com.houyi.notarization.utils;

import com.houyi.notarization.mode.Identity;

/**
 * 持有当前身份证信息
 * @author changhua.jiang
 * @since 2018/9/13 上午11:38
 */

public class CurrentIdentityUtils {
    private static Identity identity;

    public static void save(Identity i){
        identity = i;
    }

    public static Identity currentIdentity(){
        return identity;
    }
}
