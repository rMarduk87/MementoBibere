package rpt.tool.mementobibere.java.userinfo.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import rpt.tool.mementobibere.R;
import rpt.tool.mementobibere.java.userinfo.Constant;
import rpt.tool.mementobibere.java.userinfo.helper.HeightWeightHelper;
import rpt.tool.mementobibere.java.userinfo.utils.InputFilterWeightRange;
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager;


public class InitUserInfo_Four extends MasterBaseFragment
{
	View item_view;

	AppCompatTextView lbl_goal,lbl_unit,lbl_set_goal_manually;
	float totalIntake;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.init_user_info_four_fragment, container, false);

		FindViewById();
		Body();

		return item_view;
	}

	public String getData(@NonNull String str)
	{
		return  str.replace(",",".");
	}

	public void setUserVisibleHint(boolean isVisibleToUser) {

		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser)
		{
			if(SharedPreferencesManager.INSTANCE.getSetManualGoal())
			{
				totalIntake= SharedPreferencesManager.INSTANCE.getManualIntake();
				SharedPreferencesManager.INSTANCE.setTotalIntake(totalIntake);

				lbl_goal.setText(getData("" + totalIntake));

				if (SharedPreferencesManager.INSTANCE.getPersonWeight())
				{
					lbl_unit.setText("ml");
				}
				else
				{
					lbl_unit.setText("0z UK");
				}
			}
			else
			{
				calculate_goal();
			}
		}
		else{
			//no
		}

	}

	private void FindViewById()
	{
		lbl_set_goal_manually=item_view.findViewById(R.id.lbl_set_goal_manually);
		lbl_goal=item_view.findViewById(R.id.lbl_goal);
		lbl_unit=item_view.findViewById(R.id.lbl_unit);
	}

	private void Body()
	{
		if(SharedPreferencesManager.INSTANCE.getSetManualGoal()) {
			totalIntake= SharedPreferencesManager.INSTANCE.getManualIntake();
			SharedPreferencesManager.INSTANCE.setTotalIntake(totalIntake);


			lbl_goal.setText(getData("" + totalIntake));

			if (SharedPreferencesManager.INSTANCE.getPersonWeight())
			{
				lbl_unit.setText("ml");
			}
			else
			{
				lbl_unit.setText("0z UK");
			}
		}
		else
		{
			calculate_goal();
		}

		lbl_set_goal_manually.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showSetManuallyGoalDialog();
			}
		});
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
				lbl_unit.setText("ml");
				SharedPreferencesManager.INSTANCE.setUnitString("ml");
				totalIntake = (float) tot_drink;
			}
			else
			{
				lbl_unit.setText("0z UK");
				SharedPreferencesManager.INSTANCE.setUnitString("0z UK");
				totalIntake = (float) tot_drink_fl_oz;
			}

			totalIntake=Math.round(totalIntake);
			lbl_goal.setText(getData("" + totalIntake));
			SharedPreferencesManager.INSTANCE.setTotalIntake(totalIntake);
		}
	}

    boolean isExecute=true,isExecuteSeekbar=true;

	public void showSetManuallyGoalDialog()
	{
		final Dialog dialog = new Dialog(act);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


		final View view = LayoutInflater.from(act).inflate(R.layout.dialog_set_manually_goal, null, false);


		final AppCompatEditText lbl_goal2=view.findViewById(R.id.lbl_goal);
		AppCompatTextView lbl_unit2=view.findViewById(R.id.lbl_unit);
		RelativeLayout btn_cancel=view.findViewById(R.id.btn_cancel);
		RelativeLayout btn_save=view.findViewById(R.id.btn_save);
		final SeekBar seekbarGoal=view.findViewById(R.id.seekbarGoal);


		if(SharedPreferencesManager.INSTANCE.getSetManualGoal())
			lbl_goal2.setText( getData(""+SharedPreferencesManager.INSTANCE.getManualIntake()));
		else
			lbl_goal2.setText( getData(""+SharedPreferencesManager.INSTANCE.getTotalIntake()));

		lbl_unit2.setText(SharedPreferencesManager.INSTANCE.getPersonWeight()?"ml":"0z UK");



		if(SharedPreferencesManager.INSTANCE.getPersonWeight()) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				seekbarGoal.setMin(900);
			}
			seekbarGoal.setMax(8000);
			lbl_goal2.setFilters(new InputFilter[]{new InputFilterWeightRange(0,8000),new InputFilter.LengthFilter(4)});
			//lbl_goal2.setMaxL
		}
		else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				seekbarGoal.setMin(30);
			}
			seekbarGoal.setMax(270);
			lbl_goal2.setFilters(new InputFilter[]{new InputFilterWeightRange(0,270),new InputFilter.LengthFilter(3)});
		}

		int f= SharedPreferencesManager.INSTANCE.getSetManualGoal()?
				(int) SharedPreferencesManager.INSTANCE.getManualIntake():
				(int) SharedPreferencesManager.INSTANCE.getTotalIntake();
		seekbarGoal.setProgress(f);

        lbl_goal2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isExecuteSeekbar=false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try
                {
                    if(!sh.check_blank_data(lbl_goal2.getText().toString().trim()) && isExecute)
                    {
                        int data = Integer.parseInt(lbl_goal2.getText().toString().trim());
                        seekbarGoal.setProgress(data);
                    }

                }
                catch (Exception e){}

                isExecuteSeekbar=true;
            }
        });

		seekbarGoal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBars, int progress, boolean fromUser) {
                if(isExecuteSeekbar)
                {
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
					{
						if(SharedPreferencesManager.INSTANCE.getPersonWeight())
						{
							progress=progress<900?900:progress;
						}
						else
						{
							progress=progress<30?30:progress;
						}
						seekbarGoal.setProgress(progress);
					}

                    lbl_goal2.setText("" + progress);
                }


			}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isExecute=false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isExecute=true;
            }
		});




		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});

		btn_save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

				if(SharedPreferencesManager.INSTANCE.getPersonWeight() &&
						Float.parseFloat(lbl_goal2.getText().toString().trim())>=900)
				{
					totalIntake = Float.parseFloat(lbl_goal2.getText().toString().trim());
					SharedPreferencesManager.INSTANCE.setTotalIntake(totalIntake);
					lbl_goal.setText(getData("" + totalIntake));
					SharedPreferencesManager.INSTANCE.setSetManualGoal(true);
					SharedPreferencesManager.INSTANCE.setManualIntake(totalIntake);
					dialog.dismiss();
				}
				else
				{
					if(!SharedPreferencesManager.INSTANCE.getPersonWeight() &&
							Float.parseFloat(lbl_goal2.getText().toString().trim())>=30)
					{
						totalIntake = Float.parseFloat(lbl_goal2.getText().toString().trim());
						SharedPreferencesManager.INSTANCE.setTotalIntake(totalIntake);
						lbl_goal.setText(getData("" + totalIntake));
						SharedPreferencesManager.INSTANCE.setSetManualGoal(true);
						SharedPreferencesManager.INSTANCE.setManualIntake(totalIntake);
						dialog.dismiss();
					}
					else {
						ah.customAlert(sh.get_string(R.string.set_daily_goal_validation));
					}
				}
			}
		});

		dialog.setContentView(view);

		dialog.show();
	}
}