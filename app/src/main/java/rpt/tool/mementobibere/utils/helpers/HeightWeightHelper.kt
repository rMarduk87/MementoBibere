package rpt.tool.mementobibere.utils.helpers

import java.text.DecimalFormat
import kotlin.math.pow

/**
 * This class is used for any calculations connected with Weight and Height
 *   - KG to LB converter (and vice versa)
 * - Feet to Cm converter (and vice versa)
 *   - BMI Calculation
 *
 */
object HeightWeightHelper {
    /**
     *
     * @param value double that is formatted
     * @return double that has 1 decimal place
     */
    private fun format(value: Double): Double {
        try {
            if (value != 0.0) {
                val df = DecimalFormat("###.##")
                return df.format(value).replace(",", ".")
                    .replace("٫", ".").toDouble()
            } else {
                return (-1).toDouble()
            }
        } catch (e: Exception) {
        }

        return (-1).toDouble()
    }

    /**
     *
     * @param lb - pounds
     * @return kg rounded to 1 decimal place
     */
    fun lbToKgConverter(lb: Double): Double {
        return format(lb * 0.453592)
        //0.45359237
    }

    /**
     *
     * @param kg - kilograms
     * @return lb rounded to 1 decimal place
     */
    fun kgToLbConverter(kg: Double): Double {
        return format(kg * 2.204624420183777)
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
    fun cmToFeetConverter(cm: Double): Double {
        return format(cm / 30)
    }

    /**
     *
     * @param feet - feet
     * @return centimeters rounded to 1 decimal place
     */
    /*public static double feetToCmConverter(double feet) {
        return format(feet * 30.48 );
    }*/
    fun feetToCmConverter(feet: Double): Double {
        return format(feet * 30)
    }

    /**
     *
     * @param height in **cm**
     * @param weight in **kilograms**
     * @return BMI index with 1 decimal place
     */
    fun getBMIKg(height: Double, weight: Double): Double {
        val meters = height / 100
        return format(weight / meters.pow(2.0))
    }

    /**
     *
     * @param height in **feet**
     * @param weight in **pounds**
     * @return BMI index with 1 decimal place
     */
    fun getBMILb(height: Double, weight: Double): Double {
        val inch = (height * 12).toInt()
        return format((weight * 703) / inch.toDouble().pow(2.0))
    }

    /**
     *
     * @param bmi (Body Mass Index)
     * @return BMI classification based on the bmi number
     */
    fun getBMIClassification(bmi: Double): String {
        if (bmi <= 0) return "unknown"

        val classification = if (bmi < 18.5) {
            "underweight"
        } else if (bmi < 25) {
            "normal"
        } else if (bmi < 30) {
            "overweight"
        } else {
            "obese"
        }

        return classification
    }

    //=====================================
    fun ozToMlConverter(oz: Double): Double {
        return format(oz * 29.5735)
    }

    fun mlToOzConverter(ml: Double): Double {
        return format(ml * 0.03381405650328842)
    } //0.033814
} //end of class
