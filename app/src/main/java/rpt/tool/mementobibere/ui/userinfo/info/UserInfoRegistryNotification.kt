package rpt.tool.mementobibere.ui.userinfo.info

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.time.Timepoint
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.BaseAppCompatActivity
import rpt.tool.mementobibere.databinding.FragmentUserInfoRegistryNotificationBinding
import rpt.tool.mementobibere.utils.URLFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UserInfoRegistryNotification : 
    BaseFragment<FragmentUserInfoRegistryNotificationBinding>(
        FragmentUserInfoRegistryNotificationBinding::inflate) {


    var from_hour: Int = 8
    var from_minute: Int = 0
    var to_hour: Int = 22
    var to_minute: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()
        setCount()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            setCount()
        } else {
        }
        
    }


    private fun body() {
        binding.rdo15.text = "15 " + sh!!.get_string(R.string.str_min)
        binding.rdo30.text = "30 " + sh!!.get_string(R.string.str_min)
        binding.rdo45.text = "45 " + sh!!.get_string(R.string.str_min)
        binding.rdo60.text = "1 " + sh!!.get_string(R.string.str_hour)
        
        binding.txtWakeupTime.setOnClickListener {
            openAutoTimePicker(binding.txtWakeupTime, true)
        }
        
        binding.txtBedTime.setOnClickListener {
            openAutoTimePicker(binding.txtBedTime, false)
        }

        binding.rdo15.setOnClickListener { setCount() }

        binding.rdo30.setOnClickListener { setCount() }

        binding.rdo45.setOnClickListener { setCount() }

        binding.rdo60.setOnClickListener { setCount() }
    }

    fun openAutoTimePicker(appCompatTextView: AppCompatTextView?, isFrom: Boolean) {
        val onTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, second ->
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
                    
                    time = "$hourOfDay:$minute:00"
                    dt = sdf.parse(time)
                    formatedDate = sdfs.format(dt)
                    appCompatTextView!!.text = "" + formatedDate
                    
                    setCount()
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }

        val now = Calendar.getInstance(Locale.getDefault())

        if (isFrom) {
            now[Calendar.HOUR_OF_DAY] = from_hour
            now[Calendar.MINUTE] = from_minute
        } else {
            now[Calendar.HOUR_OF_DAY] = to_hour
            now[Calendar.MINUTE] = to_minute
        }
        val tpd: TimePickerDialog
        if (!DateFormat.is24HourFormat(requireActivity())) {
            tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now[Calendar.HOUR_OF_DAY],
                now[Calendar.MINUTE], false
            )
            tpd.setSelectableTimes(generateTimepoints(23.50, 30))

            tpd.setMaxTime(23, 30, 0)
        } else {
            //24 hrs format
            tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now[Calendar.HOUR_OF_DAY],
                now[Calendar.MINUTE], true
            )

            tpd.setSelectableTimes(generateTimepoints(23.50, 30))

            tpd.setMaxTime(23, 30, 0)
        }

        tpd.accentColor = BaseAppCompatActivity.getThemeColor(requireContext())
        tpd.show(requireActivity().fragmentManager, "Datepickerdialog")
        tpd.accentColor = BaseAppCompatActivity.getThemeColor(requireContext())
    }

    val difference: Int
        get() {
            val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

            var date1: Date? = null
            var date2: Date? = null
            try {
                date1 =
                    simpleDateFormat.parse(binding.txtWakeupTime.getText().toString().trim { it <= ' ' })
                date2 = simpleDateFormat.parse(binding.txtBedTime.getText().toString().trim { it <= ' ' })

                val difference = date2.time - date1.time
                val days = (difference / (1000 * 60 * 60 * 24)).toInt()
                val hours = ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60)).toInt()
                val min =
                    (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)).toInt() / (1000 * 60)

                return min
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return 0
        }

    val isNextDayEnd: Boolean
        get() {
            val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

            var date1: Date? = null
            var date2: Date? = null
            try {
                date1 =
                    simpleDateFormat.parse(binding.txtWakeupTime.getText().toString().trim { it <= ' ' })
                date2 = simpleDateFormat.parse(binding.txtBedTime.getText().toString().trim { it <= ' ' })

                return date1.time > date2.time
            } catch (e: Exception) {
            }

            return false
        }

    private fun setCount() {

        val startTime = Calendar.getInstance(Locale.getDefault())
        startTime[Calendar.HOUR_OF_DAY] = from_hour
        startTime[Calendar.MINUTE] = from_minute
        startTime[Calendar.SECOND] = 0

        val endTime = Calendar.getInstance(Locale.getDefault())
        endTime[Calendar.HOUR_OF_DAY] = to_hour
        endTime[Calendar.MINUTE] = to_minute
        endTime[Calendar.SECOND] = 0

        // @@@@@
        if (isNextDayEnd) endTime.add(Calendar.DATE, 1)

        val mills = endTime.timeInMillis - startTime.timeInMillis

        val hours = (mills / (1000 * 60 * 60)).toInt()
        val mins = ((mills / (1000 * 60)) % 60).toInt()
        val total_minutes = ((hours * 60) + mins).toFloat()

        val interval =
            if (binding.rdo15.isChecked) 15 else if (binding.rdo30.isChecked) 30 else if (binding.rdo45.isChecked) 45 else 60

        var consume = 0 // @@@@@
        if (total_minutes > 0) consume =
            Math.round(URLFactory.DAILY_WATER_VALUE / (total_minutes / interval))

        val unit = if (ph!!.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) "ml" else "fl oz"

        binding.lblMessage.text = sh!!.get_string(R.string.str_goal_consume).replace("$1", "$consume $unit")
            .replace("$2", "" + URLFactory.DAILY_WATER_VALUE + " " + unit)

        ph!!.savePreferences(
            URLFactory.WAKE_UP_TIME,
            binding.txtWakeupTime.getText().toString().trim { it <= ' ' })
        ph!!.savePreferences(URLFactory.WAKE_UP_TIME_HOUR, from_hour)
        ph!!.savePreferences(URLFactory.WAKE_UP_TIME_MINUTE, from_minute)

        ph!!.savePreferences(
            URLFactory.BED_TIME,binding.txtBedTime.getText().toString().trim { it <= ' ' })
        ph!!.savePreferences(URLFactory.BED_TIME_HOUR, to_hour)
        ph!!.savePreferences(URLFactory.BED_TIME_MINUTE, to_minute)

        ph!!.savePreferences(URLFactory.INTERVAL, interval)

        if (consume > URLFactory.DAILY_WATER_VALUE) ph!!.savePreferences(
            URLFactory.IGNORE_NEXT_STEP,
            true
        )
        else if (consume == 0) ph!!.savePreferences(URLFactory.IGNORE_NEXT_STEP, true)
        else ph!!.savePreferences(URLFactory.IGNORE_NEXT_STEP, false)

    }

    companion object {
        fun generateTimepoints(maxHour: Double, minutesInterval: Int): Array<Timepoint> {
            val lastValue = (maxHour * 60).toInt()

            val timepoints: MutableList<Timepoint> = ArrayList<Timepoint>()

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
}