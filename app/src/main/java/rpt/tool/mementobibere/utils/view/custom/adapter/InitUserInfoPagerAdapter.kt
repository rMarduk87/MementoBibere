package rpt.tool.mementobibere.utils.view.custom.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import rpt.tool.mementobibere.java.userinfo.fragment.InitUserInfo_Eight
import rpt.tool.mementobibere.java.userinfo.fragment.InitUserInfo_Five
import rpt.tool.mementobibere.java.userinfo.fragment.InitUserInfo_Four
import rpt.tool.mementobibere.java.userinfo.fragment.InitUserInfo_Nine
import rpt.tool.mementobibere.java.userinfo.fragment.InitUserInfo_Seven
import rpt.tool.mementobibere.java.userinfo.fragment.InitUserInfo_Six
import rpt.tool.mementobibere.java.userinfo.fragment.InitUserInfo_Three
import rpt.tool.mementobibere.java.userinfo.fragment.InitUserInfo_Two


class InitUserInfoPagerAdapter(fm: FragmentManager?, context: Context) :
    FragmentStatePagerAdapter(fm!!) {
    var tab2Fragment: InitUserInfo_Two
    var tab3Fragment: InitUserInfo_Three
    var tab4Fragment: InitUserInfo_Four
    var tab5Fragment: InitUserInfo_Five
    var tab6Fragment: InitUserInfo_Six
    var tab7Fragment: InitUserInfo_Seven
    var tab8Fragment: InitUserInfo_Eight
    var tab9Fragment: InitUserInfo_Nine
    var mContext: Context

    init {
        tab2Fragment = InitUserInfo_Two()
        tab3Fragment = InitUserInfo_Three()
        tab4Fragment = InitUserInfo_Four()
        tab5Fragment = InitUserInfo_Five()
        tab6Fragment = InitUserInfo_Six()
        tab7Fragment = InitUserInfo_Seven()
        tab8Fragment = InitUserInfo_Eight()
        tab9Fragment = InitUserInfo_Nine()
        mContext = context
    }

    override fun getItem(position: Int): Fragment {
        var returned:Fragment = tab3Fragment
        if (position == 1) {
            returned = tab3Fragment
        } else if (position == 2) {
            returned = tab7Fragment
        } else if (position == 3) {
            returned = tab8Fragment
        }else if (position == 4) {
            returned = tab9Fragment
        } else if (position == 5) {
            returned = tab4Fragment
        } else if (position == 6) {
            returned = tab6Fragment
        } else if (position == 7) {
            returned = tab5Fragment
        } else {
            returned = tab2Fragment
        }
        return returned

    }

    override fun getCount(): Int {
        return 8
    }
}