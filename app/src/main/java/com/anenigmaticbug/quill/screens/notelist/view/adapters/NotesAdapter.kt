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
import com.anenigmaticbug.quill.screens.notelist.view.model.ViewLayerNote
import com.anenigmaticbug.quill.screens.shared.core.model.Id
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import kotlinx.android.synthetic.main.row_notes_rcy.view.*

class NotesAdapter(private val clickListener: ClickListener) : ListAdapter<ViewLayerNote, NotesAdapter.NoteVHolder>(DiffCallback()) {

    interface ClickListener {

        fun onNoteOfIdSelected(id: Id)

        fun onNoteOfIdTrashed(id: Id)

        fun onNoteOfIdDeleted(id: Id)

        fun onNoteOfIdRestored(id: Id)

        fun onNoteOfIdEMailed(id: Id)
    }

    private val viewBinderHelper = ViewBinderHelper().also { it.setOpenOnlyOne(true) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVHolder {
        val rootPOV = LayoutInflater.from(parent.context).inflate(R.layout.row_notes_rcy, parent, false)
        return NoteVHolder(rootPOV)
    }

    override fun onBindViewHolder(holder: NoteVHolder, position: Int) {
        val note = getItem(position)

        holder.lastModifiedLBL.text = note.lastModified
        holder.headingLBL.text = note.heading
        holder.contentLBL.text = note.content

        holder.upperLayerPOV.setOnClickListener {
            clickListener.onNoteOfIdSelected(note.id)
        }

        holder.noteAction1BTN.setImageResource(
            if(note.isTrashed) { R.drawable.ic_restore_40dp_40dp } else { R.drawable.ic_mail_40dp_40dp }
        )

        holder.noteAction2BTN.setImageResource(
            if(note.isTrashed) { R.drawable.ic_delete_40dp_40dp } else { R.drawable.ic_trash_40dp_40dp }
        )

        holder.noteAction1BTN.setOnClickListener {
            when(note.isTrashed) {
                true  -> clickListener.onNoteOfIdRestored(note.id)
                false -> clickListener.onNoteOfIdEMailed(note.id)
            }
        }

        holder.noteAction2BTN.setOnClickListener {
            when(note.isTrashed) {
                true  -> clickListener.onNoteOfIdDeleted(note.id)
                false -> clickListener.onNoteOfIdTrashed(note.id)
            }
        }

        viewBinderHelper.bind(holder.rootPOV as SwipeRevealLayout, note.id.toString())

        (holder.rootPOV as SwipeRevealLayout).close(true)
    }

    class NoteVHolder(val rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val upperLayerPOV: View = rootPOV.upperLayerPOV
        val lastModifiedLBL: TextView = rootPOV.lastModifiedLBL
        val headingLBL: TextView = rootPOV.headingLBL
        val contentLBL: TextView = rootPOV.contentLBL
        val noteAction1BTN: ImageView = rootPOV.noteAction1BTN
        val noteAction2BTN: ImageView = rootPOV.noteAction2BTN
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