package rpt.tool.mementobibere.ui.statistics

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.FragmentStatsBinding
import rpt.tool.mementobibere.utils.custom.NonSwipeableViewPager
import rpt.tool.mementobibere.utils.navigation.safeNavController
import rpt.tool.mementobibere.utils.navigation.safeNavigate
import rpt.tool.mementobibere.utils.view.adapters.ReportPagerAdapter


class StatsFragment : BaseFragment<FragmentStatsBinding>(FragmentStatsBinding::inflate){

    var viewPager: NonSwipeableViewPager? = null
    var reportPagerAdapter: ReportPagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        body()
    }

    private fun body() {

        binding.rdoWeek.setOnClickListener{ viewPager!!.currentItem = 0 }
        binding.rdoMonth.setOnClickListener{ viewPager!!.currentItem = 1 }
        binding.rdoYear.setOnClickListener{ viewPager!!.currentItem = 2 }

        binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_drink_report)
        binding.include1.leftIconBlock.setOnClickListener{ finish() }
        binding.include1.rightIconBlock.visibility = View.GONE

        binding.tabs.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        reportPagerAdapter = ReportPagerAdapter(requireActivity().supportFragmentManager,
            requireContext())
        viewPager!!.adapter = reportPagerAdapter
        viewPager!!.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        viewPager!!.offscreenPageLimit = 5

        binding.tabs.setupWithViewPager(viewPager)
    }

    private fun finish() {
        safeNavController?.safeNavigate(StatsFragmentDirections
            .actionStatsFragmentToDrinkFragment())
    }
}