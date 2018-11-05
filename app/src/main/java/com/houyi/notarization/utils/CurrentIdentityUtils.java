package com.houyi.notarization.utils;

import com.houyi.notarization.mode.Person;

/**
 * 持有当前身份证信息
 * @author changhua.jiang
 * @since 2018/9/13 上午11:38
 */

public class CurrentIdentityUtils {
    private static Person identity;

    public static void save(Person i){
        identity = i;
    }

    public static Person currentIdentity(){
        return identity;
    }
}
