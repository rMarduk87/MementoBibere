package rpt.tool.mementobibere.java.userinfo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import rpt.tool.mementobibere.R;


public class InitUserInfo_Five extends MasterBaseFragment
{
	View item_view;



	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.init_user_info_five_fragment, container, false);

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
		}
		else{
			//no
		}

	}

	private void FindViewById()
	{

	}

	private void Body()
	{

	}
}