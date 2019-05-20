package com.revinper.revbetcalc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class CustomSeparator extends LinearLayout {

    public CustomSeparator(Context context)
    {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_customseparator, this, true);
    }

    public CustomSeparator(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_customseparator, this, true);
    }

}
