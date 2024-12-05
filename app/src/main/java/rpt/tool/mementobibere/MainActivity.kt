package rpt.tool.mementobibere

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import rpt.tool.mementobibere.migration.utils.SharedPreferencesManager
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.extensions.toData
import rpt.tool.mementobibere.utils.extensions.toMigration
import rpt.tool.mementobibere.utils.extensions.toReal
import rpt.tool.mementobibere.utils.extensions.toStringDate
import rpt.tool.mementobibere.utils.extensions.toStringHour
import rpt.tool.mementobibere.utils.extensions.toStringMinute
import rpt.tool.mementobibere.utils.helpers.SqliteHelper
import rpt.tool.mementobibere.utils.log.d
import rpt.tool.mementobibere.utils.log.w


class MainActivity : BaseAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var installStateUpdatedListener: InstallStateUpdatedListener
    private lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var sqliteHelper: SqliteHelper

    private val pm = PermissionManager(this)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun initPermissions() {
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
        sqliteHelper = SqliteHelper(this,this)
        sqliteHelper.start(ph!!.getBoolean(URLFactory.MIGRATION))
        covertSharedPref()
    }

    private fun covertSharedPref() {
        if(!ph!!.getBoolean(URLFactory.MIGRATION)){
            var bloodDonorKey = SharedPreferencesManager.bloodDonorKey==1
            var setBloodDonor = SharedPreferencesManager.setBloodDonor
            var totalIntake = SharedPreferencesManager.totalIntake
            var waterUnit = SharedPreferencesManager.unitString
            var hideWelcome = SharedPreferencesManager.firstRun
            var setGender = SharedPreferencesManager.setGender
            var gender = SharedPreferencesManager.gender==1
            var setWorkOut = SharedPreferencesManager.setWorkOut
            var workOut = SharedPreferencesManager.workType==3
            var setClimate = SharedPreferencesManager.setClimate
            var climate = SharedPreferencesManager.climate
            var setWeight = SharedPreferencesManager.setWeight
            var weight = SharedPreferencesManager.weight.toString()
            var weightUnit = SharedPreferencesManager.weightUnit == 0
            var notificationStatus = SharedPreferencesManager.notificationStatus
            var interval = SharedPreferencesManager.notificationFreq
            var sleeping = SharedPreferencesManager.sleepingTime
            var wake = SharedPreferencesManager.wakeUpTime

            if(weight == "0"){
                weight = if(weightUnit) "80" else "176"
            }

            ph!!.savePreferences(URLFactory.SET_MANUALLY_GOAL, false)

            ph!!.savePreferences(
                URLFactory.BLOOD_DONOR,bloodDonorKey)

            ph!!.savePreferences(
                URLFactory.SET_BLOOD_DONOR,setBloodDonor)

            ph!!.savePreferences(URLFactory.DAILY_WATER,totalIntake)
            ph!!.savePreferences(URLFactory.WATER_UNIT,waterUnit.toReal())
            URLFactory.DAILY_WATER_VALUE = totalIntake
            URLFactory.WATER_UNIT_VALUE = waterUnit.toReal()
            ph!!.savePreferences(URLFactory.HIDE_WELCOME_SCREEN,hideWelcome)
            ph!!.savePreferences(
                URLFactory.USER_GENDER,gender)

            ph!!.savePreferences(
                URLFactory.SET_USER_GENDER,setGender)

            ph!!.savePreferences(
                URLFactory.IS_ACTIVE,workOut)

            ph!!.savePreferences(
                URLFactory.SET_WORK_OUT,setWorkOut)

            ph!!.savePreferences(
                URLFactory.WEATHER_CONSITIONS,climate.toMigration())

            ph!!.savePreferences(
                URLFactory.SET_CLIMATE,setClimate)

            ph!!.savePreferences(
                URLFactory.PERSON_WEIGHT,weight)

            ph!!.savePreferences(
                URLFactory.SET_WEIGHT,setWeight)

            ph!!.savePreferences(
                URLFactory.PERSON_WEIGHT_UNIT,weightUnit)

            ph!!.savePreferences(
                URLFactory.DISABLE_NOTIFICATION,notificationStatus)

            ph!!.savePreferences(
                URLFactory.INTERVAL,interval)

            var wakeUp = wake.toData()
            var bed = sleeping.toData()


            ph!!.savePreferences(URLFactory.WAKE_UP_TIME,wakeUp.toStringDate())
            ph!!.savePreferences(URLFactory.BED_TIME,bed.toStringDate())
            ph!!.savePreferences(URLFactory.WAKE_UP_TIME_HOUR, wakeUp.toStringHour())
            ph!!.savePreferences(URLFactory.WAKE_UP_TIME_MINUTE, wakeUp.toStringMinute())
            ph!!.savePreferences(URLFactory.BED_TIME_HOUR, bed.toStringHour())
            ph!!.savePreferences(URLFactory.BED_TIME_MINUTE, bed.toStringMinute())

            val settings: SharedPreferences =
                this.getSharedPreferences("user_pref", MODE_PRIVATE)
            settings.edit().clear().commit()

            ph!!.savePreferences(URLFactory.MIGRATION,true)
        }

    }

    override fun onNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_activity_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}








