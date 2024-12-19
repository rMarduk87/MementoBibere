package rpt.tool.mementobibere.ui.userinfo.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.FragmentUserProfileBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.helpers.HeightWeightHelper.lbToKgConverter
import rpt.tool.mementobibere.utils.helpers.HeightWeightHelper.mlToOzConverter
import rpt.tool.mementobibere.utils.log.d
import rpt.tool.mementobibere.utils.log.e
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import rpt.tool.mementobibere.utils.navigation.safeNavController
import rpt.tool.mementobibere.utils.navigation.safeNavigate
import rpt.tool.mementobibere.widget.NewAppWidget
import java.io.File


class UserProfileFragment:BaseFragment<FragmentUserProfileBinding>(FragmentUserProfileBinding::inflate) {

    var weight_kg_lst: MutableList<String> = ArrayList()
    var weight_lb_lst: MutableList<String> = ArrayList()
    var height_cm_lst: MutableList<String> = ArrayList()
    var height_feet_lst: MutableList<String> = ArrayList()
    val PICK_Camera_IMAGE: Int = 2
    val SELECT_FILE1: Int = 1
    var imageUri: Uri? = null
    var selectedImage: Uri? = null
    private var selectedImagePath: String? = null
    var mDropdown: PopupWindow? = null
    var mDropdownWeather: PopupWindow? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.navigationBarColor =
            requireContext().resources.getColor(R.color.water_color)

        AppUtils.convertUpperCase(binding.lblGender)
        AppUtils.convertUpperCase(binding.lblWeight)
        AppUtils.convertUpperCase(binding.lblHeight)
        AppUtils.convertUpperCase(binding.lblGoal)
        AppUtils.convertUpperCase(binding.lblActive)
        AppUtils.convertUpperCase(binding.lblBloodDonor)
        AppUtils.convertUpperCase(binding.lblPregnant)
        AppUtils.convertUpperCase(binding.lblBreastfeeding)
        AppUtils.convertUpperCase(binding.lblWeather)
        AppUtils.convertUpperCase(binding.lblOtherFactor)

        binding.txtUserName.text = SharedPreferencesManager.userName
        binding.txtGender.text = if (SharedPreferencesManager.userGender) sh!!.get_string(R.string.str_female) else sh!!.get_string(
            R.string.str_male
        )
        loadPhoto()

        val str: String = SharedPreferencesManager.personHeight + " " +
                (if (SharedPreferencesManager.heightUnit) "cm" else "feet")
        binding.txtHeight.text = str

        val str2: String =
            (if (SharedPreferencesManager.heightUnit) URLFactory.decimalFormat2.format(
                SharedPreferencesManager.personWeight.toDouble()
            ) + " kg" else SharedPreferencesManager.personWeight + " lb")
        binding.txtWeight.text = str2

        val str3: String = (AppUtils.getData("" + URLFactory.DAILY_WATER_VALUE.toInt()) + " "
                + (if (SharedPreferencesManager.heightUnit) "ml" else "fl oz"))
        binding.txtGoal.text = str3

        header()
        body()

