package rpt.tool.mementobibere.java.userinfo.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import rpt.tool.mementobibere.java.userinfo.utils.InputFilterWeightRange;
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager;
import rpt.tool.mementobibere.utils.view.custom.picker.HorizontalPicker;


public class InitUserInfo_Three extends MasterBaseFragment
{
	View item_view;

	AppCompatEditText txt_height,txt_weight;

	boolean isExecute=true,isExecuteSeekbar=true;
	RadioButton rdo_cm,rdo_feet;
	List<Double> height_feet_elements=new ArrayList<>();

	boolean isExecuteWeight=true,isExecuteSeekbarWeight=true;
	RadioButton rdo_kg,rdo_lb;

	List<String> weight_lb_lst=new ArrayList<>();
	List<String> weight_kg_lst=new ArrayList<>();
	HorizontalPicker pickerLB,pickerKG;

	List<String> height_cm_lst=new ArrayList<>();
	List<String> height_feet_lst=new ArrayList<>();
	HorizontalPicker pickerCM,pickerFeet;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.init_user_info_three_fragment, container, false);


        FindViewById();

		if(SharedPreferencesManager.INSTANCE.getPersonWeight()) {
			init_WeightLB();
			init_WeightKG();
			pickerLB.setVisibility(View.GONE);
		}
		else {
			init_WeightKG();
			init_WeightLB();
			pickerKG.setVisibility(View.GONE);
		}

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

		rdo_kg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				pickerKG.setVisibility(b?View.VISIBLE:View.GONE);
				pickerLB.setVisibility(b?View.GONE:View.VISIBLE);
			}
		});

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

	public void init_WeightKG()
	{
		pickerKG=item_view.findViewById(R.id.pickerKG);

		weight_kg_lst.clear();
		float f=30.0f;
		weight_kg_lst.add(""+f);
		for(int k=0;k<200;k++)
		{
			f+=1;
			weight_kg_lst.add(""+f);
		}

		final CharSequence[] st=new CharSequence[weight_kg_lst.size()];
		for(int k=0;k<weight_kg_lst.size();k++)
		{
			st[k]=""+weight_kg_lst.get(k);
		}

		pickerKG.setValues(st);
		pickerKG.setSideItems(1);
		pickerKG.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
			@Override
			public void onItemSelected(int index) {

				//if(isExecuteSeekbarWeight){
					txt_weight.setText(st[index]);

					Log.d("MYHSCROLL : ","onItemSelected KG");
				//}
			}
		});

		try {
			pickerKG.setTextSize(act.getResources().getDimension(R.dimen.dp_30));
		}
		catch (Exception e){}
	}

	public void init_WeightLB()
	{
		pickerLB=item_view.findViewById(R.id.pickerLB);

		weight_lb_lst.clear();
		for(int k=66;k<288;k++)
		{
		    weight_lb_lst.add(""+k);
		}

		final CharSequence[] st=new CharSequence[weight_lb_lst.size()];
		for(int k=0;k<weight_lb_lst.size();k++)
		{
			st[k]=""+weight_lb_lst.get(k);
		}

		pickerLB.setValues(st);
		pickerLB.setSideItems(1);
		pickerLB.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
			@Override
			public void onItemSelected(int index) {
				//if(isExecuteSeekbarWeight){
					txt_weight.setText(st[index]);

					Log.d("MYHSCROLL : ","onItemSelected LB");
				//}
			}
		});

		try {
			pickerLB.setTextSize(act.getResources().getDimension(R.dimen.dp_30));
		}
		catch (Exception e){}
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
		txt_weight=item_view.findViewById(R.id.txt_weight);

		rdo_cm=item_view.findViewById(R.id.rdo_cm);
		rdo_feet=item_view.findViewById(R.id.rdo_feet);

		rdo_kg=item_view.findViewById(R.id.rdo_kg);
		rdo_lb=item_view.findViewById(R.id.rdo_lb);
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
		isExecuteWeight=false;
		isExecuteSeekbarWeight=false;


		txt_height.setFilters(new InputFilter[]{new InputFilterRange(0.00,height_feet_elements)});
		txt_weight.setFilters(new InputFilter[]{new InputFilterWeightRange(0,130)});


		isExecute=true;
		isExecuteSeekbar=true;
		isExecuteWeight=true;
		isExecuteSeekbarWeight=true;

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


		//====================================================================================


		txt_weight.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				isExecuteSeekbarWeight=false;
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {

				try {

					String weight=txt_weight.getText().toString().trim();

					if(weight.equalsIgnoreCase("."))
						txt_weight.setText("30.0");

					weight=txt_weight.getText().toString().trim();

					if (!sh.check_blank_data(weight) && isExecuteWeight)
					{
						Log.d("MYHSCROLL : ","afterTextChanged W");

                        float h = Float.parseFloat(weight);

                        int tot = (int) h;

						if (rdo_kg.isChecked()) {

							int pos=0;
							for(int k=0;k<weight_kg_lst.size();k++)
							{
								float h2 = Float.parseFloat(weight_kg_lst.get(k));

								if(h==h2) {
									pos = k;
									break;
								}
							}

							pickerKG.setSelectedItem(pos);
						}
						else
						{
							for(int k=0;k<weight_lb_lst.size();k++)
							{
								if(Integer.parseInt(weight_lb_lst.get(k))==tot)
									pickerLB.setSelectedItem(k);
							}
						}
					}

                    saveWeightData();
				}
				catch (Exception e){

				}
				isExecuteSeekbarWeight=true;
			}
		});

		rdo_kg.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View view)
            {
				if(!sh.check_blank_data(txt_weight.getText().toString()))
				{
					double weight_in_lb=Double.parseDouble(txt_weight.getText().toString());

					double weight_in_kg=0.0;

					if(weight_in_lb>0)
						weight_in_kg=Math.round(HeightWeightHelper.lbToKgConverter(weight_in_lb));

					int tmp= (int) weight_in_kg;

					int sel_pos=0;
					for(int k=0;k<weight_kg_lst.size();k++)
					{
						if(Float.parseFloat(weight_kg_lst.get(k))==Float.parseFloat(""+tmp))
							sel_pos=k;
					}

                    pickerKG.setSelectedItem(sel_pos);

					txt_weight.setFilters(new InputFilter[]{new InputFilterWeightRange(0,130)});
					txt_weight.setText(getData(""+ Constant.decimalFormat2.format(tmp)));
					rdo_kg.setClickable(false);
					rdo_lb.setClickable(true);
				}

				saveWeightData();
			}
		});

		rdo_lb.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View view)
            {
				if(!sh.check_blank_data(txt_weight.getText().toString()))
				{
					double weight_in_kg = Double.parseDouble(txt_weight.getText().toString());

					double weight_in_lb=0.0;

					if(weight_in_kg>0)
						weight_in_lb = Math.round(HeightWeightHelper.kgToLbConverter(weight_in_kg));

					int tmp= (int) weight_in_lb;

					int sel_pos=0;
					for(int k=0;k<weight_lb_lst.size();k++)
					{
						if(Float.parseFloat(weight_lb_lst.get(k))==Float.parseFloat(""+tmp))
							sel_pos=k;
					}

					pickerLB.setSelectedItem(sel_pos);

					txt_weight.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,287)});
					txt_weight.setText(getData(""+tmp));
					rdo_kg.setClickable(true);
					rdo_lb.setClickable(false);
				}

				saveWeightData();
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

		if(SharedPreferencesManager.INSTANCE.getPersonWeight()) {
			rdo_kg.setChecked(true);
			rdo_kg.setClickable(false);
			rdo_lb.setClickable(true);
		}
		else {
			rdo_lb.setChecked(true);
			rdo_kg.setClickable(true);
			rdo_lb.setClickable(false);
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

		if(!sh.check_blank_data(String.valueOf(SharedPreferencesManager.INSTANCE.getWeightS()))) {
			if(rdo_kg.isChecked())
			{
				txt_weight.setFilters(new InputFilter[]{new InputFilterWeightRange(0,130)});
				txt_weight.setText(getData(String.valueOf(SharedPreferencesManager.INSTANCE.getWeightS())));
			}
			else
			{
				txt_weight.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,287)});
				txt_weight.setText(getData(String.valueOf(SharedPreferencesManager.INSTANCE.getWeightS())));
			}
		}
		else
		{
			if(rdo_kg.isChecked()) {
				txt_weight.setFilters(new InputFilter[]{new InputFilterWeightRange(0,130)});
				txt_weight.setText("80.0");
			}
			else
			{
				txt_weight.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,287)});
				txt_weight.setText("176");
			}
		}
	}

	public void saveData()
	{
		Log.d("saveData",""+txt_height.getText().toString().trim()+" @@@ "+txt_weight.getText().toString().trim());

		SharedPreferencesManager.INSTANCE.setHeight(""+txt_height.getText().toString().trim());
		SharedPreferencesManager.INSTANCE.setPersonHeight(rdo_cm.isChecked());
		SharedPreferencesManager.INSTANCE.setSetManualGoal(false);
		SharedPreferencesManager.INSTANCE.setSetHeight(true);
	}

	public void saveWeightData()
	{
		Log.d("saveWeightData",""+rdo_kg.isChecked()+" @@@ "+txt_weight.getText().toString().trim());
		SharedPreferencesManager.INSTANCE.setWeightS(txt_weight.getText().toString().trim());
		SharedPreferencesManager.INSTANCE.setWeightUnit(rdo_kg.isChecked()?1:0);
		SharedPreferencesManager.INSTANCE.setCurrent_unitInt(rdo_kg.isChecked()?0:1);
		SharedPreferencesManager.INSTANCE.setNew_unitInt(rdo_kg.isChecked()?0:1);
		SharedPreferencesManager.INSTANCE.setUnitString(rdo_kg.isChecked()?"ml":"0z UK");
		SharedPreferencesManager.INSTANCE.setSetWeight(true);
		SharedPreferencesManager.INSTANCE.setSetManualGoal(false);
	}

	public String getData(@NonNull String str)
	{
		return  str.replace(",",".");
	}
}