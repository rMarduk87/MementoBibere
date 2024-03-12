package rpt.tool.mementobibere.java.userinfo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import rpt.tool.mementobibere.R;
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager;


public class InitUserInfo_Eight extends MasterBaseFragment
{
	View item_view;

	RelativeLayout sunny_block,cloudy_block,rainy_block,snow_block;
	ImageView img_sunny,img_cloudy,img_rainy,img_snow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.init_user_info_eight_fragment, container, false);

		FindViewById();
		Body();

		return item_view;
	}

	public void setUserVisibleHint(boolean isVisibleToUser) {

		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser)
		{
		}
		else{
		}

	}

	private void FindViewById()
	{
		sunny_block=item_view.findViewById(R.id.sunny_block);
		cloudy_block=item_view.findViewById(R.id.cloudy_block);
		rainy_block=item_view.findViewById(R.id.rainy_block);
		snow_block=item_view.findViewById(R.id.snow_block);

		img_sunny=item_view.findViewById(R.id.img_sunny);
		img_cloudy=item_view.findViewById(R.id.img_cloudy);
		img_rainy=item_view.findViewById(R.id.img_rainy);
		img_snow=item_view.findViewById(R.id.img_snow);
	}

	private void Body()
	{
		setWeather(SharedPreferencesManager.INSTANCE.getClimate());

		sunny_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setWeather(0);
			}
		});

		cloudy_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setWeather(1);
			}
		});

		rainy_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setWeather(2);
			}
		});

		snow_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setWeather(3);
			}
		});
	}

	public void setWeather(int idx)
	{
		SharedPreferencesManager.INSTANCE.setSetManualGoal(false);
		SharedPreferencesManager.INSTANCE.setClimate(idx);
		SharedPreferencesManager.INSTANCE.setSetClimate(true);

		sunny_block.setBackground(idx==0?mContext.getResources().getDrawable(R.drawable.rdo_gender_select)
				:mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
		img_sunny.setImageResource(idx==0?R.drawable.sunny_selected:R.drawable.sunny);

		cloudy_block.setBackground(idx==1?mContext.getResources().getDrawable(R.drawable.rdo_gender_select)
				:mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
		img_cloudy.setImageResource(idx==1?R.drawable.cloudy_selected:R.drawable.cloudy);

		rainy_block.setBackground(idx==2?mContext.getResources().getDrawable(R.drawable.rdo_gender_select)
				:mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
		img_rainy.setImageResource(idx==2?R.drawable.rainy_selected:R.drawable.rainy);

		snow_block.setBackground(idx==3?mContext.getResources().getDrawable(R.drawable.rdo_gender_select)
				:mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
		img_snow.setImageResource(idx==3?R.drawable.snow_selected:R.drawable.snow);

	}
}