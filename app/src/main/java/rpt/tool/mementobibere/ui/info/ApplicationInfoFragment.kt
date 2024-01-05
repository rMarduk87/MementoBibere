package rpt.tool.mementobibere.ui.info

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.LottieAnimationView
import github.com.st235.lib_expandablebottombar.MenuItemDescriptor
import rpt.tool.mementobibere.databinding.ApplicationInfoFragmentBinding
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.PartialPrincipalInfoBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.extensions.toCalculatedValue
import rpt.tool.mementobibere.utils.extensions.toMonth
import rpt.tool.mementobibere.utils.extensions.toNumberString
import rpt.tool.mementobibere.utils.extensions.toYear
import rpt.tool.mementobibere.utils.helpers.AlarmHelper
import rpt.tool.mementobibere.utils.helpers.SqliteHelper
import rpt.tool.mementobibere.utils.navigation.safeNavController
import rpt.tool.mementobibere.utils.navigation.safeNavigate
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Locale

class ApplicationInfoFragment:
    BaseFragment<ApplicationInfoFragmentBinding>(ApplicationInfoFragmentBinding::inflate) {

    private var stringColor: String = ""
    private var inTook: Float = 0f
    private lateinit var sqliteHelper: SqliteHelper
    private lateinit var principal : PartialPrincipalInfoBinding
    private lateinit var sharedPref: SharedPreferences
    private var themeInt : Int = 0
    private var unit : Int = 0
    private var currentToneUri: String? = ""
    private var notificMsg: String = ""
    private var notificFrequency: Int = 45
    private var splash : Boolean = true
    private var tips : Boolean = true
    private var weight: String = ""
    private var workType: Int = -1
    private var customTarget: String = ""
    private var wakeupTime: Long = 0
    private var sleepingTime: Long = 0
    private var currentUnitint: Int = 0
    private var newUnitint: Int = 0
    private var weightUnit: Int = 0
    private var oldWeight: Int = 0
    private var oldWorkTime: Int = 0
    private var genderChoice : Int = -1
    private var genderChoiceOld : Int = -1
    private var climate: Int = -1
    private var climateOld: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = requireActivity().getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        themeInt = sharedPref.getInt(AppUtils.THEME_KEY,0)
        super.onViewCreated(view, savedInstanceState)
        principal = PartialPrincipalInfoBinding.bind(binding.root)
        val htmlLegacy = "https://www.termsfeed.com/live/d1615b20-2bc9-4048-8b73-b674c2aeb1c5"

        stringColor = when(themeInt){
            0->"#41B279"
            1->"#29704D"
            2->"#6A91DE"
            3->"#FF6200EE"
            else -> {"#41B279"}
        }

        setLayout()
        initBottomBars()

        android.text.format.DateFormat.is24HourFormat(requireContext())

        principal.etPrivacy.editText!!.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(htmlLegacy))
            startActivity(browserIntent)
        }

        binding.principal.etNotificationText.editText!!.setText(
            sharedPref.getString(
                AppUtils.NOTIFICATION_MSG_KEY,
                getString(R.string.hey_lets_drink_some_water)
            )
        )

        currentToneUri = sharedPref.getString(
            AppUtils.NOTIFICATION_TONE_URI_KEY,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
        )

        binding.principal.etRingtone.editText!!.setText(
            RingtoneManager.getRingtone(
                requireContext(),
                Uri.parse(currentToneUri)
            ).getTitle(requireContext())
        )

        binding.principal.etRingtone.editText!!.setOnClickListener {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
            intent.putExtra(
                RingtoneManager.EXTRA_RINGTONE_TITLE,
                getString(R.string.select_ringtone_for_notifications)
            )
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentToneUri)
            startActivityForResult(intent, 999)
        }
        val editor = sharedPref.edit()

        if(sharedPref.contains(AppUtils.START_TIME_KEY)){
            editor.remove(AppUtils.START_TIME_KEY)
        }

        if(sharedPref.contains(AppUtils.STOP_TIME_KEY)){
            editor.remove(AppUtils.STOP_TIME_KEY)
        }
        editor.apply()

        manageUserInfo()

        binding.principal.btnUpdate.setOnClickListener {
            notificMsg = binding.principal.etNotificationText.editText!!.text.toString()
            if(TextUtils.isEmpty(notificMsg))
            {
                showMessage(getString(R.string.please_a_notification_message), it, true)
            }
            else{
                var editor = sharedPref.edit()
                editor.putString(AppUtils.NOTIFICATION_MSG_KEY, notificMsg)
                editor.putString(AppUtils.NOTIFICATION_TONE_URI_KEY,currentToneUri.toString())
                editor.apply()
                saveUserInfo()
                safeNavController?.safeNavigate(
                    ApplicationInfoFragmentDirections.actionInfoFragmentToDrinkFragment())
            }
        }
    }

    private fun saveUserInfo() {
        val currentTarget = sharedPref.getFloat(AppUtils.TOTAL_INTAKE_KEY, 0f)
        weight = binding.principal.etWeight.editText!!.text.toString()
        customTarget = binding.principal.etTarget.editText!!.text.toString()

        val editor = sharedPref.edit()
        val sqliteHelper = SqliteHelper(requireContext())

        if(genderChoice==-1){
            showMessage(getString(R.string.gender_hint))
        }

        if(weight != oldWeight.toString() ||
            workType != oldWorkTime || genderChoice != genderChoiceOld || climate != climateOld) {
            val totalIntake = AppUtils.calculateIntake(
                weight.toInt(),
                workType,
                weightUnit, genderChoice, climate
            )
            val df = DecimalFormat("#")
            df.roundingMode = RoundingMode.CEILING
            editor.putFloat(AppUtils.TOTAL_INTAKE_KEY, df.format(totalIntake).toFloat())

            editor.apply()

            sqliteHelper.updateTotalIntake(
                AppUtils.getCurrentOnlyDate()!!,
                df.format(totalIntake).toFloat(), AppUtils.calculateExtensions(newUnitint)
            )
        }
        when {
            TextUtils.isEmpty(weight) -> showMessage(getString(R.string.please_input_your_weight))

            weight.toInt() > AppUtils.getMaxWeight(weightUnit) || weight.toInt() < AppUtils.getMinWeight(weightUnit) ->
                showMessage(getString(R.string.please_input_a_valid_weight))

            TextUtils.isEmpty(customTarget) -> showMessage(getString(R.string.please_input_your_custom_target))
            !AppUtils.isValidDate(binding.principal.etSleepTime.editText!!.text.toString(),
                binding.principal.etWakeUpTime.editText!!.text.toString()) ->
                showMessage(getString(R.string.please_input_a_valid_rest_time))
            else -> {

                editor.putInt(AppUtils.WEIGHT_KEY, weight.toInt())
                editor.putInt(AppUtils.WORK_TIME_KEY, workType)
                editor.putLong(AppUtils.WAKEUP_TIME_KEY, wakeupTime)
                editor.putLong(AppUtils.SLEEPING_TIME_KEY, sleepingTime)
                editor.putInt(AppUtils.GENDER_KEY,genderChoice)
                editor.putInt(AppUtils.CLIMATE_KEY,climate)

                if (currentTarget != customTarget.toFloat()) {
                    editor.putFloat(AppUtils.TOTAL_INTAKE_KEY, customTarget.toFloat())

                    sqliteHelper.updateTotalIntake(
                        AppUtils.getCurrentOnlyDate()!!,
                        customTarget.toFloat(), AppUtils.calculateExtensions(newUnitint)
                    )
                } else {
                    val totalIntake = AppUtils.calculateIntake(
                        weight.toInt(),
                        workType,
                        weightUnit, genderChoice, climate
                    )
                    val df = DecimalFormat("#")
                    df.roundingMode = RoundingMode.CEILING
                    editor.putFloat(AppUtils.TOTAL_INTAKE_KEY, df.format(totalIntake).toFloat())

                    sqliteHelper.updateTotalIntake(
                        AppUtils.getCurrentOnlyDate()!!,
                        df.format(totalIntake).toFloat(), AppUtils.calculateExtensions(newUnitint)
                    )
                }

                editor.putBoolean(AppUtils.NO_UPDATE_UNIT,true)

                editor.apply()

                Toast.makeText(requireContext(), getString(rpt.tool.mementobibere.R.string.values_updated_successfully), Toast.LENGTH_SHORT).show()
                var alarm = AlarmHelper()
                alarm.cancelAlarm(requireContext())
            }
        }
    }

    private fun manageUserInfo() {
        val is24h = android.text.format.DateFormat.is24HourFormat(requireContext())

        sharedPref = requireContext().getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)

        binding.principal.etWeight.editText!!.setText("" + sharedPref.getInt(AppUtils.WEIGHT_KEY, 0))
        binding.principal.etTarget.editText!!.setText("" + sharedPref.getFloat(AppUtils.TOTAL_INTAKE_KEY, 0f).toNumberString())

        workType = sharedPref.getInt(AppUtils.WORK_TIME_KEY, 0)

        var text = when(workType){
            0->getString(R.string.calm)
            1->getString(R.string.normal)
            2->getString(R.string.lively)
            3->getString(R.string.intense)
            else -> {getString(R.string.calm)}
        }

        binding.principal.etWorkType.editText!!.setText(text)

        climate = sharedPref.getInt(AppUtils.CLIMATE_KEY, -1)

        var textC = when(climate){
            0->getString(R.string.cold)
            1->getString(R.string.fresh)
            2->getString(R.string.mild)
            3->getString(R.string.torrid)
            else -> {getString(R.string.cold)}
        }

        binding.principal.etClimate.editText!!.setText(textC)

        oldWeight = sharedPref.getInt(AppUtils.WEIGHT_KEY, 0)
        oldWorkTime = sharedPref.getInt(AppUtils.WORK_TIME_KEY, 0)

        currentUnitint = sharedPref.getInt(AppUtils.UNIT_KEY,0)
        newUnitint = sharedPref.getInt(AppUtils.UNIT_NEW_KEY,0)
        weightUnit = sharedPref.getInt(AppUtils.WEIGHT_UNIT_KEY,0)
        themeInt = sharedPref.getInt(AppUtils.THEME_KEY,0)
        genderChoiceOld = sharedPref.getInt(AppUtils.GENDER_KEY, -1)
        climateOld = sharedPref.getInt(AppUtils.CLIMATE_KEY, -1)

        val unit = AppUtils.calculateExtensions(newUnitint)
        if(Locale.getDefault().language == "de"){
            binding.principal.etTarget.hint = getString(R.string.custom_intake_hint) + " " + unit + " ein"
        }
        else{
            binding.principal.etTarget.hint = getString(R.string.custom_intake_hint) + " " + unit
        }


        wakeupTime = sharedPref.getLong(AppUtils.WAKEUP_TIME_KEY, 1558323000000)
        sleepingTime = sharedPref.getLong(AppUtils.SLEEPING_TIME_KEY, 1558369800000)
        val cal = Calendar.getInstance()
        cal.timeInMillis = wakeupTime
        binding.principal.etWakeUpTime.editText!!.setText(
            String.format(
                "%02d:%02d",
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE)
            )
        )
        cal.timeInMillis = sleepingTime
        binding.principal.etSleepTime.editText!!.setText(
            String.format(
                "%02d:%02d",
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE)
            )
        )

        binding.principal.etWakeUpTime.editText!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = wakeupTime

            val mTimePicker = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->

                    val time = Calendar.getInstance()
                    time.set(Calendar.HOUR_OF_DAY, selectedHour)
                    time.set(Calendar.MINUTE, selectedMinute)
                    wakeupTime = time.timeInMillis

                    binding.principal.etWakeUpTime.editText!!.setText(
                        String.format("%02d:%02d", selectedHour, selectedMinute)
                    )
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24h
            )
            mTimePicker.setTitle(getString(R.string.select_wakeup_time))
            mTimePicker.show()
        }


        binding.principal.etSleepTime.editText!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = sleepingTime

            val mTimePicker = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->

                    val time = Calendar.getInstance()
                    time.set(Calendar.HOUR_OF_DAY, selectedHour)
                    time.set(Calendar.MINUTE, selectedMinute)
                    sleepingTime = time.timeInMillis

                    binding.principal.etSleepTime.editText!!.setText(
                        String.format("%02d:%02d", selectedHour, selectedMinute)
                    )
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24h
            )
            mTimePicker.setTitle(getString(R.string.select_sleeping_time))
            mTimePicker.show()
        }

        genderChoice = sharedPref.getInt(AppUtils.GENDER_KEY,-1)
        when(genderChoice){
            0->{
                binding.principal.textView18.text = " (" + getString(R.string.man) + ")"
                binding.principal.textView18.setTextColor(resources.getColor(R.color.colorSkyBlue))
            }

            1->{
                binding.principal.textView18.text = " (" + getString(R.string.woman) + ")"
                binding.principal.textView18.setTextColor(resources.getColor(R.color.pink))
            }
        }

        binding.principal.btnMan.setOnClickListener {
            genderChoice = 0
            showMessage(getString(R.string.you_selected_man),false)
            binding.principal.textView18.text = " (" + getString(R.string.man) + ")"
            binding.principal.textView18.setTextColor(resources.getColor(R.color.colorSkyBlue))
        }

        binding.principal.btnWoman.setOnClickListener {
            genderChoice = 1
            showMessage(getString(R.string.you_selected_woman),false)
            binding.principal.textView18.text = " (" + getString(R.string.woman) + ")"
            binding.principal.textView18.setTextColor(resources.getColor(R.color.pink))
        }

        binding.principal.etWorkType.editText!!.setOnClickListener {

            val li = LayoutInflater.from(requireContext())
            val promptsView = li.inflate(R.layout.custom_input_dialog4, null)

            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(promptsView)


            val btnCalm = promptsView
                .findViewById(R.id.btnCalm) as LottieAnimationView
            val btnNormal = promptsView
                .findViewById(R.id.btnNormal) as LottieAnimationView
            val btnLively = promptsView
                .findViewById(R.id.btnLively) as LottieAnimationView
            val btnIntense = promptsView
                .findViewById(R.id.btnIntense) as LottieAnimationView


            btnCalm.setOnClickListener{
                workType = 0
                val editor = sharedPref.edit()
                editor.putInt(AppUtils.WORK_TIME_KEY, workType)
                editor.apply()
                showMessage(getString(R.string.you_selected_calm),false)
            }

            btnNormal.setOnClickListener{
                workType = 1
                val editor = sharedPref.edit()
                editor.putInt(AppUtils.WORK_TIME_KEY, workType)
                editor.apply()
                showMessage(getString(R.string.you_selected_normal),false)
            }

            btnLively.setOnClickListener{
                workType = 2
                val editor = sharedPref.edit()
                editor.putInt(AppUtils.WORK_TIME_KEY, workType)
                editor.apply()
                showMessage(getString(R.string.you_selected_lively),false)
            }

            btnIntense.setOnClickListener{
                workType = 3
                val editor = sharedPref.edit()
                editor.putInt(AppUtils.WORK_TIME_KEY, workType)
                editor.apply()
                showMessage(getString(R.string.you_selected_intense),false)
            }

            alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                var text = when(workType){
                    0->getString(R.string.calm)
                    1->getString(R.string.normal)
                    2->getString(R.string.lively)
                    3->getString(R.string.intense)
                    else -> {getString(R.string.calm)}
                }

                binding.principal.etWorkType.editText!!.setText(text)

            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }




            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        binding.principal.etClimate.editText!!.setOnClickListener {

            val li = LayoutInflater.from(requireContext())
            val promptsView = li.inflate(R.layout.custom_input_dialog5, null)

            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(promptsView)


            val btnCold = promptsView
                .findViewById(R.id.btnCold) as LottieAnimationView
            val btnFresh = promptsView
                .findViewById(R.id.btnFresh) as LottieAnimationView
            val btnMild = promptsView
                .findViewById(R.id.btnMild) as LottieAnimationView
            val btnTorrid = promptsView
                .findViewById(R.id.btnTorrid) as LottieAnimationView


            btnCold.setOnClickListener{
                climate = 0
                val editor = sharedPref.edit()
                editor.putInt(AppUtils.CLIMATE_KEY, climate)
                editor.apply()
                showMessage(getString(R.string.you_selected_cold),false)
            }

            btnFresh.setOnClickListener{
                climate = 1
                val editor = sharedPref.edit()
                editor.putInt(AppUtils.CLIMATE_KEY, climate)
                editor.apply()
                showMessage(getString(R.string.you_selected_fresh),false)
            }

            btnMild.setOnClickListener{
                climate = 2
                val editor = sharedPref.edit()
                editor.putInt(AppUtils.CLIMATE_KEY, climate)
                editor.apply()
                showMessage(getString(R.string.you_selected_mild),false)
            }

            btnTorrid.setOnClickListener{
                climate = 3
                val editor = sharedPref.edit()
                editor.putInt(AppUtils.CLIMATE_KEY, climate)
                editor.apply()
                showMessage(getString(R.string.you_selected_torrid),false)
            }

            alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                var text = when(climate){
                    0->getString(R.string.cold)
                    1->getString(R.string.fresh)
                    2->getString(R.string.mild)
                    3->getString(R.string.torrid)
                    else -> {getString(R.string.cold)}
                }

                binding.principal.etClimate.editText!!.setText(text)

            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }




            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        if(Locale.getDefault().language == "de"){
            binding.principal.etWeight.editText!!.hint = requireContext().getString(R.string.weight_hint) + " " + AppUtils.calculateExtensionsForWeight(weightUnit,requireContext()) +  " ein"
        }
        else{
            binding.principal.etWeight.editText!!.hint = requireContext().getString(R.string.weight_hint) + " " + AppUtils.calculateExtensionsForWeight(weightUnit,requireContext())
        }
    }

    private fun setLayout() {

        when(themeInt){
            0-> toLightTheme()
            1-> toDarkTheme()
            2-> toWaterTheme()
            3-> toGrapeTheme()
        }
    }

    private fun toGrapeTheme() {
        setBackgroundColor(requireContext().getColor(R.color.purple_500))
        binding.principal.infoTopTitle.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.darkTV.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.systemUnitTV.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.notificationTV.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.legal.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.etNotificationText.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etRingtone.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etPrivacy.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etNotificationText.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etRingtone.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etPrivacy.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view.
        setBackgroundColor(requireContext().getColor(R.color.gray))
        binding.principal.view3.
        setBackgroundColor(requireContext().getColor(R.color.gray))
        binding.principal.view4.
        setBackgroundColor(requireContext().getColor(R.color.gray))
        binding.principal.view5.
        setBackgroundColor(requireContext().getColor(R.color.gray))
        binding.principal.view6.
        setBackgroundColor(requireContext().getColor(R.color.gray))
        binding.principal.view7.
        setBackgroundColor(requireContext().getColor(R.color.gray))
        binding.principal.btnUpdate.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.userInfo.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.etWeight.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWorkType.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWakeUpTime.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etSleepTime.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etTarget.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWeight.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWorkType.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWakeUpTime.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etSleepTime.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etTarget.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etClimate.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etClimate.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.splashScreen.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.tips.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.textView17.setTextColor(requireContext().getColor(R.color.colorBlack))
    }

    private fun toWaterTheme() {
        setBackgroundColor(requireContext().getColor(R.color.colorSecondaryDarkW))
        binding.principal.infoTopTitle.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.darkTV.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.systemUnitTV.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.notificationTV.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.legal.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etNotificationText.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etRingtone.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etPrivacy.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etNotificationText.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etRingtone.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etPrivacy.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view3.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view4.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view5.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view6.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view7.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.btnUpdate.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.userInfo.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWeight.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWorkType.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWakeUpTime.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etSleepTime.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etTarget.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWeight.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWorkType.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWakeUpTime.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etSleepTime.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etTarget.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etClimate.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etClimate.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.splashScreen.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.tips.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.textView17.setTextColor(requireContext().getColor(R.color.colorWhite))
    }

    private fun toDarkTheme() {
        setBackgroundColor(requireContext().getColor(R.color.darkGreen))
        binding.principal.infoTopTitle.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.darkTV.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.systemUnitTV.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.notificationTV.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.legal.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.etNotificationText.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etRingtone.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etPrivacy.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etNotificationText.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etRingtone.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etPrivacy.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.view.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.view3.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.view4.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.view5.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.view6.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.view7.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.btnUpdate.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.userInfo.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.etWeight.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etWorkType.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etWakeUpTime.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etSleepTime.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etTarget.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etWeight.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etWorkType.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etWakeUpTime.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etSleepTime.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etTarget.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etClimate.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.etClimate.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.principal.splashScreen.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.tips.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.principal.textView17.setTextColor(requireContext().getColor(R.color.colorBlack))
    }

    private fun toLightTheme() {
        setBackgroundColor(requireContext().getColor(R.color.colorSecondaryDark))
        binding.principal.infoTopTitle.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.darkTV.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.systemUnitTV.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.notificationTV.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.legal.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etNotificationText.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etRingtone.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etPrivacy.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etNotificationText.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etRingtone.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etPrivacy.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view3.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view4.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view5.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view6.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.view7.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.btnUpdate.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.userInfo.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWeight.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWorkType.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWakeUpTime.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etSleepTime.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etTarget.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWeight.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWorkType.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etWakeUpTime.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etSleepTime.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etTarget.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etClimate.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.etClimate.editText!!.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.splashScreen.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.tips.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.principal.textView17.setTextColor(requireContext().getColor(R.color.colorWhite))
    }


    private fun setBackgroundColor(color: Int) {
        binding.principal.layoutPrincipal.setBackgroundColor(color)
    }

    private fun initBottomBars() {

        val menu = principal.darkThemeBottomBar.menu
        val menu2 = principal.unitSystemBottomBar.menu
        val menu3 = principal.notificationBottomBar.menu
        val menu4 = principal.splashScreenBottomBar.menu
        val menu5 = principal.tipsBottomBar.menu

        for (i in AppUtils.listIdsInfoTheme.indices) {
            menu.add(
                MenuItemDescriptor.Builder(
                    requireContext(),
                    AppUtils.listIdsInfoTheme[i],
                    AppUtils.listInfoTheme[i],
                    AppUtils.listStringInfoTheme[i],
                    Color.parseColor(stringColor)
                )
                    .build()
            )
        }

        for (i in AppUtils.listIdsInfoSystem.indices) {
            menu2.add(
                MenuItemDescriptor.Builder(
                    requireContext(),
                    AppUtils.listIdsInfoSystem[i],
                    AppUtils.listInfoSystem[i],
                    AppUtils.listStringInfoSystem[i],
                    Color.parseColor(stringColor)
                )
                    .build()
            )
        }

        for (i in AppUtils.listIdsFreq.indices) {
            menu3.add(
                MenuItemDescriptor.Builder(
                    requireContext(),
                    AppUtils.listIdsFreq[i],
                    AppUtils.listFreq[i],
                    AppUtils.listStringFreq[i],
                    Color.parseColor(stringColor)
                )
                    .build()
            )
        }

        for (i in AppUtils.listIdsSplash.indices) {
            menu4.add(
                MenuItemDescriptor.Builder(
                    requireContext(),
                    AppUtils.listIdsSplash[i],
                    AppUtils.listIconSplash[i],
                    AppUtils.listStringSplash[i],
                    Color.parseColor(stringColor)
                )
                    .build()
            )
        }

        for (i in AppUtils.listIdsTips.indices) {
            menu5.add(
                MenuItemDescriptor.Builder(
                    requireContext(),
                    AppUtils.listIdsTips[i],
                    AppUtils.listIconTips[i],
                    AppUtils.listStringTips[i],
                    Color.parseColor(stringColor)
                )
                    .build()
            )
        }

        principal.darkThemeBottomBar.onItemSelectedListener = { _, i, _ ->
            when(i.id) {
                R.id.icon_light -> themeInt = 0
                R.id.icon_dark -> themeInt = 1
                R.id.icon_water -> themeInt = 2
                R.id.icon_grape -> themeInt = 3
            }

            setThemeToShared()

        }

        principal.unitSystemBottomBar.onItemSelectedListener = { _, i, _ ->
            when(i.id) {
                R.id.icon_ml -> unit = 0
                R.id.icon_oz_uk -> unit = 1
                R.id.icon_oz_us -> unit = 2

            }

            setSystemUnit()

        }

        when (themeInt) {
            0 -> menu.select(R.id.icon_light)
            1 -> menu.select(R.id.icon_dark)
            2 -> menu.select(R.id.icon_water)
            3 -> menu.select(R.id.icon_grape)
            else -> {
                menu.select(R.id.icon_light)
                themeInt = 0
            }
        }

        unit = sharedPref.getInt(AppUtils.UNIT_KEY,0)

        when (unit) {
            0 -> menu2.select(R.id.icon_ml)
            1 -> menu2.select(R.id.icon_oz_uk)
            2 -> menu2.select(R.id.icon_oz_us)
            else -> {
                menu2.select(R.id.icon_ml)
                unit = 0
            }
        }

        notificFrequency = sharedPref.getInt(AppUtils.NOTIFICATION_FREQUENCY_KEY, 30)
        when (notificFrequency) {
            30 -> menu3.select(R.id.icon_30)
            45 -> menu3.select(R.id.icon_45)
            60 -> menu3.select(R.id.icon_60)
            else -> {
                menu3.select(R.id.icon_30)
                notificFrequency = 30
            }
        }

        principal.notificationBottomBar.onItemSelectedListener = { _, i, _ ->
            when(i.id) {
                R.id.icon_30 -> notificFrequency = 30
                R.id.icon_45 -> notificFrequency = 45
                R.id.icon_60 -> notificFrequency = 60
            }
            sharedPref.edit().putInt(AppUtils.NOTIFICATION_FREQUENCY_KEY, notificFrequency).apply()
        }

        splash = sharedPref.getBoolean(AppUtils.SEE_SPLASH_KEY,true)

        when (splash) {
            true -> menu4.select(R.id.icon_on)
            false -> menu4.select(R.id.icon_off)
        }

        principal.splashScreenBottomBar.onItemSelectedListener = { _, i, _ ->
            when(i.id) {
                R.id.icon_on -> splash = true
                R.id.icon_off -> splash = false
            }

            setSplash()

        }

        tips = sharedPref.getBoolean(AppUtils.SEE_TIPS_KEY,true)

        when (tips) {
            true -> menu5.select(R.id.tips_on)
            false -> menu5.select(R.id.tips_off)
        }

        principal.tipsBottomBar.onItemSelectedListener = { _, i, _ ->
            when(i.id) {
                R.id.tips_on -> tips = true
                R.id.tips_off -> tips = false
            }

            setTips()

        }
    }

    private fun setTips() {
        val editor = sharedPref.edit()
        editor.putBoolean(AppUtils.SEE_TIPS_KEY, splash)
        editor.apply()
    }

    private fun setSplash() {
        val editor = sharedPref.edit()
        editor.putBoolean(AppUtils.SEE_SPLASH_KEY, splash)
        editor.apply()
    }

    private fun setSystemUnit() {
        sqliteHelper = SqliteHelper(requireContext())
        inTook = sqliteHelper.getIntook(AppUtils.getCurrentOnlyDate()!!)
        if(inTook > 0){
            if(sqliteHelper.resetIntook(AppUtils.getCurrentOnlyDate()!!) == 1){
                sqliteHelper.addIntook(AppUtils.getCurrentOnlyDate()!!,
                    inTook.toCalculatedValue(sharedPref.getInt(AppUtils.UNIT_KEY,0),unit),
                    AppUtils.calculateExtensions(unit),AppUtils.getCurrentOnlyDate()!!.toMonth(),
                    AppUtils.getCurrentOnlyDate()!!.toYear()
                )
            }
        }
        val editor = sharedPref.edit()
        editor.putInt(AppUtils.UNIT_NEW_KEY, unit)
        editor.putFloat(AppUtils.VALUE_50_KEY,AppUtils.firstConversion(50f,unit))
        editor.putFloat(AppUtils.VALUE_100_KEY,AppUtils.firstConversion(100f,unit))
        editor.putFloat(AppUtils.VALUE_150_KEY,AppUtils.firstConversion(150f,unit))
        editor.putFloat(AppUtils.VALUE_200_KEY,AppUtils.firstConversion(200f,unit))
        editor.putFloat(AppUtils.VALUE_250_KEY,AppUtils.firstConversion(250f,unit))
        editor.putFloat(AppUtils.VALUE_300_KEY,AppUtils.firstConversion(300f,unit))
        editor.apply()
    }

    private fun setThemeToShared() {
        sqliteHelper = SqliteHelper(requireContext())
        sqliteHelper.updateAll(themeInt)
        var old = sharedPref.getInt(AppUtils.THEME_KEY, 0)
        val editor = sharedPref.edit()
        editor.putInt(AppUtils.THEME_KEY, themeInt)
        editor.apply()
        if(old != themeInt){
            stringColor = when(themeInt){
                0->"#41B279"
                1->"#29704D"
                2->"#6A91DE"
                3->"#FF6200EE"
                else -> {"#41B279"}
            }
            setLayout()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 999) {

            val uri = data!!.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) as Uri
            currentToneUri = uri.toString()
            sharedPref.edit().putString(AppUtils.NOTIFICATION_TONE_URI_KEY, currentToneUri).apply()
            val ringtone = RingtoneManager.getRingtone(requireContext(), uri)
            binding.principal.etRingtone.editText!!.setText(ringtone.getTitle(requireContext()))

        }
    }

    @SuppressLint("InflateParams")
    private fun showMessage(error: String, isError:Boolean = true) {
        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        val customView: View = if(isError){
            layoutInflater.inflate(R.layout.error_toast_layout, null)}
        else{
            layoutInflater.inflate(R.layout.info_toast_layout, null)
        }

        val text = customView.findViewById<TextView>(R.id.tvMessage)
        text.text = error
        toast.view = customView
        toast.show()
    }

}