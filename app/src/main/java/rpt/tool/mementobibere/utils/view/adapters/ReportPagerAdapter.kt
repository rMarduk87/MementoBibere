package rpt.tool.mementobibere.utils.view.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.ui.statistics.month.StatsMonthFragment
import rpt.tool.mementobibere.ui.statistics.week.StatsWeekFragment
import rpt.tool.mementobibere.ui.statistics.year.StatsYearFragment

class ReportPagerAdapter(fm: FragmentManager?, context: Context) : FragmentStatePagerAdapter(fm!!) {
    var tab1Fragment: StatsWeekFragment = StatsWeekFragment()
    var tab2Fragment: StatsMonthFragment = StatsMonthFragment()
    var tab3Fragment: StatsYearFragment = StatsYearFragment()

    var mContext: Context = context

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                tab1Fragment
            }
            1 -> {
                tab2Fragment
            }
            else -> {
                tab3Fragment
            }
        }
    }


    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when (position) {
            0 -> {
                title = mContext.resources.getString(R.string.str_week)
            }
            1 -> {
                title = mContext.resources.getString(R.string.str_month)
            }
            2 -> {
                title = mContext.resources.getString(R.string.str_year)
            }
        }
        return title
    }
}
