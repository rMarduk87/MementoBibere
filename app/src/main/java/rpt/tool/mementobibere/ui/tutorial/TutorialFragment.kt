package rpt.tool.mementobibere.ui.tutorial

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.snackbar.Snackbar
import com.skydoves.balloon.BalloonAlign
import com.skydoves.balloon.balloon
import github.com.st235.lib_expandablebottombar.Menu
import github.com.st235.lib_expandablebottombar.MenuItemDescriptor
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.InitUserInfoActivity
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.WalkthroughActivity
import rpt.tool.mementobibere.databinding.TutorialFragmentBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.balloon.tutorial.FifthHelpBalloonFactory
import rpt.tool.mementobibere.utils.balloon.tutorial.FirstHelpBalloonFactory
import rpt.tool.mementobibere.utils.balloon.tutorial.FourthHelpBalloonFactory
import rpt.tool.mementobibere.utils.balloon.tutorial.SecondHelpBalloonFactory
import rpt.tool.mementobibere.utils.balloon.tutorial.SeventhHelpBalloonFactory
import rpt.tool.mementobibere.utils.balloon.tutorial.SixthHelpBalloonFactory
import rpt.tool.mementobibere.utils.balloon.tutorial.ThirdHelpBalloonFactory
import rpt.tool.mementobibere.utils.extensions.toExtractFloat
import rpt.tool.mementobibere.utils.extensions.toMainTheme
import rpt.tool.mementobibere.utils.extensions.toNumberString
import rpt.tool.mementobibere.utils.helpers.SqliteHelper
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import rpt.tool.mementobibere.utils.navigation.safeNavController
import rpt.tool.mementobibere.utils.navigation.safeNavigate

class TutorialFragment : BaseFragment<TutorialFragmentBinding>(TutorialFragmentBinding::inflate) {
    private var start: Boolean = false
    private var saveIntook: Float = 0f
    private var isTutorial: Boolean = false
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
    private var selectedOption: Float? = null
    private var snackbar: Snackbar? = null
    private var themeInt: Int = 0
    private var btnSelected: Int? = null
    private val firstHelpBalloon by balloon<FirstHelpBalloonFactory>()
    private val secondHelpBalloon by balloon<SecondHelpBalloonFactory>()
    private val thirdHelpBalloon by balloon<ThirdHelpBalloonFactory>()
    private val fourthHelpBalloon by balloon<FourthHelpBalloonFactory>()
    private val fifthHelpBalloon by balloon<FifthHelpBalloonFactory>()
    private val sixthHelpBalloon by balloon<SixthHelpBalloonFactory>()
    private val seventhHelpBalloon by balloon<SeventhHelpBalloonFactory>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sqliteHelper = SqliteHelper(requireContext())
        dateNow = AppUtils.getCurrentOnlyDate()!!
        setTheme()
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setBackGround()

        if(SharedPreferencesManager.bloodDonorKey==1 &&
            !sqliteHelper.getAvisDay(dateNow)){
            binding.calendarAvis.visibility = View.VISIBLE
            binding.calendarAvisHelp!!.visibility = View.VISIBLE
        }
        else if(SharedPreferencesManager.bloodDonorKey==1 &&
            sqliteHelper.getAvisDay(dateNow)){
            binding.calendarAvis.visibility = View.VISIBLE
            binding.infoAvis!!.visibility = View.VISIBLE
            binding.calendarAvisHelp!!.visibility = View.VISIBLE
        }
        else{
            binding.calendarAvis.visibility = View.GONE
            binding.infoAvis!!.visibility = View.GONE
            binding.calendarAvisHelp!!.visibility = View.GONE
        }

        totalIntake = SharedPreferencesManager.totalIntake

        if (SharedPreferencesManager.firstRun) {
            startActivity(Intent(requireContext(), WalkthroughActivity::class.java))
            requireActivity().finish()
        } else if (totalIntake <= 0) {
            startActivity(Intent(requireContext(), InitUserInfoActivity::class.java))
            requireActivity().finish()
        }

        viewWindow = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        initBottomBar()
        if (!SharedPreferencesManager.firstRun || totalIntake > 0) {
            SharedPreferencesManager.value_300 =  AppUtils.firstConversion(300f,
                SharedPreferencesManager.new_unitInt)
            initIntookValue()
            setValueForDrinking()
        }

