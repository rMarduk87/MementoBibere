package rpt.tool.mementobibere.ui.userinfo.info

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.databinding.FragmentUserInfoGenderChoiceBinding
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager

class UserInfoGenderChoiceFragment :
    BaseFragment<FragmentUserInfoGenderChoiceBinding>(FragmentUserInfoGenderChoiceBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()
    }

    private fun body() {
        binding.maleBlock.setOnClickListener { setGender(true) }

        binding.femaleBlock.setOnClickListener { setGender(false) }
        

        binding.txtUserName.setText(SharedPreferencesManager.userName)
        setGender(!SharedPreferencesManager.userGender)

        binding.txtUserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                SharedPreferencesManager.userName = binding.txtUserName.
                getText().toString().trim { it <= ' ' }

                SharedPreferencesManager.setUserName = true
            }
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setGender(isMale: Boolean) {
        SharedPreferencesManager.setManuallyGoal = false

        if (isMale) {
            SharedPreferencesManager.userGender = false
            SharedPreferencesManager.isPregnant = false
            SharedPreferencesManager.isBreastfeeding = false

            binding.maleBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgMale.setImageResource(R.drawable.ic_male_selected)

            binding.femaleBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgFemale.setImageResource(R.drawable.ic_female_normal)

            SharedPreferencesManager.setGender = true

        } else {
            SharedPreferencesManager.userGender = false
            SharedPreferencesManager.setGender = true

            binding.maleBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgMale.setImageResource(R.drawable.ic_male_normal)

            binding.femaleBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgFemale.setImageResource(R.drawable.ic_female_selected)
        }
    }
}