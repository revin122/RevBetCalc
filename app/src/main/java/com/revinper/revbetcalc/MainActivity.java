package com.revinper.revbetcalc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, BetItemClickListenerInterface {

    private Dialog bet_dialog;
    private String str_betamount;
    private String str_bettype;
    private TextView tvbet;
    private double betamount;
    private int previous_betamount_selected;
    private int current_betamount_selected;
    private int previous_bettype_selected;
    private int current_bettype_selected;
    private Spinner betamount_spinner;
    private Spinner bettype_spinner;
    private boolean boxbtn_disabled;
    private boolean singlebtn_disabled;
    private boolean wheelbtn_disabled;
    private int selected_control;
    private int selected_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvbet = (TextView)this.findViewById(R.id.bet);
        try {
            for(int i = 0; i < 3; i++) {
                int id = R.id.class.getField("control" + i).getInt(0);
                TextView control = this.findViewById(id);
                control.setOnClickListener(this);
            }

            for(int i = 1; i <= 20; i++) {
                int id = R.id.class.getField("number" + i).getInt(0);
                ImageButton ivnumber = this.findViewById(id);
                ivnumber.setOnClickListener(this);
            }

            ImageButton ivnumber = this.findViewById(R.id.clear);
            ivnumber.setOnClickListener(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        LinearLayout ll = this.findViewById(R.id.betlayout);
        ll.setOnClickListener(this);

        previous_betamount_selected = 3;
        current_betamount_selected = previous_betamount_selected;
        previous_bettype_selected = 0;
        current_bettype_selected = previous_bettype_selected;
        selected_control = 0;

        bet_dialog = new Dialog(this);
        bet_dialog.setContentView(R.layout.dialog_bet);
        //bet_dialog.setTitle("Bet Dialog");
        bet_dialog.setCancelable(true);
        Button confirmbtn = bet_dialog.findViewById(R.id.confirm);
        confirmbtn.setOnClickListener(this);
        Button cancelbtn = bet_dialog.findViewById(R.id.cancel);
        cancelbtn.setOnClickListener(this);

        betamount_spinner = bet_dialog.findViewById(R.id.spinneramount);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bet_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        betamount_spinner.setAdapter(adapter);
        betamount_spinner.setSelection(current_betamount_selected);
        betamount_spinner.setOnItemSelectedListener(this);

        bettype_spinner = bet_dialog.findViewById(R.id.spinnertype);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.type_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bettype_spinner.setAdapter(adapter2);
        bettype_spinner.setSelection(current_bettype_selected);
        bettype_spinner.setOnItemSelectedListener(this);

        WindowManager.LayoutParams lp = bet_dialog.getWindow().getAttributes();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;

        str_betamount = "$2";
        str_bettype = "WIN";
        betamount = 2.0;
        singlebtn_disabled = false;
        boxbtn_disabled = true;
        wheelbtn_disabled = true;

        generateBetCalculatorItems(determineTotalBetCalculatorItems(selected_control, current_bettype_selected));
    }

    private int determineTotalBetCalculatorItems(int active_control, int bettype) {
        int total = 0;

        switch(bettype) {
            //win
            case 0:
                //place
            case 1:
                //show
            case 2:
                //across the board
            case 3:
                total = 10;
                break;
            //Quinella
            case 4:
                //Exacta
            case 5:
                //Daily Double
            case 9:
                if(active_control == 0 || active_control == 2)
                    total = 2;
                else
                    total = 1;
                break;
            //Trifecta
            case 6:
                //Pick 3
            case 10:
                if(active_control == 0 || active_control == 2)
                    total = 3;
                else
                    total = 1;
                break;
            //Superfecta
            case 7:
                //Pick 4
            case 11:
                if(active_control == 0 || active_control == 2)
                    total = 4;
                else
                    total = 1;
                break;
            //Super High 5
            case 8:
                //Pick 5
            case 12:
                if(active_control == 0 || active_control == 2)
                    total = 5;
                else
                    total = 1;
                break;
            //Pick 6
            case 13:
                //Place Pick All
            case 14:
                if(active_control == 0 || active_control == 2)
                    total = 6;
                break;
        }

        return total;
    }

    private void generateBetCalculatorItems(int total) {
        TextView tvtotalwager = (TextView)this.findViewById(R.id.totalwager);
        tvtotalwager.setText("$0.00");

        LinearLayout ll = this.findViewById(R.id.betfields);
        ll.removeAllViews();
        for(int i = 0; i < total; i++)
        {
            CalculatorItem calculatorItem = new CalculatorItem(this);

            int title_number = i + 1;
            calculatorItem.setTitle(title_number + ".");
            calculatorItem.setClickListener(new BetItemClickListener(this));
            calculatorItem.setMainLayoutTag(calculatorItem);
            calculatorItem.setTag(new Integer(i));

            if(i == 0)
            {
                calculatorItem.selected();
                selected_item = 0;
            }

            ll.addView(calculatorItem);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.control0:
                if(!singlebtn_disabled) {
                    controlbtnClicked(0);
                }
                break;
            case R.id.control1:
                if(!boxbtn_disabled) {
                    controlbtnClicked(1);
                }
                break;
            case R.id.control2:
                if(!wheelbtn_disabled) {
                    controlbtnClicked(2);
                }
                break;
            case R.id.betlayout:
                betamount_spinner.setSelection(current_betamount_selected);
                bettype_spinner.setSelection(current_bettype_selected);
                bet_dialog.show();
                break;
            case R.id.confirm:
                if(checkifBetisLegal()) {
                    if(previous_betamount_selected != current_betamount_selected || previous_bettype_selected != current_bettype_selected) {
                        previous_betamount_selected = current_betamount_selected;
                        previous_bettype_selected = current_bettype_selected;

                        str_betamount = betamount_spinner.getItemAtPosition(current_betamount_selected).toString();

                        switch(current_betamount_selected) {
                            case 0: betamount = 0.10; break;
                            case 1: betamount = 0.50; break;
                            case 2: betamount = 1.00; break;
                            case 3: betamount = 2.00; break;
                            case 4: betamount = 3.00; break;
                            case 5: betamount = 4.00; break;
                            case 6: betamount = 5.00; break;
                            case 7: betamount = 10.00; break;
                            case 8: betamount = 20.00; break;
                            case 9: betamount = 50.00; break;
                            case 10: betamount = 100.00; break;
                        }

                        str_bettype = bettype_spinner.getItemAtPosition(current_bettype_selected).toString();
                        switch(current_bettype_selected) {
                            //win
                            case 0:
                                //place
                            case 1:
                                //show
                            case 2:
                                //across the board
                            case 3:
                                boxbtn_disabled = true;
                                wheelbtn_disabled = true;
                                if(selected_control == 1 || selected_control == 2)
                                {
                                    selected_control = 0;
                                    clickedControlFields(0);
                                }
                                break;
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                                boxbtn_disabled = false;
                                wheelbtn_disabled = false;
                                break;
                            default:
                                boxbtn_disabled = true;
                                wheelbtn_disabled = false;
                                if(selected_control == 1) {
                                    selected_control = 0;
                                    clickedControlFields(0);
                                }
                                break;
                        }

                        tvbet.setText(str_betamount.toUpperCase() + " " + str_bettype.toUpperCase());
                        generateBetCalculatorItems(determineTotalBetCalculatorItems(selected_control, current_bettype_selected));
                    }

                    bet_dialog.dismiss();
                }
                break;
            case R.id.cancel:
                current_betamount_selected = previous_betamount_selected;
                current_bettype_selected = previous_bettype_selected;
                bet_dialog.dismiss();
                break;

            default:
                if(view.getId() == R.id.clear) {
                    generateBetCalculatorItems(determineTotalBetCalculatorItems(selected_control, current_bettype_selected));
                } else {
                    try {
                        for(int i = 1; i <= 20; i++) {
                            int id = R.id.class.getField("number" + i).getInt(0);
                            if(view.getId() == id) {
                                numberClicked(i-1);
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void controlbtnClicked(int index) {
        if(selected_control != index) {
            selected_control = index;
            generateBetCalculatorItems(determineTotalBetCalculatorItems(selected_control, current_bettype_selected));
        }

        clickedControlFields(index);
    }

    public void clickedControlFields(int index_clicked) {
        for(int i = 0; i < 3; i++) {
            try {
                int id = R.id.class.getField("control" + i).getInt(0);
                TextView control = this.findViewById(id);

                if(index_clicked != i) {
                    control.setTextColor(Color.parseColor("#44FFFFFF"));
                    int backgroundid = R.drawable.class.getField("segmentedcontrol" + i + "off").getInt(0);
                    control.setBackgroundResource(backgroundid);
                }
                else {
                    control.setTextColor(Color.parseColor("#FFFFFFFF"));
                    int backgroundid = R.drawable.class.getField("segmentedcontrol" + i).getInt(0);
                    control.setBackgroundResource(backgroundid);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void numberClicked(int index) {
        LinearLayout ll = this.findViewById(R.id.betfields);
        CalculatorItem cl = (CalculatorItem)ll.getChildAt(selected_item);
        double total_amount = 0.00;
        DecimalFormat df = new DecimalFormat("0.00");
        int multiplier = 0;
        ArrayList<ArrayList<String>> dataArray = new ArrayList();
        //SINGLE
        if(selected_control == 0) {
            String value = (index + 1) + "";
            cl.setValue(value);

            switch(current_bettype_selected) {
                //win
                case 0:
                    //place
                case 1:
                    //show
                case 2:
                    multiplier = 0;
                    for(int i = 0; i < ll.getChildCount(); i++) {
                        CalculatorItem items = (CalculatorItem)ll.getChildAt(i);
                        if(items.totalValueItems() > 0) {
                            multiplier++;
                        }
                    }
                    total_amount = multiplier * betamount;
                    break;
                //across the board
                case 3:

                    multiplier = 0;
                    for(int i = 0; i < ll.getChildCount(); i++) {
                        CalculatorItem items = (CalculatorItem)ll.getChildAt(i);
                        if(items.totalValueItems() > 0) {
                            multiplier++;
                        }
                    }

                    total_amount = multiplier * (betamount * 3);
                    break;
                //Quinella
                case 4:
                    //Exacta
                case 5:
                    //Trifecta
                case 6:
                    //Superfecta
                case 7:
                    //Super High 5
                case 8:
                    //Daily Double
                case 9:
                    //Pick 3
                case 10:
                    //Pick 4
                case 11:
                    //Pick 5
                case 12:
                    //Pick 6
                case 13:
                    //Place Pick All
                case 14:
                    multiplier = 0;
                    for(int i = 0; i < ll.getChildCount(); i++) {
                        CalculatorItem items = (CalculatorItem)ll.getChildAt(i);
                        if(items.totalValueItems() > 0) {
                            multiplier++;
                        }
                    }
                    if(multiplier == ll.getChildCount()) {
                        total_amount = betamount;
                    }

                    break;
            }
        }
        //BOX
        else if(selected_control == 1) {
            String value = (index + 1) + "";
            cl.addValue(value);

            for(int i = 0; i < ll.getChildCount(); i++) {
                CalculatorItem items = (CalculatorItem)ll.getChildAt(i);
                if(items.totalValueItems() > 0) {
                    dataArray.add(items.getItemValues());
                }
            }

            for(int i = 0; i < dataArray.size(); i++) {
                float numberOfHorses = 0.0f;
                //QUINELLA
                if(current_bettype_selected == 4) {
                    numberOfHorses = dataArray.get(i).size();
                }
                else {
                    numberOfHorses = ((current_bettype_selected - 5) + 2);
                }
                if(dataArray.get(i).size() < numberOfHorses)
                    continue;
                float fmultiplier = 0.0f;
                //QUINELLA
                if(current_bettype_selected == 4) {
                    fmultiplier = (((float)numberOfHorses - 1.0f) * ((float)numberOfHorses/2.0f));
                }
                else {
                    fmultiplier = 1;
                }
                if(current_bettype_selected != 4) {
                    for(int z = 0; z < numberOfHorses; z++)
                    {
                        fmultiplier = fmultiplier * (dataArray.get(i).size() - z);
                    }
                }
                total_amount = total_amount + (betamount * fmultiplier);
            }
        }
        //WHEEL
        else if(selected_control == 2) {
            String value = (index + 1) + "";
            cl.addValue(value);

            switch(current_bettype_selected) {
                //Quinella
                case 4:
                    multiplier = 0;
                    ArrayList<String> set1 = null;
                    ArrayList<String> set2 = null;
                    for(int i = 0; i < ll.getChildCount(); i++) {
                        CalculatorItem items = (CalculatorItem)ll.getChildAt(i);
                        if(i == 0)
                            set1 = items.getItemValues();
                        else
                            set2 = items.getItemValues();
                        if(items.totalValueItems() > 0) {
                            multiplier++;
                        }
                    }
                    if(multiplier == ll.getChildCount()) {
                        int duplicate = 0;
                        for(int m = 0; m < set1.size(); m++) {
                            int n = Integer.parseInt(set1.get(m));
                            for(int o = 0; o < set2.size(); o++) {
                                int p = Integer.parseInt(set2.get(o));
                                if(n == p) {
                                    duplicate++;
                                    break;
                                }
                            }
                        }
                        double uniqueduplicatepairs = (Math.pow(duplicate, 2) - duplicate)/2;
                        multiplier = (int)((set1.size() * set2.size()) - duplicate - uniqueduplicatepairs);
                        total_amount = betamount * multiplier;
                    }
                    break;
                //Exacta
                case 5:
                    //Trifecta
                case 6:
                    //Superfecta
                case 7:
                    //Super High 5
                case 8:
				/*
				 if ([dataArray count] >= ([wagerTypes indexOfObject:kWAGERTYPE] - [wagerTypes indexOfObject:@"Exacta"] + 2)) {
				NSMutableArray* order = [[NSMutableArray alloc] init];
				int multiplier = (int)[self wheel:dataArray withOrder:order];
				wagerTotal = [wagerAmount decimalNumberByMultiplyingBy:((NSDecimalNumber*)[NSDecimalNumber numberWithInt:multiplier])];
			}
				 */
                    multiplier = 0;

                    for(int i = 0; i < ll.getChildCount(); i++) {
                        CalculatorItem items = (CalculatorItem)ll.getChildAt(i);
                        if(items.totalValueItems() > 0) {
                            multiplier++;
                            dataArray.add(items.getItemValues());
                        }
                    }
                    if(multiplier == ll.getChildCount()) {
                        ArrayList<String> order = new ArrayList<String>();
                        multiplier = wheel(dataArray, order);
                        total_amount = betamount * multiplier;
                    }
                    break;
                //Daily Double
                case 9:
                    //Pick 3
                case 10:
                    //Pick 4
                case 11:
                    //Pick 5
                case 12:
                    //Pick 6
                case 13:
                    //Place Pick All
                case 14:
                    multiplier = 0;
                    for(int i = 0; i < ll.getChildCount(); i++) {
                        CalculatorItem items = (CalculatorItem) ll.getChildAt(i);
                        if(items.totalValueItems() > 0) {
                            multiplier++;
                            dataArray.add(items.getItemValues());
                        }
                    }
                    if(multiplier == ll.getChildCount()) {
                        multiplier = 0;
                        //All Items added
                        for(int i = 0; i < dataArray.size(); i++) {
                            int numberOfHorses = numberOfUniqueInArray(dataArray.get(i));
                            if(multiplier < 1) multiplier = 1;
                            multiplier = multiplier * numberOfHorses;
                        }
                        total_amount = betamount * multiplier;
                    }
                    break;
            }
        }
        cl.displayValues();

        TextView tvtotalwager = (TextView)this.findViewById(R.id.totalwager);
        tvtotalwager.setText("$" + df.format(total_amount));
    }

    public int numberOfUniqueInArray(ArrayList<String> array) {
        ArrayList<String> mutable = new ArrayList<String>();
        int k = array.size();
        while(k-- > 0) {
            String number = array.get(k);
            int m = mutable.size();
            boolean exists = false;
            while(m-- > 0) {
                String _number = mutable.get(m);
                if(number.equalsIgnoreCase(_number)) {
                    exists = true;
                }
            }
            if(!exists) {
                mutable.add(number);
            }
        }
        return(mutable.size());
    }

    private boolean checkifBetisLegal() {
        boolean legal = true;
        String amount = "10¢";

        switch(current_bettype_selected) {
            //Needs $2
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 9:
            case 13:
                if(current_betamount_selected < 3) {
                    legal = false;
                    amount = "$2";
                }
                break;
            //Needs $1
            case 5:
            case 6:
            case 10:
            case 14:
                if(current_betamount_selected < 2) {
                    legal = false;
                    amount = "$1";
                }
                break;
            //Needs 10c
            case 7:
                break;
            //Needs 50c
            case 8:
            case 11:
            case 12:
                if(current_betamount_selected < 1) {
                    legal = false;
                    amount = "50¢";
                }
                break;
        }

        String error = "A " + amount + " minumun is required for " + bettype_spinner.getItemAtPosition(current_bettype_selected).toString();

        //show error dialog
        if(!legal) {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Rev Bet Calc");
            ad.setMessage(error);
            ad.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            ad.show();
        }

        return legal;
    }

    public int wheel(ArrayList<ArrayList<String>> sets, ArrayList<String> order) {
        int curr_step = order.size();
        if(curr_step >= sets.size()) {
            return 1;
        }
        else {
            int count = 0;
            ArrayList<String> set = sets.get(curr_step);
            for(int i = 0; i < set.size(); i++) {
                if(!doesInt(Integer.parseInt(set.get(i)), order)) {
                    ArrayList<String> newOrder = new ArrayList<String>(order);
                    newOrder.add(set.get(i));
                    count += wheel(sets, newOrder);
                }
            }
            return count;
        }
    }

    public boolean doesInt(int m, ArrayList<String> set) {
        for(int i = 0; i < set.size(); i++) {
            int n = Integer.parseInt(set.get(i));
            if(m == n)
                return true;
        }

        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinneramount) {
            current_betamount_selected = i;
        }
        else if(adapterView.getId() == R.id.spinnertype) {
            current_bettype_selected = i;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void betItemClickListenerClicked(CalculatorItem selectedItem) {
        unselectAllBelItems();

        selectedItem.selected();
        Integer i = (Integer)selectedItem.getTag();
        selected_item = i.intValue();
    }

    public void unselectAllBelItems() {
        LinearLayout ll = this.findViewById(R.id.betfields);

        for(int i = 0; i < ll.getChildCount(); i++) {
            CalculatorItem cl = (CalculatorItem)ll.getChildAt(i);
            cl.unselected();
        }
    }
}
