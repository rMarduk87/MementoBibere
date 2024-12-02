package rpt.tool.mementobibere.ui.userinfo

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.IOnBackPressed
import rpt.tool.mementobibere.MainActivity
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant.finish
import rpt.tool.mementobibere.databinding.FragmentInitUserInfoBinding
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.log.d
import rpt.tool.mementobibere.utils.receiver.MyAlarmManager.cancelRecurringAlarm
import rpt.tool.mementobibere.utils.receiver.MyAlarmManager.scheduleAutoRecurringAlarm
import rpt.tool.mementobibere.utils.view.adapters.UserInfoPagerAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Suppress("DEPRECATION")
class InitUserInfoFragment:
    BaseFragment<FragmentInitUserInfoBinding>(FragmentInitUserInfoBinding::inflate),
    IOnBackPressed {

    var userInfoPagerAdapter: UserInfoPagerAdapter? = null

    var current_page_idx: Int = 0
    var max_page: Int = 7

    var intent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.navigationBarColor = requireContext().resources.getColor(
                R.color.water_color)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dotsIndicator.isClickable = false
        userInfoPagerAdapter = UserInfoPagerAdapter(requireActivity().supportFragmentManager, requireContext())
        binding.viewPager.setAdapter(userInfoPagerAdapter)
        binding.viewPager.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                current_page_idx = position

                if (position == 0) {
                    binding.btnBack.visibility = View.GONE
                    binding.space.visibility = View.GONE
                } else {
                    binding.btnBack.visibility = View.VISIBLE
                    binding.space.visibility = View.VISIBLE
                }

                if (position == max_page - 1) {
                    binding.lblNext.text = sh!!.get_string(R.string.str_get_started)
                } else {
                    binding.lblNext.text = sh!!.get_string(R.string.str_next)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.viewPager.setOffscreenPageLimit(10)
        binding.dotsIndicator.setViewPager(binding.viewPager)

        binding.btnBack.setOnClickListener(View.OnClickListener {
            if (current_page_idx > 0);
            current_page_idx -= 1
            binding.viewPager.setCurrentItem(current_page_idx)
        })

        binding.btnNext.setOnClickListener(View.OnClickListener { //ah!!,customAlert(""+ph!!.getString(URLFactory.USER_NAME));
            if (current_page_idx === 0) {
                if (sh!!.check_blank_data(ph!!.getString(URLFactory.USER_NAME))) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_your_name_validation))
                    return@OnClickListener
                }

                if (ph!!.getString(URLFactory.USER_NAME)!!.length < 3) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_valid_name_validation))
                    return@OnClickListener
                }
            }
            if (current_page_idx === 1) {
                try {
                    if (sh!!.check_blank_data(ph!!.getString(URLFactory.PERSON_HEIGHT))) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_height_validation))
                        return@OnClickListener
                    }

                    if (sh!!.check_blank_data(ph!!.getString(URLFactory.PERSON_WEIGHT))) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_weight_validation))
                        return@OnClickListener
                    }

                    val `val` = ("" + ph!!.getString(URLFactory.PERSON_HEIGHT)).toFloat()
                    if (`val` < 2) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_height_validation))
                        return@OnClickListener
                    }

                    val val2 = ("" + ph!!.getString(URLFactory.PERSON_WEIGHT)).toFloat()
                    if (val2 < 30) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_weight_validation))
                        return@OnClickListener
                    }
                } catch (e: Exception) {
                }
            }

            if (current_page_idx === 5) {
                if (sh!!.check_blank_data(ph!!.getString(URLFactory.WAKE_UP_TIME)) || sh!!.check_blank_data(
                        ph!!.getString(URLFactory.BED_TIME)
                    )
                ) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_from_to_invalid_validation))
                    return@OnClickListener
                } else if (ph!!.getBoolean(URLFactory.IGNORE_NEXT_STEP)) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_from_to_invalid_validation))
                    return@OnClickListener
                }

                //setAlarm();
            }
            if (current_page_idx < max_page - 1) {
                current_page_idx += 1
                binding.viewPager.setCurrentItem(current_page_idx)
            } else {
                gotoHomeScreen()
            }
        })
























































        /*val is24h = android.text.format.DateFormat.is24HourFormat(requireContext())

        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        wakeupTime = SharedPreferencesManager.wakeUpTime
        sleepingTime = SharedPreferencesManager.sleepingTime

        binding.etWakeUpTime.editText!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = wakeupTime

            val mTimePicker = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->

                    val time = Calendar.getInstance()
                    time.set(Calendar.HOUR_OF_DAY, selectedHour)
                    time.set(Calendar.MINUTE, selectedMinute)
                    wakeupTime = time.timeInMillis

                    binding.etWakeUpTime.editText!!.setText(
                        String.format("%02d:%02d", selectedHour, selectedMinute)
                    )
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24h
            )
            mTimePicker.setTitle(getString(R.string.select_wakeup_time))
            mTimePicker.show()
        }


        binding.etSleepTime.editText!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = sleepingTime

            val mTimePicker = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->

                    val time = Calendar.getInstance()
                    time.set(Calendar.HOUR_OF_DAY, selectedHour)
                    time.set(Calendar.MINUTE, selectedMinute)
                    sleepingTime = time.timeInMillis

                    binding.etSleepTime.editText!!.setText(
                        String.format("%02d:%02d", selectedHour, selectedMinute)
                    )
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24h
            )
            mTimePicker.setTitle(getString(R.string.select_sleeping_time))
            mTimePicker.show()
        }

        binding.etGender.editText!!.setOnClickListener {

            val li = LayoutInflater.from(requireContext())
            val promptsView = li.inflate(R.layout.custom_input_dialog2, null)

            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(promptsView)


            val btnMan: LottieAnimationView = promptsView
                            .findViewById(R.id.btnMan)
            val btnWoman: LottieAnimationView = promptsView
                            .findViewById(R.id.btnWoman)


            btnMan.setOnClickListener{
                gender = 0
                SharedPreferencesManager.gender = gender
                showMessage(
                    getString(R.string.you_selected_man), it,
                    type= rpt.tool.mementobibere.utils.AppUtils.Companion.TypeMessage.MAN
                )
            }

            btnWoman.setOnClickListener{
                gender = 1
                SharedPreferencesManager.gender = gender
                showMessage(
                    getString(R.string.you_selected_woman), it,
                    type= rpt.tool.mementobibere.utils.AppUtils.Companion.TypeMessage.WOMAN
                )
            }

            alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                var text = if(gender==0){
                    getString(R.string.man)
                }
                else{
                    getString(R.string.woman)
                }

                binding.etGender.editText!!.setText(text)

            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        binding.btnAvis.setOnClickListener{
            if(bloodDonor==0){
                bloodDonor = 1
                showMessage(getString(R.string.you_selected_avis), it)
            }
            else{
                bloodDonor = 0
                showMessage(getString(R.string.you_selected_no_avis), it)
            }
        }


        binding.etWorkType.editText!!.setOnClickListener {

            val li = LayoutInflater.from(requireContext())
            val promptsView = li.inflate(R.layout.custom_input_dialog4, null)

            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(promptsView)


            val btnCalm: LottieAnimationView = promptsView
                            .findViewById(R.id.btnCalm)
            val btnNormal: LottieAnimationView = promptsView
                            .findViewById(R.id.btnNormal)
            val btnLively: LottieAnimationView = promptsView
                            .findViewById(R.id.btnLively)
            val btnIntense: LottieAnimationView = promptsView
                            .findViewById(R.id.btnIntense)


            btnCalm.setOnClickListener{
                workType = 0
                SharedPreferencesManager.workType = workType
                showMessage(
                    getString(R.string.you_selected_calm), it,
                    type= rpt.tool.mementobibere.utils.AppUtils.Companion.TypeMessage.WORKTYPE, workType = workType
                )
            }

            btnNormal.setOnClickListener{
                workType = 1
                SharedPreferencesManager.workType = workType
                showMessage(
                    getString(R.string.you_selected_normal), it,
                    type= rpt.tool.mementobibere.utils.AppUtils.Companion.TypeMessage.WORKTYPE, workType = workType
                )
            }

            btnLively.setOnClickListener{
                workType = 2
                SharedPreferencesManager.workType = workType
                showMessage(
                    getString(R.string.you_selected_lively), it,
                    type= rpt.tool.mementobibere.utils.AppUtils.Companion.TypeMessage.WORKTYPE, workType = workType
                )
            }

            btnIntense.setOnClickListener{
                workType = 3
                SharedPreferencesManager.workType = workType
                showMessage(
                    getString(R.string.you_selected_intense), it,
                    type= rpt.tool.mementobibere.utils.AppUtils.Companion.TypeMessage.WORKTYPE, workType = workType
                )
            }

            alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                var text = when(workType){
                    0->getString(R.string.calm)
                    1->getString(R.string.normal)
                    2->getString(R.string.lively)
                    3->getString(R.string.intense)
                    else -> {getString(R.string.calm)}
                }

                binding.etWorkType.editText!!.setText(text)

            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }




            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        binding.etClimate.editText!!.setOnClickListener {

            val li = LayoutInflater.from(requireContext())
            val promptsView = li.inflate(R.layout.custom_input_dialog5, null)

            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(promptsView)


            val btnCold: LottieAnimationView = promptsView
                            .findViewById(R.id.btnCold)
            val btnFresh: LottieAnimationView = promptsView
                            .findViewById(R.id.btnFresh)
            val btnMild: LottieAnimationView = promptsView
                            .findViewById(R.id.btnMild)
            val btnTorrid: LottieAnimationView = promptsView
                            .findViewById(R.id.btnTorrid)


            btnCold.setOnClickListener{
                climate = 0
                SharedPreferencesManager.climate = climate
                showMessage(
                    getString(R.string.you_selected_cold), it,
                    type= rpt.tool.mementobibere.utils.AppUtils.Companion.TypeMessage.CLIMATE
                )
            }

            btnFresh.setOnClickListener{
                climate = 1
                SharedPreferencesManager.climate = climate
                showMessage(
                    getString(R.string.you_selected_fresh), it,
                    type= rpt.tool.mementobibere.utils.AppUtils.Companion.TypeMessage.CLIMATE
                )
            }

            btnMild.setOnClickListener{
                climate = 2
                SharedPreferencesManager.climate = climate
                showMessage(
                    getString(R.string.you_selected_mild), it,
                    type= rpt.tool.mementobibere.utils.AppUtils.Companion.TypeMessage.CLIMATE
                )
            }

            btnTorrid.setOnClickListener{
                climate = 3
                SharedPreferencesManager.climate = climate
                showMessage(
                    getString(R.string.you_selected_torrid), it,
                    type= rpt.tool.mementobibere.utils.AppUtils.Companion.TypeMessage.CLIMATE
                )
            }

            alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                var text = when(climate){
                    0->getString(R.string.cold)
                    1->getString(R.string.fresh)
                    2->getString(R.string.mild)
                    3->getString(R.string.torrid)
                    else -> {getString(R.string.cold)}
                }

                binding.etClimate.editText!!.setText(text)

            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        binding.btnContinue.setOnClickListener {

            val imm: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.initUserInfoParentLayout.windowToken, 0)

            weight = binding.etWeight.editText!!.text.toString()


            when {
                TextUtils.isEmpty(weight) -> showError(getString(R.string.please_input_your_weight),it)

                weight.toInt() > rpt.tool.mementobibere.utils.AppUtils.getMaxWeight(weightUnit) || weight.toInt() < rpt.tool.mementobibere.utils.AppUtils.getMinWeight(weightUnit) ->
                    showError(getString(R.string.please_input_a_valid_weight), it)

                !rpt.tool.mementobibere.utils.AppUtils.isValidDate(binding.etSleepTime.editText!!.text.toString(),binding.etWakeUpTime.editText!!.text.toString()) -> showError(getString(R.string.please_input_a_valid_rest_time), it)

                TextUtils.isEmpty(binding.etGender.editText!!.text.toString()) -> showError(getString(R.string.gender_hint),it)
                TextUtils.isEmpty(binding.etWorkType.editText!!.text.toString()) -> showError(getString(R.string.work_type_hint),it)
                TextUtils.isEmpty(binding.etClimate.editText!!.text.toString()) -> showError(getString(R.string.climate_set_hint),it)

                else -> {

                    SharedPreferencesManager.weight = weight.toInt()
                    SharedPreferencesManager.workType = workType
                    SharedPreferencesManager.wakeUpTime = wakeupTime
                    SharedPreferencesManager.sleepingTime = sleepingTime
                    SharedPreferencesManager.firstRun = false
                    SharedPreferencesManager.setWeight = true
                    SharedPreferencesManager.setGender = true
                    SharedPreferencesManager.setWorkOut = true
                    SharedPreferencesManager.setClimate = true
                    SharedPreferencesManager.startTutorial = false
                    SharedPreferencesManager.bloodDonorKey = bloodDonor
                    SharedPreferencesManager.setBloodDonor = true

                    val totalIntake = rpt.tool.mementobibere.utils.AppUtils.calculateIntake(weight.toInt(), workType,weightUnit,
                        gender, climate, 0,unit )
                    val df = DecimalFormat("#")
                    df.roundingMode = RoundingMode.CEILING
                    SharedPreferencesManager.totalIntake = df.format(totalIntake).toFloat()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        initBottomBars()*/
    }

    /*private fun initBottomBars() {
        val menu = binding.unitSystemBottomBar.menu
        val menu2 = binding.weightSystemBottomBar.menu


        for (i in rpt.tool.mementobibere.utils.AppUtils.listIdsInfoSystem.indices) {
            menu.add(
                MenuItemDescriptor.Builder(
                    requireContext(),
                    rpt.tool.mementobibere.utils.AppUtils.listIdsInfoSystem[i],
                    rpt.tool.mementobibere.utils.AppUtils.listInfoSystem[i],
                    rpt.tool.mementobibere.utils.AppUtils.listStringInfoSystem[i],
                    Color.parseColor("#41B279")
                )
                    .build()
            )
        }

        for (i in rpt.tool.mementobibere.utils.AppUtils.listIdsWeightSystem.indices) {
            menu2.add(
                MenuItemDescriptor.Builder(
                    requireContext(),
                    rpt.tool.mementobibere.utils.AppUtils.listIdsWeightSystem[i],
                    rpt.tool.mementobibere.utils.AppUtils.listWeightSystem[i],
                    rpt.tool.mementobibere.utils.AppUtils.listStringWeightSystem[i],
                    Color.parseColor("#41B279")
                )
                    .build()
            )
        }

        setWeightUnit()

        binding.unitSystemBottomBar.onItemSelectedListener = { _, i, _ ->
            when(i.id) {
                R.id.icon_ml -> unit = 0
                R.id.icon_oz_uk -> unit = 1
                R.id.icon_oz_us -> unit = 2

            }

            setSystemUnit()

        }

        unit = SharedPreferencesManager.current_unitInt

        when (unit) {
            0 -> menu.select(R.id.icon_ml)
            1 -> menu.select(R.id.icon_oz_uk)
            2 -> menu.select(R.id.icon_oz_us)
            else -> {
                menu.select(R.id.icon_ml)
                unit = 0
            }
        }

        binding.weightSystemBottomBar.onItemSelectedListener = { _, i, _ ->
            when(i.id) {
                R.id.icon_kg -> weightUnit = 0
                R.id.icon_lbl -> weightUnit = 1
            }

            setWeightUnit()

        }

    }*/

    /*private fun setSystemUnit() {
        SharedPreferencesManager.value_50 = rpt.tool.mementobibere.utils.AppUtils.firstConversion(50f,unit)
        SharedPreferencesManager.value_100 = rpt.tool.mementobibere.utils.AppUtils.firstConversion(100f,unit)
        SharedPreferencesManager.value_150 = rpt.tool.mementobibere.utils.AppUtils.firstConversion(150f,unit)
        SharedPreferencesManager.value_200 = rpt.tool.mementobibere.utils.AppUtils.firstConversion(200f,unit)
        SharedPreferencesManager.value_250 = rpt.tool.mementobibere.utils.AppUtils.firstConversion(250f,unit)
        SharedPreferencesManager.value_300 = rpt.tool.mementobibere.utils.AppUtils.firstConversion(300f,unit)
        SharedPreferencesManager.value_350 = rpt.tool.mementobibere.utils.AppUtils.firstConversion(350f,unit)
        SharedPreferencesManager.new_unitInt = unit

    }*/

    /*private fun setWeightUnit() {
        SharedPreferencesManager.weightUnit = weightUnit
    }*/

    /*@SuppressLint("InflateParams", "RestrictedApi")
    private fun showError(error: String, view: View) {
        val snackBar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
        val customSnackView: View =
            layoutInflater.inflate(R.layout.error_toast_layout, null)
        snackBar.view.setBackgroundColor(Color.TRANSPARENT)
        val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout

        val text = customSnackView.findViewById<TextView>(R.id.tvMessage)
        text.text = error

        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(customSnackView, 0)
        snackBar.show()
    }*/

    fun isNextDayEnd(): Boolean {
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = simpleDateFormat.parse(ph!!.getString(URLFactory.WAKE_UP_TIME))
            date2 = simpleDateFormat.parse(ph!!.getString(URLFactory.BED_TIME))

            return date1.getTime() > date2.getTime()
        } catch (e: java.lang.Exception) {
        }

        return false
    }

    fun setAlarm() {
        val minute_interval: Int = ph!!.getInt(URLFactory.INTERVAL)

        if (sh!!.check_blank_data(ph!!.getString(URLFactory.WAKE_UP_TIME)) || sh!!.check_blank_data(
                ph!!.getString(
                    URLFactory.BED_TIME
                )
            )
        ) {
            //ah!!,customAlert(sh!!.get_string(R.string.str_from_to_invalid_validation));
            return
        } else {
            val startTime: Calendar = Calendar.getInstance(Locale.getDefault())
            startTime.set(Calendar.HOUR_OF_DAY, ph!!.getInt(URLFactory.WAKE_UP_TIME_HOUR))
            startTime.set(Calendar.MINUTE, ph!!.getInt(URLFactory.WAKE_UP_TIME_MINUTE))
            startTime.set(Calendar.SECOND, 0)

            val endTime: Calendar = Calendar.getInstance(Locale.getDefault())
            endTime.set(Calendar.HOUR_OF_DAY, ph!!.getInt(URLFactory.BED_TIME_HOUR))
            endTime.set(Calendar.MINUTE, ph!!.getInt(URLFactory.BED_TIME_MINUTE))
            endTime.set(Calendar.SECOND, 0)

            if (isNextDayEnd()) endTime.add(Calendar.DATE, 1)

            if (startTime.timeInMillis < endTime.timeInMillis) {
                deleteAutoAlarm(true)

                var _id = System.currentTimeMillis().toInt()


                val initialValues = ContentValues()
                initialValues.put(
                    "AlarmTime",
                    ("" + ph!!.getString(URLFactory.WAKE_UP_TIME)).toString() + " - " + ph!!.getString(
                        URLFactory.BED_TIME
                    )
                )
                initialValues.put("AlarmId", "" + _id)
                initialValues.put("AlarmType", "R")
                initialValues.put("AlarmInterval", "" + minute_interval)
                dh!!.INSERT("tbl_alarm_details", initialValues)

                val getLastId: String = dh!!.GET_LAST_ID("tbl_alarm_details")

                while (startTime.timeInMillis <= endTime.timeInMillis) {
                    d(
                        "ALARMTIME  : ",
                        (("" + startTime.get(Calendar.HOUR_OF_DAY)).toString() + ":" + startTime.get(
                            Calendar.MINUTE
                        )).toString() + ":" + startTime.get(Calendar.SECOND)
                    )

                    try {
                        _id = System.currentTimeMillis().toInt()

                        var formatedDate = ""
                        val sdf: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        val sdfs: SimpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        var dt: Date
                        var time = ""

                        time =
                            (startTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + startTime.get(Calendar.MINUTE)).toString() + ":" + startTime.get(
                                Calendar.SECOND
                            )
                        dt = sdf.parse(time)
                        formatedDate = sdfs.format(dt)

                        if (!dh!!.IS_EXISTS(
                                "tbl_alarm_details",
                                "AlarmTime='$formatedDate'"
                            ) && !dh!!.IS_EXISTS(
                                "tbl_alarm_sub_details",
                                "AlarmTime='$formatedDate'"
                            )
                        ) {
                            scheduleAutoRecurringAlarm(requireContext(), startTime, _id)

                            val initialValues2 = ContentValues()
                            initialValues2.put("AlarmTime", "" + formatedDate)
                            initialValues2.put("AlarmId", "" + _id)
                            initialValues2.put("SuperId", "" + getLastId)
                            dh!!.INSERT("tbl_alarm_sub_details", initialValues2)


                            val _id_sunday = System.currentTimeMillis().toInt()
                            val _id_monday = System.currentTimeMillis().toInt()
                            val _id_tuesday = System.currentTimeMillis().toInt()
                            val _id_wednesday = System.currentTimeMillis().toInt()
                            val _id_thursday = System.currentTimeMillis().toInt()
                            val _id_friday = System.currentTimeMillis().toInt()
                            val _id_saturday = System.currentTimeMillis().toInt()
                            val initialValues3 = ContentValues()
                            initialValues3.put("AlarmTime", "" + formatedDate)
                            initialValues3.put("AlarmId", "" + _id_sunday)
                            initialValues3.put("SundayAlarmId", "" + _id_sunday)
                            initialValues3.put("MondayAlarmId", "" + _id_monday)
                            initialValues3.put("TuesdayAlarmId", "" + _id_tuesday)
                            initialValues3.put("WednesdayAlarmId", "" + _id_wednesday)
                            initialValues3.put("ThursdayAlarmId", "" + _id_thursday)
                            initialValues3.put("FridayAlarmId", "" + _id_friday)
                            initialValues3.put("SaturdayAlarmId", "" + _id_saturday)
                            initialValues3.put("AlarmType", "M")
                            initialValues3.put("AlarmInterval", "0")
                            dh!!.INSERT("tbl_alarm_details", initialValues3)
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                    startTime.add(Calendar.MINUTE, minute_interval)
                }
            } else {
                return
            }
        }
    }

    fun deleteAutoAlarm(alsoData: Boolean) {
        val arr_data: ArrayList<HashMap<String, String>> = dh!!.getdata("tbl_alarm_details")

        for (k in arr_data.indices) {
            cancelRecurringAlarm(requireContext(), arr_data[k]["AlarmId"]!!.toInt())

            val arr_data2: ArrayList<HashMap<String, String>> =
                dh!!.getdata("tbl_alarm_sub_details", "SuperId=" + arr_data[k]["id"])
            for (j in arr_data2.indices) cancelRecurringAlarm(
                requireContext(), arr_data2[j]["AlarmId"]!!
                    .toInt()
            )
        }

        if (alsoData) {
            dh!!.REMOVE("tbl_alarm_details")
            dh!!.REMOVE("tbl_alarm_sub_details")
        }
    }

    fun gotoHomeScreen() {
        ph!!.savePreferences(URLFactory.HIDE_WELCOME_SCREEN, true)

        setAlarm()

        intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent!!)
        finish()
    }

    override fun onBackPressed(): Boolean {
        if (current_page_idx > 0) {
            current_page_idx -= 1
            binding.viewPager.setCurrentItem(current_page_idx)
            return true
        } else {
            false
        }
        return true
    }
}