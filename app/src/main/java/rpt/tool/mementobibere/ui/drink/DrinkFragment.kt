package rpt.tool.mementobibere.ui.drink

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import rpt.tool.mementobibere.InitUserInfoActivity
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant
import rpt.tool.mementobibere.databinding.FragmentDrinkBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.custom.InputFilterWeightRange
import rpt.tool.mementobibere.utils.data.Container
import rpt.tool.mementobibere.utils.data.Menu
import rpt.tool.mementobibere.utils.data.NextReminderModel
import rpt.tool.mementobibere.utils.helpers.HeightWeightHelper.mlToOzConverter
import rpt.tool.mementobibere.utils.helpers.HeightWeightHelper.ozToMlConverter
import rpt.tool.mementobibere.utils.log.d
import rpt.tool.mementobibere.utils.log.e
import rpt.tool.mementobibere.utils.log.v
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import rpt.tool.mementobibere.utils.navigation.safeNavController
import rpt.tool.mementobibere.utils.navigation.safeNavigate
import rpt.tool.mementobibere.utils.view.adapters.ContainerAdapterNew
import rpt.tool.mementobibere.utils.view.adapters.MenuAdapter
import rpt.tool.mementobibere.widget.NewAppWidget
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random


@Suppress("NAME_SHADOWING")
class DrinkFragment :
    rpt.tool.mementobibere.BaseFragment<FragmentDrinkBinding>(FragmentDrinkBinding::inflate) {

    private lateinit var dateNow: String
    var menu_name: ArrayList<Menu> = ArrayList<Menu>()
    var menuAdapter: MenuAdapter? = null
    var filter_cal: Calendar? = null
    var today_cal: Calendar? = null
    var yesterday_cal: Calendar? = null
    var containerArrayList: ArrayList<Container> = ArrayList()
    var adapter: ContainerAdapterNew? = null
    var drink_water: Float = 0f
    var old_drink_water: Float = 0f
    var selected_pos: Int = 0
    var max_bottle_height: Int = 870
    var progress_bottle_height: Int = 0
    var cp: Int = 0
    var np: Int = 0
    var ringtone: Ringtone? = null
    var handler: Handler? = null
    var runnable: Runnable? = null
    var btnclick: Boolean = true
    var bottomSheetDialog: BottomSheetDialog? = null
    var handlerReminder: Handler? = null
    var runnableReminder: Runnable? = null


    @SuppressLint("BatteryLife")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!SharedPreferencesManager.hideWelcomeScreen) {
            startActivity(Intent(requireContext(), InitUserInfoActivity::class.java))
            requireActivity().finish()
        }

        if(!SharedPreferencesManager.setGender || !SharedPreferencesManager.setUserName
            || !SharedPreferencesManager.setBloodDonor ||
            !SharedPreferencesManager.setWorkOut ||
            !SharedPreferencesManager.setClimate ||
            !SharedPreferencesManager.setHeight || !SharedPreferencesManager.setWeight){
            startActivity(Intent(requireContext(), InitUserInfoActivity::class.java))
            requireActivity().finish()
        }

        if (SharedPreferencesManager.dailyWater == 0f) {
            URLFactory.DAILY_WATER_VALUE = 2500f
        } else {
            URLFactory.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater
        }

        if (sh!!.check_blank_data("" + SharedPreferencesManager.waterUnit)) {
            URLFactory.WATER_UNIT_VALUE = "ml"
        } else {
            URLFactory.WATER_UNIT_VALUE = SharedPreferencesManager.waterUnit!!
        }

        dateNow = AppUtils.getCurrentOnlyDate()!!

        if(SharedPreferencesManager.bloodDonorKey && !dh!!.IS_EXISTS(
                    "tbl_blood_donor",
                    "BloodDonorDate='$dateNow'"
                )){
            binding.avisLayout.visibility = VISIBLE
        }else if(SharedPreferencesManager.bloodDonorKey && dh!!.IS_EXISTS(
                "tbl_blood_donor",
                "Date='$dateNow'"
            )){
            binding.avisLayout.visibility = VISIBLE
            binding.avisInfo.visibility = VISIBLE
        }
        else{
            binding.avisLayout.visibility = GONE
            binding.avisInfo.visibility = GONE
        }

        ringtone = RingtoneManager.getRingtone(
            requireContext(),
            Uri.parse(("android.resource://" + requireContext().packageName) + "/" + R.raw.fill_water_sound)
        )

        frame()

    }

    @SuppressLint("SimpleDateFormat")
    override fun onStart() {
        super.onStart()

        if(dh!!.IS_EXISTS(
                "tbl_blood_donor",
                "BloodDonorDate='$dateNow'"
            )){
            binding.lblTotalGoal.setTextColor(resources.getColor(R.color.red))
            binding.lblTotalDrunk.setTextColor(resources.getColor(R.color.red))
        }

        binding.imgCalendar.setOnClickListener{
            val calendar = Calendar.getInstance()

            val mDatePicker = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    calendar.add(Calendar.DAY_OF_MONTH, -1)

                    val myFormat = "dd-MM-yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat)
                    val preAvis = sdf.format(calendar.time)
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                    val avis = sdf.format(calendar.time)
                    var initialValues = ContentValues()
                    initialValues.put("BloodDonorDate", "" + preAvis)
                    dh!!.INSERT("tbl_blood_donor", initialValues)
                    initialValues = ContentValues()
                    initialValues.put("BloodDonorDate", "" + avis)
                    dh!!.INSERT("tbl_blood_donor", initialValues)
                    if (sdf.format(calendar.time) == dateNow) {
                        if (SharedPreferencesManager.dailyWater < 2000f) {
                            SharedPreferencesManager.dailyWater = 2000f
                            URLFactory.DAILY_WATER_VALUE = 2000f
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            mDatePicker.datePicker.minDate = AppUtils.getMinDate()
            mDatePicker.setTitle("")
            mDatePicker.show()
        }

        binding.avisInfo.setOnClickListener {
            ah!!.Show_Alert_Dialog(getString(R.string.tomorrow_you_will_donate))
        }
    }

    override fun onResume() {
        super.onResume()

        if (URLFactory.RELOAD_DASHBOARD) {
            init()
        } else {
            URLFactory.RELOAD_DASHBOARD = true
        }
    }

    private fun frame(){
        binding.contentFrame.viewTreeObserver
            .addOnGlobalLayoutListener {
                val w: Int = binding.contentFrame.width
                val h: Int = binding.contentFrame.height
                v("getWidthHeight", "$w   -   $h")
            }

        binding.contentFrameTest.viewTreeObserver
            .addOnGlobalLayoutListener{
                val w: Int = binding.contentFrameTest.width
                val h: Int = binding.contentFrameTest.height
                v("getWidthHeight test", "$w   -   $h")
                max_bottle_height = h - 30
            }
    }

    private fun init(){
        initMenuScreen()
        frame()
        body()
        getAllReminderData()
        fetchNextReminder()
    }

    private fun initMenuScreen() {
        filter_cal = Calendar.getInstance(Locale.getDefault())
        today_cal = Calendar.getInstance(Locale.getDefault())
        yesterday_cal = Calendar.getInstance(Locale.getDefault())
        yesterday_cal!!.add(Calendar.DATE, -1)

        menuBody()

        binding.include1.lblToolbarTitle.setOnClickListener {
            filter_cal!!.timeInMillis = today_cal!!.timeInMillis
            binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_today)
            setCustomDate(dth!!.getDate(filter_cal!!.timeInMillis, URLFactory.DATE_FORMAT))
        }

        binding.lblUserName.text = SharedPreferencesManager.userName
    }

    @SuppressLint("RtlHardcoded")
    private fun menuBody() {
        loadPhoto()

        binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_today)
        binding.lblUserName.text = SharedPreferencesManager.userName

        menu_name.clear()
        menu_name.add(Menu(sh!!.get_string(R.string.str_home), true))
        menu_name.add(Menu(sh!!.get_string(R.string.str_drink_history), false))
        menu_name.add(Menu(sh!!.get_string(R.string.str_drink_report), false))
        menu_name.add(Menu(sh!!.get_string(R.string.str_settings), false))
        menu_name.add(Menu(sh!!.get_string(R.string.str_faqs), false))
        menu_name.add(Menu(sh!!.get_string(R.string.str_privacy_policy), false))
        menu_name.add(Menu(sh!!.get_string(R.string.str_tell_a_friend), false))

        menuAdapter = MenuAdapter(requireActivity(), menu_name, object : MenuAdapter.CallBack {
            @SuppressLint("RtlHardcoded")
            override fun onClickSelect(menu: Menu?, position: Int) {
                binding.drawerLayout.closeDrawer(Gravity.LEFT)

                when (position) {
                    1 -> {
                        safeNavController?.safeNavigate(
                            DrinkFragmentDirections.actionDrinkFragmentToHistoryFragment()
                        )
                    }
                    2 -> {
                        safeNavController?.safeNavigate(
                            DrinkFragmentDirections.actionDrinkFragmentToStatsFragment()
                        )
                    }
                    3 -> {
                        safeNavController?.safeNavigate(
                            DrinkFragmentDirections.actionDrinkFragmentToSettingsFragment()
                        )
                    }
                    4 -> {
                        safeNavController?.safeNavigate(
                            DrinkFragmentDirections.actionDrinkFragmentToFaqFragment()
                        )
                    }
                    5 -> {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.setData(Uri.parse(URLFactory.PRIVACY_POLICY_URL))
                        startActivity(i)
                    }
                    6 -> {
                        val str = sh!!.get_string(R.string.app_share_txt)
                            .replace("#1", URLFactory.APP_SHARE_URL)

                        ih!!.ShareText(requireContext().getString(R.string.app_name), str)
                    }
                }
            }
        })

        binding.btnContactUs.setOnClickListener{
            try {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "rptechnologies.info@gmail.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "")
                intent.putExtra(Intent.EXTRA_TEXT, "")
                startActivity(intent)
            } catch (ex: java.lang.Exception) {
                ex.message?.let { it1 -> e(Throwable(ex), it1) }
            }
        }

        binding.leftDrawer.setLayoutManager(
            LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
        )

        binding.leftDrawer.setAdapter(menuAdapter)

        binding.openProfile.setOnClickListener {
            try {
                if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT))
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
            } catch (e: java.lang.Exception) {
                e.message?.let { it1 -> e(Throwable(e), it1) }
            }
            safeNavController?.safeNavigate(
                DrinkFragmentDirections.actionDrinkFragmentToUserProfileFragment()
            )
        }

        binding.include1.btnAlarm.setOnClickListener{ showReminderDialog() }

        binding.include1.btnMenu.setOnClickListener{
            try {
                if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT))
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                else binding.drawerLayout.openDrawer(Gravity.LEFT)
            } catch (e: java.lang.Exception) {
                e.message?.let { it1 -> e(Throwable(e), it1) }
            }
        }

        binding.include1.imgPre.setOnClickListener {
            filter_cal!!.add(Calendar.DATE, -1)
            if (dth!!.getDate(filter_cal!!.timeInMillis, URLFactory.DATE_FORMAT).equals(
                    dth!!.getDate(
                        yesterday_cal!!.timeInMillis, URLFactory.DATE_FORMAT
                    ), ignoreCase = true
                )
            ) binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_yesterday)
            else binding.include1.lblToolbarTitle.text = dth!!.getDate(
                filter_cal!!.timeInMillis,
                URLFactory.DATE_FORMAT
            )
            setCustomDate(dth!!.getDate(filter_cal!!.timeInMillis, URLFactory.DATE_FORMAT))
        }

        binding.include1.imgNext.setOnClickListener {
            filter_cal!!.add(Calendar.DATE, 1)
            if (filter_cal!!.timeInMillis > today_cal!!.timeInMillis) {
                filter_cal!!.add(Calendar.DATE, -1)
                return@setOnClickListener
            }

            if (dth!!.getDate(filter_cal!!.timeInMillis, URLFactory.DATE_FORMAT).equals(
                    dth!!.getDate(
                        today_cal!!.timeInMillis, URLFactory.DATE_FORMAT
                    ), ignoreCase = true
                )
            ) binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_today)
            else if (dth!!.getDate(filter_cal!!.timeInMillis, URLFactory.DATE_FORMAT).equals(
                    dth!!.getDate(
                        yesterday_cal!!.timeInMillis, URLFactory.DATE_FORMAT
                    ), ignoreCase = true
                )
            ) binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_yesterday)
            else binding.include1.lblToolbarTitle.text = dth!!.getDate(
                filter_cal!!.timeInMillis,
                URLFactory.DATE_FORMAT
            )
            setCustomDate(dth!!.getDate(filter_cal!!.timeInMillis, URLFactory.DATE_FORMAT))
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showReminderDialog() {
        safeNavController?.safeNavigate(
            DrinkFragmentDirections.actionDrinkFragmentToReminderDialogFragment()
        )
    }

    private fun loadPhoto() {
        if (sh!!.check_blank_data(SharedPreferencesManager.userPhoto)) {
            Glide.with(requireActivity()).load(
                if (SharedPreferencesManager.userGender)
                    R.drawable.female_white
                else
                    R.drawable.male_white
            ).apply(RequestOptions.circleCropTransform())
                .into(binding.imgUser)
        } else {
            var ex = false

            try {
                val f = File(SharedPreferencesManager.userPhoto)
                if (f.exists()) ex = true
            } catch (e: java.lang.Exception) {
                e.message?.let { it1 -> e(Throwable(e), it1) }
            }

            if (ex) {
                Glide.with(requireActivity()).load(SharedPreferencesManager.userPhoto)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imgUser)
            } else {
                Glide.with(requireActivity()).load(
                    if (SharedPreferencesManager.userGender)
                        R.drawable.female_white
                    else
                        R.drawable.male_white
                ).apply(RequestOptions.circleCropTransform())
                    .into(binding.imgUser)
            }
        }
    }

    private fun setCustomDate(date: String?) {
        count_specific_day_drink(date!!)
    }

    fun count_specific_day_drink(custom_date: String) {
        val arr_dataO = dh!!.getdata(
            "tbl_drink_details",
            "DrinkDate ='$custom_date'"
        )
        old_drink_water = 0f
        for (k in arr_dataO.indices) {
            if (URLFactory.WATER_UNIT_VALUE.equals(
                    "ml",
                    ignoreCase = true
                )
            ){
                val x = ("" + arr_dataO[k]["ContainerValue"]).toDouble()
                old_drink_water += x.toFloat()
            }
            else{
                val y = ("" + arr_dataO[k]["ContainerValueOZ"]).toDouble()
                old_drink_water += y.toFloat()
            }
        }

        val arr_data22 = dh!!.getdata(
            "tbl_drink_details",
            "DrinkDate ='$custom_date'", "id", 1
        )

        var total_drink = 0.0

        if (arr_data22.size > 0) {
            total_drink = if (URLFactory.WATER_UNIT_VALUE.equals(
                    "ml",
                    ignoreCase = true
                )
            ) arr_data22[0]["TodayGoal"]!!
                .toDouble()
            else arr_data22[0]["TodayGoalOZ"]!!.toDouble()
        }

        val arr_data = dh!!.getdata(
            "tbl_drink_details",
            "DrinkDate ='$custom_date'"
        )

        drink_water = 0f
        for (k in arr_data.indices) {
            drink_water += if (URLFactory.WATER_UNIT_VALUE.equals(
                    "ml",
                    ignoreCase = true
                )
            ) arr_data[k]["ContainerValue"]!!
                .toInt()
            else arr_data[k]["ContainerValueOZ"]!!.toInt()
        }

        if (custom_date.equals(
                dth!!.getCurrentDate(URLFactory.DATE_FORMAT),
                ignoreCase = true
            )
        ) URLFactory.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater
        else if (total_drink > 0) URLFactory.DAILY_WATER_VALUE = ("" + total_drink).toFloat()
        else URLFactory.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater

        binding.lblTotalDrunk.text = AppUtils.getData("" + (drink_water).toInt() + " " + URLFactory.WATER_UNIT_VALUE)
        binding.lblTotalGoal.text = AppUtils.getData("" + (URLFactory.DAILY_WATER_VALUE).toInt() + " " + URLFactory.WATER_UNIT_VALUE)

        refresh_bottle(false, false)
    }

    private fun refresh_bottle(isFromCurrentProgress: Boolean, isRegularAnimation: Boolean) {
        val animationDuration = (if (isRegularAnimation) 50 else 5).toLong()

        if (handler != null && runnable != null) handler!!.removeCallbacks(runnable!!)

        btnclick = false

        cp = progress_bottle_height
        np = Math.round((drink_water * max_bottle_height) / URLFactory.DAILY_WATER_VALUE)

        if (cp <= np && isFromCurrentProgress) {
            binding.animationView.visibility = VISIBLE
            runnable = Runnable {
                if (cp > max_bottle_height) {
                    btnclick = true
                    callDialog()
                } else if (cp < np) {
                    cp += 6
                    binding.contentFrame.layoutParams.height = cp
                    binding.contentFrame.requestLayout()
                    handler!!.postDelayed(runnable!!, animationDuration)
                } else {
                    btnclick = true
                    callDialog()
                }
            }
            handler = Handler()
            handler!!.postDelayed(runnable!!, animationDuration)
        } else if (np == 0) {
            binding.animationView.visibility = GONE
            binding.contentFrame.layoutParams.height = np
            binding.contentFrame.requestLayout()
            btnclick = true
            callDialog()
        } else {
            binding.contentFrame.layoutParams.height = 0
            cp = 0
            binding.animationView.visibility = VISIBLE
            runnable = Runnable {
                if (cp > max_bottle_height) {
                    btnclick = true
                    callDialog()
                } else if (cp < np) {
                    cp += 6
                    binding.contentFrame.layoutParams.height = cp
                    binding.contentFrame.requestLayout()
                    handler!!.postDelayed(runnable!!, animationDuration)
                } else {
                    btnclick = true
                    callDialog()
                }
            }
            handler = Handler()
            handler!!.postDelayed(runnable!!, animationDuration)
        }

        progress_bottle_height = np

        if (np > 0) binding.animationView.visibility = VISIBLE
        else binding.animationView.visibility = GONE
    }

    private fun callDialog() {
        if (old_drink_water < URLFactory.DAILY_WATER_VALUE) {
            if (drink_water >= URLFactory.DAILY_WATER_VALUE) {
                showDailyGoalReachedDialog()
            }
        }
        old_drink_water = drink_water
    }

    private fun showDailyGoalReachedDialog() {

        safeNavController?.safeNavigate(
            DrinkFragmentDirections
                .actionDrinkFragmentToReachedGoalDialogFragment(drink_water,dateNow)
        )
    }


    @SuppressLint("SetTextI18n")
    private fun body() {
        val arr_data = dh!!.getdata(
            "tbl_drink_details",
            "DrinkDate ='" + dth!!.getCurrentDate(URLFactory.DATE_FORMAT) + "'"
        )
        old_drink_water = 0f
        for (k in arr_data.indices) {

            if (URLFactory.WATER_UNIT_VALUE.equals(
                    "ml",
                    ignoreCase = true
                )
            ){
                val x = ("" + arr_data[k]["ContainerValue"]).toDouble()
                old_drink_water += x.toFloat()
            }
            else {
                val y = ("" + arr_data[k]["ContainerValueOZ"]).toDouble()
                old_drink_water += y.toFloat()
            }
            
        }

        count_today_drink(false)
        
        binding.selectedContainerBlock.setOnClickListener{ openChangeContainerPicker() }
        
        binding.openHistory.setOnClickListener {
            safeNavController?.safeNavigate(
                DrinkFragmentDirections.actionDrinkFragmentToHistoryFragment()
            )
        }

        binding.openReached.setOnClickListener {
            safeNavController?.safeNavigate(
                DrinkFragmentDirections.actionDrinkFragmentToReachedFragment()
            )
        }
        
        binding.addWater.setOnClickListener {
            if (containerArrayList.size > 0) {
                if (!dth!!.getDate(filter_cal!!.timeInMillis, URLFactory.DATE_FORMAT)
                        .equals(
                            dth!!.getDate(today_cal!!.timeInMillis, URLFactory.DATE_FORMAT),
                            ignoreCase = true
                        )
                ) {
                    return@setOnClickListener
                }

                if (!btnclick) return@setOnClickListener

                btnclick = false

                val random = Random()

                if (random.nextBoolean()) {
                    URLFactory.RELOAD_DASHBOARD = false
                    if (URLFactory.LOAD_VIDEO_ADS) {
                        execute_add_water()
                        URLFactory.RELOAD_DASHBOARD = true
                    } else {
                        execute_add_water()
                        URLFactory.RELOAD_DASHBOARD = true
                    }
                } else {
                    execute_add_water()
                }
            }
        }

        load_all_container()

        val unit = SharedPreferencesManager.waterUnit

        if (unit.equals("ml", ignoreCase = true)) {
            binding.containerName.text = "${containerArrayList[selected_pos].containerValue} $unit"
            if (containerArrayList[selected_pos].isCustom) Glide.with(requireContext())
                .load(R.drawable.ic_custom_ml).into(binding.imgSelectedContainer)
            else Glide.with(requireContext())
                .load(getImage(containerArrayList[selected_pos].containerValue!!))
                .into(binding.imgSelectedContainer)
        } else {
            binding.containerName.text =
                "${containerArrayList[selected_pos].containerValueOZ} $unit"
            if (containerArrayList[selected_pos].isCustom) Glide.with(requireContext())
                .load(R.drawable.ic_custom_ml).into(binding.imgSelectedContainer)
            else Glide.with(requireContext())
                .load(getImage(containerArrayList[selected_pos].containerValueOZ!!))
                .into(binding.imgSelectedContainer)
        }

        adapter =
            ContainerAdapterNew(requireActivity(), containerArrayList, object : ContainerAdapterNew.CallBack {
                @SuppressLint("NotifyDataSetChanged")
                override fun onClickSelect(container: Container?, position: Int) {
                    bottomSheetDialog!!.dismiss()

                    selected_pos = position

                    SharedPreferencesManager.selectedContainer = container!!.containerId!!.toInt()

                    for (k in containerArrayList.indices) {
                        containerArrayList[k].isSelected(false)
                    }

                    containerArrayList[position].isSelected(true)

                    adapter!!.notifyDataSetChanged()

                    val unit = SharedPreferencesManager.waterUnit

                    if (unit.equals("ml", ignoreCase = true)) {
                        binding.containerName.text = "${container.containerValue} $unit"
                        if (container.isCustom) Glide.with(requireContext()).load(R.drawable.ic_custom_ml)
                            .into(binding.imgSelectedContainer)
                        else Glide.with(requireContext()).load(getImage(container.containerValue!!))
                            .into(binding.imgSelectedContainer)
                    } else {
                        binding.containerName.text = "${container.containerValueOZ} $unit"
                        if (container.isCustom) Glide.with(requireContext()).load(R.drawable.ic_custom_ml)
                            .into(binding.imgSelectedContainer)
                        else Glide.with(requireContext()).load(getImage(container.containerValueOZ!!))
                            .into(binding.imgSelectedContainer)
                    }
                }
            })
    }

    private fun count_today_drink(isRegularAnimation: Boolean) {
        val arr_data = dh!!.getdata(
            "tbl_drink_details",
            "DrinkDate ='" + dth!!.getDate(filter_cal!!
                .timeInMillis, URLFactory.DATE_FORMAT) + "'"
        )

        drink_water = 0f
        for (k in arr_data.indices) {
            if (URLFactory.WATER_UNIT_VALUE.equals(
                    "ml",
                    ignoreCase = true
                )
            ){
                val x = ("" + arr_data[k]["ContainerValue"]).toDouble()
                drink_water += x.toFloat()
            }
            else {
                val y = ("" + arr_data[k]["ContainerValueOZ"]).toDouble()
                drink_water += y.toFloat()
            }
        }

        binding.lblTotalDrunk.text = AppUtils.getData("" +
                (drink_water).toInt() + " " + URLFactory.WATER_UNIT_VALUE)
        binding.lblTotalGoal.text = AppUtils.getData("" +
                (URLFactory.DAILY_WATER_VALUE).toInt() + " " + URLFactory.WATER_UNIT_VALUE)

        refresh_bottle(true, isRegularAnimation)
    }

    private fun openChangeContainerPicker() {
        bottomSheetDialog = BottomSheetDialog(requireActivity())
        
        bottomSheetDialog!!.setOnShowListener(OnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                        as FrameLayout?
                    ?: return@OnShowListener
            val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
            bottomSheet.background = null
        })

        val layoutInflater = LayoutInflater.from(requireActivity())
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_change_container,
            null, false)

        val containerRecyclerViewN = view.findViewById<RecyclerView>(R.id.containerRecyclerView)
        val add_custom_container = view.findViewById<RelativeLayout>(R.id.add_custom_container)

        val manager = GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL,
            false)
        containerRecyclerViewN.layoutManager = manager
        containerRecyclerViewN.adapter = adapter

        add_custom_container.setOnClickListener {
            bottomSheetDialog!!.dismiss()
            openCustomContainerPicker()
        }

        bottomSheetDialog!!.setContentView(view)

        bottomSheetDialog!!.show()
    }

    @SuppressLint("InflateParams", "SetTextI18n", "NotifyDataSetChanged", "Recycle")
    private fun openCustomContainerPicker() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View = LayoutInflater.from(requireActivity())
            .inflate(R.layout.bottom_sheet_add_custom_container, null, false)

        val btn_cancel = view.findViewById<RelativeLayout>(R.id.btn_cancel)
        val btn_add = view.findViewById<RelativeLayout>(R.id.btn_add)
        val img_cancel = view.findViewById<ImageView>(R.id.img_cancel)

        val txt_value = view.findViewById<AppCompatEditText>(R.id.txt_value)
        val lbl_unit = view.findViewById<AppCompatTextView>(R.id.lbl_unit)

        lbl_unit.text =
            sh!!.get_string(R.string.str_capacity).replace("$1", URLFactory.WATER_UNIT_VALUE)

        if (URLFactory.WATER_UNIT_VALUE.equals("ml", ignoreCase = true)) txt_value.filters =
            arrayOf<InputFilter>(InputFilterWeightRange(1.0, 8000.0))
        else txt_value.filters = arrayOf<InputFilter>(InputFilterWeightRange(1.0, 270.0))

        txt_value.requestFocus()

        btn_cancel.setOnClickListener { dialog.cancel() }

        img_cancel.setOnClickListener { dialog.cancel() }

        btn_add.setOnClickListener {
            if (sh!!.check_blank_data(txt_value.text.toString().trim { it <= ' ' })) {
                ah!!.customAlert(sh!!.get_string(R.string.str_enter_value_validation))
            } else if (txt_value.text.toString().trim { it <= ' ' }.toInt() == 0) {
                ah!!.customAlert(sh!!.get_string(R.string.str_enter_value_validation))
            } else {
                var tml = 0.0
                var tfloz = 0.0
                if (URLFactory.WATER_UNIT_VALUE.equals("ml", ignoreCase = true)) {
                    tml = txt_value.text.toString().trim { it <= ' ' }.toFloat().toDouble()
                    tfloz = mlToOzConverter(tml)
                } else {
                    tfloz = txt_value.text.toString().trim { it <= ' ' }.toFloat().toDouble()
                    tml = ozToMlConverter(tfloz)
                }

                d("HeightWeightHelper", "$tml @@@ $tfloz")
                
                val c = Constant.SDB!!.rawQuery(
                    "SELECT MAX(ContainerID) FROM tbl_container_details",
                    null
                )
                var nextContainerID = 0

                try {
                    if (c.count > 0) {
                        c.moveToNext()
                        nextContainerID = c.getString(0).toInt() + 1
                    }
                } catch (e: java.lang.Exception) {
                    e.message?.let { it1 -> e(Throwable(e), it1) }
                }

                val initialValues = ContentValues()

                initialValues.put("ContainerID", "" + nextContainerID)
                initialValues.put("ContainerValue", "" + Math.round(tml))
                initialValues.put("ContainerValueOZ", "" + Math.round(tfloz))
                initialValues.put("IsOpen", "1")
                initialValues.put("IsCustom", "1")

                dh!!.INSERT("tbl_container_details", initialValues)

                load_all_container()

                SharedPreferencesManager.selectedContainer = nextContainerID

                var tmp_pos = -1

                for (k in containerArrayList.indices) {
                    try {
                        if (nextContainerID == containerArrayList[k].containerId!!.toInt()) {
                            containerArrayList[k].isSelected(true)
                            tmp_pos = k
                        } else containerArrayList[k].isSelected(false)
                    } catch (e: java.lang.Exception) {
                        containerArrayList[k].isSelected(false)
                    }
                }


                val unit = SharedPreferencesManager.waterUnit

                if (tmp_pos >= 0) {
                    selected_pos = tmp_pos

                    val menu = containerArrayList[tmp_pos]

                    if (unit.equals("ml", ignoreCase = true)) {
                        binding.containerName.text = "" + menu.containerValue + " " + unit
                        if (menu.isCustom) Glide.with(requireContext()).load(R.drawable.ic_custom_ml)
                            .into(binding.imgSelectedContainer)
                        else Glide.with(requireContext()).load(getImage(menu.containerValue!!))
                            .into(
                                binding.imgSelectedContainer
                            )
                    } else {
                        binding.containerName.text = "" + menu.containerValueOZ + " " + unit
                        if (menu.isCustom) Glide.with(requireContext()).load(R.drawable.ic_custom_ml)
                            .into(
                                binding.imgSelectedContainer
                            )
                        else Glide.with(requireContext()).load(getImage(menu.containerValueOZ!!))
                            .into(
                                binding.imgSelectedContainer
                            )
                    }
                }
                adapter!!.notifyDataSetChanged()

                dialog.dismiss()
            }
        }
        dialog.setContentView(view)

        dialog.show()
    }

    private fun execute_add_water() {

        if (URLFactory.WATER_UNIT_VALUE.equals("ml", ignoreCase = true)
            && drink_water > 8000
        ) {
            showDailyMoreThanTargetDialog()
            btnclick = true
            return
        } else if (!(URLFactory.WATER_UNIT_VALUE.equals("ml", ignoreCase = true))
            && drink_water > 270
        ) {
            showDailyMoreThanTargetDialog()
            btnclick = true
            return
        }

        var count_drink_after_add_current_water = drink_water

        if (URLFactory.WATER_UNIT_VALUE.equals(
                "ml",
                ignoreCase = true
            )
        ) count_drink_after_add_current_water += 
            ("" + containerArrayList[selected_pos].containerValue).toFloat()
        else if (!(URLFactory.WATER_UNIT_VALUE.equals(
                "ml",
                ignoreCase = true
            ))
        ) count_drink_after_add_current_water += 
            ("" + containerArrayList[selected_pos].containerValueOZ).toFloat()


        d(
            "above8000", ("" + URLFactory.WATER_UNIT_VALUE + " @@@  " + drink_water
                    + " @@@ " + count_drink_after_add_current_water)
        )

        if (URLFactory.WATER_UNIT_VALUE.equals("ml", ignoreCase = true)
            && count_drink_after_add_current_water > 8000
        ) {
            if (drink_water >= 8000) showDailyMoreThanTargetDialog()
            else if (URLFactory.DAILY_WATER_VALUE < (8000 - 
                        ("" + containerArrayList[selected_pos].containerValue).toFloat())) 
                showDailyMoreThanTargetDialog()
        } else if (!(URLFactory.WATER_UNIT_VALUE.equals("ml", ignoreCase = true))
            && count_drink_after_add_current_water > 270
        ) {
            if (drink_water >= 270) showDailyMoreThanTargetDialog()
            else if (URLFactory.DAILY_WATER_VALUE < (270 - 
                        ("" + containerArrayList[selected_pos].containerValueOZ).toFloat()))
                showDailyMoreThanTargetDialog()
        }

        if (drink_water == 8000f && 
            URLFactory.WATER_UNIT_VALUE.equals("ml", ignoreCase = true)) {
            btnclick = true
            return
        } else if (drink_water == 270f && !URLFactory.WATER_UNIT_VALUE.equals(
                "ml",
                ignoreCase = true
            )
        ) {
            btnclick = true
            return
        }


        if (!SharedPreferencesManager.disableSoundWhenAddWater) {
            ringtone!!.stop()
            ringtone!!.play()
        }

        val initialValues = ContentValues()

        initialValues.put("ContainerValue", "" + containerArrayList[selected_pos].containerValue)
        initialValues.put(
            "ContainerValueOZ",
            "" + containerArrayList[selected_pos].containerValueOZ
        )
        initialValues.put(
            "DrinkDate",
            "" + dth!!.getDate(filter_cal!!.timeInMillis, URLFactory.DATE_FORMAT)
        )
        initialValues.put("DrinkTime", "" + dth!!.getCurrentTime(true))
        initialValues.put(
            "DrinkDateTime", ("" + dth!!.getDate(filter_cal!!.timeInMillis, URLFactory.DATE_FORMAT)
                    + " " + dth!!.getCurrentDate("HH:mm:ss"))
        )

        if (URLFactory.WATER_UNIT_VALUE.equals("ml", ignoreCase = true)) {
            initialValues.put("TodayGoal", "" + URLFactory.DAILY_WATER_VALUE)
            initialValues.put(
                "TodayGoalOZ",
                "" + mlToOzConverter(URLFactory.DAILY_WATER_VALUE.toDouble())
            )
        } else {
            initialValues.put(
                "TodayGoal",
                "" + ozToMlConverter(URLFactory.DAILY_WATER_VALUE.toDouble())
            )
            initialValues.put("TodayGoalOZ", "" + URLFactory.DAILY_WATER_VALUE)
        }

        dh!!.INSERT("tbl_drink_details", initialValues)


        count_today_drink(true)


        val intent = Intent(requireActivity(), NewAppWidget::class.java)
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        val ids = AppWidgetManager.getInstance(requireActivity()).getAppWidgetIds(
            ComponentName(
                requireActivity(),
                NewAppWidget::class.java
            )
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        requireActivity().sendBroadcast(intent)
    }

    private fun showDailyMoreThanTargetDialog() {
        safeNavController?.safeNavigate(
            DrinkFragmentDirections.actionDrinkFragmentToDailyMoreThanTargetDialogFragment()
        )
    }

    private fun load_all_container() {
        containerArrayList.clear()

        val arr_container = dh!!.getdata("tbl_container_details", "IsCustom", 1)

        var selected_container_id = "1"

        selected_container_id = if (SharedPreferencesManager.selectedContainer == 0) "1"
        else "" + SharedPreferencesManager.selectedContainer

        for (k in arr_container.indices) {
            val container = Container()
            container.containerId = arr_container[k]["ContainerID"]
            container.containerValue = arr_container[k]["ContainerValue"]
            container.containerValueOZ = arr_container[k]["ContainerValueOZ"]
            container.isOpen(
                arr_container[k]["IsOpen"].equals(
                    "1",
                    ignoreCase = true
                )
            )
            container.isSelected(
                selected_container_id.equals(
                    arr_container[k]["ContainerID"],
                    ignoreCase = true
                )
            )
            container.isCustom(
                arr_container[k]["IsCustom"].equals(
                    "1",
                    ignoreCase = true
                )
            )
            if (container.isSelected) selected_pos = k //+1

            containerArrayList.add(container)
        }
    }

    fun getImage(`val`: String): Int {
        var drawable: Int = R.drawable.ic_custom_ml

        if (URLFactory.WATER_UNIT_VALUE.equals("ml", ignoreCase = true)) {
            if (`val`.toFloat() == 50f) drawable = R.drawable.ic_50_ml
            else if (`val`.toFloat() == 100f) drawable = R.drawable.ic_100_ml
            else if (`val`.toFloat() == 150f) drawable = R.drawable.ic_150_ml
            else if (`val`.toFloat() == 200f) drawable = R.drawable.ic_200_ml
            else if (`val`.toFloat() == 250f) drawable = R.drawable.ic_250_ml
            else if (`val`.toFloat() == 300f) drawable = R.drawable.ic_300_ml
            else if (`val`.toFloat() == 500f) drawable = R.drawable.ic_500_ml
            else if (`val`.toFloat() == 600f) drawable = R.drawable.ic_600_ml
            else if (`val`.toFloat() == 700f) drawable = R.drawable.ic_700_ml
            else if (`val`.toFloat() == 800f) drawable = R.drawable.ic_800_ml
            else if (`val`.toFloat() == 900f) drawable = R.drawable.ic_900_ml
            else if (`val`.toFloat() == 1000f) drawable = R.drawable.ic_1000_ml
        } else {
            if (`val`.toFloat() == 2f) drawable = R.drawable.ic_50_ml
            else if (`val`.toFloat() == 3f) drawable = R.drawable.ic_100_ml
            else if (`val`.toFloat() == 5f) drawable = R.drawable.ic_150_ml
            else if (`val`.toFloat() == 7f) drawable = R.drawable.ic_200_ml
            else if (`val`.toFloat() == 8f) drawable = R.drawable.ic_250_ml
            else if (`val`.toFloat() == 10f) drawable = R.drawable.ic_300_ml
            else if (`val`.toFloat() == 17f) drawable = R.drawable.ic_500_ml
            else if (`val`.toFloat() == 20f) drawable = R.drawable.ic_600_ml
            else if (`val`.toFloat() == 24f) drawable = R.drawable.ic_700_ml
            else if (`val`.toFloat() == 27f) drawable = R.drawable.ic_800_ml
            else if (`val`.toFloat() == 30f) drawable = R.drawable.ic_900_ml
            else if (`val`.toFloat() == 34f) drawable = R.drawable.ic_1000_ml
        }

        return drawable
    }

    private fun getAllReminderData() {
        val reminder_data: MutableList<NextReminderModel> = ArrayList()

        val arr_data = dh!!.getdata("tbl_alarm_details")

        for (k in arr_data.indices) {
            if (arr_data[k]["AlarmType"].equals("R", ignoreCase = true)) {
                if (!SharedPreferencesManager.isManualReminder) {
                    val arr_data2 =
                        dh!!.getdata("tbl_alarm_sub_details",
                            "SuperId='" + arr_data[k]["id"] + "'")
                    for (j in arr_data2.indices) {
                        reminder_data.add(
                            NextReminderModel(
                                getMillisecond(arr_data2[j]["AlarmTime"]!!),
                                arr_data2[j]["AlarmTime"]!!
                            )
                        )
                    }
                }
            } else {
                if (SharedPreferencesManager.isManualReminder) {
                    if (arr_data[k]["IsOff"].equals("0", ignoreCase = true)) {
                        reminder_data.add(
                            NextReminderModel(
                                getMillisecond(arr_data[k]["AlarmTime"]!!),
                                arr_data[k]["AlarmTime"]!!
                            )
                        )
                    }
                }
            }
        }

        val mDate = Date()
        reminder_data.sort()
        var tmp_pos = 0
        for (k in reminder_data.indices) {
            if (reminder_data[k].millisecond > mDate.time) {
                tmp_pos = k
                break
            }
        }

        binding.nextReminderBlock.visibility = VISIBLE

        if (reminder_data.size > 0) {

            binding.lblNextReminder.text = sh!!.get_string(R.string.str_next_reminder)
                .replace("$1", reminder_data[tmp_pos].time)
        } else binding.nextReminderBlock.visibility = View.INVISIBLE
    }

    private fun getMillisecond(givenDateString: String): Long {
        var givenDateString = givenDateString
        var timeInMilliseconds: Long = 0

        givenDateString = dth!!.getFormatDate("yyyy-MM-dd") + " " + givenDateString

        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US)
        try {
            val mDate = sdf.parse(givenDateString)
            if (mDate != null) {
                timeInMilliseconds = mDate.time
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            e.message?.let { e(Throwable(e), it) }
            ah!!.Show_Alert_Dialog(e.message)
        }

        return timeInMilliseconds
    }

    private fun fetchNextReminder() {
        runnableReminder = Runnable {
            getAllReminderData()
            handlerReminder!!.postDelayed(runnableReminder!!, 1000)
        }
        handlerReminder = Handler()
        handlerReminder!!.postDelayed(runnableReminder!!, 1000)
    }
}