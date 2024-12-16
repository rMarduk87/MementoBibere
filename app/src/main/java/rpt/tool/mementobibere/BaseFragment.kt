package rpt.tool.mementobibere

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Alert_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Bitmap_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Database_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Date_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Intent_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Json_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Map_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.String_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Utility_Function
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Zip_Helper
import rpt.tool.mementobibere.utils.Inflate
import rpt.tool.mementobibere.utils.helpers.DB_Helper


abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    var dbh: DB_Helper? = null
    var uf: Utility_Function? = null
    var ah: Alert_Helper? = null
    var jh: Json_Helper? = null
    var bh: Bitmap_Helper? = null
    var dth: Date_Helper? = null
    var dh: Database_Helper? = null
    var mah: Map_Helper? = null
    var sh: String_Helper? = null
    var zh: Zip_Helper? = null
    var ih: Intent_Helper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dbh = DB_Helper(requireContext(),requireActivity())
        uf = Utility_Function(requireContext(), requireActivity())
        ah = Alert_Helper(requireContext())
        jh = Json_Helper(requireContext())
        bh = Bitmap_Helper(requireContext())
        dth = Date_Helper()
        dh = Database_Helper(requireContext(), requireActivity())
        ih = Intent_Helper(requireContext(), requireActivity())
        mah = Map_Helper()
        sh = String_Helper(requireContext(), requireActivity())
        zh = Zip_Helper(requireContext())

        uf!!.permission_StrictMode()
        
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}