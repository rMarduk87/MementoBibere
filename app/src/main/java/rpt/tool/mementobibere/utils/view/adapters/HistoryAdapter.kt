package rpt.tool.mementobibere.utils.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.data.model.History
import rpt.tool.mementobibere.utils.extensions.equalsIgnoreCase

@SuppressLint("NewApi")
class HistoryAdapter(
    var mContext: Context,
    historyArrayList: ArrayList<History>,
    var callBack: CallBack
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder?>() {
    private val historyArrayList: ArrayList<History> = historyArrayList

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return historyArrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_item_history, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.lbl_date.text = historyArrayList[position].drinkDate
        holder.lbl_total_day_water.text = historyArrayList[position].totalML

        val str = " " + historyArrayList[position].containerMeasure

        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) holder.container_name.text =
            historyArrayList[position].containerValue + str
        else holder.container_name.text = historyArrayList[position].containerValueOZ + str
        holder.lbl_time.text = historyArrayList[position].drinkTime

        if (position == 0) holder.super_item_block.setBackgroundColor(mContext.resources.getColor(R.color.colorPrimary))
        else holder.super_item_block.setBackgroundColor(mContext.resources.getColor(R.color.white))

        if (position != 0) {
            if (showHeader(position)) holder.item_header_block.visibility = View.VISIBLE
            else holder.item_header_block.visibility = View.GONE
        } else holder.item_header_block.visibility = View.VISIBLE

        holder.divider.visibility = View.VISIBLE

        Glide.with(mContext).load(getImage(position)).into(holder.imageView)

        holder.item_block.setOnClickListener(View.OnClickListener {
            callBack.onClickSelect(
                historyArrayList[position], position
            )
        })

        holder.btnRemoveRow.setOnClickListener {
            callBack.onClickRemove(
                historyArrayList[position],
                position
            )
        }
    }

    private fun showHeader(position: Int): Boolean {
        return !historyArrayList[position].drinkDate!!
            .equalsIgnoreCase(historyArrayList[position - 1].drinkDate!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lbl_date: TextView = itemView.findViewById<TextView>(R.id.lbl_date)
        var lbl_total_day_water: TextView =
            itemView.findViewById<TextView>(R.id.lbl_total_day_water)
        var imageView: ImageView = itemView.findViewById<ImageView>(R.id.container_img)
        var item_block: LinearLayout = itemView.findViewById<LinearLayout>(R.id.item_block)
        var item_header_block: LinearLayout =
            itemView.findViewById<LinearLayout>(R.id.item_header_block)
        var container_name: TextView = itemView.findViewById<TextView>(R.id.container_name)
        var lbl_time: TextView = itemView.findViewById<TextView>(R.id.lbl_time)
        var divider: View = itemView.findViewById<View>(R.id.divider)
        var super_item_block: RelativeLayout =
            itemView.findViewById<RelativeLayout>(R.id.super_item_block)

        var btnRemoveRow: ImageView = itemView.findViewById<ImageView>(R.id.btnRemoveRow)
    }

    interface CallBack {
        fun onClickSelect(history: History?, position: Int)
        fun onClickRemove(history: History?, position: Int)
    }

    private fun getImage(pos: Int): Int {
        var drawable: Int = R.drawable.ic_custom_ml

        if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
            val `val`: String = historyArrayList[pos].containerValue.toString()

            if (`val`.toDouble().toInt() == 50) drawable = R.drawable.ic_50_ml
            else if (`val`.toDouble() .toInt()== 100) drawable = R.drawable.ic_100_ml
            else if (`val`.toDouble() .toInt()== 150) drawable = R.drawable.ic_150_ml
            else if (`val`.toDouble() .toInt()== 200) drawable = R.drawable.ic_200_ml
            else if (`val`.toDouble() .toInt()== 250) drawable = R.drawable.ic_250_ml
            else if (`val`.toDouble() .toInt()== 300) drawable = R.drawable.ic_300_ml
            else if (`val`.toDouble() .toInt()== 500) drawable = R.drawable.ic_500_ml
            else if (`val`.toDouble() .toInt()== 600) drawable = R.drawable.ic_600_ml
            else if (`val`.toDouble() .toInt()== 700) drawable = R.drawable.ic_700_ml
            else if (`val`.toDouble() .toInt()== 800) drawable = R.drawable.ic_800_ml
            else if (`val`.toDouble() .toInt()== 900) drawable = R.drawable.ic_900_ml
            else if (`val`.toDouble() .toInt()== 1000) drawable = R.drawable.ic_1000_ml
        } else {
            val `val`: String = historyArrayList[pos].containerValueOZ.toString()

            if (`val`.toDouble() .toInt()== 2) drawable = R.drawable.ic_50_ml
            else if (`val`.toDouble() .toInt()== 3) drawable = R.drawable.ic_100_ml
            else if (`val`.toDouble() .toInt()== 5) drawable = R.drawable.ic_150_ml
            else if (`val`.toDouble() .toInt()== 7) drawable = R.drawable.ic_200_ml
            else if (`val`.toDouble() .toInt()== 8) drawable = R.drawable.ic_250_ml
            else if (`val`.toDouble() .toInt()== 10) drawable = R.drawable.ic_300_ml
            else if (`val`.toDouble() .toInt()== 17) drawable = R.drawable.ic_500_ml
            else if (`val`.toDouble() .toInt()== 20) drawable = R.drawable.ic_600_ml
            else if (`val`.toDouble() .toInt()== 24) drawable = R.drawable.ic_700_ml
            else if (`val`.toDouble() .toInt()== 27) drawable = R.drawable.ic_800_ml
            else if (`val`.toDouble() .toInt()== 30) drawable = R.drawable.ic_900_ml
            else if (`val`.toDouble() .toInt()== 34) drawable = R.drawable.ic_1000_ml
        }

        return drawable
    }
}