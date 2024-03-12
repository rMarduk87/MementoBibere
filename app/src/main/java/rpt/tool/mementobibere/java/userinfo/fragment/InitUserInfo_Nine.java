package rpt.tool.mementobibere.java.userinfo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import rpt.tool.mementobibere.R;
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager;

public class InitUserInfo_Nine extends  MasterBaseFragment{
    View item_view;

    RelativeLayout donor_block,non_donor_block;
    ImageView img_donor,img_non_donor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        item_view=inflater.inflate(R.layout.init_user_info_nine_fragment, container, false);

        FindViewById();
        Body();

        return item_view;
    }

    private void FindViewById()
    {
        donor_block=item_view.findViewById(R.id.donor_block);
        non_donor_block=item_view.findViewById(R.id.non_donor_block);


        img_donor=item_view.findViewById(R.id.img_donor);
        img_non_donor=item_view.findViewById(R.id.img_non_donor);

    }

    private void Body()
    {
        setBloodDonor(SharedPreferencesManager.INSTANCE.getBloodDonorKey());

        donor_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setBloodDonor(1);
            }
        });

        non_donor_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setBloodDonor(0);
            }
        });
    }

    public void setBloodDonor(int idx)
    {
        SharedPreferencesManager.INSTANCE.setSetManualGoal(false);
        SharedPreferencesManager.INSTANCE.setSetBloodDonor(true);
        SharedPreferencesManager.INSTANCE.setBloodDonorKey(idx);

        donor_block.setBackground(idx==1?mContext.getResources().getDrawable(R.drawable.rdo_gender_select)
                :mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
        img_donor.setImageResource(idx==1?R.drawable.ic_donor_selected:R.drawable.ic_donor_normal);

        non_donor_block.setBackground(idx==0?mContext.getResources().getDrawable(R.drawable.rdo_gender_select)
                :mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
        img_non_donor.setImageResource(idx==0?R.drawable.ic_non_donor_selected:R.drawable.ic_non_donor_normal);
    }
}