        init_WeightKG()
        init_WeightLB()
        init_HeightCM()
        init_HeightFeet()
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
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e), it) }
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

    private fun header() {
        binding.headerBlock.lblToolbarTitle.text = sh!!.get_string(R.string.str_my_profile)
        binding.headerBlock.leftIconBlock.setOnClickListener{ finish() }
        binding.headerBlock.rightIconBlock.visibility = View.GONE
    }

    private fun finish() {
        safeNavController?.safeNavigate(UserProfileFragmentDirections
            .actionUserProfileFragmentToDrinkFragment())
    }

    private fun body() {
        binding.changeProfile.setOnClickListener {
            openPicker()
        }

        binding.genderBlock.setOnClickListener{ v ->
            initiatePopupWindow(v)
        }

        binding.editUserNameBlock.setOnClickListener { openNameDialog() }
        
        binding.goalBlock.setOnClickListener{ showSetManuallyGoalDialog() }
        binding.heightBlock.setOnClickListener{ openHeightDialog() }
        binding.weightBlock.setOnClickListener{ openWeightDialog() }
        binding.switchActive.setChecked(SharedPreferencesManager.isActive)
        binding.switchActive.setOnCheckedChangeListener { _,
         isChecked ->
            SharedPreferencesManager.isActive = isChecked
            val tmp_weight = "" + SharedPreferencesManager.personWeight
            val isFemale: Boolean = SharedPreferencesManager.userGender
            val min = (if (SharedPreferencesManager.heightUnit) 900 else 30).toFloat()
            val max = (if (SharedPreferencesManager.heightUnit) 8000 else 270).toFloat()
            val weatherIdx: Int = SharedPreferencesManager.climate

            d("maxmaxmaxmax : ", "$max @@@ $min  @@@  $tmp_weight")

            var tmp_kg = 0.0
            tmp_kg = if (SharedPreferencesManager.heightUnit) {
                ("" + tmp_weight).toDouble()
            } else {
                lbToKgConverter(tmp_weight.toDouble())
            }

            d("maxmaxmaxmax : ", "" + tmp_kg)

            var diff = 0.0

            diff = if (isFemale) tmp_kg * URLFactory.DEACTIVE_FEMALE_WATER
            else tmp_kg * URLFactory.DEACTIVE_MALE_WATER

            d("maxmaxmaxmax DIFF : ", "" + diff)

            diff *= if (weatherIdx == 1) URLFactory.WEATHER_CLOUDY
            else if (weatherIdx == 2) URLFactory.WEATHER_RAINY
            else if (weatherIdx == 3) URLFactory.WEATHER_SNOW
            else URLFactory.WEATHER_SUNNY


            d("maxmaxmaxmax : ", "" + diff + " @@@ " + URLFactory.DAILY_WATER_VALUE)

            if (isChecked) {
                if (SharedPreferencesManager.heightUnit) {
                    URLFactory.DAILY_WATER_VALUE += diff.toFloat()
                } else {
                    URLFactory.DAILY_WATER_VALUE += mlToOzConverter(diff).toFloat()
                }

                if (URLFactory.DAILY_WATER_VALUE > max) URLFactory.DAILY_WATER_VALUE = max
            } else {
                if (SharedPreferencesManager.heightUnit) {
                    URLFactory.DAILY_WATER_VALUE -= diff.toFloat()
                } else {
                    URLFactory.DAILY_WATER_VALUE -= mlToOzConverter(diff).toFloat()
                }

                if (URLFactory.DAILY_WATER_VALUE > max) URLFactory.DAILY_WATER_VALUE = max
            }

            URLFactory.DAILY_WATER_VALUE = Math.round(URLFactory.DAILY_WATER_VALUE).toFloat()

            val str: String = AppUtils.getData("" + URLFactory.DAILY_WATER_VALUE.toInt()) + " " +
                    (if (SharedPreferencesManager.heightUnit) "ml" else "fl oz")

            binding.txtGoal.text = str
            SharedPreferencesManager.dailyWater = URLFactory.DAILY_WATER_VALUE
        }

        binding.switchBloodDonor.setChecked(SharedPreferencesManager.bloodDonorKey)
        binding.switchBloodDonor.setOnCheckedChangeListener { _,isChecked ->
            SharedPreferencesManager.bloodDonorKey = isChecked
        }
        
        binding.switchBreastfeeding.setChecked(SharedPreferencesManager.isBreastfeeding)
        binding.switchBreastfeeding.setOnCheckedChangeListener{ _, isChecked ->
            SharedPreferencesManager.isBreastfeeding = isChecked
            setSwitchData(isChecked, URLFactory.BREASTFEEDING_WATER)
        }

        binding.switchPregnant.setChecked(SharedPreferencesManager.isPregnant)
        binding.switchPregnant.setOnCheckedChangeListener{ _, isChecked ->
            SharedPreferencesManager.isPregnant = isChecked
            setSwitchData(isChecked, URLFactory.PREGNANT_WATER)
        }

        binding.otherFactors.visibility = if (SharedPreferencesManager.userGender)
            View.VISIBLE else View.GONE

        val str = when(SharedPreferencesManager.climate){
            1-> sh!!.get_string(R.string.cloudy)
            2-> sh!!.get_string(R.string.rainy)
            3-> sh!!.get_string(R.string.snow)
            else-> sh!!.get_string(R.string.sunny)
        }
        binding.txtWeather.text = str

        binding.weatherBlock.setOnClickListener {
            initiateWeatherPopupWindow(
                binding.switchActive
            )
        }
        
        calculateActiveValue()
    }

    @SuppressLint("InflateParams")
    fun openPicker() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())

        val layoutInflater = LayoutInflater.from(requireActivity())
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_pick_image, null, false)

        val btnGallery = view.findViewById<AppCompatTextView>(R.id.btnGallery)
        val btnCamera = view.findViewById<AppCompatTextView>(R.id.btnCamera)
        val btnCancel = view.findViewById<AppCompatTextView>(R.id.btnCancel)
        val btnRemove = view.findViewById<AppCompatTextView>(R.id.btnRemove)

        val btnRemoveLine = view.findViewById<View>(R.id.btnRemoveLine)

        if (sh!!.check_blank_data(SharedPreferencesManager.userPhoto)) {
            btnRemove.visibility = View.GONE
            btnRemoveLine.visibility = View.GONE
        } else {
            btnRemove.visibility = View.VISIBLE
            btnRemoveLine.visibility = View.VISIBLE
        }

        btnGallery.setOnClickListener {
            selectImage()
        }

        btnCamera.setOnClickListener {
            captureImage()
        }

        btnRemove.setOnClickListener {
            bottomSheetDialog.dismiss()
            val dialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                .setMessage(sh!!.get_string(R.string.str_remove_photo_confirmation_message))
                .setPositiveButton(
                    sh!!.get_string(R.string.str_yes), { dialog, _ ->
                        dialog.dismiss()
                        SharedPreferencesManager.userPhoto = ""
                        loadPhoto()
                    }
                )
                .setNegativeButton(
                    sh!!.get_string(R.string.str_no), { dialog, _ -> dialog.dismiss() }
                )
            dialog.show()
        }

        btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }

        
        bottomSheetDialog.setContentView(view)

        bottomSheetDialog.show()
    }

    private fun setSwitchData(isChecked: Boolean, water: Double) {
        var diff = 0.0
        val min = (if (SharedPreferencesManager.heightUnit) 900 else 30).toFloat()
        val max = (if (SharedPreferencesManager.heightUnit) 8000 else 270).toFloat()

        diff = if (SharedPreferencesManager.heightUnit) water
        else mlToOzConverter(water)


        if (isChecked) {
            URLFactory.DAILY_WATER_VALUE += diff.toFloat()

            if (URLFactory.DAILY_WATER_VALUE > max) URLFactory.DAILY_WATER_VALUE = max
        } else {
            URLFactory.DAILY_WATER_VALUE -= diff.toFloat()

            if (URLFactory.DAILY_WATER_VALUE < min) URLFactory.DAILY_WATER_VALUE = min
        }


        URLFactory.DAILY_WATER_VALUE = Math.round(URLFactory.DAILY_WATER_VALUE).toFloat()

        val str: String = AppUtils.getData("" + URLFactory.DAILY_WATER_VALUE.toInt()) + " " +
                (if (SharedPreferencesManager.heightUnit) "ml" else "fl oz")

        binding.txtGoal.text = str
        SharedPreferencesManager.dailyWater = URLFactory.DAILY_WATER_VALUE

        calculateActiveValue()
    }


    private fun initiatePopupWindow(v: View): PopupWindow? {
        try {
            val mInflater = requireContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout: View = mInflater.inflate(R.layout.row_item_gender, null)
            
            val lbl_male = layout.findViewById<TextView>(R.id.lbl_male)
            lbl_male.text = sh!!.get_string(R.string.str_male)
            val lbl_female = layout.findViewById<TextView>(R.id.lbl_female)

            lbl_female.text = sh!!.get_string(R.string.str_female)

            lbl_male.setOnClickListener {
                SharedPreferencesManager.userGender = false
                loadPhoto()
                mDropdown!!.dismiss()
                binding.txtGender.text = sh!!.get_string(R.string.str_male)
                binding.otherFactors.visibility = View.GONE
                binding.switchBreastfeeding.setChecked(false)
                binding.switchPregnant.setChecked(false)
                calculate_goal()
            }

            lbl_female.setOnClickListener {
                SharedPreferencesManager.userGender = true
                loadPhoto()
                mDropdown!!.dismiss()
                binding.txtGender.text = sh!!.get_string(R.string.str_female)
                binding.otherFactors.visibility = View.VISIBLE
                calculate_goal()
            }


            layout.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            mDropdown = PopupWindow(
                layout, FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, true
            )

            mDropdown!!.showAsDropDown(v, 5, 5)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return mDropdown
    }

    @SuppressLint("InflateParams")
    private fun initiateWeatherPopupWindow(v: View): PopupWindow? {
        try {
            val mInflater = requireContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout: View = mInflater.inflate(R.layout.row_item_weather, null)
            
            val lbl_sunny = layout.findViewById<TextView>(R.id.lbl_sunny)
            lbl_sunny.text = sh!!.get_string(R.string.sunny)

            val lbl_cloudy = layout.findViewById<TextView>(R.id.lbl_cloudy)
            lbl_cloudy.text = sh!!.get_string(R.string.cloudy)

            val lbl_rainy = layout.findViewById<TextView>(R.id.lbl_rainy)
            lbl_rainy.text = sh!!.get_string(R.string.rainy)

            val lbl_snow = layout.findViewById<TextView>(R.id.lbl_snow)
            lbl_snow.text = sh!!.get_string(R.string.snow)

            lbl_sunny.setOnClickListener {
                SharedPreferencesManager.climate = 0
                mDropdownWeather!!.dismiss()
                binding.txtWeather.text = sh!!.get_string(R.string.sunny)
                calculate_goal()
            }

            lbl_cloudy.setOnClickListener {
                SharedPreferencesManager.climate = 1
                mDropdownWeather!!.dismiss()
                binding.txtWeather.text = sh!!.get_string(R.string.cloudy)
                calculate_goal()
            }

            lbl_rainy.setOnClickListener {
                SharedPreferencesManager.climate = 2
                mDropdownWeather!!.dismiss()
                binding.txtWeather.text = sh!!.get_string(R.string.rainy)
                calculate_goal()
            }

            lbl_snow.setOnClickListener {
                SharedPreferencesManager.climate = 3
                mDropdownWeather!!.dismiss()
                binding.txtWeather.text = sh!!.get_string(R.string.snow)
                calculate_goal()
            }


            layout.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            mDropdownWeather = PopupWindow(
                layout, FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, true
            )

            mDropdownWeather!!.showAsDropDown(v, 5, 5)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return mDropdownWeather
    }

    private fun openNameDialog() {
       safeNavController?.safeNavigate(UserProfileFragmentDirections
           .actionUserProfileToUserNameDialogFragment())
    }


    private fun showSetManuallyGoalDialog() {
        safeNavController?.safeNavigate(UserProfileFragmentDirections
            .actionUserProfileToSetManuallyGoalDialogFragment())
    }


    private fun openHeightDialog() {
        safeNavController?.safeNavigate(UserProfileFragmentDirections
            .actionUserProfileToUserHeghtDialogFragment())
    }


    @SuppressLint("SetTextI18n")
    fun openWeightDialog() {
        safeNavController?.safeNavigate(UserProfileFragmentDirections
            .actionUserProfileToUserWeightDialogFragment())
    }


    private fun calculate_goal() {
        val tmp_weight = "" + SharedPreferencesManager.personWeight

        val isFemale: Boolean = SharedPreferencesManager.userGender
        val isActive: Boolean = SharedPreferencesManager.isActive
        val isPregnant: Boolean = SharedPreferencesManager.isPregnant
        val isBreastfeeding: Boolean = SharedPreferencesManager.isBreastfeeding
        val weatherIdx: Int = SharedPreferencesManager.climate

        if (!sh!!.check_blank_data(tmp_weight)) {
            var tot_drink = 0.0
            var tmp_kg = 0.0
            tmp_kg = if (SharedPreferencesManager.heightUnit) {
                ("" + tmp_weight).toDouble()
            } else {
                lbToKgConverter(tmp_weight.toDouble())
            }

            tot_drink =
                if (isFemale) if (isActive) tmp_kg * URLFactory.ACTIVE_FEMALE_WATER 
                else tmp_kg * URLFactory.FEMALE_WATER
                else if (isActive) tmp_kg * URLFactory.ACTIVE_MALE_WATER else tmp_kg * 
                        URLFactory.MALE_WATER

            tot_drink *= if (weatherIdx == 1) URLFactory.WEATHER_CLOUDY
            else if (weatherIdx == 2) URLFactory.WEATHER_RAINY
            else if (weatherIdx == 3) URLFactory.WEATHER_SNOW
            else URLFactory.WEATHER_SUNNY

            if (isPregnant && isFemale) {
                tot_drink += URLFactory.PREGNANT_WATER
            }

            if (isBreastfeeding && isFemale) {
                tot_drink += URLFactory.BREASTFEEDING_WATER
            }

            if (tot_drink < 900) tot_drink = 900.0

            if (tot_drink > 8000) tot_drink = 8000.0

            val tot_drink_fl_oz = mlToOzConverter(tot_drink)

            if (SharedPreferencesManager.heightUnit) {
                URLFactory.DAILY_WATER_VALUE = tot_drink.toFloat()
            } else {
                URLFactory.DAILY_WATER_VALUE = tot_drink_fl_oz.toFloat()
            }

            URLFactory.DAILY_WATER_VALUE = Math.round(URLFactory.DAILY_WATER_VALUE).toFloat()

            val str: String = AppUtils.getData("" + URLFactory.DAILY_WATER_VALUE.toInt()) + " " +
                    (if (SharedPreferencesManager.heightUnit) "ml" else "fl oz")

            binding.txtGoal.text = str
            SharedPreferencesManager.dailyWater = URLFactory.DAILY_WATER_VALUE

            refreshWidget()

            calculateActiveValue()
        }
    }

    override fun onResume() {
        super.onResume()
        calculate_goal()
    }


    @SuppressLint("SetTextI18n")
    private fun calculateActiveValue() {
        var pstr = ""

        pstr = if (SharedPreferencesManager.heightUnit) {
            URLFactory.PREGNANT_WATER.toInt().toString() + " ml"
        } else {
            Math.round(mlToOzConverter(URLFactory.PREGNANT_WATER)).toInt().toString() + " fl oz"
        }

        binding.lblPregnant.text = sh!!.get_string(R.string.pregnant)
        AppUtils.convertUpperCase(binding.lblPregnant)
        binding.lblPregnant.text = binding.lblPregnant.getText().toString() + " (+" + pstr + ")"

        //====================================
        var bstr = ""

        bstr = if (SharedPreferencesManager.heightUnit) {
            URLFactory.BREASTFEEDING_WATER.toInt().toString() + " ml"
        } else {
            Math.round(mlToOzConverter(URLFactory.BREASTFEEDING_WATER)).toInt()
                .toString() + " fl oz"
        }

        binding.lblBreastfeeding.text = sh!!.get_string(R.string.breastfeeding)
        AppUtils.convertUpperCase(binding.lblBreastfeeding)
        binding.lblBreastfeeding.text = binding.lblBreastfeeding.getText().toString() +
                " (+" + bstr + ")"

        //====================================
        val tmp_weight = "" + SharedPreferencesManager.personWeight
        val isFemale: Boolean = SharedPreferencesManager.userGender
        val weatherIdx: Int = SharedPreferencesManager.climate

        var tmp_kg = 0.0
        tmp_kg = if (SharedPreferencesManager.heightUnit) {
            ("" + tmp_weight).toDouble()
        } else {
            lbToKgConverter(tmp_weight.toDouble())
        }

        //====================
        var diff = 0.0

        diff = if (isFemale) tmp_kg * URLFactory.DEACTIVE_FEMALE_WATER
        else tmp_kg * URLFactory.DEACTIVE_MALE_WATER


        //====================
        diff *= if (weatherIdx == 1) URLFactory.WEATHER_CLOUDY
        else if (weatherIdx == 2) URLFactory.WEATHER_RAINY
        else if (weatherIdx == 3) URLFactory.WEATHER_SNOW
        else URLFactory.WEATHER_SUNNY


        //====================
        bstr = ""

        bstr = if (SharedPreferencesManager.heightUnit) {
            Math.round(diff).toInt().toString() + " ml"
        } else {
            Math.round(mlToOzConverter(diff)).toInt().toString() + " fl oz"
        }

        binding.lblActive.text = sh!!.get_string(R.string.active)
        AppUtils.convertUpperCase(binding.lblActive)
        binding.lblActive.text = binding.lblActive.getText().toString() + " (+" + bstr + ")"
    }

    private fun refreshWidget() {
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


    //===============
    fun init_WeightKG() {
        weight_kg_lst.clear()
        var f = 30.0f
        weight_kg_lst.add("" + f)
        for (k in 0..199) {
            f += 0.5.toFloat()
            weight_kg_lst.add("" + f)
        }

        val st = arrayOfNulls<CharSequence>(weight_kg_lst.size)
        for (k in weight_kg_lst.indices) {
            st[k] = "" + weight_kg_lst[k]
        }
    }

    fun init_WeightLB() {
        weight_lb_lst.clear()
        for (k in 66..287) {
            weight_lb_lst.add("" + k)
        }

        val st = arrayOfNulls<CharSequence>(weight_lb_lst.size)
        for (k in weight_lb_lst.indices) {
            st[k] = "" + weight_lb_lst[k]
        }
    }


    //===============
    fun init_HeightCM() {
        height_cm_lst.clear()
        for (k in 60..240) {
            height_cm_lst.add("" + k)
        }

        val st = arrayOfNulls<CharSequence>(height_cm_lst.size)
        for (k in height_cm_lst.indices) {
            st[k] = "" + height_cm_lst[k]
        }
    }

    fun init_HeightFeet() {
        height_feet_lst.clear()
        height_feet_lst.add("2.0")
        height_feet_lst.add("2.1")
        height_feet_lst.add("2.2")
        height_feet_lst.add("2.3")
        height_feet_lst.add("2.4")
        height_feet_lst.add("2.5")
        height_feet_lst.add("2.6")
        height_feet_lst.add("2.7")
        height_feet_lst.add("2.8")
        height_feet_lst.add("2.9")
        height_feet_lst.add("2.10")
        height_feet_lst.add("2.11")
        height_feet_lst.add("3.0")
        height_feet_lst.add("3.1")
        height_feet_lst.add("3.2")
        height_feet_lst.add("3.3")
        height_feet_lst.add("3.4")
        height_feet_lst.add("3.5")
        height_feet_lst.add("3.6")
        height_feet_lst.add("3.7")
        height_feet_lst.add("3.8")
        height_feet_lst.add("3.9")
        height_feet_lst.add("3.10")
        height_feet_lst.add("3.11")
        height_feet_lst.add("4.0")
        height_feet_lst.add("4.1")
        height_feet_lst.add("4.2")
        height_feet_lst.add("4.3")
        height_feet_lst.add("4.4")
        height_feet_lst.add("4.5")
        height_feet_lst.add("4.6")
        height_feet_lst.add("4.7")
        height_feet_lst.add("4.8")
        height_feet_lst.add("4.9")
        height_feet_lst.add("4.10")
        height_feet_lst.add("4.11")
        height_feet_lst.add("5.0")
        height_feet_lst.add("5.1")
        height_feet_lst.add("5.2")
        height_feet_lst.add("5.3")
        height_feet_lst.add("5.4")
        height_feet_lst.add("5.5")
        height_feet_lst.add("5.6")
        height_feet_lst.add("5.7")
        height_feet_lst.add("5.8")
        height_feet_lst.add("5.9")
        height_feet_lst.add("5.10")
        height_feet_lst.add("5.11")
        height_feet_lst.add("6.0")
        height_feet_lst.add("6.1")
        height_feet_lst.add("6.2")
        height_feet_lst.add("6.3")
        height_feet_lst.add("6.4")
        height_feet_lst.add("6.5")
        height_feet_lst.add("6.6")
        height_feet_lst.add("6.7")
        height_feet_lst.add("6.8")
        height_feet_lst.add("6.9")
        height_feet_lst.add("6.10")
        height_feet_lst.add("6.11")
        height_feet_lst.add("7.0")
        height_feet_lst.add("7.1")
        height_feet_lst.add("7.2")
        height_feet_lst.add("7.3")
        height_feet_lst.add("7.4")
        height_feet_lst.add("7.5")
        height_feet_lst.add("7.6")
        height_feet_lst.add("7.7")
        height_feet_lst.add("7.8")
        height_feet_lst.add("7.9")
        height_feet_lst.add("7.10")
        height_feet_lst.add("7.11")
        height_feet_lst.add("8.0")

        val st = arrayOfNulls<CharSequence>(height_feet_lst.size)
        for (k in height_feet_lst.indices) {
            st[k] = "" + height_feet_lst[k]
        }
    }


    //===============================================
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_FILE1)
    }

    private fun captureImage() {
        getSaveImageUri()
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            imageUri
        )

        startActivityForResult(cameraIntent, PICK_Camera_IMAGE)
    }

    private fun getSaveImageUri() {
        try {
            val root: File = File(
                (Environment.getExternalStorageDirectory()
                    .toString() + "/" + URLFactory.APP_DIRECTORY_NAME + "/" + URLFactory.APP_PROFILE_DIRECTORY_NAME + "/")
            )
            if (!root.exists()) {
                root.mkdirs()
            }
            //String imageName = "profile_image_" + System.currentTimeMillis() + ".png";
            val imageName = "profile_image.png"

            val sdImageMainDirectory: File = File(root, imageName)


            imageUri = FileProvider.getUriForFile(
                requireActivity(),
                requireActivity().packageName + ".provider",
                sdImageMainDirectory
            )
            selectedImagePath = sdImageMainDirectory.absolutePath
        } catch (e: java.lang.Exception) {
            d("Incident Photo ", "Error occurred. Please try again later." + e.message)
        }
    }
}