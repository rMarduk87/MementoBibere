package rpt.tool.mementobibere.basic.appbasiclibs.adapter

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import rpt.tool.mementobibere.R

class CustomShareAdapter(ctx: Context, pm: PackageManager?, apps: List<ResolveInfo?>?) :
    ArrayAdapter<ResolveInfo?>(ctx, R.layout.custom_share_list, apps!!) {
    private var pm: PackageManager? = null


    var appName: ArrayList<String> = ArrayList()

    init {
        this.pm = pm
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = newView(parent)
        }

        bindView(position, convertView)

        return (convertView)
    }

    private fun newView(parent: ViewGroup): View {
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return (li.inflate(R.layout.custom_share_list, parent, false))
    }

    fun getAppName(position: Int): String {
        return "" + getItem(position)!!.loadLabel(pm)
    }

    private fun bindView(position: Int, row: View) {
        val label = row.findViewById<View>(R.id.label) as TextView
        val icon = row.findViewById<View>(R.id.icon) as ImageView

        appName.add("" + getItem(position)!!.loadLabel(pm))

        label.text = getItem(position)!!.loadLabel(pm)
        icon.setImageDrawable(getItem(position)!!.loadIcon(pm))
    }
}
