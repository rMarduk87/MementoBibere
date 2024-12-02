package rpt.tool.mementobibere.utils.custom;

import static rpt.tool.mementobibere.utils.log.LogUtilsKt.d;
import android.text.InputFilter;
import android.text.Spanned;
import androidx.annotation.NonNull;
import java.util.List;

public class InputFilterRange implements InputFilter {

    private double min;
    List<Double> elements;

    public InputFilterRange(double min, List<Double> elements) {
        this.min = min;
        this.elements = elements;
    }

    @Override
    public CharSequence filter(@NonNull CharSequence source, int start, int end, @NonNull Spanned dest, int dstart, int dend) {
        try {

            String str=dest.toString() + source.toString();
            d("CharSequence"," -> "+str.length());


            double input = Double.parseDouble(dest.toString() + source.toString());
            if (isInRange(min, elements,input,str))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(double a, List<Double>  b, double c, @NonNull String cc) {
        if(cc.length()>4)
        {
            return false;
        }

        for(int k=0;k<b.size();k++)
        {

            if(b.get(k)==c)
                return true;
        }
        return false;
    }
}