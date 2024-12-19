package rpt.tool.mementobibere.utils.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.utils.data.model.ReachedGoal

@SuppressLint("NewApi")
class ReachedAdapter(
    var mContext: Context,
    reachedArrayList: ArrayList<ReachedGoal>,
    var callBack: CallBack
) : RecyclerView.Adapter<ReachedAdapter.ViewHolder?>() {
    private val reachedArrayList: ArrayList<ReachedGoal> = reachedArrayList

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return reachedArrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_item_reached, parent, false)
        )
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.data.text = reachedArrayList[position].date
        holder.lbl_qta.text = reachedArrayList[position].containerValue + " ml - " +
                reachedArrayList[position].containerValueOZ + " fl oz"

        Glide.with(mContext).load(mContext.getDrawable(R.drawable.ic_dashboard_reached))
            .into(holder.imageView)

        holder.btnRemoveRow.setOnClickListener {
            callBack.onClickRemove(
                reachedArrayList[position],
                position
            )
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById<ImageView>(R.id.reached_img)
        var data: TextView = itemView.findViewById<TextView>(R.id.data)
        var lbl_qta: TextView = itemView.findViewById<TextView>(R.id.lbl_qta)
        var btnRemoveRow: ImageView = itemView.findViewById<ImageView>(R.id.btnRemoveRow)
    }

    interface CallBack {
        fun onClickSelect(reached: ReachedGoal?, position: Int)
        fun onClickRemove(reached: ReachedGoal?, position: Int)
    }
}