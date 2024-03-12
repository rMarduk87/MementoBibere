package rpt.tool.mementobibere.java.userinfo.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.util.List;

public class InputFilterRange implements InputFilter {

    private double min;
    List<Double> elements;

    public InputFilterRange(double min, List<Double> elements) {
        this.min = min;
        this.elements = elements;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {   
        try {
            //Log.d("CharSequence",""+dest.toString() +" @@@ "+ source.toString());
            String str=dest.toString() + source.toString();
            Log.d("CharSequence"," -> "+str.length());
            /*if(str.length()>=5)
                return null;*/

            double input = Double.parseDouble(dest.toString() + source.toString());
            if (isInRange(min, elements,input,str))
                return null;
        } catch (NumberFormatException nfe) { }     
        return "";
    }

    private boolean isInRange(double a, List<Double>  b, double c, String cc) {
        //return b > a ? c >= a && c <= b : c >= b && c <= a;
        if(cc.length()>4)
        {
            return false;
        }

        for(int k=0;k<b.size();k++)
        {
            //Log.d("CharSequence"," -> "+b.get(k)+" @@@ "+c);
                /*String tb=""+b.get(k);
                String tc=""+c;
                Log.d("CharSequence"," -> "+tb+" @@@ "+tc);*/

            if(b.get(k)==c)
                //if(tb.equalsIgnoreCase(tc))
                return true;
        }
        return false;
    }
}