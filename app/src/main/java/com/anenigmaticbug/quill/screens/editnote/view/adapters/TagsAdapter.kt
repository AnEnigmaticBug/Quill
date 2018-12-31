package com.anenigmaticbug.quill.screens.editnote.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anenigmaticbug.quill.R
import kotlinx.android.synthetic.main.row_tags_rcy_edit_note.view.*

class TagsAdapter(private val clickListener: ClickListener) : ListAdapter<String, TagsAdapter.TagVHolder>(DiffCallback()) {

    interface ClickListener {

        fun onDeleteTagAction(tag: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagVHolder {
        val rootPOV = LayoutInflater.from(parent.context).inflate(R.layout.row_tags_rcy_edit_note, parent, false)
        return TagVHolder(rootPOV)
    }

    override fun onBindViewHolder(holder: TagVHolder, position: Int) {
        val tag = getItem(position)

        holder.nameLBL.text = tag

        holder.removeBTN.setOnClickListener {
            clickListener.onDeleteTagAction(tag)
        }
    }

    class TagVHolder(rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
        val removeBTN: ImageView = rootPOV.removeBTN
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(p0: String, p1: String): Boolean {
            return p0 == p1
        }

        override fun areContentsTheSame(p0: String, p1: String): Boolean {
            return p0 == p1
        }
    }
}