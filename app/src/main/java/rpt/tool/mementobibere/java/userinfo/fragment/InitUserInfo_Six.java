package rpt.tool.mementobibere.java.userinfo.fragment;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rpt.tool.mementobibere.R;
import rpt.tool.mementobibere.java.userinfo.MasterBaseAppCompatActivity;
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager;

public class InitUserInfo_Six extends MasterBaseFragment
{
	View item_view;

	AppCompatTextView txt_wakeup_time,txt_bed_time;
	RadioButton rdo_15,rdo_30,rdo_45,rdo_60;

	int from_hour=8,from_minute=0,to_hour=22,to_minute=0;

	AppCompatTextView lbl_message;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.init_user_info_six_fragment, container, false);

		FindViewById();
		Body();

		setCount();

		return item_view;
	}

	public void setUserVisibleHint(boolean isVisibleToUser) {

		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser)
		{
			setCount();
		}
		else{
		}
	}


	private void FindViewById()
	{
		txt_wakeup_time=item_view.findViewById(R.id.txt_wakeup_time);
		txt_bed_time=item_view.findViewById(R.id.txt_bed_time);

		rdo_15=item_view.findViewById(R.id.rdo_15);
		rdo_30=item_view.findViewById(R.id.rdo_30);
		rdo_45=item_view.findViewById(R.id.rdo_45);
		rdo_60=item_view.findViewById(R.id.rdo_60);

		lbl_message=item_view.findViewById(R.id.lbl_message);
	}

	private void Body()
	{
		rdo_15.setText("15 "+sh.get_string(R.string.min));
		rdo_30.setText("30 "+sh.get_string(R.string.min));
		rdo_45.setText("45 "+sh.get_string(R.string.min));
		rdo_60.setText("1 "+sh.get_string(R.string.hour));

		txt_wakeup_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openAutoTimePicker(txt_wakeup_time,true);
                //openTimePicker(txt_wakeup_time);
			}
		});

		txt_bed_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openAutoTimePicker(txt_bed_time,false);
                //openTimePicker(txt_bed_time);
			}
		});

		rdo_15.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCount();
			}
		});

		rdo_30.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCount();
			}
		});

		rdo_45.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCount();
			}
		});

		rdo_60.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCount();
			}
		});
	}

	public void openAutoTimePicker(final AppCompatTextView appCompatTextView, final boolean isFrom)
	{
		TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second)
			{
				String formatedDate = "";
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
				SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.getDefault());
				Date dt;
				String time = "";

				try
				{
					if(isFrom)
					{
						from_hour=hourOfDay;
						from_minute=minute;
					}
					else
					{
						to_hour=hourOfDay;
						to_minute=minute;
					}
					time = "" + hourOfDay + ":" + minute + ":" + "00";
					dt = sdf.parse(time);
					formatedDate = sdfs.format(dt);
					appCompatTextView.setText("" + formatedDate);

					setCount();
				}
				catch (ParseException e)
				{
					e.printStackTrace();
				}
			}
		};

        Calendar now = Calendar.getInstance(Locale.getDefault());

		if(isFrom) {
			now.set(Calendar.HOUR_OF_DAY, from_hour);
			now.set(Calendar.MINUTE, from_minute);
		}
		else {
			now.set(Calendar.HOUR_OF_DAY, to_hour);
			now.set(Calendar.MINUTE, to_minute);
		}
		TimePickerDialog tpd;
		if (!DateFormat.is24HourFormat(act)) {
			tpd = TimePickerDialog.newInstance(
					onTimeSetListener,
					now.get(Calendar.HOUR_OF_DAY),
					now.get(Calendar.MINUTE), false);

			tpd.setSelectableTimes(generateTimepoints(23.50, 30));

			tpd.setMaxTime(23, 30, 00);
		} else {
			//24 hrs format
			tpd = TimePickerDialog.newInstance(
					onTimeSetListener,
					now.get(Calendar.HOUR_OF_DAY),
					now.get(Calendar.MINUTE), true);

			tpd.setSelectableTimes(generateTimepoints(23.50, 30));

			tpd.setMaxTime(23, 30, 00);
		}


		tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
		tpd.show(act.getFragmentManager(), "Datepickerdialog");
		tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
	}

	@NonNull
	public static Timepoint[] generateTimepoints(double maxHour, int minutesInterval) {

		int lastValue = (int) (maxHour * 60);

		List<Timepoint> timepoints = new ArrayList<>();

		for (int minute = 0; minute <= lastValue; minute += minutesInterval) {
			int currentHour = minute / 60;
			int currentMinute = minute - (currentHour > 0 ? (currentHour * 60) : 0);
			if (currentHour == 24)
				continue;
			timepoints.add(new Timepoint(currentHour, currentMinute));
		}
		return timepoints.toArray(new Timepoint[timepoints.size()]);
	}

	public int getDifference()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a",Locale.getDefault());

		Date date1 = null;
		Date date2 = null;
		try {
			date1 = simpleDateFormat.parse(txt_wakeup_time.getText().toString().trim());
			date2 = simpleDateFormat.parse(txt_bed_time.getText().toString().trim());

			long difference = date2.getTime() - date1.getTime();
			int days = (int) (difference / (1000*60*60*24));
			int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
			int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

			return min;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return  0;

	}

	public boolean isNextDayEnd()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a",Locale.getDefault());

		Date date1 = null;
		Date date2 = null;
		try {
			date1 = simpleDateFormat.parse(txt_wakeup_time.getText().toString().trim());
			date2 = simpleDateFormat.parse(txt_bed_time.getText().toString().trim());

			if(date1.getTime()>date2.getTime())
				return true;
			else
				return false;
		}
		catch (Exception e){}

		return false;
	}

	public void setCount()
	{
		Calendar startTime = Calendar.getInstance(Locale.getDefault());
		startTime.set(Calendar.HOUR_OF_DAY, from_hour);
		startTime.set(Calendar.MINUTE, from_minute);
		startTime.set(Calendar.SECOND,0);

		Calendar endTime = Calendar.getInstance(Locale.getDefault());
		endTime.set(Calendar.HOUR_OF_DAY, to_hour);
		endTime.set(Calendar.MINUTE, to_minute);
		endTime.set(Calendar.SECOND,0);

		if(isNextDayEnd())
			endTime.add(Calendar.DATE,1);

		long mills = endTime.getTimeInMillis() - startTime.getTimeInMillis();

		int hours = (int) (mills/(1000 * 60 * 60));
		int mins = (int) ((mills/(1000*60)) % 60);
		float total_minutes=(hours*60)+mins;

		int interval=rdo_15.isChecked()?15:rdo_30.isChecked()?30:rdo_45.isChecked()?45:60;

		int consume=0;
		if(total_minutes>0)
			consume= Math.round(SharedPreferencesManager.INSTANCE.getTotalIntake()/(total_minutes/interval));

		String unit=SharedPreferencesManager.INSTANCE.getPersonWeight()?"ml":"0z UK";

		lbl_message.setText(sh.get_string(R.string.goal_consume).replace("$1",""+consume+" "+unit).replace("$2",""+(int)SharedPreferencesManager.INSTANCE.getTotalIntake()+" "+unit));

		SharedPreferencesManager.INSTANCE.setWakeUpTimeS(txt_wakeup_time.getText().toString().trim());
		SharedPreferencesManager.INSTANCE.setWakeUpTimeHour(from_hour);
		SharedPreferencesManager.INSTANCE.setWakeUpTimeMins(from_minute);

		SharedPreferencesManager.INSTANCE.setSleepingTimeS(txt_bed_time.getText().toString().trim());
		SharedPreferencesManager.INSTANCE.setSleepingTimeHour(to_hour);
		SharedPreferencesManager.INSTANCE.setSleepingTimeMins(to_minute);

		SharedPreferencesManager.INSTANCE.setNotificationFreq(interval);

		if(consume>SharedPreferencesManager.INSTANCE.getTotalIntake())
			SharedPreferencesManager.INSTANCE.setIgnoreNextStep(true);
		else if(consume==0)
			SharedPreferencesManager.INSTANCE.setIgnoreNextStep(true);
		else
			SharedPreferencesManager.INSTANCE.setIgnoreNextStep(false);

	}
}