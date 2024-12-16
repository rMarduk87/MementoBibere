package rpt.tool.mementobibere.ui.userinfo

import android.content.ContentValues
import android.content.Intent
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
import rpt.tool.mementobibere.utils.log.e
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import rpt.tool.mementobibere.utils.receiver.MyAlarmManager.cancelRecurringAlarm
import rpt.tool.mementobibere.utils.receiver.MyAlarmManager.scheduleAutoRecurringAlarm
import rpt.tool.mementobibere.utils.view.adapters.UserInfoPagerAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Suppress("DEPRECATION", "UNUSED_EXPRESSION", "DEPRECATED_IDENTITY_EQUALS")
class InitUserInfoFragment:
    BaseFragment<FragmentInitUserInfoBinding>(FragmentInitUserInfoBinding::inflate),
    IOnBackPressed {

    var userInfoPagerAdapter: UserInfoPagerAdapter? = null

    var current_page_idx: Int = 0
    var max_page: Int = 7

    var intent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().window.navigationBarColor = requireContext().resources.getColor(
            R.color.water_color)
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

        binding.btnBack.setOnClickListener {
            if (current_page_idx > 0)
            current_page_idx -= 1
            binding.viewPager.setCurrentItem(current_page_idx)
        }

        binding.btnNext.setOnClickListener(View.OnClickListener {
            if (current_page_idx === 0) {
                if (sh!!.check_blank_data(SharedPreferencesManager.userName)) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_your_name_validation))
                    return@OnClickListener
                }

                if (SharedPreferencesManager.userName.length < 3) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_valid_name_validation))
                    return@OnClickListener
                }
            }
            if (current_page_idx === 1) {
                try {
                    if (sh!!.check_blank_data(SharedPreferencesManager.personHeight)) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_height_validation))
                        return@OnClickListener
                    }

                    if (sh!!.check_blank_data(SharedPreferencesManager.personHeight)) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_weight_validation))
                        return@OnClickListener
                    }

                    val `val` = ("" + SharedPreferencesManager.personHeight).toFloat()
                    if (`val` < 2) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_height_validation))
                        return@OnClickListener
                    }

                    val val2 = ("" + SharedPreferencesManager.personHeight).toFloat()
                    if (val2 < 30) {
                        ah!!.customAlert(sh!!.get_string(R.string.str_weight_validation))
                        return@OnClickListener
                    }
                } catch (e: Exception) {
                    e.message?.let { it1 -> e(Throwable(e), it1) }
                }
            }

            if (current_page_idx === 5) {
                if (sh!!.check_blank_data(SharedPreferencesManager.wakeUpTime) || sh!!.check_blank_data(
                        SharedPreferencesManager.sleepingTime
                    )
                ) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_from_to_invalid_validation))
                    return@OnClickListener
                } else if (SharedPreferencesManager.ignoreNextStep) {
                    ah!!.customAlert(sh!!.get_string(R.string.str_from_to_invalid_validation))
                    return@OnClickListener
                }
            }
            if (current_page_idx < max_page - 1) {
                current_page_idx += 1
                binding.viewPager.setCurrentItem(current_page_idx)
            } else {
                gotoHomeScreen()
            }
        })
    }

    fun isNextDayEnd(): Boolean {
        val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = SharedPreferencesManager.wakeUpTime.let { simpleDateFormat.parse(it) }
            date2 = SharedPreferencesManager.sleepingTime.let { simpleDateFormat.parse(it) }

            return date1!!.time > date2!!.time
        } catch (e: java.lang.Exception) {
            e.message?.let { e(Throwable(e), it) }
        }

        return false
    }

    private fun setAlarm() {
        val minute_interval = SharedPreferencesManager.notificationFreq

        if (sh!!.check_blank_data(SharedPreferencesManager.wakeUpTime) || sh!!.check_blank_data(
                SharedPreferencesManager.sleepingTime
            )
        ) {
            return
        } else {
            val startTime: Calendar = Calendar.getInstance(Locale.getDefault())
            startTime.set(Calendar.HOUR_OF_DAY, SharedPreferencesManager.wakeUpTimeHour)
            startTime.set(Calendar.MINUTE, SharedPreferencesManager.wakeUpTimeMinute)
            startTime.set(Calendar.SECOND, 0)

            val endTime: Calendar = Calendar.getInstance(Locale.getDefault())
            endTime.set(Calendar.HOUR_OF_DAY, SharedPreferencesManager.sleepTimeHour)
            endTime.set(Calendar.MINUTE, SharedPreferencesManager.sleepTimeMinute)
            endTime.set(Calendar.SECOND, 0)

            if (isNextDayEnd()) endTime.add(Calendar.DATE, 1)

            if (startTime.timeInMillis < endTime.timeInMillis) {
                deleteAutoAlarm(true)

                var _id = System.currentTimeMillis().toInt()


                val initialValues = ContentValues()
                initialValues.put(
                    "AlarmTime",
                    "" + SharedPreferencesManager.wakeUpTime.toString() + " - " + SharedPreferencesManager.sleepingTime)
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
                            (startTime.get(Calendar.HOUR_OF_DAY).toString() +
                                    ":" + startTime.get(Calendar.MINUTE)).toString() +
                                    ":" + startTime.get(
                                Calendar.SECOND
                            )
                        dt = sdf.parse(time)!!
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
                        e.message?.let { e(Throwable(e), it) }
                        e.printStackTrace()
                    }

                    startTime.add(Calendar.MINUTE, minute_interval.toInt())
                }
            } else {
                return
            }
        }
    }

    private fun deleteAutoAlarm(alsoData: Boolean) {
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

    private fun gotoHomeScreen() {
        SharedPreferencesManager.hideWelcomeScreen = true

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