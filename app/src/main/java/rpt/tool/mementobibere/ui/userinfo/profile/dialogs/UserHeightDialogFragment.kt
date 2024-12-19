package rpt.tool.mementobibere.ui.userinfo.profile.dialogs

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import rpt.tool.mementobibere.BaseDialogFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant.sh
import rpt.tool.mementobibere.databinding.DialogHeightBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.custom.DigitsInputFilter
import rpt.tool.mementobibere.utils.custom.InputFilterRange
import rpt.tool.mementobibere.utils.log.d
import rpt.tool.mementobibere.utils.log.e
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager


class UserHeightDialogFragment:BaseDialogFragment<DialogHeightBinding>(DialogHeightBinding::inflate) {

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.txtName.requestFocus()
        
        binding.rdoCm.setOnClickListener {
            if (!sh!!.check_blank_data(binding.txtName.getText().toString())) {
                var final_height_cm = 61

                try {
                    val tmp_height: String = AppUtils.getData(binding.txtName.getText().toString().trim())

                    val d = (binding.txtName.getText().toString().trim().toFloat()) as Int

                    d("after_decimal", "" + tmp_height.indexOf("."))

                    if (tmp_height.indexOf(".") > 0) {
                        val after_decimal = tmp_height.substring(tmp_height.indexOf(".") + 1)

                        if (!sh!!.check_blank_data(after_decimal)) {
                            val after_decimal_int = after_decimal.toInt()

                            val final_height = ((d * 12) + after_decimal_int).toDouble()

                            final_height_cm = Math.round(final_height * 2.54).toInt()
                            
                        } else {
                            final_height_cm = Math.round(d * 12 * 2.54).toInt()
                            
                        }
                    } else {
                        final_height_cm = Math.round(d * 12 * 2.54).toInt()
                    }
                } catch (e: Exception) {
                    e.message?.let { it1 -> e(Throwable(e), it1) }
                }

                binding.rdoFeet.isClickable = true
                binding.rdoCm.isClickable = false
                binding.txtName.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 240.0))
                binding.txtName.setText(AppUtils.getData("" + final_height_cm))
                binding.txtName.setSelection(binding.txtName.length())
                
            } else {
                binding.rdoFeet.isChecked = true
                binding.rdoCm.isChecked = false
            }
        }

        binding.rdoFeet.setOnClickListener{
            if (!sh!!.check_blank_data(binding.txtName.getText().toString())) {
                var final_height_feet = "5.0"

                try {
                    val d = (binding.txtName.getText().toString().trim().toFloat()) as Int

                    val tmp_height_inch = Math.round(d / 2.54).toInt()

                    val first = tmp_height_inch / 12
                    val second = tmp_height_inch % 12
                    
                    final_height_feet = "$first.$second"
                } catch (e: Exception) {
                    e.message?.let { it1 -> e(Throwable(e), it1) }
                }

                binding.rdoFeet.isClickable = false
                binding.rdoCm.isClickable = true
                binding.txtName.filters = arrayOf<InputFilter>(
                    InputFilterRange(
                        0.00,
                        AppUtils.getHeightFeetElements()
                    )
                )
                binding.txtName.setText(AppUtils.getData(final_height_feet))
                binding.txtName.setSelection(binding.txtName.length())
                
            } else {
                binding.rdoFeet.isChecked = false
                binding.rdoCm.isChecked = true
            }
        }

        if (SharedPreferencesManager.heightUnit) {
            binding.rdoCm.isChecked = true
            binding.rdoCm.isClickable = false
            binding.rdoFeet.isClickable = true
        } else {
            binding.rdoFeet.isChecked = true
            binding.rdoCm.isClickable = true
            binding.rdoFeet.isClickable = false
        }

        if (!sh!!.check_blank_data(SharedPreferencesManager.personHeight)) {
            if (binding.rdoCm.isChecked) {
                binding.txtName.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 240.0))
                binding.txtName.setText(AppUtils.getData(SharedPreferencesManager.personHeight))
            } else {
                binding.txtName.filters = arrayOf<InputFilter>(
                    InputFilterRange(
                        0.00,
                        AppUtils.getHeightFeetElements()
                    )
                )
                binding.txtName.setText(AppUtils.getData(SharedPreferencesManager.personHeight))
            }
        } else {
            if (binding.rdoCm.isChecked) {
                binding.txtName.filters = arrayOf<InputFilter>(DigitsInputFilter(3, 0, 240.0))
                binding.txtName.setText("150")
            } else {
                binding.txtName.filters = arrayOf<InputFilter>(
                    InputFilterRange(
                        0.00,
                        AppUtils.getHeightFeetElements()
                    )
                )
                binding.txtName.setText("5.0")
            }
        }
        
        binding.btnCancel.setOnClickListener{ dialog!!.cancel() }
        
        binding.imgCancel.setOnClickListener{ dialog!!.cancel() }

        binding.txtName.setSelection(binding.txtName.getText().toString().length)
        
        binding.btnAdd.setOnClickListener {
            if (sh!!.check_blank_data(binding.txtName.getText().toString().trim())) {
                ah!!.customAlert(sh!!.get_string(rpt.tool.mementobibere.R.string.str_height_validation))
            } else {
                var str: String = binding.txtName.getText().toString().trim()

                if (binding.rdoFeet.isChecked) {
                    if (!str.contains(".11") && !str.contains(".10")) str =
                        URLFactory.decimalFormat2.format(str.toDouble())
                }

                str += " " + (if (binding.rdoFeet.isChecked) "feet" else "cm")
                
                saveData(binding.txtName)

                dialog!!.dismiss()
            }
        }
        
    }

    private fun saveData(txt_name: AppCompatEditText) {
        d("saveData", "" + txt_name.text.toString().trim { it <= ' ' })

        SharedPreferencesManager.personHeight = txt_name.text.toString().trim { it <= ' ' }
        SharedPreferencesManager.heightUnit = binding.rdoCm.isChecked
        
        SharedPreferencesManager.setManuallyGoal = false
    }
}