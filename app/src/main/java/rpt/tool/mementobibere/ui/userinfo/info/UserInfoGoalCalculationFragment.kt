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
import rpt.tool.mementobibere.utils.log.e
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager

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
            if (SharedPreferencesManager.setManuallyGoal) {

                URLFactory.DAILY_WATER_VALUE = SharedPreferencesManager.setManuallyGoalValue
                SharedPreferencesManager.dailyWater = URLFactory.DAILY_WATER_VALUE
                binding.lblGoal.text = getData("" + URLFactory.DAILY_WATER_VALUE)

                if (SharedPreferencesManager.weightUnit) {
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
        if (SharedPreferencesManager.setManuallyGoal) {
            URLFactory.DAILY_WATER_VALUE = SharedPreferencesManager.setManuallyGoalValue
            SharedPreferencesManager.dailyWater = URLFactory.DAILY_WATER_VALUE

            binding.lblGoal.text = getData("" + URLFactory.DAILY_WATER_VALUE)

            if (SharedPreferencesManager.weightUnit) {
                binding.lblUnit.text = "ml"
            } else {
                binding.lblUnit.text = "fl oz"
            }
        } else {
            calculate_goal()
        }

        binding.lblSetGoalManually.setOnClickListener { showSetManuallyGoalDialog() }
    }

    @SuppressLint("SetTextI18n")
    private fun calculate_goal() {
        val tmp_weight = "" + SharedPreferencesManager.personWeight

        val isFemale: Boolean = SharedPreferencesManager.userGender
        val isActive: Boolean = SharedPreferencesManager.isActive
        val isPregnant: Boolean = SharedPreferencesManager.isPregnant
        val isBreastfeeding: Boolean = SharedPreferencesManager.isBreastfeeding
        val weatherIdx: Int = SharedPreferencesManager.climate

        if (!sh!!.check_blank_data(tmp_weight)) {
            var tot_drink = 0.0
            var tmp_kg = 0.0
            tmp_kg = if (SharedPreferencesManager.weightUnit) {
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

            if (SharedPreferencesManager.weightUnit) {
                binding.lblUnit.text = "ml"
                URLFactory.DAILY_WATER_VALUE = tot_drink.toFloat()
            } else {
                binding.lblUnit.text = "fl oz"
                URLFactory.DAILY_WATER_VALUE = tot_drink_fl_oz.toFloat()
            }

            URLFactory.DAILY_WATER_VALUE = Math.round(URLFactory.DAILY_WATER_VALUE).toFloat()
            binding.lblGoal.text = getData("" + URLFactory.DAILY_WATER_VALUE)

            SharedPreferencesManager.dailyWater = URLFactory.DAILY_WATER_VALUE
        }
    }


    var isExecute: Boolean = true
    var isExecuteSeekbar: Boolean = true

    @SuppressLint("InflateParams")
    private fun showSetManuallyGoalDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View =
            LayoutInflater.from(requireActivity()).inflate(
                R.layout.dialog_set_manually_goal, null, false)


        val lbl_goal2 = view.findViewById<AppCompatEditText>(R.id.lbl_goal)
        val lbl_unit2 = view.findViewById<AppCompatTextView>(R.id.lbl_unit)
        val btn_cancel = view.findViewById<RelativeLayout>(R.id.btn_cancel)
        val btn_save = view.findViewById<RelativeLayout>(R.id.btn_save)
        val seekbarGoal: SeekBar = view.findViewById<SeekBar>(R.id.seekbarGoal)

        if (SharedPreferencesManager.setManuallyGoal) lbl_goal2.setText(
            getData(
                "" + SharedPreferencesManager.setManuallyGoalValue)
        )
        else lbl_goal2.setText(getData("" + SharedPreferencesManager.dailyWater))

        lbl_unit2.text = if (SharedPreferencesManager.weightUnit) "ml" else "fl oz"

        if (SharedPreferencesManager.weightUnit) {
            seekbarGoal.min = 900
            seekbarGoal.max = 8000
            lbl_goal2.filters = arrayOf<InputFilter>(
                InputFilterWeightRange(0.0, 8000.0),
                InputFilter.LengthFilter(4)
            )
        } else {
            seekbarGoal.min = 30
            seekbarGoal.max = 270
            lbl_goal2.filters = arrayOf<InputFilter>(
                InputFilterWeightRange(0.0, 270.0),
                InputFilter.LengthFilter(3)
            )
        }

        val f =
            if (SharedPreferencesManager.setManuallyGoal)
                SharedPreferencesManager.setManuallyGoalValue else
                    SharedPreferencesManager.dailyWater
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
                    e.message?.let { e(Throwable(e), it) }
                }

                isExecuteSeekbar = true
            }
        })

        seekbarGoal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBars: SeekBar, progress: Int, fromUser: Boolean) {
                var progress = progress
                if (isExecuteSeekbar) {
                    progress = if (SharedPreferencesManager.weightUnit) {
                        if (progress < 900) 900 else progress
                    } else {
                        if (progress < 30) 30 else progress
                    }
                    seekbarGoal.progress = progress

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
            if (SharedPreferencesManager.weightUnit && lbl_goal2.getText().toString()
                    .trim { it <= ' ' }
                    .toFloat() >= 900
            ) {
                URLFactory.DAILY_WATER_VALUE =
                    lbl_goal2.getText().toString().trim { it <= ' ' }.toFloat()
                SharedPreferencesManager.dailyWater = URLFactory.DAILY_WATER_VALUE
                binding.lblGoal.text = getData("" + URLFactory.DAILY_WATER_VALUE)
                SharedPreferencesManager.setManuallyGoal = true
                SharedPreferencesManager.setManuallyGoalValue = URLFactory.DAILY_WATER_VALUE
                dialog.dismiss()
            } else {
                if (!SharedPreferencesManager.weightUnit && lbl_goal2.getText().toString()
                        .trim { it <= ' ' }
                        .toFloat() >= 30
                ) {
                    URLFactory.DAILY_WATER_VALUE = lbl_goal2.getText().toString().trim { it <= ' ' }
                        .toFloat()
                    SharedPreferencesManager.dailyWater = URLFactory.DAILY_WATER_VALUE
                    binding.lblGoal.text = getData("" + URLFactory.DAILY_WATER_VALUE)
                    SharedPreferencesManager.setManuallyGoal = true
                    SharedPreferencesManager.setManuallyGoalValue = URLFactory.DAILY_WATER_VALUE
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