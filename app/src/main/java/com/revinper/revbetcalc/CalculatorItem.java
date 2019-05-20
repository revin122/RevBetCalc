package com.revinper.revbetcalc;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class CalculatorItem extends LinearLayout {

    private ArrayList<String> values;
    private TextView tvValue;
    private ViewGroup.LayoutParams textviewParams;
    private ViewGroup.LayoutParams rowlayoutParams;

    public CalculatorItem(Context context, AttributeSet attrs) {

        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_calcitem, this, true);

        commonInit();
    }

    public CalculatorItem(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_calcitem, this, true);

        commonInit();
    }

    public void commonInit() {
        values = new ArrayList();
        tvValue = this.findViewById(R.id.value);
        textviewParams = tvValue.getLayoutParams();
        TableRow row = this.findViewById(R.id.rowlayout);
        rowlayoutParams = row.getLayoutParams();
    }

    public void setTitle(String title) {
        TextView tvtitle = this.findViewById(R.id.title);
        tvtitle.setText(title);
    }

    public void setClickListener(View.OnClickListener ocl) {
        LinearLayout main = this.findViewById(R.id.mainlayout);
        main.setOnClickListener(ocl);
    }

    public void selected() {
        TableLayout tvvalues = this.findViewById(R.id.values);
        tvvalues.setBackgroundColor(Color.parseColor("#FFD4D4D4"));
    }

    public void setMainLayoutTag(CalculatorItem bci) {
        LinearLayout main = this.findViewById(R.id.mainlayout);
        main.setTag(bci);
    }

    public void setValue(String value) {
        removeallValues();
        addValue(value);
    }

    public void removeallValues() {
        values.clear();
    }

    public void addValue(String value) {
        values.add(value);
    }

    public int totalValueItems() {
        return(values.size());
    }

    public ArrayList<String> getItemValues() {
        return(values);
    }

    public void displayValues() {
        TableLayout tvvalues = (TableLayout)this.findViewById(R.id.values);
        tvvalues.removeAllViews();

        TableRow tr = new TableRow(getContext());
        int total = values.size() + (6 - (values.size() % 6));
        if(total % 6 == 0 && values.size() > 5 && values.size() % 6 == 0) {
            total -= 6;
        }

        for(int i = 0; i < total; i++) {
            if(i % 6 == 0) {
                tr = new TableRow(getContext());
                tr.setLayoutParams(rowlayoutParams);
            }

            TextView tv = new TextView(getContext());
            tv.setTextColor(Color.parseColor("#FF8B008B"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvValue.getTextSize());

            tv.setLayoutParams(textviewParams);
            if(i >= values.size()) {
                tv.setText(" ");
            }
            else {
                tv.setText(values.get(i));
            }

            tr.addView(tv);

            if(i+1 == total || (i+1) % 6 == 0) {
                tvvalues.addView(tr);
            }
        }
    }

    public void unselected() {
        TableLayout tvvalues = this.findViewById(R.id.values);
        tvvalues.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
    }
}
