package rpt.tool.mementobibere

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import rpt.tool.mementobibere.databinding.ActivityInitUserInfoBinding
import rpt.tool.mementobibere.utils.AppUtils


@Suppress("DEPRECATION")
class InitUserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInitUserInfoBinding
    private var themeInt : Int = 0
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        themeInt = sharedPref.getInt(AppUtils.THEME_KEY,0)
        super.onCreate(savedInstanceState)
        binding = ActivityInitUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.init_user_info_activity_nav_host_fragment)
                    as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.init_user_info_activity_nav_host_fragment)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }

}