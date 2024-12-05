package rpt.tool.mementobibere.ui.drink

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import rpt.tool.mementobibere.InitUserInfoActivity
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.FragmentDrinkBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.URLFactory


class DrinkFragment :
    rpt.tool.mementobibere.BaseFragment<FragmentDrinkBinding>(FragmentDrinkBinding::inflate) {

    private lateinit var dateNow: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (!ph!!.getBoolean(URLFactory.HIDE_WELCOME_SCREEN)) {
            startActivity(Intent(requireContext(), InitUserInfoActivity::class.java))
            requireActivity().finish()
        }

        dateNow = AppUtils.getCurrentOnlyDate()!!

        if(ph!!.getBoolean(URLFactory.BLOOD_DONOR) && !dh!!.IS_EXISTS(
                    "tbl_blood_donor",
                    "Date='$dateNow'"
                )){
            binding.avisLayout.visibility = VISIBLE
        }else if(ph!!.getBoolean(URLFactory.BLOOD_DONOR) && dh!!.IS_EXISTS(
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
    }

    override fun onStart() {
        super.onStart()


        if(dh!!.IS_EXISTS(
                "tbl_blood_donor",
                "Date='$dateNow'"
            )){
            binding.lblTotalGoal.setTextColor(resources.getColor(R.color.red))
            binding.lblTotalDrunk.setTextColor(resources.getColor(R.color.red))
        }

        if(!ph!!.getBoolean(URLFactory.SET_USER_GENDER) || !ph!!.getBoolean(URLFactory.SET_USER_NAME)
            || !ph!!.getBoolean(URLFactory.SET_BLOOD_DONOR) ||
            !ph!!.getBoolean(URLFactory.SET_WORK_OUT) ||
            !ph!!.getBoolean(URLFactory.SET_CLIMATE) ||
            !ph!!.getBoolean(URLFactory.SET_HEIGHT) || !ph!!.getBoolean(URLFactory.SET_WEIGHT)){
            setNewUserData()
        }

        /*binding.calendarAvis.setOnClickListener{
            val calendar = Calendar.getInstance()

            val mDatePicker = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    var calendarPre = calendar
                    calendarPre.add(Calendar.DAY_OF_MONTH, -1)

                    val myFormat = "dd-MM-yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat)
                    val preAvis = sdf.format(calendarPre.time)
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                    val avis = sdf.format(calendar.time)
                    sqliteHelper.addAvis(preAvis)
                    sqliteHelper.addAvis(avis)
                    if(sdf.format(calendar.time) == dateNow){
                        if(totalIntake < 2000){
                            SharedPreferencesManager.totalIntake = 2000f
                        }
                    }
                    safeNavController?.safeNavigate(DrinkFragmentDirections
                        .actionDrinkFragmentToSelfFragment())
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            mDatePicker.datePicker.minDate = rpt.tool.mementobibere.utils.AppUtils.getMinDate()
            mDatePicker.setTitle("")
            mDatePicker.show()
        }

        binding.infoAvis!!.setOnClickListener {
            showMessage(
                getString(R.string.tomorrow_you_will_donate), it, duration = 3500
            )
        }*/
    }

    private fun setNewUserData() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        val view: View = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_new_data, null, false)


        val img_cancel = view.findViewById<ImageView>(R.id.img_cancel)

        img_cancel.setOnClickListener {
            startActivity(Intent(requireContext(), InitUserInfoActivity::class.java))
            requireActivity().finish()
            dialog.dismiss() }

        dialog.setOnDismissListener { }

        dialog.setContentView(view)

        dialog.show()
    }
}