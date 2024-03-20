package rpt.tool.mementobibere.ui.drink

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Ringtone
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuAdapter
import androidx.appcompat.widget.AppCompatEditText
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.WalkThroughtActivity
import rpt.tool.mementobibere.data.models.Container
import rpt.tool.mementobibere.data.models.SoundModel
import rpt.tool.mementobibere.databinding.NewDrinkFragmentBinding
import rpt.tool.mementobibere.java.userinfo.InitUserInfoActivity
import rpt.tool.mementobibere.ui.adapters.ContainerAdapterNew
import rpt.tool.mementobibere.ui.adapters.SoundAdapter
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.helpers.SqliteHelper
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import rpt.tool.mementobibere.utils.navigation.safeNavController
import rpt.tool.mementobibere.utils.navigation.safeNavigate
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Calendar

class NewDrinkFragment : BaseFragment<NewDrinkFragmentBinding>(NewDrinkFragmentBinding::inflate) {

    var menu_name: ArrayList<Menu> = ArrayList<Menu>()
    @SuppressLint("RestrictedApi")
    var menuAdapter: MenuAdapter? = null
    var filter_cal: Calendar? = null
    var today_cal: Calendar? = null
    var yesterday_cal: Calendar? = null
    var lst_sounds: List<SoundModel> = ArrayList<SoundModel>()
    var soundAdapter: SoundAdapter? = null
    var containerArrayList: java.util.ArrayList<Container> = java.util.ArrayList<Container>()
    var adapter: ContainerAdapterNew? = null
    var drink_water = 0f
    var old_drink_water = 0f
    var selected_pos = 0
    var bottomSheetDialog: BottomSheetDialog? = null
    var handler: Handler? = null
    var runnable: Runnable? = null
    var handlerReminder: Handler? = null
    var runnableReminder: Runnable? = null
    var animationView: LottieAnimationView? = null
    var max_bottle_height = 870
    var progress_bottle_height = 0
    var cp = 0
    var np = 0
    var ringtone: Ringtone? = null
    var btnclick = true
    private lateinit var sqliteHelper: SqliteHelper
    private lateinit var dateNow: String
    private var refreshed: Boolean = false
    private var clicked: Int = 0
    private var counter: Int = 0
    private var selectedOption: Float? = null
    private var btnSelected: Int? = null
    private var intookToRefresh: Float = 0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sqliteHelper = SqliteHelper(requireContext())
        dateNow = AppUtils.getCurrentOnlyDate()!!
        super.onViewCreated(view, savedInstanceState)

        if(SharedPreferencesManager.bloodDonorKey==1 &&
            !sqliteHelper.getAvisDay(dateNow)){
            binding.avis.visibility = View.VISIBLE
        }
        else if(SharedPreferencesManager.bloodDonorKey==1 &&
            sqliteHelper.getAvisDay(dateNow)){
            binding.avis.visibility = View.VISIBLE
        }
        else{
            binding.avis.visibility = View.GONE
        }

        if (SharedPreferencesManager.firstRun) {
            startActivity(Intent(requireContext(), WalkThroughtActivity::class.java))
            requireActivity().finish()
        } else if (SharedPreferencesManager.totalIntake <= 0) {
            if (SharedPreferencesManager.totalIntake <= 0) {
                startActivity(Intent(requireContext(), InitUserInfoActivity::class.java))
                requireActivity().finish()
            }
        }

