package rpt.tool.mementobibere.ui.userinfo.info

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.FragmentUserInfoGoalCalculationBinding
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.custom.InputFilterWeightRange
import rpt.tool.mementobibere.utils.helpers.HeightWeightHelper

class UserInfoGoalCalculationFragment :
    BaseFragment<FragmentUserInfoGoalCalculationBinding>(FragmentUserInfoGoalCalculationBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()

    }
    
    private fun getData(str: String): String {
        return str.replace(",", ".")
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (ph!!.getBoolean(URLFactory.SET_MANUALLY_GOAL)) {

                URLFactory.DAILY_WATER_VALUE = ph!!.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE)
                ph!!.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE)
                binding.lblGoal.text = getData("" + URLFactory.DAILY_WATER_VALUE)

                if (ph!!.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                    binding.lblUnit.text = "ml"
                } else {
                    binding.lblUnit.text = "fl oz"
                }
            } else {
                calculate_goal()
            }
        } else {
            //no
        }
    }

    @SuppressLint("SetTextI18n")
    private fun body() {
        if (ph!!.getBoolean(URLFactory.SET_MANUALLY_GOAL)) {
            URLFactory.DAILY_WATER_VALUE = ph!!.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE)
            ph!!.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE)

            binding.lblGoal.text = getData("" + URLFactory.DAILY_WATER_VALUE)

            if (ph!!.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                binding.lblUnit.text = "ml"
            } else {
                binding.lblUnit.text = "fl oz"
            }
        } else {
            calculate_goal()
        }

        binding.lblSetGoalManually.setOnClickListener { showSetManuallyGoalDialog() }
    }

    private fun calculate_goal() {
        val tmp_weight = "" + ph!!.getString(URLFactory.PERSON_WEIGHT)

        val isFemale: Boolean = ph!!.getBoolean(URLFactory.USER_GENDER)
        val isActive: Boolean = ph!!.getBoolean(URLFactory.IS_ACTIVE)
        val isPregnant: Boolean = ph!!.getBoolean(URLFactory.IS_PREGNANT)
        val isBreastfeeding: Boolean = ph!!.getBoolean(URLFactory.IS_BREATFEEDING)
        val weatherIdx: Int = ph!!.getInt(URLFactory.WEATHER_CONSITIONS)

        if (!sh!!.check_blank_data(tmp_weight)) {
            var tot_drink = 0.0
            var tmp_kg = 0.0
            tmp_kg = if (ph!!.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                ("" + tmp_weight).toDouble()
            } else {
                HeightWeightHelper.lbToKgConverter(tmp_weight.toDouble())
            }


            tot_drink =
                if (isFemale) if (isActive) tmp_kg * URLFactory.ACTIVE_FEMALE_WATER else tmp_kg * URLFactory.FEMALE_WATER
                else if (isActive) tmp_kg * URLFactory.ACTIVE_MALE_WATER else tmp_kg * URLFactory.MALE_WATER

            tot_drink *= when (weatherIdx) {
                1 -> URLFactory.WEATHER_CLOUDY
                2 -> URLFactory.WEATHER_RAINY
                3 -> URLFactory.WEATHER_SNOW
                else -> URLFactory.WEATHER_SUNNY
            }

            if (isPregnant && isFemale) {
                tot_drink += URLFactory.PREGNANT_WATER
            }

            if (isBreastfeeding && isFemale) {
                tot_drink += URLFactory.BREASTFEEDING_WATER
            }

            if (tot_drink < 900) tot_drink = 900.0

            if (tot_drink > 8000) tot_drink = 8000.0

            val tot_drink_fl_oz: Double = HeightWeightHelper.mlToOzConverter(tot_drink)

            if (ph!!.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                binding.lblUnit.text = "ml"
                URLFactory.DAILY_WATER_VALUE = tot_drink.toFloat()
            } else {
                binding.lblUnit.text = "fl oz"
                URLFactory.DAILY_WATER_VALUE = tot_drink_fl_oz.toFloat()
            }

            URLFactory.DAILY_WATER_VALUE = Math.round(URLFactory.DAILY_WATER_VALUE).toFloat()
            binding.lblGoal.text = getData("" + URLFactory.DAILY_WATER_VALUE)

            ph!!.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE)
        }
    }


    var isExecute: Boolean = true
    var isExecuteSeekbar: Boolean = true

    private fun showSetManuallyGoalDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View =
            LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_set_manually_goal, null, false)


        val lbl_goal2 = view.findViewById<AppCompatEditText>(R.id.lbl_goal)
        val lbl_unit2 = view.findViewById<AppCompatTextView>(R.id.lbl_unit)
        val btn_cancel = view.findViewById<RelativeLayout>(R.id.btn_cancel)
        val btn_save = view.findViewById<RelativeLayout>(R.id.btn_save)
        val seekbarGoal: SeekBar = view.findViewById<SeekBar>(R.id.seekbarGoal)

        if (ph!!.getBoolean(URLFactory.SET_MANUALLY_GOAL)) lbl_goal2.setText(
            getData(
                "" + ph!!.getFloat(
                    URLFactory.SET_MANUALLY_GOAL_VALUE
                ) as Int
            )
        )
        else lbl_goal2.setText(getData("" + ph!!.getFloat(URLFactory.DAILY_WATER) as Int))

        lbl_unit2.text = if (ph!!.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) "ml" else "fl oz"

        if (ph!!.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekbarGoal.min = 900
            }
            seekbarGoal.max = 8000
            lbl_goal2.filters = arrayOf<InputFilter>(
                InputFilterWeightRange(0.0, 8000.0),
                InputFilter.LengthFilter(4)
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekbarGoal.min = 30
            }
            seekbarGoal.max = 270
            lbl_goal2.filters = arrayOf<InputFilter>(
                InputFilterWeightRange(0.0, 270.0),
                InputFilter.LengthFilter(3)
            )
        }

        val f =
            if (ph!!.getBoolean(URLFactory.SET_MANUALLY_GOAL)) ph!!.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE) else ph!!.getFloat(
                URLFactory.DAILY_WATER
            )
        seekbarGoal.progress = f.toInt()

        lbl_goal2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                isExecuteSeekbar = false
            }

            override fun afterTextChanged(editable: Editable) {
                try {
                    if (!sh!!.check_blank_data(
                            lbl_goal2.getText().toString().trim { it <= ' ' }) && isExecute
                    ) {
                        val data: Int = lbl_goal2.getText().toString().trim { it <= ' ' }.toInt()
                        seekbarGoal.progress = data
                    }
                } catch (e: Exception) {
                }

                isExecuteSeekbar = true
            }
        })

        seekbarGoal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBars: SeekBar, progress: Int, fromUser: Boolean) {
                var progress = progress
                if (isExecuteSeekbar) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        progress = if (ph!!.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
                            if (progress < 900) 900 else progress
                        } else {
                            if (progress < 30) 30 else progress
                        }
                        seekbarGoal.progress = progress
                    }

                    lbl_goal2.setText("" + progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isExecute = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isExecute = true
            }
        })


        btn_cancel.setOnClickListener { dialog.dismiss() }

        btn_save.setOnClickListener {
            if (ph!!.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) && lbl_goal2.getText().toString()
                    .trim { it <= ' ' }
                    .toFloat() >= 900
            ) {
                URLFactory.DAILY_WATER_VALUE =
                    lbl_goal2.getText().toString().trim { it <= ' ' }.toFloat()
                ph!!.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE)
                binding.lblGoal.text = getData("" + URLFactory.DAILY_WATER_VALUE)
                ph!!.savePreferences(URLFactory.SET_MANUALLY_GOAL, true)
                ph!!.savePreferences(URLFactory.SET_MANUALLY_GOAL_VALUE, URLFactory.DAILY_WATER_VALUE)
                dialog.dismiss()
            } else {
                if (!ph!!.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) && lbl_goal2.getText().toString()
                        .trim { it <= ' ' }
                        .toFloat() >= 30
                ) {
                    URLFactory.DAILY_WATER_VALUE = lbl_goal2.getText().toString().trim { it <= ' ' }
                        .toFloat()
                    ph!!.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE)
                    binding.lblGoal.text = getData("" + URLFactory.DAILY_WATER_VALUE)
                    ph!!.savePreferences(URLFactory.SET_MANUALLY_GOAL, true)
                    ph!!.savePreferences(
                        URLFactory.SET_MANUALLY_GOAL_VALUE,
                        URLFactory.DAILY_WATER_VALUE
                    )
                    dialog.dismiss()
                } else {
                    ah!!.customAlert(sh!!.get_string(R.string.str_set_daily_goal_validation))
                }
            }
        }

        dialog.setContentView(view)

        dialog.show()
    }
}