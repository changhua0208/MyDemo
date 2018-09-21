package com.jch.mydemo;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author changhua.jiang
 * @since 2018/9/21 下午3:30
 */

public class MyRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    private boolean checked = false;

    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        checked = super.isChecked();
    }

    public boolean isSelected(){
        return checked;
    }

    public void setSelected(boolean selected){
        checked = selected;
        setChecked(checked);
    }
}
