package rpt.tool.mementobibere.java.userinfo.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import rpt.tool.mementobibere.utils.helpers.AlertHelper;
import rpt.tool.mementobibere.utils.helpers.StringHelper;

public class MasterBaseDialogFragment extends BottomSheetDialogFragment {

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

    @Override
    public void onStart() {
        super.onStart();
    }
}
