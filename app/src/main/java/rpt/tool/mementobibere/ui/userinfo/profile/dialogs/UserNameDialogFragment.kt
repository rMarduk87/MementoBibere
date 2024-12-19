package rpt.tool.mementobibere.ui.userinfo.profile.dialogs

import android.os.Bundle
import android.view.View
import rpt.tool.mementobibere.BaseDialogFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant.sh
import rpt.tool.mementobibere.databinding.DialogUserNameBinding
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager

class UserNameDialogFragment:BaseDialogFragment<DialogUserNameBinding>(DialogUserNameBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgCancel.setOnClickListener { dialog!!.dismiss() }

        binding.txtName.requestFocus()

        binding.btnCancel.setOnClickListener { dialog!!.cancel() }

        binding.imgCancel.setOnClickListener { dialog!!.cancel() }

        binding.txtName.setText(SharedPreferencesManager.userName)
        binding.txtName.setSelection(binding.txtName.text.toString().trim { it <= ' ' }.length)

        binding.btnAdd.setOnClickListener {
            if (sh!!.check_blank_data(binding.txtName.text.toString().trim { it <= ' ' })) {
                ah!!.customAlert(sh!!.get_string(R.string.str_your_name_validation))
            } else if (binding.txtName.text.toString().trim { it <= ' ' }.length < 3) {
                ah!!.customAlert(sh!!.get_string(R.string.str_valid_name_validation))
            } else {
                SharedPreferencesManager.userName =
                    binding.txtName.text.toString().trim { it <= ' ' }

                dialog!!.dismiss()
            }
        }
    }

}