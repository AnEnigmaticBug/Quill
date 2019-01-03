package com.anenigmaticbug.quill.screens.editnote.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anenigmaticbug.quill.R
import com.anenigmaticbug.quill.screens.editnote.EditNoteViewModel
import com.anenigmaticbug.quill.screens.editnote.EditNoteViewModelFactory
import com.anenigmaticbug.quill.screens.editnote.view.adapters.TagsAdapter
import com.anenigmaticbug.quill.screens.editnote.view.dialogs.InsertTagDialog
import com.anenigmaticbug.quill.screens.editnote.view.model.UiOrder
import com.anenigmaticbug.quill.screens.editnote.view.model.ViewLayerNote
import kotlinx.android.synthetic.main.fra_edit_note.view.*
import kotlinx.android.synthetic.main.viw_info_drawer.view.*

class EditNoteFragment : Fragment(), TagsAdapter.ClickListener {

    private lateinit var viewModel: EditNoteViewModel

    private val headingWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            viewModel.onUpdateHeadingAction(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    private val contentWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            viewModel.onUpdateContentAction(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val id = arguments!!.getLong("ID").let {
            if(it == 0L) { null } else { it }
        }

        viewModel = ViewModelProviders.of(this, EditNoteViewModelFactory(id))[EditNoteViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.fra_edit_note, container, false)

        rootPOV.tagsRCY.adapter = TagsAdapter(this)

        rootPOV.backBTN.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }

        rootPOV.infoBTN.setOnClickListener {
            (rootPOV as DrawerLayout).openDrawer(GravityCompat.END)
        }
        
        rootPOV.addTagBTN.setOnClickListener {
            InsertTagDialog().show(childFragmentManager, "AddTag")
        }

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.ShowWorking -> showWorkingState(it.note)
                is UiOrder.ShowFailure -> showFailureState(it.message)
            }
        })

        viewModel.toast.observe(this, Observer {
            if(it != null) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        })

        return rootPOV
    }

    override fun onDeleteTagAction(tag: String) {
        viewModel.onDeleteTagAction(tag)
    }

    private fun showWorkingState(note: ViewLayerNote) {
        view!!.headingTXT.setTextWithNotifyingWatcher(note.heading, headingWatcher)
        view!!.contentTXT.setTextWithNotifyingWatcher(note.content, contentWatcher)
        view!!.dateFDV.datetime = note.lastModified
        (view!!.tagsRCY.adapter as TagsAdapter).submitList(note.tags)
        view!!.wordCountLBL.text = note.wordCount.toString()
        view!!.charCountLBL.text = note.charCount.toString()
    }

    private fun showFailureState(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Set's text without triggering watcher and preserves the cursor position
    private fun EditText.setTextWithNotifyingWatcher(text: String, watcher: TextWatcher) {
        removeTextChangedListener(watcher)
        val cursorPos = selectionStart
        setText(text)
        setSelection(cursorPos)
        addTextChangedListener(watcher)
    }
}