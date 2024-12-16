package rpt.tool.mementobibere.ui.userinfo.info

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.databinding.FragmentUserInfoGenderChoiceBinding
import rpt.tool.mementobibere.utils.URLFactory

class UserInfoGenderChoiceFragment :
    BaseFragment<FragmentUserInfoGenderChoiceBinding>(FragmentUserInfoGenderChoiceBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()
    }

    private fun body() {
        binding.maleBlock.setOnClickListener { setGender(true) }

        binding.femaleBlock.setOnClickListener { setGender(false) }
        

        binding.txtUserName.setText(spm!!.getString(URLFactory.USER_NAME))
        setGender(!spm!!.getBoolean(URLFactory.USER_GENDER))

        binding.txtUserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                spm!!.savePreferences(
                    URLFactory.USER_NAME,
                    binding.txtUserName.getText().toString().trim { it <= ' ' })

                spm!!.savePreferences(URLFactory.SET_USER_NAME, true)
            }
        })
    }

    private fun setGender(isMale: Boolean) {
        spm!!.savePreferences(URLFactory.SET_MANUALLY_GOAL, false)

        if (isMale) {

            spm!!.savePreferences(URLFactory.USER_GENDER, false)

            spm!!.savePreferences(URLFactory.IS_PREGNANT, false)
            spm!!.savePreferences(URLFactory.IS_BREATFEEDING, false)

            binding.maleBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgMale.setImageResource(R.drawable.ic_male_selected)

            binding.femaleBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgFemale.setImageResource(R.drawable.ic_female_normal)

            spm!!.savePreferences(URLFactory.SET_USER_GENDER, true)

        } else {

            spm!!.savePreferences(URLFactory.USER_GENDER, true)
            spm!!.savePreferences(URLFactory.SET_USER_GENDER, true)

            binding.maleBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgMale.setImageResource(R.drawable.ic_male_normal)

            binding.femaleBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgFemale.setImageResource(R.drawable.ic_female_selected)
        }
    }
}