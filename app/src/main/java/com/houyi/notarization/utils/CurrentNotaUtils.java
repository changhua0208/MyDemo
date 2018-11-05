package com.houyi.notarization.utils;

import com.houyi.notarization.mode.Notarization;

/**
 * @author changhua.jiang
 * @since 2018/11/5 8:48 PM
 */

public class CurrentNotaUtils {
    private static Notarization nota;

    public static void save(Notarization i){
        nota = i;
    }

    public static Notarization currentNota(){
        return nota;
    }
}
