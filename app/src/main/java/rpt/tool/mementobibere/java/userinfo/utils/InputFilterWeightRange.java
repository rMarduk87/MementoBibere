package rpt.tool.mementobibere.java.userinfo.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterWeightRange implements InputFilter {

    private double min;
    private double max;

    public InputFilterWeightRange(double min,double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {   
        try {
            String str=dest.toString() + source.toString();

            double input = Double.parseDouble(dest.toString() + source.toString());
            if (isInRange(min, max,input,str))
                return null;
        } catch (NumberFormatException nfe) { }     
        return "";
    }

    private boolean isInRange(double a, double  b, double c, String cc) {
        //return b > a ? c >= a && c <= b : c >= b && c <= a;
        if(cc.length()>5)
        {
            return false;
        }
        if(c>b)
        {
            return false;
        }
        if(c<a)
        {
            return false;
        }

        if(c%0.5==0)
            return true;


        return false;
    }
}