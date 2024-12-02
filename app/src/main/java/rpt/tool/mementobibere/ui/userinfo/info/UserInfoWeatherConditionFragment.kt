package rpt.tool.mementobibere.ui.userinfo.info

import android.os.Bundle
import android.view.View
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.FragmentUserInfoWeatherConditionBinding
import rpt.tool.mementobibere.utils.URLFactory

class UserInfoWeatherConditionFragment : 
    BaseFragment<FragmentUserInfoWeatherConditionBinding>(FragmentUserInfoWeatherConditionBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()
    }

    private fun body() {
        setWeather(ph!!.getInt(URLFactory.WEATHER_CONSITIONS))

        binding.sunnyBlock.setOnClickListener{ setWeather(0) }

        binding.cloudyBlock.setOnClickListener{ setWeather(1) }

        binding.rainyBlock.setOnClickListener{ setWeather(2) }

        binding.snowBlock.setOnClickListener{ setWeather(3) }
    }

    private fun setWeather(idx: Int) {
        ph!!.savePreferences(URLFactory.SET_MANUALLY_GOAL, false)

        ph!!.savePreferences(URLFactory.WEATHER_CONSITIONS, idx)

        binding.sunnyBlock.background = if (idx == 0) requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
        else requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
        binding.imgSunny.setImageResource(if (idx == 0) R.drawable.sunny_selected else R.drawable.sunny)

        binding.cloudyBlock.background = if (idx == 1) requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
        else requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
        binding.imgCloudy.setImageResource(if (idx == 1) R.drawable.cloudy_selected else R.drawable.cloudy)

        binding.rainyBlock.background = if (idx == 2) requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
        else requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
        binding.imgRainy.setImageResource(if (idx == 2) R.drawable.rainy_selected else R.drawable.rainy)

        binding.snowBlock.background = if (idx == 3) requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
        else requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
        binding.imgSnow.setImageResource(if (idx == 3) R.drawable.snow_selected else R.drawable.snow)

        ph!!.savePreferences(URLFactory.SET_CLIMATE, true)

    }
}