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
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.utils.data.BackUpFileModel

@SuppressLint("NewApi")
class FileAdapter(var mContext: Context, files: List<BackUpFileModel>, var callBack: CallBack) :
    RecyclerView.Adapter<FileAdapter.ViewHolder?>() {
    private val files: List<BackUpFileModel> = files

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_item_sound, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lbl_sound_name.text = files[position].name

        if (files[position].isSelected) {
            holder.img_selected.visibility = View.VISIBLE
            holder.item_block.background
                .setTint(mContext.resources.getColor(R.color.colorPrimary))
        } else {
            holder.img_selected.visibility = View.INVISIBLE
            holder.item_block.background.setTint(mContext.resources.getColor(R.color.white))
        }

        holder.item_block.setOnClickListener(View.OnClickListener {
            callBack.onClickSelect(
                files[position], position
            )
        })
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img_selected: ImageView = itemView.findViewById<ImageView>(R.id.img_selected)
        var item_block: LinearLayout = itemView.findViewById<LinearLayout>(R.id.item_block)
        var lbl_sound_name: TextView = itemView.findViewById<TextView>(R.id.lbl_sound_name)
    }

    interface CallBack {
        fun onClickSelect(file: BackUpFileModel?, position: Int)
    }
}

