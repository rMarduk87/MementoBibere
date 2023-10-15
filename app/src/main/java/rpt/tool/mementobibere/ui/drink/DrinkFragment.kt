package rpt.tool.mementobibere.ui.drink

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import github.com.st235.lib_expandablebottombar.Menu
import github.com.st235.lib_expandablebottombar.MenuItem
import github.com.st235.lib_expandablebottombar.MenuItemDescriptor
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.InitUserInfoActivity
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.WalkThroughActivity
import rpt.tool.mementobibere.databinding.DrinkFragmentBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.extensions.toCalculatedValue
import rpt.tool.mementobibere.utils.extensions.toExtractFloat
import rpt.tool.mementobibere.utils.extensions.toMainTheme
import rpt.tool.mementobibere.utils.extensions.toNumberString
import rpt.tool.mementobibere.utils.helpers.AlarmHelper
import rpt.tool.mementobibere.utils.helpers.SqliteHelper
import rpt.tool.mementobibere.utils.navigation.safeNavController
import rpt.tool.mementobibere.utils.navigation.safeNavigate


class DrinkFragment : BaseFragment<DrinkFragmentBinding>(DrinkFragmentBinding::inflate) {

    private var enabled: Boolean = true
    private lateinit var unit: String
    private lateinit var menuNotify: Menu
    private lateinit var menuNotNotify: Menu
    private lateinit var outValue: TypedValue
    private lateinit var viewWindow: View
    private var totalIntake: Float = 0f
    private var inTook: Float = 0f
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sqliteHelper: SqliteHelper
    private lateinit var dateNow: String
    private var notificStatus: Boolean = false
    private var selectedOption: Float? = null
    private var snackbar: Snackbar? = null
    private var themeInt: Int = 0
    private var current_unitInt: Int = 0
    private var new_unitInt: Int = 0
    private var value_50: Float = 50f
    private var value_100: Float = 100f
    private var value_150: Float = 150f
    private var value_200: Float = 200f
    private var value_250: Float = 250f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = requireActivity().getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        themeInt = sharedPref.getInt(AppUtils.THEME_KEY,0)
        current_unitInt = sharedPref.getInt(AppUtils.UNIT_KEY,0)
        new_unitInt = sharedPref.getInt(AppUtils.UNIT_NEW_KEY,0)
        value_50 = sharedPref.getFloat(AppUtils.VALUE_50_KEY,50f)
        value_100 = sharedPref.getFloat(AppUtils.VALUE_100_KEY,100f)
        value_150 = sharedPref.getFloat(AppUtils.VALUE_150_KEY,150f)
        value_200 = sharedPref.getFloat(AppUtils.VALUE_200_KEY,200f)
        value_250 = sharedPref.getFloat(AppUtils.VALUE_250_KEY,250f)
        setTheme()
        super.onViewCreated(view, savedInstanceState)
        setBackGround()

        sqliteHelper = SqliteHelper(requireContext())

        totalIntake = try {
            sharedPref.getFloat(AppUtils.TOTAL_INTAKE_KEY, 0f)
                .toCalculatedValue(current_unitInt,new_unitInt)
        }catch (ex:Exception){
            var totalIntakeOld = sharedPref.getInt(AppUtils.TOTAL_INTAKE_KEY,0)
            var editor = sharedPref.edit()
            editor.remove(AppUtils.TOTAL_INTAKE_KEY)
            editor.putFloat(AppUtils.TOTAL_INTAKE_KEY,totalIntakeOld.toFloat())
            editor.apply()
            sharedPref.getFloat(AppUtils.TOTAL_INTAKE_KEY, 0f)
                .toCalculatedValue(current_unitInt,new_unitInt)
        }


        if (sharedPref.getBoolean(AppUtils.FIRST_RUN_KEY, true)) {
            startActivity(Intent(requireContext(), WalkThroughActivity::class.java))
        } else if (totalIntake <= 0) {
            startActivity(Intent(requireContext(), InitUserInfoActivity::class.java))
        }

        dateNow = AppUtils.getCurrentOnlyDate()!!

