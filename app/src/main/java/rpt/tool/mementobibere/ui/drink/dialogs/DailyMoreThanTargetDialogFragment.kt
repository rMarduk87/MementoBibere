package rpt.tool.mementobibere.ui.drink.dialogs

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import rpt.tool.mementobibere.BaseDialogFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant.sh
import rpt.tool.mementobibere.databinding.DialogGoalTargetReachedBinding
import rpt.tool.mementobibere.utils.URLFactory

class DailyMoreThanTargetDialogFragment:BaseDialogFragment<DialogGoalTargetReachedBinding>(DialogGoalTargetReachedBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        if (URLFactory.WATER_UNIT_VALUE.equals(
                "ml",
                ignoreCase = true
            )
        )
            binding.imgBottle.setImageResource(R.drawable.ic_limit_ml)
        else binding.imgBottle.setImageResource(R.drawable.ic_limit_oz)

        val desc = if (URLFactory.WATER_UNIT_VALUE.equals(
                "ml",
                ignoreCase = true
            )
        ) "8000 ml" else "270 fl oz"

        binding.lblDesc.text =
            sh!!.get_string(R.string.str_you_should_not_drink_more_then_target).replace("$1", desc)

        binding.imgCancel.setOnClickListener { dialog!!.dismiss() }
    }
}