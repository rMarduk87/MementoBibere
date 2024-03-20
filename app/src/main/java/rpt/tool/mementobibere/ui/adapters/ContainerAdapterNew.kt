package rpt.tool.mementobibere.ui.adapters

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
import rpt.tool.mementobibere.data.models.Container
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager

@SuppressLint("NewApi")
class ContainerAdapterNew(
    var mContext: Context,
    containerArrayList: ArrayList<Container>,
    callBack: CallBack
) : RecyclerView.Adapter<ContainerAdapterNew.ViewHolder?>() {
    private val containerArrayList: ArrayList<Container>
    private val callBack: CallBack

    init {
        this.containerArrayList = containerArrayList
        this.callBack = callBack
    }

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
            if (SharedPreferencesManager.unitString.equals("ml", ignoreCase = true)) {
                if (containerArrayList[position].containerValue!!.contains(".")
                ) holder.textView.text = AppUtils.decimalFormat.format(
                    containerArrayList[position].containerValue!!.toDouble()
                ) + " " + SharedPreferencesManager.unitString else holder.textView.text =
                    containerArrayList[position].containerValue + " " + SharedPreferencesManager.unitString
            } else {
                if (containerArrayList[position].containerValue!!.contains(".")
                ) holder.textView.text = AppUtils.decimalFormat.format(
                    containerArrayList[position].containerValueOZ!!.toDouble()
                ) + " " + SharedPreferencesManager.unitString else holder.textView.text =
                    containerArrayList[position].containerValueOZ + " " + SharedPreferencesManager.unitString
            }
        } catch (e: Exception) {
        }
        if (containerArrayList[position].isCustom) Glide.with(mContext)
            .load(R.drawable.ic_custom_ml).into(holder.imageView) else Glide.with(mContext)
            .load(getImage(position)).into(holder.imageView)
        holder.item_block.setOnClickListener(View.OnClickListener {
            callBack.onClickSelect(
                containerArrayList[position], position
            )
        })
        if (containerArrayList[position].isSelected) {
            holder.img_selected.setVisibility(View.VISIBLE)
        } else {
            holder.img_selected.setVisibility(View.INVISIBLE)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var imageView: ImageView
        var item_block: LinearLayout
        var img_selected: ImageView

        init {
            textView = itemView.findViewById<TextView>(R.id.container_name)
            imageView = itemView.findViewById<ImageView>(R.id.container_img)
            item_block = itemView.findViewById<LinearLayout>(R.id.item_block)
            img_selected = itemView.findViewById<ImageView>(R.id.img_selected)
        }
    }

    interface CallBack {
        fun onClickSelect(container: Container?, position: Int)
    }

    fun getImage(pos: Int): Int {
        var drawable: Int = R.drawable.ic_custom_ml
        if (SharedPreferencesManager.unitString.equals("ml", ignoreCase = true)) {
            if (containerArrayList[pos].containerValue!!.toDouble() == 50.toDouble()) drawable =
                R.drawable.ic_50_ml else if (containerArrayList[pos].containerValue!!.toFloat() == 100.toFloat()
            ) drawable = R.drawable.ic_100_ml else if (containerArrayList[pos].containerValue!!.toFloat() == 150.toFloat()
            ) drawable = R.drawable.ic_150_ml else if (containerArrayList[pos].containerValue!!
                    .toFloat() == 200.toFloat()
            ) drawable = R.drawable.ic_200_ml else if (containerArrayList[pos].containerValue!!
                    .toFloat() == 250.toFloat()
            ) drawable = R.drawable.ic_250_ml else if (containerArrayList[pos].containerValue!!
                    .toFloat() == 300.toFloat()
            ) drawable = R.drawable.ic_300_ml else if (containerArrayList[pos].containerValue!!
                    .toFloat() == 500.toFloat()
            ) drawable = R.drawable.ic_500_ml else if (containerArrayList[pos].containerValue!!
                    .toFloat() == 600.toFloat()
            ) drawable = R.drawable.ic_600_ml else if (containerArrayList[pos].containerValue!!
                    .toFloat() == 700.toFloat()
            ) drawable = R.drawable.ic_700_ml else if (containerArrayList[pos].containerValue!!
                    .toFloat() == 800.toFloat()
            ) drawable = R.drawable.ic_800_ml else if (containerArrayList[pos].containerValue!!
                    .toFloat() == 900.toFloat()
            ) drawable = R.drawable.ic_900_ml else if (containerArrayList[pos].containerValue!!
                    .toFloat() == 1000.toFloat()
            ) drawable = R.drawable.ic_1000_ml
        } else {
            if (containerArrayList[pos].containerValueOZ!!.toFloat() == 2.toFloat()) drawable =
                R.drawable.ic_50_ml else if (containerArrayList[pos].containerValueOZ!!
                    .toFloat() == 1.69f
            ) drawable = R.drawable.ic_100_ml else if (containerArrayList[pos].containerValueOZ!!
                    .toFloat() == 3.38f
            ) drawable = R.drawable.ic_150_ml else if (containerArrayList[pos].containerValueOZ!!
                    .toFloat() == 5.07f
            ) drawable = R.drawable.ic_200_ml else if (containerArrayList[pos].containerValueOZ!!
                    .toFloat() == 6.76f
            ) drawable = R.drawable.ic_250_ml else if (containerArrayList[pos].containerValueOZ!!
                    .toFloat() == 8.45f
            ) drawable = R.drawable.ic_300_ml else if (containerArrayList[pos].containerValueOZ!!
                    .toFloat() == 10.14f
            ) drawable = R.drawable.ic_500_ml else if (containerArrayList[pos].containerValueOZ!!
                    .toFloat() == 16.91f
            ) drawable = R.drawable.ic_600_ml else if (containerArrayList[pos].containerValueOZ!!
                    .toFloat() == 20.28f
            ) drawable = R.drawable.ic_700_ml else if (containerArrayList[pos].containerValueOZ!!
                    .toFloat() == 23.64f
            ) drawable = R.drawable.ic_800_ml else if (containerArrayList[pos].containerValueOZ!!
                    .toFloat() == 27.05f
            ) drawable = R.drawable.ic_900_ml else if (containerArrayList[pos].containerValueOZ!!
                    .toFloat() == 33.81f
            ) drawable = R.drawable.ic_1000_ml
        }
        return drawable
    }
}