        viewWindow = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        initBottomBar()
        if (!sharedPref.getBoolean(AppUtils.FIRST_RUN_KEY, true) || totalIntake > 0) {
            initIntookValue()
            setValueForDrinking()
        }
    }

    private fun initIntookValue() {
        unit = AppUtils.calculateExtensions(current_unitInt)
        binding.ml50!!.text = "${value_50.toNumberString()} $unit"
        binding.ml100!!.text = "${value_100.toNumberString()} $unit"
        binding.ml150!!.text = "${value_150.toNumberString()} $unit"
        binding.ml200!!.text = "${value_200.toNumberString()} $unit"
        binding.ml250!!.text = "${value_250.toNumberString()} $unit"
    }

    private fun initBottomBar() {

        menuNotify = binding.bottomBarNotify.menu
        menuNotNotify = binding.bottomBarNotNotify.menu

        createMenu()

    }

    private fun createMenu() {

        var colorString = if(themeInt==0){
            "#41B279"
        }
        else{
            "#29704D"
        }

        for (i in AppUtils.listIds.indices) {
            menuNotify.add(
                MenuItemDescriptor.Builder(
                    requireContext(),
                    AppUtils.listIds[i],
                    AppUtils.listIconNotify[i],
                    AppUtils.listStringNotify[i],
                    Color.parseColor(colorString)
                )
                    .build()
            )
        }

        for (i in AppUtils.listIds.indices) {
            menuNotNotify.add(
                MenuItemDescriptor.Builder(
                    requireContext(),
                    AppUtils.listIds[i],
                    AppUtils.listIconNotNotify[i],
                    AppUtils.listStringNotNotify[i],
                    Color.parseColor(colorString)
                )
                    .build()
            )
        }

        binding.bottomBarNotify.onItemSelectedListener = { _, i, _ ->
            manageListeners(i)


        }

        binding.bottomBarNotify.onItemReselectedListener = { _, i, _ ->
            manageListeners(i)
        }

        binding.bottomBarNotNotify.onItemSelectedListener = { _, i, _ ->
            manageListeners(i)

        }

        binding.bottomBarNotNotify.onItemReselectedListener = { _, i, _ ->
            manageListeners(i)
        }
    }

    private fun manageListeners(i: MenuItem) {
        when(i.id) {
            R.id.icon_bell -> manageNotification()
            R.id.icon_edit -> goToBottomEdit()
            R.id.icon_other -> goToBottomInfo()
            R.id.icon_stats -> goToStatsActivity()
        }
    }

    private fun goToStatsActivity() {
        Handler(requireContext().mainLooper).postDelayed({
            var edit = sharedPref.edit()
            edit.putFloat(AppUtils.TOTAL_INTAKE_KEY,totalIntake)
            edit.putString(AppUtils.UNIT_STRING,unit)
            edit.apply()
            safeNavController?.safeNavigate(
                DrinkFragmentDirections.actionDrinkFragmentToStatsFragment())

        }, TIME)

    }

    private fun goToBottomInfo() {
        Handler(requireContext().mainLooper).postDelayed({

            var edit = sharedPref.edit()
            edit.putFloat(AppUtils.TOTAL_INTAKE_KEY,totalIntake)
            edit.apply()
            safeNavController?.safeNavigate(
                DrinkFragmentDirections.actionDrinkFragmentToInfoFragment())

        }, TIME)

    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun addDrinkedWater() {

        if (selectedOption != null) {
            if ((inTook * 100 / totalIntake) <= 140) {
                if (sqliteHelper.addIntook(dateNow, selectedOption!!,unit) > 0) {
                    inTook += selectedOption!!
                    setWaterLevel(inTook, totalIntake)
                    showMessage(getString(R.string.your_water_intake_was_saved),viewWindow,false,
                        AppUtils.Companion.TypeMessage.SAVE)
                }
            } else {
                showMessage(getString(R.string.you_already_achieved_the_goal), viewWindow)
            }
            selectedOption = null
            binding.tvCustom.text = "Custom"
            binding.op50ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op100ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op150ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op200ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op250ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.opCustom.background = requireContext().getDrawable(outValue.resourceId)

            // remove pending notifications
            val mNotificationManager : NotificationManager = requireActivity()
                .getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.cancelAll()
        } else {
            YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(binding.cardView)
            showMessage(getString(R.string.please_select_an_option),viewWindow,true)
        }
    }

    private fun goToBottomEdit() {
        var editor = sharedPref.edit()
        editor.putFloat(AppUtils.TOTAL_INTAKE_KEY,totalIntake)
        editor.apply()
        Handler(requireContext().mainLooper).postDelayed({

            safeNavController?.safeNavigate(DrinkFragmentDirections
                .actionDrinkFragmentToEditInfoBottomSheetFragment())

        }, TIME)
    }

    private fun manageNotification() {
        val alarm = AlarmHelper()
        if(enabled){
            notificStatus = !notificStatus
            var editor = sharedPref.edit()
            editor.putBoolean(AppUtils.NOTIFICATION_STATUS_KEY, notificStatus)
            editor.apply()
            if (notificStatus) {
                binding.bottomBarNotNotify.visibility = View.GONE
                binding.bottomBarNotify.visibility = View.VISIBLE
                Snackbar.make(viewWindow, getString(R.string.notification_enabled), Snackbar.LENGTH_SHORT).show()
                alarm.setAlarm(
                    requireContext(),
                    sharedPref.getInt(AppUtils.NOTIFICATION_FREQUENCY_KEY, 30).toLong())
            } else {
                binding.bottomBarNotNotify.visibility = View.VISIBLE
                binding.bottomBarNotify.visibility = View.GONE
                Snackbar.make(viewWindow, getString(R.string.notification_disabled), Snackbar.LENGTH_SHORT).show()
                alarm.cancelAlarm(requireContext())
            }
        }
    }

    private fun setBackGround() {
        when(themeInt){
            0->toLightTheme()
            1->toDarkTheme()
        }
    }

    private fun toDarkTheme() {
        binding.mainActivityParent.background = requireContext().getDrawable(R.drawable.ic_app_bg_dark)
        binding.textView5.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.tvIntook.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.tvTotalIntake.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.bottomBarNotify.setBackgroundColorRes(R.color.gray_btn_bg_pressed_color)
        binding.bottomBarNotNotify.setBackgroundColorRes(R.color.gray_btn_bg_pressed_color)
    }

    private fun toLightTheme() {
        binding.mainActivityParent.background = requireContext().getDrawable(R.drawable.ic_app_bg)
        binding.textView5.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.tvIntook.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.tvTotalIntake.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.bottomBarNotify.setBackgroundColorRes(R.color.colorWhite)
        binding.bottomBarNotNotify.setBackgroundColorRes(R.color.colorWhite)
    }

    private fun setTheme() {
        val theme = themeInt.toMainTheme()
        requireActivity().setTheme(theme)

    }

    private fun updateValues() {
        totalIntake = try {
            sharedPref.getFloat(AppUtils.TOTAL_INTAKE_KEY, 0f)
        }catch (ex:Exception){
            var totalIntakeOld = sharedPref.getInt(AppUtils.TOTAL_INTAKE_KEY,0)
            var editor = sharedPref.edit()
            editor.remove(AppUtils.TOTAL_INTAKE_KEY)
            editor.putFloat(AppUtils.TOTAL_INTAKE_KEY,totalIntakeOld.toFloat())
            editor.apply()
            sharedPref.getFloat(AppUtils.TOTAL_INTAKE_KEY, 0f)
        }

        var noUpdate = sharedPref.getBoolean(AppUtils.NO_UPDATE_UNIT,false)
        if(!noUpdate){
            totalIntake = sharedPref.getFloat(AppUtils.TOTAL_INTAKE_KEY, 0f)
                .toCalculatedValue(current_unitInt,calculateRealUnit(current_unitInt,new_unitInt))

            unit = AppUtils.calculateExtensions(new_unitInt)
            sqliteHelper.updateTotalIntake(
                AppUtils.getCurrentOnlyDate()!!,
                totalIntake, unit)
        }

        var editor = sharedPref.edit()
        editor.putBoolean(AppUtils.NO_UPDATE_UNIT, false)
        editor.putFloat(AppUtils.TOTAL_INTAKE_KEY,totalIntake)
        editor.apply()

        sqliteHelper.addAll(dateNow, 0, totalIntake,unit)

        inTook = sqliteHelper.getIntook(dateNow)

        setWaterLevel(inTook, totalIntake)
    }

    private fun calculateRealUnit(currentUnitint: Int, newUnitint: Int): Int {
        val realUnit = new_unitInt
        if(currentUnitint==newUnitint){
            val c = sqliteHelper.getTotalIntake(dateNow)
            if(c.moveToFirst()){
                val unit = AppUtils.extractIntConversion(c.getString(1))
                return if(unit==new_unitInt){
                    realUnit
                } else{
                    unit
                }
            }
        }
        return realUnit
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

        val new = sharedPref.getBoolean(AppUtils.RESET_NOTIFICATION_KEY,true)
        if(new){
            val mNotificationManager : NotificationManager = requireActivity()
                .getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationManager.deleteNotificationChannel("rpt.tools.mementobibere.CHANNELONE")
            }
            var edit = sharedPref.edit()
            edit.putBoolean(AppUtils.RESET_NOTIFICATION_KEY,false)
            edit.apply()
        }

        outValue = TypedValue()
        requireContext().applicationContext.theme.resolveAttribute(
            android.R.attr.selectableItemBackground,
            outValue,
            true
        )

        if(!sharedPref.getBoolean(AppUtils.SET_WEIGHT_UNIT, false)){
            safeNavController?.safeNavigate(DrinkFragmentDirections.actionDrinkFragmentToSelectWeightBottomSheetFragment())
        }

        notificStatus = sharedPref.getBoolean(AppUtils.NOTIFICATION_STATUS_KEY, true)
        val alarm = AlarmHelper()
        if (!alarm.checkAlarm(requireContext()) && notificStatus) {
            binding.bottomBarNotNotify.visibility = View.GONE
            binding.bottomBarNotify.visibility = View.VISIBLE
            alarm.setAlarm(
                requireContext(),
                sharedPref.getInt(AppUtils.NOTIFICATION_FREQUENCY_KEY, 30).toLong()
            )
        }

        if (notificStatus) {
            binding.bottomBarNotNotify.visibility = VISIBLE
            binding.bottomBarNotify.visibility = GONE
        }

        if (!notificStatus) {
            binding.bottomBarNotNotify.visibility = GONE
            binding.bottomBarNotify.visibility = VISIBLE
        }

        current_unitInt = sharedPref.getInt(AppUtils.UNIT_KEY,0)
        new_unitInt = sharedPref.getInt(AppUtils.UNIT_NEW_KEY,0)

        updateValues()

        binding.op50ml.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = binding.ml50!!.text.toString().toExtractFloat()
            binding.op50ml.background = requireContext().getDrawable(R.drawable.option_select_bg)
            binding.op100ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op150ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op200ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op250ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.opCustom.background = requireContext().getDrawable(outValue.resourceId)
            addDrinkedWater()
        }

        binding.op100ml.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = binding.ml100!!.text.toString().toExtractFloat()
            binding.op50ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op100ml.background = requireContext().getDrawable(R.drawable.option_select_bg)
            binding.op150ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op200ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op250ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.opCustom.background = requireContext().getDrawable(outValue.resourceId)
            addDrinkedWater()
        }

        binding.op150ml.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = binding.ml150!!.text.toString().toExtractFloat()
            binding.op50ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op100ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op150ml.background = requireContext().getDrawable(R.drawable.option_select_bg)
            binding.op200ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op250ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.opCustom.background = requireContext().getDrawable(outValue.resourceId)
            addDrinkedWater()
        }

        binding.op200ml.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = binding.ml200!!.text.toString().toExtractFloat()
            binding.op50ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op100ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op150ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op200ml.background = requireContext().getDrawable(R.drawable.option_select_bg)
            binding.op250ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.opCustom.background = requireContext().getDrawable(outValue.resourceId)
            addDrinkedWater()
        }

        binding.op250ml.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = binding.ml250!!.text.toString().toExtractFloat()
            binding.op50ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op100ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op150ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op200ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op250ml.background = requireContext().getDrawable(R.drawable.option_select_bg)
            binding.opCustom.background = requireContext().getDrawable(outValue.resourceId)
            addDrinkedWater()
        }

        binding.opCustom.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }

            val li = LayoutInflater.from(requireContext())
            val promptsView = li.inflate(R.layout.custom_input_dialog, null)

            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(promptsView)

            val userInput = promptsView
                .findViewById(R.id.etCustomInput) as TextInputLayout

            alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                val inputText = userInput.editText!!.text.toString()
                if (!TextUtils.isEmpty(inputText)) {
                    binding.tvCustom.text = "$inputText $unit"
                    selectedOption = binding.tvCustom.text.toString().toExtractFloat()
                    addDrinkedWater()
                }
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            binding.op50ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op100ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op150ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op200ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op250ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.opCustom.background = requireContext().getDrawable(R.drawable.option_select_bg)
        }
        if (!sharedPref.getBoolean(AppUtils.FIRST_RUN_KEY, true) || totalIntake > 0) {
            setValueForDrinking()
        }


    }

    override fun onResume() {
        super.onResume()

        themeInt = sharedPref.getInt(AppUtils.THEME_KEY,0)
        current_unitInt = sharedPref.getInt(AppUtils.UNIT_KEY,0)
        new_unitInt = sharedPref.getInt(AppUtils.UNIT_NEW_KEY,0)
        setTheme()
        setBackGround()
        val editor = sharedPref.edit()
        editor.putInt(AppUtils.UNIT_KEY,new_unitInt)
        editor.apply()
        refreshAlarm(sharedPref.getBoolean(AppUtils.NOTIFICATION_STATUS_KEY,true))
    }


    private fun setWaterLevel(inTook: Float, totalIntake: Float) {

        YoYo.with(Techniques.SlideInDown)
            .duration(500)
            .playOn(binding.tvIntook)
        binding.tvIntook.text = "" + inTook.toNumberString()
        binding.tvTotalIntake.text = "/"+totalIntake.toNumberString()+ " " + "$unit"
        val progress = ((inTook / totalIntake) * 100).toInt()
        YoYo.with(Techniques.Pulse)
            .duration(500)
            .playOn(binding.intakeProgress)
        binding.intakeProgress.currentProgress = progress
        if ((inTook * 100 / totalIntake) > 140) {
            showMessage(getString(R.string.you_achieved_the_goal),binding.mainActivityParent)
        }
    }

    private fun setValueForDrinking() {
        unit = AppUtils.calculateExtensions(new_unitInt)
        value_50 = sharedPref.getFloat(AppUtils.VALUE_50_KEY,50f)
        value_100 = sharedPref.getFloat(AppUtils.VALUE_100_KEY,100f)
        value_150 = sharedPref.getFloat(AppUtils.VALUE_150_KEY,150f)
        value_200 = sharedPref.getFloat(AppUtils.VALUE_200_KEY,200f)
        value_250 = sharedPref.getFloat(AppUtils.VALUE_250_KEY,250f)
        binding.ml50!!.text = "${value_50.toNumberString()} $unit"
        binding.ml100!!.text = "${value_100.toNumberString()} $unit"
        binding.ml150!!.text = "${value_150.toNumberString()} $unit"
        binding.ml200!!.text = "${value_200.toNumberString()} $unit"
        binding.ml250!!.text = "${value_250.toNumberString()} $unit"
        binding.tvTotalIntake!!.text = "/" +totalIntake.toNumberString() + " " + "$unit"
    }

    companion object {

        const val TIME: Long = 650
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        var alarm = AlarmHelper()
        when (requestCode) {
            123 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    binding.bottomBarNotNotify.visibility = View.GONE
                    binding.bottomBarNotify.visibility = View.VISIBLE
                    Snackbar.make(viewWindow, getString(R.string.notification_enabled), Snackbar.LENGTH_SHORT).show()
                    alarm.setAlarm(
                        requireContext(),
                        sharedPref.getInt(AppUtils.NOTIFICATION_FREQUENCY_KEY, 30).toLong())

                } else {
                    binding.bottomBarNotNotify.visibility = View.VISIBLE
                    binding.bottomBarNotify.visibility = View.GONE
                    Snackbar.make(viewWindow, getString(R.string.notification_disabled), Snackbar.LENGTH_SHORT).show()
                    alarm.cancelAlarm(requireContext())
                }
            }
        }
    }

    fun refreshAlarm(notify: Boolean){
        var alarm = AlarmHelper()
        if(notify){
            binding.bottomBarNotNotify.visibility = View.GONE
            binding.bottomBarNotify.visibility = View.VISIBLE
            Snackbar.make(viewWindow, getString(R.string.notification_enabled), Snackbar.LENGTH_SHORT).show()
            alarm.setAlarm(
                requireContext(),
                sharedPref.getInt(AppUtils.NOTIFICATION_FREQUENCY_KEY, 30).toLong())
        }
        else{
            binding.bottomBarNotNotify.visibility = View.VISIBLE
            binding.bottomBarNotify.visibility = View.GONE
            Snackbar.make(viewWindow, getString(R.string.notification_disabled), Snackbar.LENGTH_SHORT).show()
            alarm.cancelAlarm(requireContext())
        }

    }
}