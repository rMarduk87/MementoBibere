package rpt.tool.mementobibere.ui.userinfo.profile.dialogs

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import rpt.tool.mementobibere.BaseDialogFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant.sh
import rpt.tool.mementobibere.databinding.DialogSetManuallyGoalBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.custom.InputFilterWeightRange
import rpt.tool.mementobibere.utils.log.e
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager


class SetManuallyGoalDialogFragment:
    BaseDialogFragment<DialogSetManuallyGoalBinding>(DialogSetManuallyGoalBinding::inflate) {

    var isExecute: Boolean = true
    var isExecuteSeekbar: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SharedPreferencesManager.setManuallyGoal)
            binding.lblGoal.setText(
            AppUtils.getData(SharedPreferencesManager.setManuallyGoalValue.toString())
        )
        else{
            binding.lblGoal.setText(AppUtils.getData(SharedPreferencesManager
                .dailyWater.toString()))
        }

        binding.lblUnit.text = if (SharedPreferencesManager.heightUnit) "ml" else "fl oz"

        if (SharedPreferencesManager.heightUnit) {
            binding.seekbarGoal.min = 900
            binding.lblGoal.filters =
                arrayOf(InputFilterWeightRange(0.0, 8000.0), LengthFilter(4))
        } else {
            binding.seekbarGoal.min = 30
            binding.seekbarGoal.max = 270
            binding.lblGoal.filters =
                arrayOf(InputFilterWeightRange(0.0, 270.0), LengthFilter(3))
        }

        val f = if (SharedPreferencesManager.setManuallyGoal)
            SharedPreferencesManager.setManuallyGoalValue else SharedPreferencesManager.dailyWater

        binding.seekbarGoal.progress = f.toInt()

        binding.lblGoal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                isExecuteSeekbar = false
            }

            override fun afterTextChanged(editable: Editable) {
                try {
                    if (!sh!!.check_blank_data(
                            binding.lblGoal.text.toString().trim { it <= ' ' }) && isExecute
                    ) {
                        val data = binding.lblGoal.text.toString().trim { it <= ' ' }.toInt()
                        binding.seekbarGoal.progress = data
                    }
                } catch (e: java.lang.Exception) {
                    e.message?.let { e(Throwable(e), it) }
                }

                isExecuteSeekbar = true
            }
        })

        binding.seekbarGoal.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBars: SeekBar, progress: Int, fromUser: Boolean) {
                var progress = progress
                if (isExecuteSeekbar) {
                    progress = if (SharedPreferencesManager.heightUnit) {
                        if (progress < 900) 900 else progress
                    } else {
                        if (progress < 30) 30 else progress
                    }
                    binding.seekbarGoal.progress = progress

                    binding.lblGoal.setText(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isExecute = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isExecute = true
            }
        })

        binding.btnCancel.setOnClickListener { dialog!!.dismiss() }
        binding.btnSave.setOnClickListener {
            val unit = if (SharedPreferencesManager.heightUnit) "ml" else "fl oz"
            if (SharedPreferencesManager.heightUnit && binding.lblGoal.text.toString()
                    .trim { it <= ' ' }.toFloat() >= 900
            ) {
                URLFactory.DAILY_WATER_VALUE =
                    binding.lblGoal.text.toString().trim { it <= ' ' }.toFloat()
                SharedPreferencesManager.dailyWater = URLFactory.DAILY_WATER_VALUE
                SharedPreferencesManager.setManuallyGoal = true
                SharedPreferencesManager.setManuallyGoalValue = URLFactory.DAILY_WATER_VALUE
                dialog!!.dismiss()
                refreshWidget()
            } else {
                if (!SharedPreferencesManager.heightUnit && binding.lblGoal.text.toString()
                        .trim { it <= ' ' }.toFloat() >= 30
                ) {
                    URLFactory.DAILY_WATER_VALUE =
                        binding.lblGoal.text.toString().trim { it <= ' ' }.toFloat()
                    SharedPreferencesManager.dailyWater = URLFactory.DAILY_WATER_VALUE
                    SharedPreferencesManager.setManuallyGoal = true
                    SharedPreferencesManager.setManuallyGoalValue = URLFactory.DAILY_WATER_VALUE
                    dialog!!.dismiss()

                    refreshWidget()
                } else {
                    ah!!.customAlert(sh!!.get_string(R.string.str_set_daily_goal_validation))
                }
            }
        }
    }


}