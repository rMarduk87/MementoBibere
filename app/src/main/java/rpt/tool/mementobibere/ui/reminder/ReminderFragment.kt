package rpt.tool.mementobibere.ui.reminder

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.text.format.DateFormat
import android.widget.PopupMenu
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.time.Timepoint
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.FragmentReminderBinding
import rpt.tool.mementobibere.utils.data.model.AlarmModel
import rpt.tool.mementobibere.utils.data.model.IntervalModel
import rpt.tool.mementobibere.utils.data.model.SoundModel
import rpt.tool.mementobibere.utils.log.d
import rpt.tool.mementobibere.utils.log.e
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import rpt.tool.mementobibere.utils.navigation.safeNavController
import rpt.tool.mementobibere.utils.navigation.safeNavigate
import rpt.tool.mementobibere.utils.receiver.MyAlarmManager.cancelRecurringAlarm
import rpt.tool.mementobibere.utils.receiver.MyAlarmManager.scheduleAutoRecurringAlarm
import rpt.tool.mementobibere.utils.receiver.MyAlarmManager.scheduleManualRecurringAlarm
import rpt.tool.mementobibere.utils.view.adapters.AlarmAdapter
import rpt.tool.mementobibere.utils.view.adapters.IntervalAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class ReminderFragment:BaseFragment<FragmentReminderBinding>(FragmentReminderBinding::inflate) {

    var alarmModelList: MutableList<AlarmModel> = ArrayList()
    var alarmAdapter: AlarmAdapter? = null
    var from_hour: Int = 0
    var from_minute: Int = 0
    var to_hour: Int = 0
    var to_minute: Int = 0
    var interval: Int = 30
    var lst_interval: MutableList<String> = ArrayList()
    var lst_sounds: MutableList<SoundModel> = ArrayList()
    var intervalAdapter: IntervalAdapter? = null
    var lst_intervals: MutableList<IntervalModel> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.navigationBarColor =
            requireContext().resources.getColor(R.color.str_green_card)

        body()

        var str = sh!!.get_string(R.string.str_bed_time)
        str = str.substring(0, 1).uppercase(Locale.getDefault()) + "" + str.substring(1).lowercase(
            Locale.getDefault()
        )
        binding.lblbt.text = str

        //=======
        str = sh!!.get_string(R.string.str_wakeup_time)
        str = str.substring(0, 1).uppercase(Locale.getDefault()) + "" + str.substring(1).lowercase(
            Locale.getDefault()
        )

        binding.lblwt.text = str

        val arr_data = dh!!.getdata("tbl_alarm_details")
        d("setAutoAlarmAnd", "" + Gson().toJson(arr_data))
        val arr_data2 = dh!!.getdata("tbl_alarm_sub_details")
        d("setAutoAlarmAnd", "" + Gson().toJson(arr_data2))
    }

    private fun setAutoAlarmAndRemoveAllManualAlarm() {
        for (k in alarmModelList.indices) {
            val time = alarmModelList[k]

            cancelRecurringAlarm(requireContext(), time.alarmSundayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmMondayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmTuesdayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmWednesdayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmThursdayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmFridayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmSaturdayId!!.toInt())
        }

        setAutoAlarm(false)
    }

    private fun setAllManualAlarmAndRemoveAutoAlarm() {
        val arr_data = dh!!.getdata("tbl_alarm_details", "AlarmType='R'")

        for (k in arr_data.indices) {
            cancelRecurringAlarm(requireContext(), arr_data[k]["AlarmId"]!!.toInt())

            val arr_data2 = dh!!.getdata("tbl_alarm_sub_details", "SuperId=" + arr_data[k]["id"])
            for (j in arr_data2.indices) cancelRecurringAlarm(
                requireContext(), arr_data2[j]["AlarmId"]!!
                    .toInt()
            )
        }
        
        for (k in alarmModelList.indices) {
            val time = alarmModelList[k]

            val hourOfDay = ("" +
                    dth!!.FormateDateFromString(
                        "hh:mm a",
                        "HH",
                        time.drinkTime!!.trim { it <= ' ' })).toInt()
            val minute = ("" +
                    dth!!.FormateDateFromString(
                        "hh:mm a",
                        "mm",
                        time.drinkTime!!.trim { it <= ' ' })).toInt()

            d("setAllManualAlarm : ", "" + time.sunday)

            if (time.sunday == 1) scheduleManualRecurringAlarm(
                requireContext(), Calendar.SUNDAY, hourOfDay,
                minute, ("" + time.alarmSundayId).toInt()
            )
            if (time.monday == 1) scheduleManualRecurringAlarm(
                requireContext(), Calendar.MONDAY, hourOfDay,
                minute, ("" + time.alarmMondayId).toInt()
            )
            if (time.tuesday == 1) scheduleManualRecurringAlarm(
                requireContext(), Calendar.TUESDAY, hourOfDay,
                minute, ("" + time.alarmTuesdayId).toInt()
            )
            if (time.wednesday == 1) scheduleManualRecurringAlarm(
                requireContext(), Calendar.WEDNESDAY, hourOfDay,
                minute, ("" + time.alarmWednesdayId).toInt()
            )
            if (time.thursday == 1) scheduleManualRecurringAlarm(
                requireContext(), Calendar.THURSDAY, hourOfDay,
                minute, ("" + time.alarmThursdayId).toInt()
            )
            if (time.friday == 1) scheduleManualRecurringAlarm(
                requireContext(), Calendar.FRIDAY, hourOfDay,
                minute, ("" + time.alarmFridayId).toInt()
            )
            if (time.saturday == 1) scheduleManualRecurringAlarm(
                requireContext(), Calendar.SATURDAY, hourOfDay,
                minute, ("" + time.alarmSaturdayId).toInt()
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun load_AutoDataFromDB() {
        val arr_data = dh!!.getdata("tbl_alarm_details", "AlarmType='R'")

        if (arr_data.size > 0) {
            val str_date =
                arr_data[0]["AlarmTime"]!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()

            if (str_date.size > 1) {
                binding.lblWakeupTime.text = str_date[0].trim { it <= ' ' }
                binding.lblBedTime.text = str_date[1].trim { it <= ' ' }
            }

            from_hour = ("" + dth!!.FormateDateFromString(
                "hh:mm a",
                "HH",
                str_date[0].trim { it <= ' ' })).toInt()
            from_minute = ("" + dth!!.FormateDateFromString(
                "hh:mm a",
                "mm",
                str_date[0].trim { it <= ' ' })).toInt()

            to_hour = ("" + dth!!.FormateDateFromString(
                "hh:mm a",
                "HH",
                str_date[1].trim { it <= ' ' })).toInt()
            to_minute = ("" + dth!!.FormateDateFromString(
                "hh:mm a",
                "mm",
                str_date[1].trim { it <= ' ' })).toInt()

            interval = ("" + arr_data[0]["AlarmInterval"]).toInt()

            if (arr_data[0]["AlarmInterval"].equals(
                    "60",
                    ignoreCase = true
                )
            ) binding.lblInterval.text = "1 " + sh!!.get_string(R.string.str_hour)
            else binding.lblInterval.text = arr_data[0]["AlarmInterval"] + " " +
                    sh!!.get_string(R.string.str_min)
        }
    }

    private fun body() {

        load_AutoDataFromDB()

        binding.lblWakeupTime.setOnClickListener {
            openAutoTimePicker2(
                binding.lblWakeupTime,
                true
            )
        }

        binding.lblBedTime.setOnClickListener {
            openAutoTimePicker2(
                binding.lblBedTime,
                false
            )
        }

        binding.lblInterval.setOnClickListener{ openIntervalPicker() }


        if (SharedPreferencesManager.isManualReminder) {
            binding.rdoManualAlarm.isChecked = true
            binding.manualReminderBlock.visibility = View.VISIBLE
            binding.autoReminderBlock.visibility = View.GONE
        } else {
            binding.rdoAuto.isChecked = true
            binding.manualReminderBlock.visibility = View.GONE
            binding.autoReminderBlock.visibility = View.VISIBLE
        }

        binding.rdoAutoAlarm.setOnClickListener {
            binding.manualReminderBlock.visibility = View.GONE
            binding.autoReminderBlock.visibility = View.VISIBLE
            SharedPreferencesManager.isManualReminder = false
        }

        binding.rdoManualAlarm.setOnClickListener {
            binding.manualReminderBlock.visibility = View.VISIBLE
            binding.autoReminderBlock.visibility = View.GONE
            SharedPreferencesManager.isManualReminder = true
            if (alarmModelList.size > 0)
                binding.lblNoRecordFound.visibility = View.GONE
            else binding.lblNoRecordFound.visibility = View.VISIBLE
        }

        binding.rdoAutoAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setAutoAlarmAndRemoveAllManualAlarm()
            }
        }

        binding.rdoManualAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setAllManualAlarmAndRemoveAutoAlarm()
            }
        }

        binding.saveReminder.setOnClickListener {
            if (isValidDate()) {
                setAutoAlarm(true)
            } else {
                ah!!.customAlert(sh!!.get_string(R.string.str_from_to_invalid_validation))
            }
        }

        binding.addReminder.setOnClickListener{ openTimePicker() }


        //==============================
        lst_interval.clear()
        lst_interval.add("30 " + sh!!.get_string(R.string.str_minutes))
        lst_interval.add("45 " + sh!!.get_string(R.string.str_minutes))
        lst_interval.add("60 " + sh!!.get_string(R.string.str_minutes))
        lst_interval.add("90 " + sh!!.get_string(R.string.str_minutes))
        lst_interval.add("120 " + sh!!.get_string(R.string.str_minutes))
        
        binding.include1.leftIconBlock.setOnClickListener{ finish() }
        binding.include1.rightIconBlock.setOnClickListener { view ->
            showMenu(view)
        }

        load_alarm()
        
        binding.switchVibrate.setChecked(!SharedPreferencesManager.reminderVibrate)
        binding.switchVibrate.setOnCheckedChangeListener()
        { _, isChecked ->
            SharedPreferencesManager.reminderVibrate = !isChecked
        }

        alarmAdapter = AlarmAdapter(requireActivity(), alarmModelList, object : AlarmAdapter.CallBack {
            override fun onClickSelect(time: AlarmModel?, position: Int) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onClickRemove(time: AlarmModel?, position: Int) {
                val dialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                    .setMessage(sh!!.get_string(R.string.str_reminder_remove_confirm_message))
                    .setPositiveButton(
                        sh!!.get_string(R.string.str_yes), { dialog, _ ->
                            cancelRecurringAlarm(requireContext(), time!!.alarmSundayId!!.toInt())
                            cancelRecurringAlarm(requireContext(), time.alarmMondayId!!.toInt())
                            cancelRecurringAlarm(requireContext(), time.alarmTuesdayId!!.toInt())
                            cancelRecurringAlarm(requireContext(), time.alarmWednesdayId!!.toInt())
                            cancelRecurringAlarm(requireContext(), time.alarmThursdayId!!.toInt())
                            cancelRecurringAlarm(requireContext(), time.alarmFridayId!!.toInt())
                            cancelRecurringAlarm(requireContext(), time.alarmSaturdayId!!.toInt())

                            alarmModelList.removeAt(position)
                            dh!!.REMOVE("tbl_alarm_details", "id=" + time.id)

                            alarmAdapter!!.notifyDataSetChanged()

                            if (alarmModelList.size > 0)
                                binding.lblNoRecordFound.visibility = View.GONE
                            else binding.lblNoRecordFound.visibility = View.VISIBLE
                            dialog.dismiss()
                        }
                    )
                    .setNegativeButton(
                        sh!!.get_string(R.string.str_no),
                        { dialog, _ -> dialog.dismiss() }
                    )

                dialog.show()
            }

            override fun onClickEdit(time: AlarmModel?, position: Int) {

                if (time!!.isOff !== 1) openEditTimePicker(time)
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onClickSwitch(time: AlarmModel?, position: Int, isOn: Boolean) {
                val initialValues = ContentValues()
                initialValues.put("IsOff", if (isOn) 0 else 1)
                dh!!.UPDATE("tbl_alarm_details", initialValues, "id=" + time!!.id)

                alarmModelList[position].isOff = (if (isOn) 0 else 1)

                if (isOn) {
                    val hourOfDay = ("" +
                            dth!!.FormateDateFromString(
                                "hh:mm a",
                                "HH",
                                time.drinkTime!!.trim { it <= ' ' })).toInt()
                    val minute = ("" +
                            dth!!.FormateDateFromString(
                                "hh:mm a",
                                "mm",
                                time.drinkTime!!.trim { it <= ' ' })).toInt()

                    if (time.sunday == 1) scheduleManualRecurringAlarm(
                        requireContext(), Calendar.SUNDAY, hourOfDay,
                        minute, ("" + time.alarmSundayId).toInt()
                    )
                    if (time.monday == 1) scheduleManualRecurringAlarm(
                        requireContext(), Calendar.MONDAY, hourOfDay,
                        minute, ("" + time.alarmMondayId).toInt()
                    )
                    if (time.tuesday == 1) scheduleManualRecurringAlarm(
                        requireContext(), Calendar.TUESDAY, hourOfDay,
                        minute, ("" + time.alarmTuesdayId).toInt()
                    )
                    if (time.wednesday == 1) scheduleManualRecurringAlarm(
                        requireContext(), Calendar.WEDNESDAY, hourOfDay,
                        minute, ("" + time.alarmWednesdayId).toInt()
                    )
                    if (time.thursday == 1) scheduleManualRecurringAlarm(
                        requireContext(), Calendar.THURSDAY, hourOfDay,
                        minute, ("" + time.alarmThursdayId).toInt()
                    )
                    if (time.friday == 1) scheduleManualRecurringAlarm(
                        requireContext(), Calendar.FRIDAY, hourOfDay,
                        minute, ("" + time.alarmFridayId).toInt()
                    )
                    if (time.saturday == 1) scheduleManualRecurringAlarm(
                        requireContext(), Calendar.SATURDAY, hourOfDay,
                        minute, ("" + time.alarmSaturdayId).toInt()
                    )

                    if (alarmModelList[position].sunday == 0 && alarmModelList[position].monday == 0
                        && alarmModelList[position].tuesday == 0 &&
                        alarmModelList[position].thursday == 0 &&
                        alarmModelList[position].wednesday == 0 &&
                        alarmModelList[position].friday == 0 &&
                        alarmModelList[position].sunday == 0) {
                        val tmp_from_hour = ("" +
                                dth!!.FormateDateFromString(
                                    "hh:mm a",
                                    "HH",
                                    time.drinkTime!!.trim { it <= ' ' })).toInt()
                        val tmp_from_minute = ("" +
                                dth!!.FormateDateFromString(
                                    "hh:mm a",
                                    "mm",
                                    time.drinkTime!!.trim { it <= ' ' })).toInt()


                        val date: Calendar = Calendar.getInstance(Locale.getDefault())
                        val week_pos: Int = date.get(Calendar.DAY_OF_WEEK)

                        val initialValues4 = ContentValues()

                        if (week_pos == 1) {
                            initialValues4.put("Sunday", 1)
                            alarmModelList[position].sunday = 1

                            val _id = ("" + time.alarmSundayId).toInt()

                            scheduleManualRecurringAlarm(
                                requireContext(),
                                Calendar.SUNDAY, tmp_from_hour, tmp_from_minute, _id
                            )
                        } else if (week_pos == 2) {
                            initialValues4.put("Monday", 1)
                            alarmModelList[position].monday = 1

                            val _id = ("" + time.alarmMondayId).toInt()

                            scheduleManualRecurringAlarm(
                                requireContext(),
                                Calendar.MONDAY, tmp_from_hour, tmp_from_minute, _id
                            )
                        } else if (week_pos == 3) {
                            initialValues4.put("Tuesday", 1)
                            alarmModelList[position].tuesday = 1

                            val _id = ("" + time.alarmTuesdayId).toInt()

                            scheduleManualRecurringAlarm(
                                requireContext(),
                                Calendar.TUESDAY, tmp_from_hour, tmp_from_minute, _id
                            )
                        } else if (week_pos == 4) {
                            initialValues4.put("Wednesday", 1)
                            alarmModelList[position].wednesday = 1

                            val _id = ("" + time.alarmWednesdayId).toInt()

                            scheduleManualRecurringAlarm(
                                requireContext(),
                                Calendar.WEDNESDAY, tmp_from_hour, tmp_from_minute, _id
                            )
                        } else if (week_pos == 5) {
                            initialValues4.put("Thursday", 1)
                            alarmModelList[position].thursday = 1

                            val _id = ("" + time.alarmThursdayId).toInt()

                            scheduleManualRecurringAlarm(
                                requireContext(),
                                Calendar.THURSDAY, tmp_from_hour, tmp_from_minute, _id
                            )
                        } else if (week_pos == 6) {
                            initialValues4.put("Friday", 1)
                            alarmModelList[position].friday = 1

                            val _id = ("" + time.alarmFridayId).toInt()

                            scheduleManualRecurringAlarm(
                                requireContext(),
                                Calendar.FRIDAY, tmp_from_hour, tmp_from_minute, _id
                            )
                        } else if (week_pos == 7) {
                            initialValues4.put("Saturday", 1)
                            alarmModelList[position].saturday = 1

                            val _id = ("" + time.alarmSaturdayId).toInt()

                            scheduleManualRecurringAlarm(
                                requireContext(),
                                Calendar.SATURDAY, tmp_from_hour, tmp_from_minute, _id
                            )
                        }

                        binding.alarmRecyclerView.post {
                            alarmAdapter!!.notifyDataSetChanged() }

                    }
                } else {
                    cancelRecurringAlarm(requireContext(), time.alarmSundayId!!.toInt())
                    cancelRecurringAlarm(requireContext(), time.alarmMondayId!!.toInt())
                    cancelRecurringAlarm(requireContext(), time.alarmTuesdayId!!.toInt())
                    cancelRecurringAlarm(requireContext(), time.alarmWednesdayId!!.toInt())
                    cancelRecurringAlarm(requireContext(), time.alarmThursdayId!!.toInt())
                    cancelRecurringAlarm(requireContext(), time.alarmFridayId!!.toInt())
                    cancelRecurringAlarm(requireContext(), time.alarmSaturdayId!!.toInt())
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onClickWeek(
                time: AlarmModel?,
                position: Int,
                week_pos: Int,
                isOn: Boolean
            ) {
                val tmp_from_hour = ("" +
                        dth!!.FormateDateFromString(
                            "hh:mm a",
                            "HH",
                            time!!.drinkTime!!.trim { it <= ' ' })).toInt()
                val tmp_from_minute = ("" +
                        dth!!.FormateDateFromString(
                            "hh:mm a",
                            "mm",
                            time.drinkTime!!.trim { it <= ' ' })).toInt()

                val initialValues = ContentValues()

                if (isOn) {
                    initialValues.put("IsOff", "0")
                    alarmModelList[position].isOff = (if (isOn) 0 else 1)
                }

                if (week_pos == 0) {
                    initialValues.put("Sunday", if (isOn) 1 else 0)
                    alarmModelList[position].sunday = if (isOn) 1 else 0

                    val _id = ("" + time.alarmSundayId).toInt()

                    if (isOn) {
                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.SUNDAY, tmp_from_hour, tmp_from_minute, _id
                        )
                    } else {
                        cancelRecurringAlarm(requireContext(), _id)
                    }
                } else if (week_pos == 1) {
                    initialValues.put("Monday", if (isOn) 1 else 0)
                    alarmModelList[position].monday = if (isOn) 1 else 0

                    val _id = ("" + time.alarmMondayId).toInt()

                    if (isOn) {
                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.MONDAY, tmp_from_hour, tmp_from_minute, _id
                        )
                    } else {
                        cancelRecurringAlarm(requireContext(), _id)
                    }
                } else if (week_pos == 2) {
                    initialValues.put("Tuesday", if (isOn) 1 else 0)
                    alarmModelList[position].tuesday = if (isOn) 1 else 0

                    val _id = ("" + time.alarmTuesdayId).toInt()

                    if (isOn) {
                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.TUESDAY, tmp_from_hour, tmp_from_minute, _id
                        )
                    } else {
                        cancelRecurringAlarm(requireContext(), _id)
                    }
                } else if (week_pos == 3) {
                    initialValues.put("Wednesday", if (isOn) 1 else 0)
                    alarmModelList[position].wednesday = if (isOn) 1 else 0

                    val _id = ("" + time.alarmWednesdayId).toInt()

                    if (isOn) {
                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.WEDNESDAY, tmp_from_hour, tmp_from_minute, _id
                        )
                    } else {
                        cancelRecurringAlarm(requireContext(), _id)
                    }
                } else if (week_pos == 4) {
                    initialValues.put("Thursday", if (isOn) 1 else 0)
                    alarmModelList[position].thursday = if (isOn) 1 else 0

                    val _id = ("" + time.alarmThursdayId).toInt()

                    if (isOn) {
                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.THURSDAY, tmp_from_hour, tmp_from_minute, _id
                        )
                    } else {
                        cancelRecurringAlarm(requireContext(), _id)
                    }
                } else if (week_pos == 5) {
                    initialValues.put("Friday", if (isOn) 1 else 0)
                    alarmModelList[position].friday = if (isOn) 1 else 0

                    val _id = ("" + time.alarmFridayId).toInt()

                    if (isOn) {
                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.FRIDAY, tmp_from_hour, tmp_from_minute, _id
                        )
                    } else {
                        cancelRecurringAlarm(requireContext(), _id)
                    }
                } else if (week_pos == 6) {
                    initialValues.put("Saturday", if (isOn) 1 else 0)
                    alarmModelList[position].saturday = if (isOn) 1 else 0

                    val _id = ("" + time.alarmSaturdayId).toInt()

                    if (isOn) {
                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.SATURDAY, tmp_from_hour, tmp_from_minute, _id
                        )
                    } else {
                        cancelRecurringAlarm(requireContext(), _id)
                    }
                }
                dh!!.UPDATE("tbl_alarm_details", initialValues, "id=" + time.id)

                if (alarmModelList[position].sunday == 0 && alarmModelList[position].monday == 0
                    && alarmModelList[position].tuesday == 0 &&
                    alarmModelList[position].thursday == 0 &&
                    alarmModelList[position].wednesday == 0 &&
                    alarmModelList[position].friday == 0 &&
                    alarmModelList[position].sunday == 0) {
                    val initialValues2 = ContentValues()
                    initialValues2.put("IsOff", "1")
                    dh!!.UPDATE("tbl_alarm_details", initialValues2,
                        "id=" + time.id)
                    alarmModelList[position].isOff = 1
                }

                alarmAdapter!!.notifyDataSetChanged()
            }
        })

        binding.alarmRecyclerView.setLayoutManager(
            LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
        )

        binding.alarmRecyclerView.setAdapter(alarmAdapter)

        if (SharedPreferencesManager.reminderOpt === 1) {
            binding.rdoOff.isChecked = true
        } else if (SharedPreferencesManager.reminderOpt === 2) {
            binding.rdoSilent.isChecked = true
        } else {
            binding.rdoAuto.isChecked = true
        }

        binding.rdoAuto.setOnClickListener {
            SharedPreferencesManager.reminderOpt = 0
        }

        binding.rdoOff.setOnClickListener {
            SharedPreferencesManager.reminderOpt = 1
        }

        binding.rdoSilent.setOnClickListener {
            SharedPreferencesManager.reminderOpt = 2
        }

        loadSounds()

        binding.soundBlock.setOnClickListener { openSoundMenuPicker() }
    }

    private fun finish() {
        safeNavController?.safeNavigate(ReminderFragmentDirections
            .actionReminderFragmentToDrinkFragment())
    }

    private fun showMenu(v: View?) {
        val popup: PopupMenu = PopupMenu(requireContext(), v)
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.delete_item -> {
                        // do your code
                        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                            .setMessage(sh!!.get_string(
                                R.string.str_reminder_remove_all_confirm_message))
                            .setPositiveButton(
                                sh!!.get_string(R.string.str_yes), { dialog, _ ->
                                    deleteAllManualAlarm(true)
                                    binding.rdoAutoAlarm.isChecked = true
                                    dialog.dismiss()
                                }
                            )
                            .setNegativeButton(
                                sh!!.get_string(R.string.str_no),
                                { dialog, _ -> dialog.dismiss() }
                            )

                        dialog.show()



                        return true
                    }

                    else -> return false
                }
            }
        })
        popup.inflate(R.menu.delete_all_menu)
        popup.show()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun deleteAllManualAlarm(alsoData: Boolean)
    {
        for (k in alarmModelList.indices) {
            val time = alarmModelList[k]

            cancelRecurringAlarm(requireContext(), time.alarmSundayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmMondayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmTuesdayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmWednesdayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmThursdayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmFridayId!!.toInt())
            cancelRecurringAlarm(requireContext(), time.alarmSaturdayId!!.toInt())

            if (alsoData) dh!!.REMOVE("tbl_alarm_details", "id=" + time.id)
        }

        if (alsoData) {
            alarmModelList.clear()
            alarmAdapter!!.notifyDataSetChanged()
        }

        SharedPreferencesManager.isManualReminder = false
        binding.manualReminderBlock.visibility = View.GONE
        binding.autoReminderBlock.visibility = View.VISIBLE

        setAutoAlarm(false)
    }


    private fun loadInterval() {
        lst_intervals.clear()

        lst_intervals.add(getIntervalModel(15, "15 " + sh!!.get_string(R.string.str_min)))
        lst_intervals.add(getIntervalModel(30, "30 " + sh!!.get_string(R.string.str_min)))
        lst_intervals.add(getIntervalModel(45, "45 " + sh!!.get_string(R.string.str_min)))
        lst_intervals.add(getIntervalModel(60, "1 " + sh!!.get_string(R.string.str_hour)))
    }

    private fun getIntervalModel(index: Int, name: String?): IntervalModel {
        val intervalModel = IntervalModel()
        intervalModel.id = index
        intervalModel.name = name
        intervalModel.isSelected(interval == index)

        return intervalModel
    }


    private fun openIntervalPicker() {
        loadInterval()

        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_pick_interval, null, false)


        val btn_cancel = view.findViewById<RelativeLayout>(R.id.btn_cancel)
        val btn_save = view.findViewById<RelativeLayout>(R.id.btn_save)


        val intervalRecyclerView = view.findViewById<RecyclerView>(R.id.intervalRecyclerView)

        intervalAdapter = IntervalAdapter(requireActivity(), lst_intervals, object : IntervalAdapter.CallBack {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClickSelect(time: IntervalModel?, position: Int) {
                for (k in lst_intervals.indices) {
                    lst_intervals[k].isSelected(false)
                }

                lst_intervals[position].isSelected(true)
                intervalAdapter!!.notifyDataSetChanged()
            }
        })

        intervalRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        intervalRecyclerView.adapter = intervalAdapter

        btn_cancel.setOnClickListener { dialog.dismiss() }

        btn_save.setOnClickListener {
            for (k in lst_intervals.indices) {
                if (lst_intervals[k].isSelected) {
                    interval = lst_intervals[k].id
                    binding.lblInterval.text = lst_intervals[k].name
                    break
                }
            }
            dialog.dismiss()
        }

        dialog.setContentView(view)

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun openAutoTimePicker2(appCompatTextView: AppCompatTextView, isFrom: Boolean) {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute, _ ->
                var formatedDate = ""
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val sdfs = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val dt: Date
                var time = ""

                try {
                    if (isFrom) {
                        from_hour = hourOfDay
                        from_minute = minute
                    } else {
                        to_hour = hourOfDay
                        to_minute = minute
                    }

                    d("openAutoTimePicker : ", "$from_hour  @@@@  $from_minute")

                    time = "$hourOfDay:$minute:00"
                    dt = sdf.parse(time)!!
                    formatedDate = sdfs.format(dt)
                    appCompatTextView.text = "" + formatedDate

                } catch (e: ParseException) {
                    e.message?.let { e(Throwable(e), it) }
                    e.printStackTrace()
                }
            }

        val now: Calendar = Calendar.getInstance(Locale.getDefault())
        if (isFrom) {
            now.set(Calendar.HOUR_OF_DAY, from_hour)
            now.set(Calendar.MINUTE, from_minute)
        } else {
            now.set(Calendar.HOUR_OF_DAY, to_hour)
            now.set(Calendar.MINUTE, to_minute)
        }
        val tpd: TimePickerDialog
        if (!DateFormat.is24HourFormat(requireActivity())) {
            tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), false
            )

            tpd.setSelectableTimes(generateTimepoints(23.50, 30))

            tpd.setMaxTime(23, 30, 0)
        } else {
            tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), true
            )

            tpd.setSelectableTimes(generateTimepoints(23.50, 30))

            tpd.setMaxTime(23, 30, 0)
        }



        tpd.accentColor = getThemeColor(requireContext())
        tpd.show(requireActivity().fragmentManager, "Datepickerdialog")
        tpd.accentColor = getThemeColor(requireContext())
    }

    private fun isValidDate(): Boolean {
        val startTime: Calendar = Calendar.getInstance(Locale.getDefault())
        startTime.set(Calendar.HOUR_OF_DAY, from_hour)
        startTime.set(Calendar.MINUTE, from_minute)
        startTime.set(Calendar.SECOND, 0)

        val endTime: Calendar = Calendar.getInstance(Locale.getDefault())
        endTime.set(Calendar.HOUR_OF_DAY, to_hour)
        endTime.set(Calendar.MINUTE, to_minute)
        endTime.set(Calendar.SECOND, 0)
        
        if (isNextDayEnd()) endTime.add(Calendar.DATE, 1)

        d(
            "isValidDate",
            ("" + startTime.timeInMillis).toString() + " @@@ " + endTime.timeInMillis
        )

        val mills: Long = endTime.timeInMillis - startTime.timeInMillis
        
        val hours = (mills / (1000 * 60 * 60)).toInt()
        val mins = ((mills / (1000 * 60)) % 60).toInt()
        val total_minutes = ((hours * 60) + mins).toFloat()

        if (interval <= total_minutes) return true


        return false
    }

    private fun deleteAutoAlarm(alsoData: Boolean) {
        val arr_data = dh!!.getdata("tbl_alarm_details", "AlarmType='R'")

        for (k in arr_data.indices) {
            cancelRecurringAlarm(requireContext(), arr_data[k]["AlarmId"]!!.toInt())

            val arr_data2 = dh!!.getdata("tbl_alarm_sub_details", "SuperId=" 
                    + arr_data[k]["id"])
            for (j in arr_data2.indices) cancelRecurringAlarm(
                requireContext(), arr_data2[j]["AlarmId"]!!
                    .toInt()
            )

            if (alsoData) {
                dh!!.REMOVE("tbl_alarm_details", "id=" + arr_data[k]["id"])
                dh!!.REMOVE("tbl_alarm_sub_details", "SuperId=" + arr_data[k]["id"])
            }
        }
    }

    private fun isNextDayEnd(): Boolean {
        val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = simpleDateFormat.parse(binding.lblWakeupTime.getText().toString().trim())
            date2 = simpleDateFormat.parse(binding.lblBedTime.getText().toString().trim())

            return date1.time > date2.time
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e), it) }
        }

        return false
    }

    private fun setAutoAlarm(moveScreen: Boolean) {
        val minute_interval = interval

        if (sh!!.check_blank_data(binding.lblWakeupTime.getText().toString()) ||
            sh!!.check_blank_data(binding.lblBedTime.getText().toString())
        ) {
            ah!!.customAlert(sh!!.get_string(R.string.str_from_to_invalid_validation))
            return
        } else {
            if (!moveScreen) load_AutoDataFromDB()

            d("setAutoAlarm :", "$from_hour:$from_minute  @@@  $to_hour:$to_minute")

            val startTime: Calendar = Calendar.getInstance(Locale.getDefault())
            startTime.set(Calendar.HOUR_OF_DAY, from_hour)
            startTime.set(Calendar.MINUTE, from_minute)
            startTime.set(Calendar.SECOND, 0)

            val endTime: Calendar = Calendar.getInstance(Locale.getDefault())
            endTime.set(Calendar.HOUR_OF_DAY, to_hour)
            endTime.set(Calendar.MINUTE, to_minute)
            endTime.set(Calendar.SECOND, 0)

            if (isNextDayEnd()) endTime.add(Calendar.DATE, 1)

            if (startTime.timeInMillis < endTime.timeInMillis) {
                deleteAutoAlarm(true)

                var _id = System.currentTimeMillis().toInt()

                val initialValues = ContentValues()
                initialValues.put(
                    "AlarmTime",
                    ("" + binding.lblWakeupTime.getText()
                        .toString()).toString() + " - " + binding.lblBedTime.getText().toString()
                )
                initialValues.put("AlarmId", "" + _id)
                initialValues.put("AlarmType", "R")
                initialValues.put("AlarmInterval", "" + minute_interval)
                dh!!.INSERT("tbl_alarm_details", initialValues)

                val getLastId = dh!!.GET_LAST_ID("tbl_alarm_details")

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
                        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        val sdfs = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        var dt: Date
                        var time = ""

                        time =
                            (startTime.get(Calendar.HOUR_OF_DAY).toString() +
                                    ":" + startTime.get(Calendar.MINUTE)).toString() + ":" +
                                    startTime.get(
                                Calendar.SECOND
                            )
                        dt = sdf.parse(time)!!
                        formatedDate = sdfs.format(dt)
                        scheduleAutoRecurringAlarm(requireContext(), startTime, _id)

                        val initialValues2 = ContentValues()
                        initialValues2.put("AlarmTime", "" + formatedDate)
                        initialValues2.put("AlarmId", "" + _id)
                        initialValues2.put("SuperId", "" + getLastId)
                        dh!!.INSERT("tbl_alarm_sub_details", initialValues2)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    startTime.add(Calendar.MINUTE, minute_interval)
                }

                if (moveScreen) {
                    finish()
                }
            } else {
                ah!!.customAlert(sh!!.get_string(R.string.str_from_to_invalid_validation))
                return
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun openEditTimePicker(alarmtime: AlarmModel?) {
        val tmp_from_hour = ("" +
                dth!!.FormateDateFromString(
                    "hh:mm a",
                    "HH",
                    alarmtime!!.drinkTime!!.trim { it <= ' ' })).toInt()
        val tmp_from_minute = ("" +
                dth!!.FormateDateFromString(
                    "hh:mm a",
                    "mm",
                    alarmtime.drinkTime!!.trim { it <= ' ' })).toInt()


        val onTimeSetListener: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute, _ ->
                var formatedDate = ""
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val sdfs = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val dt: Date
                var time = ""

                try {
                    time = "$hourOfDay:$minute:00"
                    dt = sdf.parse(time)!!
                    formatedDate = sdfs.format(dt)

                    if (!dh!!.IS_EXISTS(
                            "tbl_alarm_details",
                            "AlarmTime='" + formatedDate + "' AND id<>" + alarmtime.id
                        )
                    ) {

                        val initialValues = ContentValues()
                        initialValues.put("AlarmTime", "" + formatedDate)
                        dh!!.UPDATE("tbl_alarm_details", initialValues,
                            "id=" + alarmtime.id)

                        val _id_sunday = ("" + alarmtime.alarmSundayId).toInt()
                        val _id_monday = ("" + alarmtime.alarmMondayId).toInt()
                        val _id_tuesday = ("" + alarmtime.alarmTuesdayId).toInt()
                        val _id_wednesday = ("" + alarmtime.alarmWednesdayId).toInt()
                        val _id_thursday = ("" + alarmtime.alarmThursdayId).toInt()
                        val _id_friday = ("" + alarmtime.alarmFridayId).toInt()
                        val _id_saturday = ("" + alarmtime.alarmSaturdayId).toInt()


                        if (alarmtime.sunday == 1) scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.SUNDAY, hourOfDay, minute, _id_sunday
                        )

                        if (alarmtime.monday == 1) scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.MONDAY, hourOfDay, minute, _id_monday
                        )

                        if (alarmtime.tuesday == 1) scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.TUESDAY, hourOfDay, minute, _id_tuesday
                        )

                        if (alarmtime.wednesday == 1) scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.WEDNESDAY, hourOfDay, minute, _id_wednesday
                        )

                        if (alarmtime.thursday == 1) scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.THURSDAY, hourOfDay, minute, _id_thursday
                        )

                        if (alarmtime.friday == 1) scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.FRIDAY, hourOfDay, minute, _id_friday
                        )

                        if (alarmtime.saturday == 1) scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.SATURDAY, hourOfDay, minute, _id_saturday
                        )

                        load_alarm()

                        alarmAdapter!!.notifyDataSetChanged()
                    } else {
                        ah!!.customAlert(sh!!.get_string(R.string.str_set_alarm_validation))
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }

        val now: Calendar = Calendar.getInstance(Locale.getDefault())
        now.set(Calendar.HOUR_OF_DAY, tmp_from_hour)
        now.set(Calendar.MINUTE, tmp_from_minute)
        val tpd: TimePickerDialog
        if (!DateFormat.is24HourFormat(requireActivity())) {
            tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), false
            )

            tpd.setSelectableTimes(generateTimepoints(23.50, 15))

            tpd.setMaxTime(23, 30, 0)
        } else {
            tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), true
            )

            tpd.setSelectableTimes(generateTimepoints(23.50, 15))

            tpd.setMaxTime(23, 30, 0)
        }

        tpd.show(requireActivity().fragmentManager, "Datepickerdialog")
        tpd.accentColor = getThemeColor(requireContext())
    }


    private fun loadSounds() {
        lst_sounds.clear()

        lst_sounds.add(getSoundModel(0, "Default"))
        lst_sounds.add(getSoundModel(1, "Bell"))
        lst_sounds.add(getSoundModel(2, "Blop"))
        lst_sounds.add(getSoundModel(3, "Bong"))
        lst_sounds.add(getSoundModel(4, "Click"))
        lst_sounds.add(getSoundModel(5, "Echo droplet"))
        lst_sounds.add(getSoundModel(6, "Mario droplet"))
        lst_sounds.add(getSoundModel(7, "Ship bell"))
        lst_sounds.add(getSoundModel(8, "Simple droplet"))
        lst_sounds.add(getSoundModel(9, "Tiny droplet"))
    }

    private fun getSoundModel(index: Int, name: String?): SoundModel {
        val soundModel = SoundModel()
        soundModel.id = index
        soundModel.name = name
        soundModel.isSelected(SharedPreferencesManager.reminderSound === index)

        return soundModel
    }

    @SuppressLint("InflateParams")
    private fun openSoundMenuPicker() {
        safeNavController?.safeNavigate(ReminderFragmentDirections
            .actionReminderFragmentToOpenSoundPickerDialogFragment()
        )
    }

    private fun load_alarm() {
        alarmModelList.clear()

        val arr_data = dh!!.getdata("tbl_alarm_details", "AlarmType='M'")

        for (k in arr_data.indices) {
            val alarmModel = AlarmModel()
            alarmModel.drinkTime = arr_data[k]["AlarmTime"]
            alarmModel.id = arr_data[k]["id"]
            alarmModel.alarmId = arr_data[k]["AlarmId"]
            alarmModel.alarmType = arr_data[k]["AlarmType"]
            alarmModel.alarmInterval = arr_data[k]["AlarmInterval"]

            alarmModel.isOff = (arr_data[k]["IsOff"]!!.toInt())
            alarmModel.sunday = arr_data[k]["Sunday"]!!.toInt()
            alarmModel.monday = arr_data[k]["Monday"]!!.toInt()
            alarmModel.tuesday = arr_data[k]["Tuesday"]!!.toInt()
            alarmModel.wednesday = arr_data[k]["Wednesday"]!!.toInt()
            alarmModel.thursday = arr_data[k]["Thursday"]!!.toInt()
            alarmModel.friday = arr_data[k]["Friday"]!!.toInt()
            alarmModel.saturday = arr_data[k]["Saturday"]!!.toInt()

            alarmModel.alarmSundayId = arr_data[k]["SundayAlarmId"]
            alarmModel.alarmMondayId = arr_data[k]["MondayAlarmId"]
            alarmModel.alarmTuesdayId = arr_data[k]["TuesdayAlarmId"]
            alarmModel.alarmWednesdayId = arr_data[k]["WednesdayAlarmId"]
            alarmModel.alarmThursdayId = arr_data[k]["ThursdayAlarmId"]
            alarmModel.alarmFridayId = arr_data[k]["FridayAlarmId"]
            alarmModel.alarmSaturdayId = arr_data[k]["SaturdayAlarmId"]

            alarmModelList.add(alarmModel)
        }

        if (alarmModelList.size > 0) binding.lblNoRecordFound.visibility = View.GONE
        else binding.lblNoRecordFound.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun openTimePicker() {
        val onTimeSetListener: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute, _ ->
                var formatedDate = ""
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val sdfs = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val dt: Date
                var time = ""

                try {
                    time = "$hourOfDay:$minute:00"
                    dt = sdf.parse(time)!!
                    formatedDate = sdfs.format(dt)
                    if (!dh!!.IS_EXISTS("tbl_alarm_details",
                            "AlarmTime='$formatedDate'")) {
                        val _id = System.currentTimeMillis().toInt()

                        val _id_sunday = System.currentTimeMillis().toInt()

                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.SUNDAY, hourOfDay, minute, _id_sunday
                        )

                        val _id_monday = System.currentTimeMillis().toInt()

                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.MONDAY, hourOfDay, minute, _id_monday
                        )

                        val _id_tuesday = System.currentTimeMillis().toInt()

                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.TUESDAY, hourOfDay, minute, _id_tuesday
                        )

                        val _id_wednesday = System.currentTimeMillis().toInt()

                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.WEDNESDAY, hourOfDay, minute, _id_wednesday
                        )

                        val _id_thursday = System.currentTimeMillis().toInt()

                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.THURSDAY, hourOfDay, minute, _id_thursday
                        )

                        val _id_friday = System.currentTimeMillis().toInt()

                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.FRIDAY, hourOfDay, minute, _id_friday
                        )

                        val _id_saturday = System.currentTimeMillis().toInt()

                        scheduleManualRecurringAlarm(
                            requireContext(),
                            Calendar.SATURDAY, hourOfDay, minute, _id_saturday
                        )


                        val initialValues = ContentValues()
                        initialValues.put("AlarmTime", "" + formatedDate)
                        initialValues.put("AlarmId", "" + _id)

                        initialValues.put("SundayAlarmId", "" + _id_sunday)
                        initialValues.put("MondayAlarmId", "" + _id_monday)
                        initialValues.put("TuesdayAlarmId", "" + _id_tuesday)
                        initialValues.put("WednesdayAlarmId", "" + _id_wednesday)
                        initialValues.put("ThursdayAlarmId", "" + _id_thursday)
                        initialValues.put("FridayAlarmId", "" + _id_friday)
                        initialValues.put("SaturdayAlarmId", "" + _id_saturday)

                        initialValues.put("AlarmType", "M")
                        initialValues.put("AlarmInterval", "0")
                        dh!!.INSERT("tbl_alarm_details", initialValues)

                        load_alarm()

                        alarmAdapter!!.notifyDataSetChanged()

                        ah!!.customAlert(sh!!.get_string(R.string.str_successfully_set_alarm))
                    } else {
                        ah!!.customAlert(sh!!.get_string(R.string.str_set_alarm_validation))
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }

        val now: Calendar = Calendar.getInstance(Locale.getDefault())
        val tpd: TimePickerDialog
        if (!DateFormat.is24HourFormat(requireActivity())) {
            tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), false
            )

            tpd.setSelectableTimes(generateTimepoints(23.50, 15))

            tpd.setMaxTime(23, 30, 0)
        } else {
            tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), true
            )

            tpd.setSelectableTimes(generateTimepoints(23.50, 15))

            tpd.setMaxTime(23, 30, 0)
        }

        tpd.show(requireActivity().fragmentManager, "Datepickerdialog")
        tpd.accentColor = getThemeColor(requireContext())
    }

    private fun generateTimepoints(maxHour: Double, minutesInterval: Int): Array<Timepoint> {
        val lastValue = (maxHour * 60).toInt()

        val timepoints: MutableList<Timepoint> = ArrayList()

        var minute = 0
        while (minute <= lastValue) {
            val currentHour = minute / 60
            val currentMinute = minute - (if (currentHour > 0) (currentHour * 60) else 0)
            if (currentHour == 24) {
                minute += minutesInterval
                continue
            }
            timepoints.add(Timepoint(currentHour, currentMinute))
            minute += minutesInterval
        }
        return timepoints.toTypedArray<Timepoint>()
    }
}