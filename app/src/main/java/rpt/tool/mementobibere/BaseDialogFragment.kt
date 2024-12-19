package rpt.tool.mementobibere

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.StyleRes
import androidx.annotation.UiThread
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Alert_Helper
import rpt.tool.mementobibere.databinding.FragmentBaseDialogBinding
import rpt.tool.mementobibere.utils.AppUtils.Companion.gone
import rpt.tool.mementobibere.utils.AppUtils.Companion.visible
import rpt.tool.mementobibere.utils.Inflate
import rpt.tool.mementobibere.widget.NewAppWidget


abstract class BaseDialogFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) :
    DialogFragment() {

    private val handler = Handler(Looper.getMainLooper())


    private var _baseBinding: FragmentBaseDialogBinding? = null
    private val baseBinding get() = _baseBinding!!

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    open val showDismissOnStart = false
    open val delayOnShowDismissOnStart = -1
    open val onDismissClick: ((v: View) -> Unit)? = null
    var ah: Alert_Helper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _baseBinding = FragmentBaseDialogBinding.inflate(inflater, container, false)
        _binding = inflate.invoke(inflater, baseBinding.contentArea, true)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.drawable_background_tra)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return baseBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ah = Alert_Helper(requireContext())
        manageDismissIcon()
    }

    private fun manageDismissIcon() {
        baseBinding.imgCancel.setOnClickListener {
            onDismissClick?.invoke(it)
            dismiss()
        }
        /* Visibility */
        when {
            showDismissOnStart && delayOnShowDismissOnStart >= 0 -> showDismissIconAfterDelay()
            showDismissOnStart && delayOnShowDismissOnStart < 0 -> baseBinding.imgCancel.visible()
            else -> baseBinding.imgCancel.gone()
        }
    }

    protected fun showDismissIcon() {
        runCatching {
            baseBinding.imgCancel.visible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _baseBinding = null
        _binding = null
    }

    @UiThread
    private fun showDismissIconAfterDelay() {
        baseBinding.imgCancel.gone()
        handler.let {
            it.removeCallbacksAndMessages(null)
            it.postDelayed(
                {
                    runCatching {
                        baseBinding.imgCancel.visible()
                    }
                },
                delayOnShowDismissOnStart.toLong() * 1000
            )
        }
    }

    fun refreshWidget() {
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
}