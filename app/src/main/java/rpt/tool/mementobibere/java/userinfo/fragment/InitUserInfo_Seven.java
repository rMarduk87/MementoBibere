package rpt.tool.mementobibere.java.userinfo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import rpt.tool.mementobibere.R;
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager;


public class InitUserInfo_Seven extends MasterBaseFragment
{
	View item_view;

	LinearLayout block_for_female;

	RelativeLayout active_block,pregnant_block,breastfeeding_block;
	ImageView img_active,img_pregnant,img_breastfeeding;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.init_user_info_seven_fragment, container, false);

		FindViewById();
		Body();

		return item_view;
	}

	public void setUserVisibleHint(boolean isVisibleToUser) {

		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser)
		{
			//ah.customAlert("call");
			//actionView();

			if(SharedPreferencesManager.INSTANCE.isPregnant())
			{
				pregnant_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
				img_pregnant.setImageResource(R.drawable.pregnant_selected);
			}
			else
			{
				pregnant_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
				img_pregnant.setImageResource(R.drawable.pregnant);
			}

			if(SharedPreferencesManager.INSTANCE.isBreastfeeding())
			{
				breastfeeding_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
				img_breastfeeding.setImageResource(R.drawable.breastfeeding_selected);
			}
			else
			{
				breastfeeding_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
				img_breastfeeding.setImageResource(R.drawable.breastfeeding);
			}



			if(SharedPreferencesManager.INSTANCE.getGender() == 1) // female
			{

				pregnant_block.setFocusableInTouchMode(true);
				pregnant_block.setClickable(true);
				pregnant_block.setFocusable(true);
				pregnant_block.setAlpha(1);

				for (int i = 0; i < pregnant_block.getChildCount(); i++) {
					View child = pregnant_block.getChildAt(i);
					child.setEnabled(true);
				}

				breastfeeding_block.setFocusableInTouchMode(true);
				breastfeeding_block.setClickable(true);
				breastfeeding_block.setFocusable(true);
				breastfeeding_block.setAlpha(1);

				for (int i = 0; i < breastfeeding_block.getChildCount(); i++) {
					View child = breastfeeding_block.getChildAt(i);
					child.setEnabled(true);
				}
			}
			else
			{
				pregnant_block.setFocusableInTouchMode(false);
				pregnant_block.setClickable(false);
				pregnant_block.setFocusable(false);
				pregnant_block.setAlpha(0.50f);

				for (int i = 0; i < pregnant_block.getChildCount(); i++) {
					View child = pregnant_block.getChildAt(i);
					child.setEnabled(false);
				}

				breastfeeding_block.setFocusableInTouchMode(false);
				breastfeeding_block.setClickable(false);
				breastfeeding_block.setFocusable(false);
				breastfeeding_block.setAlpha(0.50f);

				for (int i = 0; i < breastfeeding_block.getChildCount(); i++) {
					View child = breastfeeding_block.getChildAt(i);
					child.setEnabled(false);
				}
			}
		}
		else{
		}

	}

	private void FindViewById()
	{
		block_for_female=item_view.findViewById(R.id.block_for_female);

		active_block=item_view.findViewById(R.id.active_block);
		pregnant_block=item_view.findViewById(R.id.pregnant_block);
		breastfeeding_block=item_view.findViewById(R.id.breastfeeding_block);

		img_active=item_view.findViewById(R.id.img_active);
		img_pregnant=item_view.findViewById(R.id.img_pregnant);
		img_breastfeeding=item_view.findViewById(R.id.img_breastfeeding);

	}

	private void Body()
	{
		setActive();
		setBreastfeeding();
		setPregnant();

		active_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(SharedPreferencesManager.INSTANCE.getWorkType() == 0)
					SharedPreferencesManager.INSTANCE.setWorkType(1);
				else
					SharedPreferencesManager.INSTANCE.setWorkType(0);

				setActive();
				SharedPreferencesManager.INSTANCE.setSetWorkOut(true);
			}
		});

		pregnant_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(SharedPreferencesManager.INSTANCE.isPregnant())
					SharedPreferencesManager.INSTANCE.setPregnant(false);
				else
					SharedPreferencesManager.INSTANCE.setPregnant(true);

				setPregnant();
			}
		});

		breastfeeding_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(SharedPreferencesManager.INSTANCE.isBreastfeeding())
					SharedPreferencesManager.INSTANCE.setBreastfeeding(false);
				else
					SharedPreferencesManager.INSTANCE.setBreastfeeding(true);

				setBreastfeeding();
			}
		});
	}

	public void setActive()
	{
		SharedPreferencesManager.INSTANCE.setSetManualGoal(false);

		if(SharedPreferencesManager.INSTANCE.getWorkType()==0)
		{
			active_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_active.setImageResource(R.drawable.active_selected);
		}
		else
		{
			active_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_active.setImageResource(R.drawable.active);
		}
	}

	public void setPregnant()
	{
		SharedPreferencesManager.INSTANCE.setSetManualGoal(false);

		if(SharedPreferencesManager.INSTANCE.isPregnant())
		{
			pregnant_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_pregnant.setImageResource(R.drawable.pregnant_selected);
		}
		else
		{
			pregnant_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_pregnant.setImageResource(R.drawable.pregnant);
		}
		SharedPreferencesManager.INSTANCE.setSetPregnantChoice(true);
	}

	public void setBreastfeeding()
	{
		SharedPreferencesManager.INSTANCE.setSetManualGoal(false);

		if(SharedPreferencesManager.INSTANCE.isBreastfeeding())
		{
			breastfeeding_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_breastfeeding.setImageResource(R.drawable.breastfeeding_selected);
		}
		else
		{
			breastfeeding_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_breastfeeding.setImageResource(R.drawable.breastfeeding);
		}

		SharedPreferencesManager.INSTANCE.setSetBreastfeedingChoice(true);
	}
}