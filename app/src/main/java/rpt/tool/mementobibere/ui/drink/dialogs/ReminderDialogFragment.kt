package rpt.tool.mementobibere.ui.drink.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import rpt.tool.mementobibere.BaseDialogFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.DialogReminderBinding
import rpt.tool.mementobibere.ui.drink.DrinkFragmentDirections
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import rpt.tool.mementobibere.utils.navigation.safeNavController
import rpt.tool.mementobibere.utils.navigation.safeNavigate

class ReminderDialogFragment:BaseDialogFragment<DialogReminderBinding>(DialogReminderBinding::inflate) {


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.advanceSettings.setOnClickListener {
            dialog!!.dismiss()
            safeNavController?.safeNavigate(
                DrinkFragmentDirections.actionDrinkFragmentToReminderFragment()
            )
        }
        
        binding.customSoundBlock.setOnClickListener { openSoundMenuPicker() }
        
        binding.switchVibrate.isChecked = !SharedPreferencesManager.reminderVibrate

        binding.switchVibrate.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesManager.reminderVibrate = !isChecked
        }

        if (SharedPreferencesManager.reminderOpt == 1) {
            binding.offBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_selected)
            binding.imgOff.setImageResource(R.drawable.ic_off_selected)
            
            binding.silentBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgSilent.setImageResource(R.drawable.ic_silent_normal)
            
            binding.autoBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgAuto.setImageResource(R.drawable.ic_auto_normal)
        } else if (SharedPreferencesManager.reminderOpt == 2) {
            binding.offBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgOff.setImageResource(R.drawable.ic_off_normal)

            binding.silentBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_selected)
            binding.imgSilent .setImageResource(R.drawable.ic_silent_selected)

            binding.autoBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgAuto.setImageResource(R.drawable.ic_auto_normal)
        } else {
            binding.offBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgOff.setImageResource(R.drawable.ic_off_normal)

            binding.silentBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgSilent .setImageResource(R.drawable.ic_silent_normal)

            binding.autoBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_selected)
            binding.imgAuto.setImageResource(R.drawable.ic_auto_selected)
        }

        binding.offBlock.setOnClickListener {
            binding.offBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_selected)
            binding.imgOff.setImageResource(R.drawable.ic_off_selected)

            binding.silentBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgSilent .setImageResource(R.drawable.ic_silent_normal)

            binding.autoBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgAuto.setImageResource(R.drawable.ic_auto_normal)
            SharedPreferencesManager.reminderOpt = 1
        }

        binding.silentBlock.setOnClickListener {
            binding.offBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgOff.setImageResource(R.drawable.ic_off_normal)

            binding.silentBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_selected)
            binding.imgSilent .setImageResource(R.drawable.ic_silent_selected)

            binding.autoBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgAuto.setImageResource(R.drawable.ic_auto_normal)
            SharedPreferencesManager.reminderOpt = 2
        }

        binding.autoBlock.setOnClickListener {
            binding.offBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgOff.setImageResource(R.drawable.ic_off_normal)

            binding.silentBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_unselected)
            binding.imgSilent .setImageResource(R.drawable.ic_silent_normal)

            binding.autoBlock.background =
                requireContext().resources.getDrawable(R.drawable.drawable_circle_selected)
            binding.imgAuto.setImageResource(R.drawable.ic_auto_selected)
            SharedPreferencesManager.reminderOpt = 0
        }

        binding.imgCancel.setOnClickListener { dialog!!.dismiss() }

        binding.btnSave.setOnClickListener { dialog!!.dismiss() }
    }

    private fun openSoundMenuPicker() {
        safeNavController?.safeNavigate(ReminderDialogFragmentDirections.
        actionReminderDialogFragmentToOpenSoundPickerDialogFragment()
        )
    }
}