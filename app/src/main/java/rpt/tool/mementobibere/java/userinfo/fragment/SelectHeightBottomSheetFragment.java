package rpt.tool.mementobibere.java.userinfo.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import java.util.ArrayList;
import java.util.List;
import rpt.tool.mementobibere.R;
import rpt.tool.mementobibere.java.userinfo.Constant;
import rpt.tool.mementobibere.java.userinfo.helper.HeightWeightHelper;
import rpt.tool.mementobibere.java.userinfo.utils.DigitsInputFilter;
import rpt.tool.mementobibere.java.userinfo.utils.InputFilterRange;
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager;
import rpt.tool.mementobibere.utils.view.custom.picker.HorizontalPicker;

public class SelectHeightBottomSheetFragment extends MasterBaseDialogFragment
{
    View item_view;

    AppCompatEditText txt_height;

    boolean isExecute=true,isExecuteSeekbar=true;
    RadioButton rdo_cm,rdo_feet;
    List<Double> height_feet_elements=new ArrayList<>();

    List<String> height_cm_lst=new ArrayList<>();
    List<String> height_feet_lst=new ArrayList<>();
    HorizontalPicker pickerCM,pickerFeet;
    Button updateBtn;
    private float totalIntake;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        item_view=inflater.inflate(R.layout.select_height_bottom_sheet_fragment, container, false);


        FindViewById();

        if(SharedPreferencesManager.INSTANCE.getPersonHeight()) {
            init_HeightFeet();
            init_HeightCM();
            pickerFeet.setVisibility(View.GONE);
        }
        else
        {
            init_HeightCM();
            init_HeightFeet();
            pickerCM.setVisibility(View.GONE);
        }

        Body();

