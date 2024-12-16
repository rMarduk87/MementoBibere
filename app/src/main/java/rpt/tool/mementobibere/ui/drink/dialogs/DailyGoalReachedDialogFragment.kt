package rpt.tool.mementobibere.ui.drink.dialogs

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.navigation.fragment.navArgs
import rpt.tool.mementobibere.BaseDialogFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant.dh
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant.ih
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant.sh
import rpt.tool.mementobibere.databinding.DialogGoalReachedBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.helpers.HeightWeightHelper.mlToOzConverter
import rpt.tool.mementobibere.utils.helpers.HeightWeightHelper.ozToMlConverter

class DailyGoalReachedDialogFragment:BaseDialogFragment<DialogGoalReachedBinding>(DialogGoalReachedBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgCancel.setOnClickListener { dialog!!.dismiss() }

        binding.btnShare.setOnClickListener {
            dialog!!
                .dismiss()

            var share_text = sh!!.get_string(R.string.str_share_text)
                .replace("$1", "" + (navArgs<DailyGoalReachedDialogFragmentArgs>().value.drink)
                        + " " + URLFactory.WATER_UNIT_VALUE)

            share_text = share_text.replace("$2", "@ " + URLFactory.APP_SHARE_URL)
            ih!!.ShareText(AppUtils.getApplicationName(requireContext()), share_text)
        }

        val containerValue = if(URLFactory.WATER_UNIT_VALUE=="ml"){
            navArgs<DailyGoalReachedDialogFragmentArgs>().value.drink.toString()
        }
        else{
            ozToMlConverter(navArgs<DailyGoalReachedDialogFragmentArgs>().value.drink.toDouble()).toString()
        }

        val containerValueOZ = if(URLFactory.WATER_UNIT_VALUE!="ml"){
            navArgs<DailyGoalReachedDialogFragmentArgs>().value.drink.toString()
        }
        else{
            mlToOzConverter(navArgs<DailyGoalReachedDialogFragmentArgs>().value.drink.toDouble()).toString()
        }

        val initialValues = ContentValues()
        initialValues.put("ContainerValue", "" + containerValue)
        initialValues.put("ContainerValueOZ", "" + containerValueOZ)
        initialValues.put("Date", "" + navArgs<DailyGoalReachedDialogFragmentArgs>().value.dateNow)

        dh!!.INSERT("tbl_goal_reached", initialValues)
    }
}