        binding.mainActivityParent.isClickable = false
        binding.bottomBarNotify.isClickable = false
        binding.bottomBarNotNotify.isClickable = false
        binding.cardView.isClickable = false
        binding.calendarAvis.isClickable = false
        binding.calendarAvisHelp.isClickable = false
        binding.infoAvis!!.isClickable = false
        binding.op50ml!!.isClickable = false
        binding.op100ml!!.isClickable = false
        binding.op150ml!!.isClickable = false
        binding.op200ml!!.isClickable = false
        binding.op250ml!!.isClickable = false
        binding.op300ml!!.isClickable = false
        binding.opCustom.isClickable = false
        binding.opDrinkAll.isClickable = false
        binding.op350ml!!.isClickable = false
    }

    private fun initIntookValue() {
        unit = AppUtils.calculateExtensions(SharedPreferencesManager.new_unitInt)
        binding.ml50!!.text = "${SharedPreferencesManager.value_50.toNumberString()} $unit"
        binding.ml100!!.text = "${SharedPreferencesManager.value_100.toNumberString()} $unit"
        binding.ml150!!.text = "${SharedPreferencesManager.value_150.toNumberString()} $unit"
        binding.ml200!!.text = "${SharedPreferencesManager.value_200.toNumberString()} $unit"
        binding.ml250!!.text = "${SharedPreferencesManager.value_250.toNumberString()} $unit"
        binding.ml300!!.text = "${SharedPreferencesManager.value_300.toNumberString()} $unit"
        binding.ml350!!.text = "${SharedPreferencesManager.value_350.toNumberString()} $unit"
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
        else if(themeInt==1){
            "#29704D"
        }
        else if(themeInt ==2){
            "#4167B2"
        }
        else if(themeInt ==3){
            "#FF6200EE"
        }
        else{
            "#F6E000"
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
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun addDrinkedWater() {

        if(start){
            inTook += selectedOption!!
        }
        else{
            inTook = 0f
        }
        setWaterLevel(inTook, totalIntake)
        showMessage(
            getString(R.string.your_water_intake_was_saved), viewWindow, false,
            AppUtils.Companion.TypeMessage.SAVE
        )
    }

    private fun setBackGround() {
        var themeInt = SharedPreferencesManager.themeInt
        when(themeInt){
            0->toLightTheme()
            1->toDarkTheme()
            2->toWaterTheme()
            3->toGrapeTheme()
            4->toBeeTheme()
        }
    }

    private fun toBeeTheme() {
        binding.mainActivityParent.background = requireContext().getDrawable(R.drawable.ic_app_bg_b)
        if(sqliteHelper.getAvisDay(dateNow)){
            binding.tvIntook.setTextColor(resources.getColor(R.color.red))
            binding.tvTotalIntake.setTextColor(resources.getColor(R.color.red))
        }
        else{
            binding.tvIntook.setTextColor(requireContext().getColor(R.color.colorBlack))
            binding.tvTotalIntake.setTextColor(requireContext().getColor(R.color.colorBlack))
        }
        binding.intakeProgress.colorBackground = requireContext().getColor(R.color.teal_700)
    }

    private fun toGrapeTheme() {
        binding.mainActivityParent.background = requireContext().getDrawable(R.drawable.ic_app_bg_g)
        if(sqliteHelper.getAvisDay(dateNow)){
            binding.tvIntook.setTextColor(resources.getColor(R.color.red))
            binding.tvTotalIntake.setTextColor(resources.getColor(R.color.red))
        }
        else{
            binding.tvIntook.setTextColor(requireContext().getColor(R.color.colorBlack))
            binding.tvTotalIntake.setTextColor(requireContext().getColor(R.color.colorBlack))
        }
        binding.intakeProgress.colorBackground = requireContext().getColor(R.color.teal_700)
    }

    private fun toWaterTheme() {
        binding.mainActivityParent.background = requireContext().getDrawable(R.drawable.ic_app_bg_w)
        if(sqliteHelper.getAvisDay(dateNow)){
            binding.tvIntook.setTextColor(resources.getColor(R.color.red))
            binding.tvTotalIntake.setTextColor(resources.getColor(R.color.red))
        }
        else{
            binding.tvIntook.setTextColor(requireContext().getColor(R.color.colorWhite))
            binding.tvTotalIntake.setTextColor(requireContext().getColor(R.color.colorWhite))
        }
        binding.bottomBarNotify.setBackgroundColorRes(R.color.colorWhite)
        binding.bottomBarNotNotify.setBackgroundColorRes(R.color.colorWhite)
        binding.intakeProgress.colorBackground = requireContext().getColor(R.color.teal_700)
    }

    private fun toDarkTheme() {
        binding.mainActivityParent.background = requireContext().getDrawable(R.drawable.ic_app_bg_dark)
        if(sqliteHelper.getAvisDay(dateNow)){
            binding.tvIntook.setTextColor(resources.getColor(R.color.red))
            binding.tvTotalIntake.setTextColor(resources.getColor(R.color.red))
        }
        else{
            binding.tvIntook.setTextColor(requireContext().getColor(R.color.colorBlack))
            binding.tvTotalIntake.setTextColor(requireContext().getColor(R.color.colorBlack))
        }
        binding.intakeProgress.colorBackground = requireContext().getColor(R.color.teal_700)
    }

    private fun toLightTheme() {
        binding.mainActivityParent.background = requireContext().getDrawable(R.drawable.ic_app_bg)
        if(sqliteHelper.getAvisDay(dateNow)){
            binding.tvIntook.setTextColor(resources.getColor(R.color.red))
            binding.tvTotalIntake.setTextColor(resources.getColor(R.color.red))
        }
        else{
            binding.tvIntook.setTextColor(requireContext().getColor(R.color.colorWhite))
            binding.tvTotalIntake.setTextColor(requireContext().getColor(R.color.colorWhite))
        }
        binding.intakeProgress.colorBackground = requireContext().getColor(R.color.teal_700)
    }

    private fun setTheme() {
        val theme = themeInt.toMainTheme()
        requireActivity().setTheme(theme)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

        setBackGround()

        if(sqliteHelper.getAvisDay(dateNow)){
            binding.tvIntook.setTextColor(resources.getColor(R.color.red))
            binding.tvTotalIntake.setTextColor(resources.getColor(R.color.red))
        }

        totalIntake = SharedPreferencesManager.totalIntake
        setValueForDrinking()

        var background =
            when(SharedPreferencesManager.themeInt){
                2->R.drawable.option_select_bg_w
                3->R.drawable.option_select_bg_g
                4->R.drawable.option_select_bg_b
                else -> R.drawable.option_select_bg
            }

        outValue = TypedValue()
        requireContext().applicationContext.theme.resolveAttribute(
            android.R.attr.selectableItemBackground,
            outValue,
            true
        )

        if (!SharedPreferencesManager.firstRun || totalIntake > 0) {
            setValueForDrinking()
        }

        startTutorial(binding.tutorial!!,background)
    }

    private fun startTutorial(view: View, background: Int) {
        isTutorial = true
        start = true
        saveIntook = inTook

        Handler(Looper.getMainLooper()).postDelayed({
            firstHelpBalloon.showAlign(
                align = BalloonAlign.BOTTOM,
                mainAnchor = binding.tutorial as View,
                subAnchorList = listOf(view),
            )
        },2000)


        Handler(Looper.getMainLooper()).postDelayed({
            firstHelpBalloon.dismiss()
            goToSecond(view,background)
        }, 5000)
    }

    private fun goToSecond(view: View, background: Int) {
        secondHelpBalloon.showAlign(
            align = BalloonAlign.BOTTOM,
            mainAnchor = binding.op50ml as View,
            subAnchorList = listOf(view),
        )

        Handler(Looper.getMainLooper()).postDelayed({
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            btnSelected = 0
            selectedOption = binding.ml50!!.text.toString().toExtractFloat()
            binding.op50ml.background = requireContext().getDrawable(background)
            binding.op100ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op150ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op200ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op250ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op300ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.opCustom.background = requireContext().getDrawable(outValue.resourceId)
            binding.opDrinkAll.background = requireContext().getDrawable(outValue.resourceId)
            binding.op350ml.background = requireContext().getDrawable(outValue.resourceId)
            addDrinkedWater()
        }, 2500)

        Handler(Looper.getMainLooper()).postDelayed({
            secondHelpBalloon.dismiss()
            goToThird(view,background)
        }, 3300)
    }

    private fun goToThird(view: View, background: Int) {
        thirdHelpBalloon.showAlign(
            align = BalloonAlign.BOTTOM,
            mainAnchor = binding.opCustom as View,
            subAnchorList = listOf(view),
        )

        Handler(Looper.getMainLooper()).postDelayed({
            thirdHelpBalloon.dismiss()
            goToFourth(view,background)
        }, 2800)


    }

    private fun goToFourth(view: View, background: Int) {
        fourthHelpBalloon.showAlign(
            align = BalloonAlign.BOTTOM,
            mainAnchor = binding.opDrinkAll as View,
            subAnchorList = listOf(view),
        )

        Handler(Looper.getMainLooper()).postDelayed({
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            btnSelected = 7
            selectedOption = AppUtils.calculateOption(inTook,totalIntake)!!
            binding.op50ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op100ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op150ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op200ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op250ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.op300ml.background = requireContext().getDrawable(outValue.resourceId)
            binding.opCustom.background = requireContext().getDrawable(outValue.resourceId)
            binding.opDrinkAll.background = requireContext().getDrawable(background)
            binding.op350ml.background = requireContext().getDrawable(outValue.resourceId)
            addDrinkedWater()
        }, 3400)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.opDrinkAll.background = requireContext().getDrawable(outValue.resourceId)
            fourthHelpBalloon.dismiss()
            inTook = saveIntook
            start = false
            addDrinkedWater()
            isTutorial = false
            goToFifth(view)
        }, 5500)
    }

    private fun goToFifth(view: View) {
        fifthHelpBalloon.showAlign(
            align = BalloonAlign.BOTTOM,
            mainAnchor = binding.btnUndo as View,
            subAnchorList = listOf(view),
        )

        Handler(Looper.getMainLooper()).postDelayed({
            fifthHelpBalloon.dismiss()
            goToSixth(view)
        }, 2650)
    }

    private fun goToSixth(view: View) {
        sixthHelpBalloon.showAlign(
            align = BalloonAlign.BOTTOM,
            mainAnchor = binding.btnRedo as View,
            subAnchorList = listOf(view),
        )

        Handler(Looper.getMainLooper()).postDelayed({
            sixthHelpBalloon.dismiss()
            goToSeventh(view)
        }, 2650)
    }

    private fun goToSeventh(view: View) {
        seventhHelpBalloon.showAlign(
            align = BalloonAlign.BOTTOM,
            mainAnchor = binding.btnRefresh as View,
            subAnchorList = listOf(view),
        )

        Handler(Looper.getMainLooper()).postDelayed({
            seventhHelpBalloon.dismiss()
            SharedPreferencesManager.startTutorial = false
            safeNavController?.safeNavigate(TutorialFragmentDirections
                .actionTutorialFragmentToDrinkFragment())
        }, 2650)
    }

    override fun onResume() {
        super.onResume()

        setTheme()
        setBackGround()
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
        binding.intakeProgress.progress = progress.toFloat()
        if(progress <= 140){
            binding.intakeProgress.setOnProgressChangeListener { binding.intakeProgress.labelText = "${
                getString(
                    R.string.drink
                )} ${it.toInt()}%" }
            binding.intakeProgress.labelText = "${
                getString(
                    R.string.drink
                )} ${progress}%"
        }

        if ((inTook * 100 / totalIntake) > 140) {
            showMessage(getString(R.string.you_achieved_the_goal), binding.mainActivityParent)
            sqliteHelper.addReachedGoal(dateNow,inTook,unit)
        }
    }

    private fun setValueForDrinking() {
        unit = AppUtils.calculateExtensions(SharedPreferencesManager.new_unitInt)
        binding.ml50!!.text = "${SharedPreferencesManager.value_50.toNumberString()} $unit"
        binding.ml100!!.text = "${SharedPreferencesManager.value_100.toNumberString()} $unit"
        binding.ml150!!.text = "${SharedPreferencesManager.value_150.toNumberString()} $unit"
        binding.ml200!!.text = "${SharedPreferencesManager.value_200.toNumberString()} $unit"
        binding.ml250!!.text = "${SharedPreferencesManager.value_250.toNumberString()} $unit"
        binding.ml300!!.text = "${SharedPreferencesManager.value_300.toNumberString()} $unit"
        binding.ml350!!.text = "${SharedPreferencesManager.value_350.toNumberString()} $unit"
        binding.tvTotalIntake!!.text = "/" +totalIntake.toNumberString() + " " + "$unit"
    }
}