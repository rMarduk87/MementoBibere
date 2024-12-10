package rpt.tool.mementobibere.ui.drink.dialogs

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import rpt.tool.mementobibere.BaseDialogFragment
import rpt.tool.mementobibere.databinding.DialogBatteryOptimizationBinding
import rpt.tool.mementobibere.utils.view.adapters.MyPageAdapter

class PermissionDialogFragment: BaseDialogFragment<DialogBatteryOptimizationBinding>(DialogBatteryOptimizationBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myPageAdapter = MyPageAdapter(requireActivity())
        binding.viewPager.adapter = myPageAdapter

        binding.viewPager.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.viewPager.offscreenPageLimit = 5
        binding.dotsIndicator.attachTo(binding.viewPager)

        binding.imgCancel.setOnClickListener { dialog!!.dismiss() }

        binding.btnSettings.setOnClickListener {
            dialog!!.dismiss()
            val intent =
                Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
            startActivity(intent)
        }

    }
}