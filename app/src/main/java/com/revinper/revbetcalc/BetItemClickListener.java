package com.revinper.revbetcalc;

import android.view.View;
import android.widget.LinearLayout;

public class BetItemClickListener implements View.OnClickListener {

    BetItemClickListenerInterface betItemClickListenerInterface;

    public BetItemClickListener(BetItemClickListenerInterface bicli) {
        this.betItemClickListenerInterface  = bicli;
    }

    @Override
    public void onClick(View view) {
        //retrive calculator item
        LinearLayout linearLayout = (LinearLayout)view;
        CalculatorItem calculatorItem = (CalculatorItem)linearLayout.getTag();

        betItemClickListenerInterface.betItemClickListenerClicked(calculatorItem);
    }


}
