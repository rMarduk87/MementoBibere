package rpt.tool.mementobibere.ui.userinfo.info

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.FragmentUserInfoStatusBinding
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager

class UserInfoStatusFragment : BaseFragment<FragmentUserInfoStatusBinding>(FragmentUserInfoStatusBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {

            if (SharedPreferencesManager.isPregnant) {
                binding.pregnantBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
                binding.imgPregnant.setImageResource(R.drawable.pregnant_selected)
            } else {
                binding.pregnantBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
                binding.imgPregnant.setImageResource(R.drawable.pregnant)
            }

            if (SharedPreferencesManager.isBreastfeeding) {
                binding.breastfeedingBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
                binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding_selected)
            } else {
                binding.breastfeedingBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
                binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding)
            }
            
            if (SharedPreferencesManager.userGender) // female
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
        } 
    }
    

    private fun body() {
        setActive()
        setBreastfeeding()
        setPregnant()
        setBloodDonor()

        binding.activeBlock.setOnClickListener {
            if (SharedPreferencesManager.isActive) SharedPreferencesManager.isActive = false
            SharedPreferencesManager.isActive = true
            setActive()
        }

        binding.pregnantBlock.setOnClickListener {
            if (SharedPreferencesManager.isPregnant)
                SharedPreferencesManager.isPregnant = false
            else SharedPreferencesManager.isPregnant = true
            setPregnant()
        }

        binding.breastfeedingBlock.setOnClickListener {
            if (SharedPreferencesManager.isBreastfeeding) 
                SharedPreferencesManager.isBreastfeeding = false
            else SharedPreferencesManager.isBreastfeeding = true
            setBreastfeeding()
        }

        binding.bloodDonorBlock.setOnClickListener {
            if (SharedPreferencesManager.bloodDonorKey) 
                SharedPreferencesManager.bloodDonorKey = false
            else SharedPreferencesManager.bloodDonorKey = true
            setBloodDonor()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setActive() {
        SharedPreferencesManager.setManuallyGoal = false

        if (SharedPreferencesManager.isActive) {
            binding.activeBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgActive.setImageResource(R.drawable.active_selected)
        } else {
            binding.activeBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgActive.setImageResource(R.drawable.active)
        }
        SharedPreferencesManager.setWorkOut = true
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setPregnant() {
        SharedPreferencesManager.setManuallyGoal = false


        if (SharedPreferencesManager.isPregnant) {
            binding.pregnantBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgPregnant.setImageResource(R.drawable.pregnant_selected)
        } else {
            binding.pregnantBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgPregnant.setImageResource(R.drawable.pregnant)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setBreastfeeding() {
        SharedPreferencesManager.setManuallyGoal = false


        if (SharedPreferencesManager.isBreastfeeding) {
            binding.breastfeedingBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding_selected)
        } else {
            binding.breastfeedingBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgBreastfeeding.setImageResource(R.drawable.breastfeeding)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setBloodDonor() {
        SharedPreferencesManager.setManuallyGoal = false


        if (SharedPreferencesManager.bloodDonorKey) {
            binding.bloodDonorBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            binding.imgBloodDonor.setImageResource(R.drawable.ic_donor_selected)
        } else {
            binding.bloodDonorBlock.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            binding.imgBloodDonor.setImageResource(R.drawable.ic_donor_normal)
        }

        SharedPreferencesManager.setBloodDonor = true
    }
}