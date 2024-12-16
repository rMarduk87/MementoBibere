package rpt.tool.mementobibere.ui.userinfo.info

import android.os.Bundle
import android.view.View
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.FragmentUserInfoStatusBinding
import rpt.tool.mementobibere.utils.URLFactory

class UserInfoStatusFragment : BaseFragment<FragmentUserInfoStatusBinding>(FragmentUserInfoStatusBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {

            if (spm!!.getBoolean(URLFactory.IS_PREGNANT)) {
                binding.pregnantBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
                binding.imgPregnant.setImageResource(R.drawable.pregnant_selected)
            } else {
                binding.pregnantBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
                binding.imgPregnant.setImageResource(R.drawable.pregnant)
            }

            if (spm!!.getBoolean(URLFactory.IS_BREATFEEDING)) {
                binding.breastfeedingBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
                binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding_selected)
            } else {
                binding.breastfeedingBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
                binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding)
            }



            if (spm!!.getBoolean(URLFactory.USER_GENDER)) // female
            {

                binding.pregnantBlock.isFocusableInTouchMode = true
                binding.pregnantBlock.isClickable = true
                binding.pregnantBlock.isFocusable = true
                binding.pregnantBlock.alpha = 1f

                for (i in 0 until binding.pregnantBlock.childCount) {
                    val child: View = binding.pregnantBlock.getChildAt(i)
                    child.isEnabled = true
                }

                binding.breastfeedingBlock.isFocusableInTouchMode = true
                binding.breastfeedingBlock.isClickable = true
                binding.breastfeedingBlock.isFocusable = true
                binding.breastfeedingBlock.alpha = 1f

                for (i in 0 until binding.breastfeedingBlock.childCount) {
                    val child: View = binding.breastfeedingBlock.getChildAt(i)
                    child.isEnabled = true
                }
            } else {

                binding.pregnantBlock.isFocusableInTouchMode = false
                binding.pregnantBlock.isClickable = false
                binding.pregnantBlock.isFocusable = false
                binding.pregnantBlock.alpha = 0.50f

                for (i in 0 until binding.pregnantBlock.childCount) {
                    val child: View = binding.pregnantBlock.getChildAt(i)
                    child.isEnabled = false
                }

                binding.breastfeedingBlock.isFocusableInTouchMode = false
                binding.breastfeedingBlock.isClickable = false
                binding.breastfeedingBlock.isFocusable = false
                binding.breastfeedingBlock.alpha = 0.50f

                for (i in 0 until binding.breastfeedingBlock.childCount) {
                    val child: View = binding.breastfeedingBlock.getChildAt(i)
                    child.isEnabled = false
                }
            }
        } else {
        }
    }
    

    private fun body() {
        setActive()
        setBreastfeeding()
        setPregnant()
        setBloodDonor()

        binding.activeBlock.setOnClickListener {
            if (spm!!.getBoolean(URLFactory.IS_ACTIVE)) spm!!.savePreferences(URLFactory.IS_ACTIVE, false)
            else spm!!.savePreferences(URLFactory.IS_ACTIVE, true)
            setActive()
        }

        binding.pregnantBlock.setOnClickListener {
            if (spm!!.getBoolean(URLFactory.IS_PREGNANT)) spm!!.savePreferences(
                URLFactory.IS_PREGNANT,
                false
            )
            else spm!!.savePreferences(URLFactory.IS_PREGNANT, true)
            setPregnant()
        }

        binding.breastfeedingBlock.setOnClickListener {
            if (spm!!.getBoolean(URLFactory.IS_BREATFEEDING)) spm!!.savePreferences(
                URLFactory.IS_BREATFEEDING,
                false
            )
            else spm!!.savePreferences(URLFactory.IS_BREATFEEDING, true)
            setBreastfeeding()
        }

        binding.bloodDonorBlock.setOnClickListener {
            if (spm!!.getBoolean(URLFactory.BLOOD_DONOR)) spm!!.savePreferences(
                URLFactory.BLOOD_DONOR,
                false
            )
            else spm!!.savePreferences(URLFactory.BLOOD_DONOR, true)
            setBloodDonor()
        }
    }

    private fun setActive() {
        spm!!.savePreferences(URLFactory.SET_MANUALLY_GOAL, false)

        if (spm!!.getBoolean(URLFactory.IS_ACTIVE)) {
            binding.activeBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgActive.setImageResource(R.drawable.active_selected)
        } else {
            binding.activeBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgActive.setImageResource(R.drawable.active)
        }

        spm!!.savePreferences(URLFactory.SET_WORK_OUT, true)
    }

    private fun setPregnant() {
        spm!!.savePreferences(URLFactory.SET_MANUALLY_GOAL, false)

        if (spm!!.getBoolean(URLFactory.IS_PREGNANT)) {
            binding.pregnantBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgPregnant.setImageResource(R.drawable.pregnant_selected)
        } else {
            binding.pregnantBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgPregnant.setImageResource(R.drawable.pregnant)
        }
    }

    private fun setBreastfeeding() {
        spm!!.savePreferences(URLFactory.SET_MANUALLY_GOAL, false)

        if (spm!!.getBoolean(URLFactory.IS_BREATFEEDING)) {
            binding.breastfeedingBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding_selected)
        } else {
            binding.breastfeedingBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding)
        }
    }

    private fun setBloodDonor() {
        spm!!.savePreferences(URLFactory.SET_MANUALLY_GOAL, false)

        if (spm!!.getBoolean(URLFactory.BLOOD_DONOR)) {
            binding.bloodDonorBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgBloodDonor.setImageResource(R.drawable.ic_donor_selected)
        } else {
            binding.bloodDonorBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgBloodDonor.setImageResource(R.drawable.ic_donor_normal)
        }

        spm!!.savePreferences(URLFactory.SET_BLOOD_DONOR, true)
    }
}