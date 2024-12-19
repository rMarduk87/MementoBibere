package rpt.tool.mementobibere.ui.userinfo.profile.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import rpt.tool.mementobibere.BaseDialogFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant.sh
import rpt.tool.mementobibere.databinding.DialogWeightBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.custom.DigitsInputFilter
import rpt.tool.mementobibere.utils.custom.InputFilterRange
import rpt.tool.mementobibere.utils.custom.InputFilterWeightRange
import rpt.tool.mementobibere.utils.helpers.HeightWeightHelper.kgToLbConverter
import rpt.tool.mementobibere.utils.helpers.HeightWeightHelper.lbToKgConverter
import rpt.tool.mementobibere.utils.log.d
import rpt.tool.mementobibere.utils.log.e
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager


class UserWeightDialogFragment:BaseDialogFragment<DialogWeightBinding>(DialogWeightBinding::inflate) {

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.txtName.requestFocus()
        
        binding.rdoKg.setOnClickListener {
            if (!sh!!.check_blank_data(binding.txtName.text.toString())) {
                val weight_in_lb = binding.txtName.text.toString().toDouble()

                var weight_in_kg = 0.0

                if (weight_in_lb > 0) weight_in_kg =
                    Math.round(lbToKgConverter(weight_in_lb)).toDouble()

                val tmp = weight_in_kg.toInt()

                binding.txtName.filters = arrayOf<InputFilter>(InputFilterWeightRange(0.0, 130.0))
                binding.txtName.setText(AppUtils.getData("" + 
                        URLFactory.decimalFormat2.format(tmp.toLong())))
                binding.rdoKg.isClickable = false
                binding.rdoLb.isClickable = true
            }
        }

        binding.rdoLb.setOnClickListener {
            if (!sh!!.check_blank_data(binding.txtName.text.toString())) {
                val weight_in_kg = binding.txtName.text.toString().toDouble()

                var weight_in_lb = 0.0

                if (weight_in_kg > 0) weight_in_lb =
                    Math.round(kgToLbConverter(weight_in_kg)).toDouble()

                val tmp = weight_in_lb.toInt()

                binding.txtName.filters = arrayOf<InputFilter>(DigitsInputFilter(
                    3, 0, 287.0))
                binding.txtName.setText(AppUtils.getData("" + tmp))
                binding.rdoKg.isClickable = true
                binding.rdoLb.isClickable = false
            }
        }

        if (SharedPreferencesManager.heightUnit) {
            binding.rdoKg.isChecked = true
            binding.rdoKg.isClickable = false
            binding.rdoLb.isClickable = true
        } else {
            binding.rdoLb.isChecked = true
            binding.rdoKg.isClickable = true
            binding.rdoLb.isClickable = false
        }

        if (!sh!!.check_blank_data(SharedPreferencesManager.personWeight)) {
            if (binding.rdoKg.isChecked) {

                binding.txtName.filters = arrayOf<InputFilter>(InputFilterWeightRange(0.0, 130.0))
                binding.txtName.setText(AppUtils.getData(SharedPreferencesManager.personWeight))
            } else {
                binding.txtName.filters = arrayOf<InputFilter>(DigitsInputFilter(3,
                    0, 287.0))
                binding.txtName.setText(AppUtils.getData(SharedPreferencesManager.personWeight))
            }
        } else {
            if (binding.rdoKg.isChecked) {

                binding.txtName.filters = arrayOf<InputFilter>(InputFilterWeightRange(0.0, 130.0))
                binding.txtName.setText("80.0")
            } else {
                binding.txtName.filters = arrayOf<InputFilter>(DigitsInputFilter(3,
                    0, 287.0))
                binding.txtName.setText("176")
            }
        }
        
        binding.btnCancel.setOnClickListener { dialog!!.cancel() }
        binding.imgCancel.setOnClickListener { dialog!!.cancel() }

        binding.txtName.setSelection(binding.txtName.text.toString().length)
        
        binding.btnAdd.setOnClickListener {
            if (sh!!.check_blank_data(binding.txtName.text.toString().trim { it <= ' ' })) {
                ah!!.customAlert(sh!!.get_string(R.string.str_weight_validation))
            } else {
                var str = binding.txtName.text.toString().trim { it <= ' ' }

                if (binding.rdoKg.isChecked) {
                    str = URLFactory.decimalFormat2.format(str.toDouble())
                }

                str += " " + (if (binding.rdoKg.isChecked) "kg" else "lb")

                saveWeightData(binding.txtName)

                dialog!!.dismiss()
            }
        }
        
    }

    private fun saveWeightData(txt_name: AppCompatEditText) {
        d(
            "saveWeightData",
            ("" + binding.rdoKg.isChecked) + " @@@ " + txt_name.text.toString()
                .trim { it <= ' ' })

        SharedPreferencesManager.personWeight = txt_name.text.toString().trim { it <= ' ' }
        SharedPreferencesManager.weightUnit = binding.rdoKg.isChecked
        SharedPreferencesManager.waterUnit = if (binding.rdoKg.isChecked) "ml" else "fl oz"
        SharedPreferencesManager.setManuallyGoal = false
        URLFactory.WATER_UNIT_VALUE = if (binding.rdoKg.isChecked) "ml" else "fl oz"
        refreshWidget()
    }

}