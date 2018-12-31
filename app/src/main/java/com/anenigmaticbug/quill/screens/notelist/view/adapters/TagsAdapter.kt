package com.anenigmaticbug.quill.screens.notelist.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anenigmaticbug.quill.R
import com.anenigmaticbug.quill.screens.notelist.view.model.ViewLayerTag
import kotlinx.android.synthetic.main.row_tags_rcy_note_list.view.*

class TagsAdapter(private val clickListener: ClickListener) : ListAdapter<ViewLayerTag, TagsAdapter.TagVHolder>(DiffCallback()) {

    interface ClickListener {

        fun onTagSelected(tag: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagVHolder {
        val rootPOV = LayoutInflater.from(parent.context).inflate(R.layout.row_tags_rcy_note_list, parent, false)
        return TagVHolder(rootPOV)
    }

    override fun onBindViewHolder(holder: TagVHolder, position: Int) {
        val tag = getItem(position)

        holder.iconIMG.setImageResource(tag.icon)
        holder.nameLBL.text = tag.name

        when(tag.isSelected) {
            true  -> {
                holder.rootPOV.setBackgroundResource(R.color.gry02)
            }
            false -> {
                holder.rootPOV.setBackgroundResource(R.color.clear)
            }
        }

        holder.rootPOV.setOnClickListener {
            clickListener.onTagSelected(tag.name)
        }
    }

    class TagVHolder(val rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val iconIMG: ImageView = rootPOV.iconIMG
        val nameLBL: TextView = rootPOV.nameLBL
    }

    class DiffCallback : DiffUtil.ItemCallback<ViewLayerTag>() {

        override fun areItemsTheSame(p0: ViewLayerTag, p1: ViewLayerTag): Boolean {
            return p0.name == p1.name
        }

        override fun areContentsTheSame(p0: ViewLayerTag, p1: ViewLayerTag): Boolean {
            return p0 == p1
        }
    }
}