package rpt.tool.mementobibere.utils.view.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import rpt.tool.mementobibere.ui.userinfo.info.UserInfoFinalFragment
import rpt.tool.mementobibere.ui.userinfo.info.UserInfoGenderChoiceFragment
import rpt.tool.mementobibere.ui.userinfo.info.UserInfoGoalCalculationFragment
import rpt.tool.mementobibere.ui.userinfo.info.UserInfoRegistryNotification
import rpt.tool.mementobibere.ui.userinfo.info.UserInfoStatusFragment
import rpt.tool.mementobibere.ui.userinfo.info.UserInfoWeatherConditionFragment
import rpt.tool.mementobibere.ui.userinfo.info.UserInfoWeightAndHeightFragment

class UserInfoPagerAdapter(fm: FragmentManager?, context: Context) :
    FragmentStatePagerAdapter(fm!!) {
    var tab2Fragment: UserInfoGenderChoiceFragment = UserInfoGenderChoiceFragment()
    var tab3Fragment: UserInfoWeightAndHeightFragment = UserInfoWeightAndHeightFragment()
    var tab4Fragment: UserInfoGoalCalculationFragment = UserInfoGoalCalculationFragment()
    var tab5Fragment: UserInfoFinalFragment = UserInfoFinalFragment()
    var tab6Fragment: UserInfoRegistryNotification = UserInfoRegistryNotification()
    var tab7Fragment: UserInfoStatusFragment = UserInfoStatusFragment()
    var tab8Fragment: UserInfoWeatherConditionFragment = UserInfoWeatherConditionFragment()

    var mContext: Context = context
    override fun getCount(): Int {
        return 7
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> {
                tab3Fragment
            }
            2 -> {
                tab7Fragment
            }
            3 -> {
                tab8Fragment
            }
            4 -> {
                tab4Fragment
            }
            5 -> {
                tab6Fragment
            }
            6 -> {
                tab5Fragment
            }
            else -> {
                tab2Fragment
            }
        }
    }

}