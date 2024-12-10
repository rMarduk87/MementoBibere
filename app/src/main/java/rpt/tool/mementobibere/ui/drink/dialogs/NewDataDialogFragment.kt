package rpt.tool.mementobibere.ui.drink.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import rpt.tool.mementobibere.BaseDialogFragment
import rpt.tool.mementobibere.InitUserInfoActivity
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.DialogNewDataBinding

class NewDataDialogFragment:BaseDialogFragment<DialogNewDataBinding>(DialogNewDataBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binding.imgCancel.setOnClickListener {
            startActivity(Intent(requireContext(), InitUserInfoActivity::class.java))
            requireActivity().finish()
            dialog!!.dismiss() }
    }
}