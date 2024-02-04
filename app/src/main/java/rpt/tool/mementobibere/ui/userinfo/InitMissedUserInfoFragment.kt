package rpt.tool.mementobibere.ui.userinfo

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import github.com.st235.lib_expandablebottombar.MenuItemDescriptor
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.MainActivity
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.InitUserInfoFragmentBinding
import rpt.tool.mementobibere.databinding.InitUserMissedInfoFragmentBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Calendar

@Suppress("DEPRECATION")
class InitMissedUserInfoFragment:
    BaseFragment<InitUserMissedInfoFragmentBinding>(InitUserMissedInfoFragmentBinding::inflate) {

    private var weight: String = ""
    private var workType: Int = 0
    private var weightUnit : Int = 0
    private var gender : Int = 0
    private var bloodDonor : Int = 0
    private var climate : Int = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setVisibility()

        binding.etGender.editText!!.setOnClickListener {

            val li = LayoutInflater.from(requireContext())
            val promptsView = li.inflate(R.layout.custom_input_dialog2, null)

            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(promptsView)


            val btnMan = promptsView
                .findViewById(R.id.btnMan) as LottieAnimationView
            val btnWoman = promptsView
                .findViewById(R.id.btnWoman) as LottieAnimationView


            btnMan.setOnClickListener{
                gender = 0
                SharedPreferencesManager.gender = gender
                showMessage(
                    getString(R.string.you_selected_man), it,
                    type=AppUtils.Companion.TypeMessage.MAN
                )
            }

            btnWoman.setOnClickListener{
                gender = 1
                SharedPreferencesManager.gender = gender
                showMessage(
                    getString(R.string.you_selected_woman), it,
                    type=AppUtils.Companion.TypeMessage.WOMAN
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
                SharedPreferencesManager.workType = workType
                showMessage(
                    getString(R.string.you_selected_calm), it,
                    type=AppUtils.Companion.TypeMessage.WORKTYPE, workType = workType
                )
            }

            btnNormal.setOnClickListener{
                workType = 1
                SharedPreferencesManager.workType = workType
                showMessage(
                    getString(R.string.you_selected_normal), it,
                    type=AppUtils.Companion.TypeMessage.WORKTYPE, workType = workType
                )
            }

            btnLively.setOnClickListener{
                workType = 2
                SharedPreferencesManager.workType = workType
                showMessage(
                    getString(R.string.you_selected_lively), it,
                    type=AppUtils.Companion.TypeMessage.WORKTYPE, workType = workType
                )
            }

            btnIntense.setOnClickListener{
                workType = 3
                SharedPreferencesManager.workType = workType
                showMessage(
                    getString(R.string.you_selected_intense), it,
                    type=AppUtils.Companion.TypeMessage.WORKTYPE, workType = workType
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
                SharedPreferencesManager.climate = climate
                showMessage(
                    getString(R.string.you_selected_cold), it,
                    type=AppUtils.Companion.TypeMessage.CLIMATE
                )
            }

            btnFresh.setOnClickListener{
                climate = 1
                SharedPreferencesManager.climate = climate
                showMessage(
                    getString(R.string.you_selected_fresh), it,
                    type=AppUtils.Companion.TypeMessage.CLIMATE
                )
            }

            btnMild.setOnClickListener{
                climate = 2
                SharedPreferencesManager.climate = climate
                showMessage(
                    getString(R.string.you_selected_mild), it,
                    type=AppUtils.Companion.TypeMessage.CLIMATE
                )
            }

            btnTorrid.setOnClickListener{
                climate = 3
                SharedPreferencesManager.climate = climate
                showMessage(
                    getString(R.string.you_selected_torrid), it,
                    type=AppUtils.Companion.TypeMessage.CLIMATE
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

            weight = SharedPreferencesManager.weight.toString()


            when {

                TextUtils.isEmpty(binding.etGender.editText!!.text.toString()) -> showError(getString(R.string.gender_hint),it)
                TextUtils.isEmpty(binding.etWorkType.editText!!.text.toString()) -> showError(getString(R.string.work_type_hint),it)
                TextUtils.isEmpty(binding.etClimate.editText!!.text.toString()) -> showError(getString(R.string.climate_set_hint),it)

                else -> {

                    SharedPreferencesManager.firstRun = false
                    SharedPreferencesManager.setWeight = true
                    SharedPreferencesManager.setGender = true
                    SharedPreferencesManager.setWorkOut = true
                    SharedPreferencesManager.setClimate = true
                    SharedPreferencesManager.startTutorial = true
                    SharedPreferencesManager.bloodDonorKey = bloodDonor
                    SharedPreferencesManager.setBloodDonor = true

                    val totalIntake = AppUtils.calculateIntake(weight.toInt(), workType,weightUnit,
                        gender, climate, 0,SharedPreferencesManager.current_unitInt )
                    val df = DecimalFormat("#")
                    df.roundingMode = RoundingMode.CEILING
                    SharedPreferencesManager.totalIntake = df.format(totalIntake).toFloat()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        initBottomBars()
    }

    private fun setVisibility() {
        TODO("Not yet implemented")
    }

    private fun initBottomBars() {
        val menu2 = binding.weightSystemBottomBar.menu

        for (i in AppUtils.listIdsWeightSystem.indices) {
            menu2.add(
                MenuItemDescriptor.Builder(
                    requireContext(),
                    AppUtils.listIdsWeightSystem[i],
                    AppUtils.listWeightSystem[i],
                    AppUtils.listStringWeightSystem[i],
                    Color.parseColor("#41B279")
                )
                    .build()
            )
        }

        setWeightUnit()

        binding.weightSystemBottomBar.onItemSelectedListener = { _, i, _ ->
            when(i.id) {
                R.id.icon_kg -> weightUnit = 0
                R.id.icon_lbl -> weightUnit = 1
            }

            setWeightUnit()

        }

    }
    private fun setWeightUnit() {
        SharedPreferencesManager.weightUnit = weightUnit
    }

    @SuppressLint("InflateParams", "RestrictedApi")
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
    }
}