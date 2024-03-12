package rpt.tool.mementobibere.java.userinfo.helper;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.text.DecimalFormat;

/**
 * This class is used for any calculations connected with Weight and Height
 *  - KG to LB converter (and vice versa)
 *  - Feet to Cm converter (and vice versa)
 *  - BMI Calculation
 *
 */
public class HeightWeightHelper {

    /**
     *
     * @param value double that is formatted
     * @return double that has 1 decimal place
     */
    private static double format ( double value)
    {
        try {
            if (value != 0) {
                DecimalFormat df = new DecimalFormat("###.##");
                return Double.valueOf(df.format(value).replace(",", ".")
                        .replace("٫", "."));
            } else {
                return -1;
            }
        }
        catch (Exception e){}

        return -1;
    }

    /**
     *
     * @param lb - pounds
     * @return kg rounded to 1 decimal place
     */
    public static double lbToKgConverter(double lb) {
        return format(lb * 0.453592 );
        //0.45359237
    }

    /**
     *
     * @param kg - kilograms
     * @return lb rounded to 1 decimal place
     */
    public static double kgToLbConverter(double kg) {
        return format(kg * 2.204624420183777);
        //2.20462262
    }

    /**
     *
     * @param cm - centimeters
     * @return feet rounded to 1 decimal place
     */
    /*public static double cmToFeetConverter(double cm) {
        return format(cm * 0.032808399 );
    }*/
    public static double cmToFeetConverter(double cm) {
        return format(cm / 30 );
    }

    /**
     *
     * @param feet - feet
     * @return centimeters rounded to 1 decimal place
     */
    /*public static double feetToCmConverter(double feet) {
        return format(feet * 30.48 );
    }*/
    public static double feetToCmConverter(double feet) {
        return format(feet * 30 );
    }

    /**
     *
     * @param height in <b>cm</b>
     * @param weight in <b>kilograms</b>
     * @return BMI index with 1 decimal place
     */
    public static double getBMIKg (double height, double weight) {
        double meters = height/100;
        return format( weight / Math.pow(meters,2));
    }

    /**
     *
     * @param height in <b>feet</b>
     * @param weight in <b>pounds</b>
     * @return BMI index with 1 decimal place
     */
    public static double getBMILb (double height, double weight) {
        int inch = (int)(height *12);
        return format((weight*703) / Math.pow(inch, 2));
    }

    /**
     *
     * @param bmi (Body Mass Index)
     * @return BMI classification based on the bmi number
     */
    @NonNull
    @Contract(pure = true)
    public static String getBMIClassification (double bmi) {

        if (bmi <= 0) return "unknown";
        String classification;

        if (bmi < 18.5) {
            classification = "underweight";
        } else if (bmi < 25) {
            classification = "normal";
        } else if (bmi < 30) {
            classification = "overweight";
        } else {
            classification = "obese";
        }

        return classification;
    }

    //=====================================

    public static double ozToMlConverter(double oz) {
        return format(oz * 29.5735 );
    }

    public static double mlToOzConverter(double ml) {
        return format(ml * 0.03381405650328842 );
    }
    //0.033814
}//end of class