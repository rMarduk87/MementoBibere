package rpt.tool.mementobibere.java.userinfo;

import rpt.tool.mementobibere.R;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class MasterBaseAppCompatActivity extends BaseAppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    public static int getThemeColor(@NonNull Context ctx)
    {
        /*if(true)
            return ctx.getResources().getColor(R.color.colorPrimaryDark2);*/

        return ctx.getResources().getColor(R.color.colorPrimaryDark);
    }

    public static int[] getThemeColorArray(Context ctx)
    {
        int[] colors = {Color.parseColor("#001455da"),Color.parseColor("#FF1455da")};

        /*if(true)
            colors = new int[]{Color.parseColor("#0034a2a9"), Color.parseColor("#FF34a2a9")};*/

        return colors;

    }
}
