package rpt.tool.mementobibere.java.userinfo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import rpt.tool.mementobibere.utils.helpers.AlertHelper;
import rpt.tool.mementobibere.utils.helpers.StringHelper;

public class BaseFragment extends Fragment
{
    public Context mContext;
    public Activity act;
    public Intent intent;
    public AlertHelper ah;
    public StringHelper sh;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext=getActivity();
        act=getActivity();

        ah=new AlertHelper(mContext);
        sh=new StringHelper(mContext,act);

    }
}
