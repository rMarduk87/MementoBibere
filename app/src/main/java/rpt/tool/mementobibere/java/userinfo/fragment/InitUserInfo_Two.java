package rpt.tool.mementobibere.java.userinfo.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import rpt.tool.mementobibere.R;
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager;


public class InitUserInfo_Two extends MasterBaseFragment
{
	View item_view;

	//RadioButton rdo_male,rdo_female;

	RelativeLayout male_block,female_block;
	ImageView img_male,img_female;
	AppCompatTextView lbl_male,lbl_female;

	boolean isMaleGender=true;

	AppCompatEditText txt_user_name;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.init_user_info_two_fragment, container, false);

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
			//no
		}

	}

	private void FindViewById()
	{
		//rdo_male=item_view.findViewById(R.id.rdo_male);
		//rdo_female=item_view.findViewById(R.id.rdo_female);

		male_block=item_view.findViewById(R.id.male_block);
		img_male=item_view.findViewById(R.id.img_male);
		lbl_male=item_view.findViewById(R.id.lbl_male);

		female_block=item_view.findViewById(R.id.female_block);
		img_female=item_view.findViewById(R.id.img_female);
		lbl_female=item_view.findViewById(R.id.lbl_female);

		txt_user_name=item_view.findViewById(R.id.txt_user_name);
	}

	private void Body()
	{
		male_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setGender(true);
			}
		});

		female_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setGender(false);
			}
		});

		txt_user_name.setText(SharedPreferencesManager.INSTANCE.getUserName());
		setGender(SharedPreferencesManager.INSTANCE.getGender() == 1);

		txt_user_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				SharedPreferencesManager.INSTANCE.setUserName(txt_user_name.getText().toString().trim());
				SharedPreferencesManager.INSTANCE.setSetUserName(true);
			}
		});

	}

	public void setGender(boolean isMale)
	{
		SharedPreferencesManager.INSTANCE.setSetManualGoal(false);

		if(isMale)
		{
			isMaleGender=true;
			SharedPreferencesManager.INSTANCE.setGender(0);
			SharedPreferencesManager.INSTANCE.setPregnant(false);
			SharedPreferencesManager.INSTANCE.setBreastfeeding(false);

			male_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_male.setImageResource(R.drawable.ic_male_selected);
			female_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_female.setImageResource(R.drawable.ic_female_normal);
		}
		else
		{
			isMaleGender=false;

			SharedPreferencesManager.INSTANCE.setGender(1);

			male_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_male.setImageResource(R.drawable.ic_male_normal);

			female_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_female.setImageResource(R.drawable.ic_female_selected);

		}
		SharedPreferencesManager.INSTANCE.setSetGender(true);
	}

}