package rpt.tool.mementobibere.utils.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.data.model.Container
import rpt.tool.mementobibere.utils.extensions.equalsIgnoreCase


@SuppressLint("NewApi")
class ContainerAdapterNew(
    var mContext: Context,
    containerArrayList: ArrayList<Container>,
    private val callBack: CallBack
) : RecyclerView.Adapter<ContainerAdapterNew.ViewHolder?>() {
    private val containerArrayList: ArrayList<Container> = containerArrayList

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return containerArrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_item_container, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                if (containerArrayList[position].containerValue!!
                        .contains(".")
                ) holder.textView.text = URLFactory.decimalFormat.format(
                    containerArrayList[position].containerValue!!.toDouble()
                ) + " " + URLFactory.WATER_UNIT_VALUE
                else holder.textView.text = containerArrayList[position].containerValue + " " + URLFactory.WATER_UNIT_VALUE
            } else {
                if (containerArrayList[position].containerValue!!
                        .contains(".")
                ) holder.textView.text = URLFactory.decimalFormat.format(
                    containerArrayList[position].containerValueOZ!!.toDouble()
                ) + " " + URLFactory.WATER_UNIT_VALUE
                else holder.textView.text = containerArrayList[position].containerValueOZ + " " + URLFactory.WATER_UNIT_VALUE
            }
        } catch (e: Exception) {
        }

        if (containerArrayList[position].isCustom) Glide.with(mContext)
            .load(R.drawable.ic_custom_ml).into(holder.imageView)
        else Glide.with(mContext).load(getImage(position)).into(holder.imageView)

        holder.item_block.setOnClickListener(View.OnClickListener {
            callBack.onClickSelect(
                containerArrayList[position], position
            )
        })

        if (containerArrayList[position].isSelected) {
            holder.img_selected.visibility = View.VISIBLE
        } else {
            holder.img_selected.visibility = View.INVISIBLE
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById<TextView>(R.id.container_name)
        var imageView: ImageView = itemView.findViewById<ImageView>(R.id.container_img)
        var item_block: LinearLayout = itemView.findViewById<LinearLayout>(R.id.item_block)
        var img_selected: ImageView = itemView.findViewById<ImageView>(R.id.img_selected)
    }

    interface CallBack {
        fun onClickSelect(container: Container?, position: Int)
    }

    private fun getImage(pos: Int): Int {
        var drawable: Int = R.drawable.ic_custom_ml

        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 50) drawable =
                R.drawable.ic_50_ml
            else if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 100) drawable =
                R.drawable.ic_100_ml
            else if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 150) drawable =
                R.drawable.ic_150_ml
            else if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 200) drawable =
                R.drawable.ic_200_ml
            else if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 250) drawable =
                R.drawable.ic_250_ml
            else if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 300) drawable =
                R.drawable.ic_300_ml
            else if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 500) drawable =
                R.drawable.ic_500_ml
            else if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 600) drawable =
                R.drawable.ic_600_ml
            else if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 700) drawable =
                R.drawable.ic_700_ml
            else if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 800) drawable =
                R.drawable.ic_800_ml
            else if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 900) drawable =
                R.drawable.ic_900_ml
            else if (containerArrayList[pos].containerValue!!.toDouble().toInt() == 1000) drawable =
                R.drawable.ic_1000_ml
        } else {
            if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 2) drawable =
                R.drawable.ic_50_ml
            else if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 3) drawable =
                R.drawable.ic_100_ml
            else if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 5) drawable =
                R.drawable.ic_150_ml
            else if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 7) drawable =
                R.drawable.ic_200_ml
            else if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 8) drawable =
                R.drawable.ic_250_ml
            else if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 10) drawable =
                R.drawable.ic_300_ml
            else if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 17) drawable =
                R.drawable.ic_500_ml
            else if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 20) drawable =
                R.drawable.ic_600_ml
            else if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 24) drawable =
                R.drawable.ic_700_ml
            else if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 27) drawable =
                R.drawable.ic_800_ml
            else if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 30) drawable =
                R.drawable.ic_900_ml
            else if (containerArrayList[pos].containerValueOZ!!.toDouble().toInt() == 34) drawable =
                R.drawable.ic_1000_ml
        }

        return drawable
    }
}

