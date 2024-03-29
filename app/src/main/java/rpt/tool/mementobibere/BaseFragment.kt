package rpt.tool.mementobibere

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.Inflate
import rpt.tool.mementobibere.utils.extensions.toAppTheme


abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences
    private var themeInt : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPref = requireActivity().getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        themeInt = sharedPref.getInt(AppUtils.THEME_KEY,0)
        setTheme()
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    private fun setTheme() {
        val theme = themeInt.toAppTheme()
        requireActivity().setTheme(theme)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("InflateParams", "RestrictedApi")
    fun showMessage(message: String, view: View, error:Boolean? = false,
                    type: AppUtils.Companion.TypeMessage = AppUtils.Companion.TypeMessage.NOTHING,
                    duration: Int = 1500,workType: Int = -1) {
        val snackBar = Snackbar.make(view, "", duration)
        val customSnackView: View =
            when(error){
                true ->layoutInflater.inflate(R.layout.error_toast_layout, null)
                else->
                    if(type == AppUtils.Companion.TypeMessage.WORKTYPE){
                        layoutInflater.inflate(R.layout.workout_toast_layout, null)
                    }
                    else{
                        layoutInflater.inflate(R.layout.info_toast_layout, null)
                    }
            }
        snackBar.view.setBackgroundColor(Color.TRANSPARENT)
        val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout

        val text = customSnackView.findViewById<TextView>(R.id.tvMessage)
        text.text = message

        if(type== AppUtils.Companion.TypeMessage.SAVE){
            val anim = customSnackView.findViewById<LottieAnimationView>(R.id.anim)
            anim.setAnimation(R.raw.save)
        }
        if(type== AppUtils.Companion.TypeMessage.MAN){
            val anim = customSnackView.findViewById<LottieAnimationView>(R.id.anim)
            anim.setAnimation(R.raw.man)
        }
        if(type== AppUtils.Companion.TypeMessage.WOMAN){
            val anim = customSnackView.findViewById<LottieAnimationView>(R.id.anim)
            anim.setAnimation(R.raw.woman)
        }
        if(type== AppUtils.Companion.TypeMessage.WORKTYPE){
            val anim = customSnackView.findViewById<LottieAnimationView>(R.id.anim)
            when(workType){
                0->anim.setAnimation(R.raw.calm)
                1->anim.setAnimation(R.raw.normal)
                2->anim.setAnimation(R.raw.lively)
                3->anim.setAnimation(R.raw.intense)
            }
        }
        if(type== AppUtils.Companion.TypeMessage.CLIMATE){
            val anim = customSnackView.findViewById<LottieAnimationView>(R.id.anim)
            when(workType){
                0->anim.setAnimation(R.raw.cold)
                1->anim.setAnimation(R.raw.fresh)
                2->anim.setAnimation(R.raw.mild)
                3->anim.setAnimation(R.raw.torrid)
            }
        }

        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(customSnackView, 0)
        snackBar.show()
    }

    fun showError(error: String) {
        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        val customView: View =
            layoutInflater.inflate(R.layout.error_toast_layout, null)

        val text = customView.findViewById<TextView>(R.id.tvMessage)
        text.text = error
        toast.view = customView
        toast.show()
    }
}