package rpt.tool.mementobibere

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.NavHostFragment
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.lorenzofelletti.permissions.PermissionManager
import com.lorenzofelletti.permissions.dispatcher.dsl.checkPermissions
import com.lorenzofelletti.permissions.dispatcher.dsl.doOnDenied
import com.lorenzofelletti.permissions.dispatcher.dsl.doOnGranted
import com.lorenzofelletti.permissions.dispatcher.dsl.withRequestCode
import rpt.tool.mementobibere.basic.appbasiclibs.BaseAppCompatActivity
import rpt.tool.mementobibere.databinding.ActivityMainBinding
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.extensions.toData
import rpt.tool.mementobibere.utils.extensions.toMigration
import rpt.tool.mementobibere.utils.extensions.toReal
import rpt.tool.mementobibere.utils.extensions.toStringDate
import rpt.tool.mementobibere.utils.extensions.toStringHour
import rpt.tool.mementobibere.utils.extensions.toStringMinute
import rpt.tool.mementobibere.migration.utils.helpers.SqliteHelper
import rpt.tool.mementobibere.migration.utils.managers.OldSharedPreferencesManager
import rpt.tool.mementobibere.utils.log.d
import rpt.tool.mementobibere.utils.log.w
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager


class MainActivity : BaseAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var installStateUpdatedListener: InstallStateUpdatedListener
    private lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var sqliteHelper: SqliteHelper

    private val pm = PermissionManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPermissions()
        initInAppUpdate()
        initConversion()
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activityResultLauncher,
                        AppUpdateOptions.newBuilder(IMMEDIATE).build()
                    )
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        pm.dispatchOnRequestPermissionsResult(requestCode, grantResults)
    }

    private fun initInAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(baseContext)
        registerInAppUpdateResultWatcher()
        registerInstallStateResultWatcher()
        checkUpdates()
    }

    private fun registerInstallStateResultWatcher() {
        installStateUpdatedListener = InstallStateUpdatedListener { installState ->
            when (installState.installStatus()) {
                InstallStatus.DOWNLOADED -> appUpdateManager.completeUpdate()
                InstallStatus.INSTALLED -> appUpdateManager.unregisterListener(
                    installStateUpdatedListener
                )

                else -> {}
            }
        }
        appUpdateManager.registerListener(installStateUpdatedListener)

    }

    private fun registerInAppUpdateResultWatcher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                if (it.resultCode != RESULT_OK) {
                    w("Update flow failed! Result code: " + it.resultCode)
                }
            }
    }

    private fun checkUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)
            ) {

                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(IMMEDIATE).build()
                )
            }
        }
    }


    private fun initPermissions() {
        pm.buildRequestResultsDispatcher {
            withRequestCode(1) {
                checkPermissions(android.Manifest.permission.POST_NOTIFICATIONS)
                doOnGranted {
                    d("Post notification permission granted")
                }
                doOnDenied {
                    d("Post notification permission denied")
                }
            }
        }

        pm checkRequestAndDispatch 1
    }

    private fun initConversion() {
        if(!SharedPreferencesManager.migration){
            sqliteHelper = SqliteHelper(this,this)
            val convert = SharedPreferencesManager.migration
            if(convert){
                covertSharedPref()
            }
            else{
                SharedPreferencesManager.migration = true
            }

        }

    }

    private fun covertSharedPref() {
        val bloodDonorKey = OldSharedPreferencesManager.bloodDonorKey==1
        val setBloodDonor = OldSharedPreferencesManager.setBloodDonor
        val totalIntake = OldSharedPreferencesManager.totalIntake
        val waterUnit = OldSharedPreferencesManager.unitString
        val hideWelcome = OldSharedPreferencesManager.firstRun
        val setGender = OldSharedPreferencesManager.setGender
        val gender = OldSharedPreferencesManager.gender==1
        val setWorkOut = OldSharedPreferencesManager.setWorkOut
        val workOut = OldSharedPreferencesManager.workType==3
        val setClimate = OldSharedPreferencesManager.setClimate
        val climate = OldSharedPreferencesManager.climate
        val setWeight = OldSharedPreferencesManager.setWeight
        var weight = OldSharedPreferencesManager.weight.toString()
        val weightUnit = OldSharedPreferencesManager.weightUnit == 0
        val notificationStatus = OldSharedPreferencesManager.notificationStatus
        val interval = OldSharedPreferencesManager.notificationFreq
        val sleeping = OldSharedPreferencesManager.sleepingTime
        val wake = OldSharedPreferencesManager.wakeUpTime

        if(weight == "0"){
            weight = if(weightUnit) "80" else "176"
        }

        SharedPreferencesManager.setManuallyGoal = false
        SharedPreferencesManager.bloodDonorKey = bloodDonorKey
        SharedPreferencesManager.setBloodDonor = setBloodDonor
        SharedPreferencesManager.dailyWater = totalIntake
        SharedPreferencesManager.waterUnit = waterUnit.toReal()
        URLFactory.DAILY_WATER_VALUE = totalIntake
        URLFactory.WATER_UNIT_VALUE = waterUnit.toReal()
        SharedPreferencesManager.hideWelcomeScreen = hideWelcome
        SharedPreferencesManager.userGender = gender
        SharedPreferencesManager.setGender = setGender
        SharedPreferencesManager.isActive = workOut
        SharedPreferencesManager.setWorkOut = setWorkOut
        SharedPreferencesManager.climate = climate.toMigration()
        SharedPreferencesManager.setClimate = setClimate
        SharedPreferencesManager.personWeight = weight
        SharedPreferencesManager.setWeight = setWeight
        SharedPreferencesManager.weightUnit = weightUnit
        SharedPreferencesManager.disableNotification = notificationStatus
        SharedPreferencesManager.notificationFreq = interval

        val wakeUp = wake.toData()
        val bed = sleeping.toData()

        SharedPreferencesManager.wakeUpTime = wakeUp.toStringDate()
        SharedPreferencesManager.sleepingTime = bed.toStringDate()
        SharedPreferencesManager.wakeUpTimeHour = wakeUp.toStringHour().toInt()
        SharedPreferencesManager.wakeUpTimeMinute = wakeUp.toStringMinute().toInt()
        SharedPreferencesManager.sleepTimeHour = bed.toStringHour().toInt()
        SharedPreferencesManager.sleepTimeMinute = bed.toStringMinute().toInt()

        val settings: SharedPreferences =
            this.getSharedPreferences("user_pref", MODE_PRIVATE)
        settings.edit().clear().apply()

        SharedPreferencesManager.migration = true

    }

    override fun onNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_activity_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}








