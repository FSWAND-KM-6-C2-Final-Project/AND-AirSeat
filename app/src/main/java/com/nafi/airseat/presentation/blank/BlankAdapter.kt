package com.nafi.airseat.presentation.blank

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nafi.airseat.R

class BlankAdapter(context: BlankActivity, arrayList: ArrayList<BlankModel>) :
    RecyclerView.Adapter<BlankAdapter.ViewHolder>() {
    private val context: Context
    private val arrayList: ArrayList<BlankModel>
    var singleitem_selectionn_position = -1

    init {
        this.context = context
        this.arrayList = arrayList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val seatClass = arrayList[position]
        holder.itemView.findViewById<TextView>(R.id.tv_title).text = seatClass.name // Fix here
        if (singleitem_selectionn_position == position) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.md_theme_inversePrimary)) // Fix here
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent)) // Fix here
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                setSingleSelection(adapterPosition)
            }
        }

        private fun setSingleSelection(adapterPosition: Int) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
            notifyItemChanged(singleitem_selectionn_position)
            singleitem_selectionn_position = adapterPosition
            notifyItemChanged(singleitem_selectionn_position)
        }
    }
}
