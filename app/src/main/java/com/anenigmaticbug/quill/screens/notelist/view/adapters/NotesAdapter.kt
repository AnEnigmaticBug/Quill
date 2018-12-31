package com.anenigmaticbug.quill.screens.notelist.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anenigmaticbug.quill.R
import com.anenigmaticbug.quill.screens.notelist.view.model.ViewLayerNote
import com.anenigmaticbug.quill.screens.shared.core.model.Id
import kotlinx.android.synthetic.main.row_notes_rcy.view.*

class NotesAdapter(private val clickListener: ClickListener) : ListAdapter<ViewLayerNote, NotesAdapter.NoteVHolder>(DiffCallback()) {

    interface ClickListener {

        fun onNoteOfIdSelected(id: Id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVHolder {
        val rootPOV = LayoutInflater.from(parent.context).inflate(R.layout.row_notes_rcy, parent, false)
        return NoteVHolder(rootPOV)
    }

    override fun onBindViewHolder(holder: NoteVHolder, position: Int) {
        val note = getItem(position)

        holder.lastModifiedLBL.text = note.lastModified
        holder.headingLBL.text = note.heading
        holder.contentLBL.text = note.content

        holder.rootPOV.setOnClickListener {
            clickListener.onNoteOfIdSelected(note.id)
        }
    }

    class NoteVHolder(val rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val lastModifiedLBL: TextView = rootPOV.lastModifiedLBL
        val headingLBL: TextView = rootPOV.headingLBL
        val contentLBL: TextView = rootPOV.contentLBL
    }

    class DiffCallback : DiffUtil.ItemCallback<ViewLayerNote>() {

        override fun areItemsTheSame(p0: ViewLayerNote, p1: ViewLayerNote): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: ViewLayerNote, p1: ViewLayerNote): Boolean {
            return p0 == p1
        }
    }
}