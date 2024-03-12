package rpt.tool.mementobibere.java.userinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import rpt.tool.mementobibere.utils.helpers.AlertHelper;
import rpt.tool.mementobibere.utils.helpers.StringHelper;

public class BaseAppCompatActivity extends AppCompatActivity
{
    public Context mContext;
    public Activity act;
    public Intent intent;
    public StringHelper sh;
    public AlertHelper ah;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mContext=BaseAppCompatActivity.this;
        act=BaseAppCompatActivity.this;
        sh=new StringHelper(mContext,act);
        ah=new AlertHelper(mContext);

    }
}
