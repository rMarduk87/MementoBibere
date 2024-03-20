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
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.data.models.SoundModel


@SuppressLint("NewApi")
class SoundAdapter(var mContext: Context, sounds: List<SoundModel>, callBack: CallBack) :
    RecyclerView.Adapter<SoundAdapter.ViewHolder?>() {
    private val sounds: List<SoundModel>
    var callBack: CallBack

    init {
        this.sounds = sounds
        this.callBack = callBack
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return sounds.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_item_sound, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lbl_sound_name.text = sounds[position].name
        if (sounds[position].isSelected) {
            holder.img_selected.setVisibility(View.VISIBLE)
            holder.item_block.background
                .setTint(mContext.resources.getColor(R.color.colorPrimary))
        } else {
            holder.img_selected.setVisibility(View.INVISIBLE)
            holder.item_block.background.setTint(mContext.resources.getColor(R.color.white))
        }
        holder.item_block.setOnClickListener(View.OnClickListener {
            callBack.onClickSelect(
                sounds[position], position
            )
        })
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img_selected: ImageView
        var item_block: LinearLayout
        var lbl_sound_name: TextView
        init {
            item_block = itemView.findViewById<LinearLayout>(R.id.item_block)
            img_selected = itemView.findViewById<ImageView>(R.id.img_selected)
            lbl_sound_name = itemView.findViewById<TextView>(R.id.lbl_sound_name)
        }
    }

    interface CallBack {
        fun onClickSelect(time: SoundModel?, position: Int)
    }
}