        if(!SharedPreferencesManager.isApplyConversion){
            ApplyConversion()
            SharedPreferencesManager.isApplyConversion = true
        }
    }

    private fun ApplyConversion() {
        SharedPreferencesManager.weightS = SharedPreferencesManager.weight.toString()
        if(SharedPreferencesManager.workType>1){
            SharedPreferencesManager.workType = 1
        }
        SharedPreferencesManager.wakeUpTimeS = AppUtils.convertData(SharedPreferencesManager.wakeUpTime)
        SharedPreferencesManager.sleepingTimeS = AppUtils.convertData(SharedPreferencesManager.sleepingTime)
        SharedPreferencesManager.wakeUpTimeHour = ((SharedPreferencesManager.wakeUpTime/ (1000 * 60 * 60) % 24).toInt())
        SharedPreferencesManager.sleepingTimeHour = ((SharedPreferencesManager.sleepingTime/ (1000 * 60 * 60) % 24).toInt())
        SharedPreferencesManager.wakeUpTimeMins = ((SharedPreferencesManager.wakeUpTime/ (1000 * 60) % 60).toInt())
        SharedPreferencesManager.sleepingTimeMins = ((SharedPreferencesManager.sleepingTime/ (1000 * 60) % 60).toInt())
    }


    override fun onStart() {
        super.onStart()

        if(sqliteHelper.getAvisDay(dateNow)){
            binding.lblNextReminder.setTextColor(resources.getColor(R.color.red))
            binding.lblTotalGoal.setTextColor(resources.getColor(R.color.red))
            binding.lblTotalDrunk.setTextColor(resources.getColor(R.color.red))
        }

        if(!SharedPreferencesManager.setWeight){
            safeNavController?.safeNavigate(NewDrinkFragmentDirections.actionDrinkFragmentToSelectWeightBottomSheetFragment())
        }

        if(!SharedPreferencesManager.setGender || !SharedPreferencesManager.setUserName){
            setFirstInfo()
        }

        if(!SharedPreferencesManager.setWorkOut){
            setOtherInfo()
        }

        if(!SharedPreferencesManager.setClimate){
            setClimate()
        }

        if(!SharedPreferencesManager.setBloodDonor){
            setBloodDonor()
        }

        if(!SharedPreferencesManager.setHeight){
            safeNavController?.safeNavigate(NewDrinkFragmentDirections.actionDrinkFragmentToSelectHeightBottomSheetFragment())
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setFirstInfo() {
        val li = LayoutInflater.from(requireContext())
        val promptsView = li.inflate(R.layout.custom_input_dialog2, null)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(promptsView)
        var male_block: RelativeLayout? = null
        var female_block:RelativeLayout? = null
        var img_male: ImageView? = null
        var img_female:ImageView? = null
        var isMaleGender = true
        var txt_user_name: AppCompatEditText? = null
        var isMale = false
        
        male_block = promptsView.findViewById<RelativeLayout>(R.id.male_block)
        img_male = promptsView.findViewById<ImageView>(R.id.img_male)

        female_block = promptsView.findViewById<RelativeLayout>(R.id.female_block)
        img_female = promptsView.findViewById<ImageView>(R.id.img_female)

        txt_user_name = promptsView.findViewById<AppCompatEditText>(R.id.txt_user_name)
        male_block.setOnClickListener(View.OnClickListener {
            SharedPreferencesManager.setManualGoal = false
            isMaleGender = true
            SharedPreferencesManager.gender = 0
            SharedPreferencesManager.isPregnant = false
            SharedPreferencesManager.isBreastfeeding = false
            male_block.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            img_male.setImageResource(R.drawable.ic_male_selected)
            female_block.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            img_female.setImageResource(R.drawable.ic_female_normal)
            SharedPreferencesManager.setGender = true})

        female_block.setOnClickListener(View.OnClickListener {
            SharedPreferencesManager.setManualGoal = false
            isMaleGender = false
            SharedPreferencesManager.gender = 1
            male_block.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            img_male.setImageResource(R.drawable.ic_male_normal)
            female_block.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            img_female.setImageResource(R.drawable.ic_female_selected)
            SharedPreferencesManager.setGender = true})

        txt_user_name.setText(SharedPreferencesManager.userName)

        txt_user_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                SharedPreferencesManager.userName = txt_user_name.getText().toString().trim { it <= ' ' }
                SharedPreferencesManager.setUserName = true
            }
        })

        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
            when (SharedPreferencesManager.gender) {
                -1 -> showMessage(getString(R.string.gender_hint), promptsView, true)
            }
            if(SharedPreferencesManager.userName.isNullOrEmpty()){
                showMessage(getString(R.string.please_input_a_valid_name),promptsView,true)
            }
            if(SharedPreferencesManager.userName.length < 3){
                showMessage(getString(R.string.please_input_a_valid_name_length),promptsView,true)
            }
        }.setNegativeButton("Cancel") { _, _ ->
            showMessage(getString(R.string.gender_hint), promptsView, true)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    
    private fun setBloodDonor() {
        val li = LayoutInflater.from(requireContext())
        val promptsView = li.inflate(R.layout.custom_input_dialog_, null)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(promptsView)
        var donor_block: RelativeLayout? = null
        var non_donor_block:RelativeLayout? = null
        var img_donor: ImageView? = null
        var img_non_donor:ImageView? = null

        donor_block = promptsView.findViewById<RelativeLayout>(R.id.donor_block)
        non_donor_block = promptsView.findViewById<RelativeLayout>(R.id.non_donor_block)
        img_donor = promptsView.findViewById<ImageView>(R.id.img_donor)
        img_non_donor = promptsView.findViewById<ImageView>(R.id.img_non_donor)

        donor_block.setOnClickListener(View.OnClickListener {
            val idx = 1
            SharedPreferencesManager.setManualGoal = false
            SharedPreferencesManager.setBloodDonor = true
            SharedPreferencesManager.bloodDonorKey = idx

            donor_block.background = if (idx == 1) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_donor.setImageResource(if (idx == 1) R.drawable.ic_donor_selected else R.drawable.ic_donor_normal)

            non_donor_block.background = if (idx == 0) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_non_donor.setImageResource(if (idx == 0) R.drawable.ic_non_donor_selected else R.drawable.ic_non_donor_normal) })

        non_donor_block.setOnClickListener(View.OnClickListener { 
            val idx = 0
            SharedPreferencesManager.setManualGoal = false
            SharedPreferencesManager.setBloodDonor = true
            SharedPreferencesManager.bloodDonorKey = idx

            donor_block.background = if (idx == 1) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_donor.setImageResource(if (idx == 1) R.drawable.ic_donor_selected else R.drawable.ic_donor_normal)

            non_donor_block.background = if (idx == 0) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_non_donor.setImageResource(if (idx == 0) R.drawable.ic_non_donor_selected else R.drawable.ic_non_donor_normal) })

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun setOtherInfo() {
        val li = LayoutInflater.from(requireContext())
        val promptsView = li.inflate(R.layout.custom_input_dialog4, null)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(promptsView)

        var block_for_female: LinearLayout? = null

        var active_block: RelativeLayout? = null
        var pregnant_block:RelativeLayout? = null
        var breastfeeding_block:RelativeLayout? = null
        var img_active: ImageView? = null
        var img_pregnant: ImageView? = null
        var img_breastfeeding: ImageView? = null

        block_for_female = promptsView.findViewById<LinearLayout>(R.id.block_for_female)

        active_block = promptsView.findViewById<RelativeLayout>(R.id.active_block)
        pregnant_block = promptsView.findViewById<RelativeLayout>(R.id.pregnant_block)
        breastfeeding_block = promptsView.findViewById<RelativeLayout>(R.id.breastfeeding_block)

        img_active = promptsView.findViewById<ImageView>(R.id.img_active)
        img_pregnant = promptsView.findViewById<ImageView>(R.id.img_pregnant)
        img_breastfeeding = promptsView.findViewById<ImageView>(R.id.img_breastfeeding)

        if (SharedPreferencesManager.isPregnant) {
            pregnant_block.background =
                requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            img_pregnant.setImageResource(R.drawable.pregnant_selected)
        } else {
            pregnant_block.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            img_pregnant.setImageResource(R.drawable.pregnant)
        }

        if (SharedPreferencesManager.isBreastfeeding) {
            breastfeeding_block.background =
                requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
            img_breastfeeding.setImageResource(R.drawable.breastfeeding_selected)
        } else {
            breastfeeding_block.background =
                requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
            img_breastfeeding.setImageResource(R.drawable.breastfeeding)
        }
        
        if (SharedPreferencesManager.gender == 1) // female
        {
            pregnant_block.setFocusableInTouchMode(true)
            pregnant_block.isClickable = true
            pregnant_block.isFocusable = true
            pregnant_block.setAlpha(1f)
            for (i in 0 until pregnant_block.childCount) {
                val child = pregnant_block.getChildAt(i)
                child.setEnabled(true)
            }
            breastfeeding_block.setFocusableInTouchMode(true)
            breastfeeding_block.isClickable = true
            breastfeeding_block.isFocusable = true
            breastfeeding_block.setAlpha(1f)
            for (i in 0 until breastfeeding_block.childCount) {
                val child = breastfeeding_block.getChildAt(i)
                child.setEnabled(true)
            }
        } else {
            pregnant_block.setFocusableInTouchMode(false)
            pregnant_block.isClickable = false
            pregnant_block.isFocusable = false
            pregnant_block.setAlpha(0.50f)
            for (i in 0 until pregnant_block.childCount) {
                val child = pregnant_block.getChildAt(i)
                child.setEnabled(false)
            }
            breastfeeding_block.setFocusableInTouchMode(false)
            breastfeeding_block.isClickable = false
            breastfeeding_block.isFocusable = false
            breastfeeding_block.setAlpha(0.50f)
            for (i in 0 until breastfeeding_block.childCount) {
                val child = breastfeeding_block.getChildAt(i)
                child.setEnabled(false)
            }
        }

        active_block.setOnClickListener(View.OnClickListener {
            if (SharedPreferencesManager.workType == 0) SharedPreferencesManager.workType = 1 else SharedPreferencesManager.workType = 0
            SharedPreferencesManager.setManualGoal = false

            if (SharedPreferencesManager.workType == 0) {
                active_block.background =
                    requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
                img_active.setImageResource(R.drawable.active_selected)
            } else {
                active_block.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
                img_active.setImageResource(R.drawable.active)
            }
            SharedPreferencesManager.setWorkOut = true
        })

        pregnant_block.setOnClickListener(View.OnClickListener {
            SharedPreferencesManager.isPregnant = !SharedPreferencesManager.isPregnant
            SharedPreferencesManager.setManualGoal = false

            if (SharedPreferencesManager.isPregnant) {
                pregnant_block.background =
                    requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
                img_pregnant.setImageResource(R.drawable.pregnant_selected)
            } else {
                pregnant_block.setBackground(
                    requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
                )
                img_pregnant.setImageResource(R.drawable.pregnant)
            }
            SharedPreferencesManager.setPregnantChoice = true
        })

        breastfeeding_block.setOnClickListener(View.OnClickListener {
            SharedPreferencesManager.isBreastfeeding = !SharedPreferencesManager.isBreastfeeding
            SharedPreferencesManager.setManualGoal = false

            if (SharedPreferencesManager.isBreastfeeding) {
                breastfeeding_block.background =
                    requireContext().resources.getDrawable(R.drawable.rdo_gender_select)
                img_breastfeeding.setImageResource(R.drawable.breastfeeding_selected)
            } else {
                breastfeeding_block.background = requireContext().resources.getDrawable(R.drawable.rdo_gender_regular)
                img_breastfeeding.setImageResource(R.drawable.breastfeeding)
            }

            SharedPreferencesManager.setBreastfeedingChoice = true
        })

        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
            when (SharedPreferencesManager.workType) {
                -1 -> showMessage(getString(R.string.work_type_hint), promptsView, true)
            }
        }.setNegativeButton("Cancel") { _, _ ->
            showMessage(getString(R.string.work_type_hint), promptsView, true)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setClimate() {
        val li = LayoutInflater.from(requireContext())
        val promptsView = li.inflate(R.layout.custom_input_dialog5, null)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(promptsView)
        var sunny_block: RelativeLayout? = null
        var cloudy_block:RelativeLayout? = null
        var rainy_block:RelativeLayout? = null
        var snow_block:RelativeLayout? = null
        var img_sunny: ImageView? = null
        var img_cloudy:android.widget.ImageView? = null
        var img_rainy:android.widget.ImageView? = null
        var img_snow:android.widget.ImageView? = null

        sunny_block = promptsView.findViewById<RelativeLayout>(R.id.sunny_block)
        cloudy_block = promptsView.findViewById<RelativeLayout>(R.id.cloudy_block)
        rainy_block = promptsView.findViewById<RelativeLayout>(R.id.rainy_block)
        snow_block = promptsView.findViewById<RelativeLayout>(R.id.snow_block)

        img_sunny = promptsView.findViewById<ImageView>(R.id.img_sunny)
        img_cloudy = promptsView.findViewById<ImageView>(R.id.img_cloudy)
        img_rainy = promptsView.findViewById<ImageView>(R.id.img_rainy)
        img_snow = promptsView.findViewById<ImageView>(R.id.img_snow)

        sunny_block.setOnClickListener(View.OnClickListener {
            val idx = 2
            SharedPreferencesManager.setManualGoal = false
            SharedPreferencesManager.climate = idx
            SharedPreferencesManager.setClimate = true

            sunny_block.background = if (idx == 0) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_sunny.setImageResource(if (idx == 0) R.drawable.sunny_selected else R.drawable.sunny)

            cloudy_block.background = if (idx == 1) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_cloudy.setImageResource(if (idx == 1) R.drawable.cloudy_selected else R.drawable.cloudy)

            rainy_block.background = if (idx == 2) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_rainy.setImageResource(if (idx == 2) R.drawable.rainy_selected else R.drawable.rainy)

            snow_block.background = if (idx == 3) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_snow.setImageResource(if (idx == 3) R.drawable.snow_selected else R.drawable.snow) })

        cloudy_block.setOnClickListener(View.OnClickListener { val idx = 0
            SharedPreferencesManager.setManualGoal = false
            SharedPreferencesManager.climate = idx
            SharedPreferencesManager.setClimate = true

            sunny_block.background = if (idx == 0) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_sunny.setImageResource(if (idx == 0) R.drawable.sunny_selected else R.drawable.sunny)

            cloudy_block.background = if (idx == 1) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_cloudy.setImageResource(if (idx == 1) R.drawable.cloudy_selected else R.drawable.cloudy)

            rainy_block.background = if (idx == 2) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_rainy.setImageResource(if (idx == 2) R.drawable.rainy_selected else R.drawable.rainy)

            snow_block.background = if (idx == 3) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_snow.setImageResource(if (idx == 3) R.drawable.snow_selected else R.drawable.snow) })

        rainy_block.setOnClickListener(View.OnClickListener { val idx = 0
            SharedPreferencesManager.setManualGoal = false
            SharedPreferencesManager.climate = idx
            SharedPreferencesManager.setClimate = true

            sunny_block.background = if (idx == 0) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_sunny.setImageResource(if (idx == 0) R.drawable.sunny_selected else R.drawable.sunny)

            cloudy_block.background = if (idx == 1) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_cloudy.setImageResource(if (idx == 1) R.drawable.cloudy_selected else R.drawable.cloudy)

            rainy_block.background = if (idx == 2) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_rainy.setImageResource(if (idx == 2) R.drawable.rainy_selected else R.drawable.rainy)

            snow_block.background = if (idx == 3) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_snow.setImageResource(if (idx == 3) R.drawable.snow_selected else R.drawable.snow) })

        snow_block.setOnClickListener(View.OnClickListener { val idx = 3
            SharedPreferencesManager.setManualGoal = false
            SharedPreferencesManager.climate = idx
            SharedPreferencesManager.setClimate = true

            sunny_block.background = if (idx == 0) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_sunny.setImageResource(if (idx == 0) R.drawable.sunny_selected else R.drawable.sunny)

            cloudy_block.background = if (idx == 1) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_cloudy.setImageResource(if (idx == 1) R.drawable.cloudy_selected else R.drawable.cloudy)

            rainy_block.background = if (idx == 2) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_rainy.setImageResource(if (idx == 2) R.drawable.rainy_selected else R.drawable.rainy)

            snow_block.background = if (idx == 3) requireContext().resources
                .getDrawable(R.drawable.rdo_gender_select) else requireContext().resources
                .getDrawable(R.drawable.rdo_gender_regular)
            img_snow.setImageResource(if (idx == 3) R.drawable.snow_selected else R.drawable.snow) })

        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
            when (SharedPreferencesManager.climate) {
                -1 -> showMessage(getString(R.string.climate_set_hint), promptsView, true)
            }
        }.setNegativeButton("Cancel") { _, _ ->
            showMessage(getString(R.string.climate_set_hint), promptsView, true)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}