        rdo_feet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pickerFeet.setVisibility(b?View.VISIBLE:View.GONE);
                pickerCM.setVisibility(b?View.GONE:View.VISIBLE);
            }
        });



        return item_view;
    }

    //===============


    public void init_HeightCM()
    {
        pickerCM=item_view.findViewById(R.id.pickerCM);

        height_cm_lst.clear();
        for(int k=60;k<241;k++)
        {
            height_cm_lst.add(""+k);
        }

        final CharSequence[] st=new CharSequence[height_cm_lst.size()];
        for(int k=0;k<height_cm_lst.size();k++)
        {
            st[k]=""+height_cm_lst.get(k);
        }

        pickerCM.setValues(st);
        pickerCM.setSideItems(1);
        pickerCM.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
            @Override
            public void onItemSelected(int index) {
                //if(isExecuteSeekbar) {
                txt_height.setText(st[index]);

                Log.d("MYHSCROLL : ","onItemSelected 2");
                //}
            }
        });

        try {
            pickerCM.setTextSize(act.getResources().getDimension(R.dimen.dp_30));
        }
        catch (Exception e){}
    }

    public void init_HeightFeet()
    {
        pickerFeet=item_view.findViewById(R.id.pickerFeet);

        height_feet_lst.clear();
        height_feet_lst.add("2.0");
        height_feet_lst.add("2.1");
        height_feet_lst.add("2.2");
        height_feet_lst.add("2.3");
        height_feet_lst.add("2.4");
        height_feet_lst.add("2.5");
        height_feet_lst.add("2.6");
        height_feet_lst.add("2.7");
        height_feet_lst.add("2.8");
        height_feet_lst.add("2.9");
        height_feet_lst.add("2.10");
        height_feet_lst.add("2.11");
        height_feet_lst.add("3.0");
        height_feet_lst.add("3.1");
        height_feet_lst.add("3.2");
        height_feet_lst.add("3.3");
        height_feet_lst.add("3.4");
        height_feet_lst.add("3.5");
        height_feet_lst.add("3.6");
        height_feet_lst.add("3.7");
        height_feet_lst.add("3.8");
        height_feet_lst.add("3.9");
        height_feet_lst.add("3.10");
        height_feet_lst.add("3.11");
        height_feet_lst.add("4.0");
        height_feet_lst.add("4.1");
        height_feet_lst.add("4.2");
        height_feet_lst.add("4.3");
        height_feet_lst.add("4.4");
        height_feet_lst.add("4.5");
        height_feet_lst.add("4.6");
        height_feet_lst.add("4.7");
        height_feet_lst.add("4.8");
        height_feet_lst.add("4.9");
        height_feet_lst.add("4.10");
        height_feet_lst.add("4.11");
        height_feet_lst.add("5.0");
        height_feet_lst.add("5.1");
        height_feet_lst.add("5.2");
        height_feet_lst.add("5.3");
        height_feet_lst.add("5.4");
        height_feet_lst.add("5.5");
        height_feet_lst.add("5.6");
        height_feet_lst.add("5.7");
        height_feet_lst.add("5.8");
        height_feet_lst.add("5.9");
        height_feet_lst.add("5.10");
        height_feet_lst.add("5.11");
        height_feet_lst.add("6.0");
        height_feet_lst.add("6.1");
        height_feet_lst.add("6.2");
        height_feet_lst.add("6.3");
        height_feet_lst.add("6.4");
        height_feet_lst.add("6.5");
        height_feet_lst.add("6.6");
        height_feet_lst.add("6.7");
        height_feet_lst.add("6.8");
        height_feet_lst.add("6.9");
        height_feet_lst.add("6.10");
        height_feet_lst.add("6.11");
        height_feet_lst.add("7.0");
        height_feet_lst.add("7.1");
        height_feet_lst.add("7.2");
        height_feet_lst.add("7.3");
        height_feet_lst.add("7.4");
        height_feet_lst.add("7.5");
        height_feet_lst.add("7.6");
        height_feet_lst.add("7.7");
        height_feet_lst.add("7.8");
        height_feet_lst.add("7.9");
        height_feet_lst.add("7.10");
        height_feet_lst.add("7.11");
        height_feet_lst.add("8.0");

        final CharSequence[] st=new CharSequence[height_feet_lst.size()];
        for(int k=0;k<height_feet_lst.size();k++)
        {
            st[k]=""+height_feet_lst.get(k);
        }

        pickerFeet.setValues(st);
        pickerFeet.setSideItems(1);
        pickerFeet.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
            @Override
            public void onItemSelected(int index) {

                //if(isExecuteSeekbar) {

                txt_height.setText(st[index]);

                Log.d("MYHSCROLL : ","onItemSelected");
                //}
            }
        });

        try {
            pickerFeet.setTextSize(act.getResources().getDimension(R.dimen.dp_30));
        }
        catch (Exception e){}
    }




    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            //ah.customAlert("call");
            //actionView();
        }
        else{
            //no
        }

    }

    private void FindViewById()
    {
        txt_height=item_view.findViewById(R.id.txt_height);
        rdo_cm=item_view.findViewById(R.id.rdo_cm);
        rdo_feet=item_view.findViewById(R.id.rdo_feet);
        updateBtn = item_view.findViewById(R.id.btnUpdate);
    }

    private void Body()
    {
        height_feet_elements.add(2.0);
        height_feet_elements.add(2.1);
        height_feet_elements.add(2.2);
        height_feet_elements.add(2.3);
        height_feet_elements.add(2.4);
        height_feet_elements.add(2.5);
        height_feet_elements.add(2.6);
        height_feet_elements.add(2.7);
        height_feet_elements.add(2.8);
        height_feet_elements.add(2.9);
        height_feet_elements.add(2.10);
        height_feet_elements.add(2.11);
        height_feet_elements.add(3.0);
        height_feet_elements.add(3.1);
        height_feet_elements.add(3.2);
        height_feet_elements.add(3.3);
        height_feet_elements.add(3.4);
        height_feet_elements.add(3.5);
        height_feet_elements.add(3.6);
        height_feet_elements.add(3.7);
        height_feet_elements.add(3.8);
        height_feet_elements.add(3.9);
        height_feet_elements.add(3.10);
        height_feet_elements.add(3.11);
        height_feet_elements.add(4.0);
        height_feet_elements.add(4.1);
        height_feet_elements.add(4.2);
        height_feet_elements.add(4.3);
        height_feet_elements.add(4.4);
        height_feet_elements.add(4.5);
        height_feet_elements.add(4.6);
        height_feet_elements.add(4.7);
        height_feet_elements.add(4.8);
        height_feet_elements.add(4.9);
        height_feet_elements.add(4.10);
        height_feet_elements.add(4.11);
        height_feet_elements.add(5.0);
        height_feet_elements.add(5.1);
        height_feet_elements.add(5.2);
        height_feet_elements.add(5.3);
        height_feet_elements.add(5.4);
        height_feet_elements.add(5.5);
        height_feet_elements.add(5.6);
        height_feet_elements.add(5.7);
        height_feet_elements.add(5.8);
        height_feet_elements.add(5.9);
        height_feet_elements.add(5.10);
        height_feet_elements.add(5.11);
        height_feet_elements.add(6.0);
        height_feet_elements.add(6.1);
        height_feet_elements.add(6.2);
        height_feet_elements.add(6.3);
        height_feet_elements.add(6.4);
        height_feet_elements.add(6.5);
        height_feet_elements.add(6.6);
        height_feet_elements.add(6.7);
        height_feet_elements.add(6.8);
        height_feet_elements.add(6.9);
        height_feet_elements.add(6.10);
        height_feet_elements.add(6.11);
        height_feet_elements.add(7.0);
        height_feet_elements.add(7.1);
        height_feet_elements.add(7.2);
        height_feet_elements.add(7.3);
        height_feet_elements.add(7.4);
        height_feet_elements.add(7.5);
        height_feet_elements.add(7.6);
        height_feet_elements.add(7.7);
        height_feet_elements.add(7.8);
        height_feet_elements.add(7.9);
        height_feet_elements.add(7.10);
        height_feet_elements.add(7.11);
        height_feet_elements.add(8.0);


        isExecute=false;
        isExecuteSeekbar=false;
        txt_height.setFilters(new InputFilter[]{new InputFilterRange(0.00,height_feet_elements)});


        isExecute=true;
        isExecuteSeekbar=true;

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate_goal();
                dismiss();
            }
        });

        txt_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isExecuteSeekbar=false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

                String height=txt_height.getText().toString().trim();

                if(height.equalsIgnoreCase("."))
                    txt_height.setText("2.0");

                height=txt_height.getText().toString().trim();

                if(!sh.check_blank_data(height) && isExecute)
                {
                    Log.d("MYHSCROLL : ","afterTextChanged");

                    float h = Float.parseFloat(height);

                    if(rdo_feet.isChecked()) {

                        for(int k=0;k<height_feet_lst.size();k++)
                        {
                            Log.d("height_feet_lst",k+"  "+h+" "+Float.parseFloat(height_feet_lst.get(k)));

                            //if(h==Float.parseFloat(height_feet_lst.get(k)))
                            if(height.equalsIgnoreCase(height_feet_lst.get(k)))
                            {
                                pickerFeet.setSelectedItem(k);
                                break;
                            }
                        }
                    }
                    else
                    {
                        for(int k=0;k<height_cm_lst.size();k++)
                        {
                            Log.d("height_cm_lst",k+"  "+h+" "+Float.parseFloat(height_cm_lst.get(k)));

                            if(h==Float.parseFloat(height_cm_lst.get(k))) {
                                pickerCM.setSelectedItem(k);
                                break;
                            }
                        }
                    }

                    saveData();
                }

                isExecuteSeekbar=true;

            }
        });

        rdo_cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sh.check_blank_data(txt_height.getText().toString()))
                {

                    int final_height_cm=61;

                    try
                    {
                        String tmp_height=getData(txt_height.getText().toString().trim());

                        int d= (int) (Float.parseFloat(txt_height.getText().toString().trim()));

                        Log.d("after_decimal", "" + tmp_height.indexOf("."));

                        if(tmp_height.indexOf(".")>0)
                        {
                            String after_decimal = tmp_height.substring(tmp_height.indexOf(".") + 1);

                            if (!sh.check_blank_data(after_decimal))
                            {
                                int after_decimal_int = Integer.parseInt(after_decimal);

                                double final_height = (d * 12) + after_decimal_int;

                                final_height_cm = (int) Math.round(final_height * 2.54);

                            }
                            else
                            {
                                final_height_cm = (int) Math.round(d * 12 * 2.54);

                            }
                        }
                        else
                        {
                            final_height_cm = (int) Math.round(d * 12 * 2.54);

                        }
                    }
                    catch (Exception e){}


                    for(int k=0;k<height_cm_lst.size();k++)
                    {
                        if(Integer.parseInt(height_cm_lst.get(k))==final_height_cm) {
                            pickerCM.setSelectedItem(k);
                            break;
                        }
                    }

                    rdo_feet.setClickable(true);
                    rdo_cm.setClickable(false);
                    txt_height.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,240)});
                    txt_height.setText(getData(""+final_height_cm));
                    txt_height.setSelection(txt_height.getText().toString().trim().length());


                    saveData();
                }
                else
                {
                    rdo_feet.setChecked(true);
                    rdo_cm.setChecked(false);
                }


            }
        });

        rdo_feet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sh.check_blank_data(txt_height.getText().toString()))
                {

                    String final_height_feet="5.0";

                    try
                    {
                        int d= (int) (Float.parseFloat(txt_height.getText().toString().trim()));

                        int tmp_height_inch = (int) Math.round(d / 2.54);

                        int first=tmp_height_inch/12;
                        int second=tmp_height_inch%12;

                        final_height_feet=first+"."+second;
                    }
                    catch (Exception e){}

                    for(int k=0;k<height_feet_lst.size();k++)
                    {
                        if(getData(final_height_feet).equalsIgnoreCase(height_feet_lst.get(k))) {
                            pickerFeet.setSelectedItem(k);
                            break;
                        }
                    }

                    rdo_feet.setClickable(false);
                    rdo_cm.setClickable(true);
                    txt_height.setFilters(new InputFilter[]{new InputFilterRange(0.00,height_feet_elements)});
                    txt_height.setText(getData(final_height_feet));
                    //txt_height.setSelection(txt_height.length());
                    txt_height.setSelection(txt_height.getText().toString().trim().length());

                    saveData();
                }
                else
                {
                    rdo_feet.setChecked(false);
                    rdo_cm.setChecked(true);
                }
            }
        });

        if(SharedPreferencesManager.INSTANCE.getPersonHeight()) {
            rdo_cm.setChecked(true);
            rdo_cm.setClickable(false);
            rdo_feet.setClickable(true);
        }
        else {
            rdo_feet.setChecked(true);
            rdo_cm.setClickable(true);
            rdo_feet.setClickable(false);
        }

        if(!sh.check_blank_data(SharedPreferencesManager.INSTANCE.getHeight())) {
            if(rdo_cm.isChecked())
            {
                txt_height.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,240)});
                txt_height.setText(getData(SharedPreferencesManager.INSTANCE.getHeight()));
            }
            else
            {
                txt_height.setFilters(new InputFilter[]{new InputFilterRange(0.00,height_feet_elements)});
                txt_height.setText(getData(SharedPreferencesManager.INSTANCE.getHeight()));
            }
        }
        else
        {
            if(rdo_cm.isChecked())
            {
                txt_height.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,240)});
                txt_height.setText("150");
            }
            else
            {
                txt_height.setFilters(new InputFilter[]{new InputFilterRange(0.00,height_feet_elements)});
                txt_height.setText("5.0");
            }
        }

    }

    public void saveData()
    {
        SharedPreferencesManager.INSTANCE.setHeight(""+txt_height.getText().toString().trim());
        SharedPreferencesManager.INSTANCE.setPersonHeight(rdo_cm.isChecked());
        SharedPreferencesManager.INSTANCE.setSetManualGoal(false);
        SharedPreferencesManager.INSTANCE.setSetHeight(true);
    }

    public void calculate_goal()
    {
        String tmp_weight=""+SharedPreferencesManager.INSTANCE.getWeightS();

        boolean isFemale=SharedPreferencesManager.INSTANCE.getGender() == 1;
        boolean isActive=SharedPreferencesManager.INSTANCE.getWorkType() == 0;
        boolean isPregnant=SharedPreferencesManager.INSTANCE.isPregnant();
        boolean isBreastfeeding=SharedPreferencesManager.INSTANCE.isBreastfeeding();
        int weatherIdx=SharedPreferencesManager.INSTANCE.getClimate();

        if(!sh.check_blank_data(tmp_weight))
        {
            double tot_drink=0;
            double tmp_kg = 0;
            if (SharedPreferencesManager.INSTANCE.getPersonWeight())
            {
                tmp_kg = Double.parseDouble("" + tmp_weight);
            }
            else
            {
                tmp_kg = HeightWeightHelper.lbToKgConverter(Double.parseDouble(tmp_weight));
            }


            if(isFemale)
                tot_drink=isActive?tmp_kg* Constant.ACTIVE_FEMALE_WATER:tmp_kg*Constant.FEMALE_WATER;
            else
                tot_drink=isActive?tmp_kg*Constant.ACTIVE_MALE_WATER:tmp_kg*Constant.MALE_WATER;

            if(weatherIdx==1)
                tot_drink*=Constant.WEATHER_CLOUDY;
            else if(weatherIdx==2)
                tot_drink*=Constant.WEATHER_RAINY;
            else if(weatherIdx==3)
                tot_drink*=Constant.WEATHER_SNOW;
            else
                tot_drink*=Constant.WEATHER_SUNNY;

            if(isPregnant && isFemale)
            {
                tot_drink+=Constant.PREGNANT_WATER;
            }

            if(isBreastfeeding && isFemale)
            {
                tot_drink+=Constant.BREASTFEEDING_WATER;
            }

            if(tot_drink<900)
                tot_drink=900;

            if(tot_drink>8000)
                tot_drink=8000;

            double tot_drink_fl_oz = HeightWeightHelper.mlToOzConverter(tot_drink);

            if (SharedPreferencesManager.INSTANCE.getPersonWeight())
            {
                SharedPreferencesManager.INSTANCE.setUnitString("ml");
                totalIntake = (float) tot_drink;
            }
            else
            {
                SharedPreferencesManager.INSTANCE.setUnitString("0z UK");
                totalIntake = (float) tot_drink_fl_oz;
            }

            totalIntake=Math.round(totalIntake);
            SharedPreferencesManager.INSTANCE.setTotalIntake(totalIntake);
        }
    }


    public String getData(@NonNull String str)
    {
        return  str.replace(",",".");
    }
}
