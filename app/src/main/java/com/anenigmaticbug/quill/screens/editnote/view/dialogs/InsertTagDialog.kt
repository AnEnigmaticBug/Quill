package com.anenigmaticbug.quill.screens.editnote.view.dialogs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anenigmaticbug.quill.R
import com.anenigmaticbug.quill.screens.editnote.EditNoteViewModel
import kotlinx.android.synthetic.main.dia_insert_tag.view.*

class InsertTagDialog : DialogFragment() {

    private lateinit var viewModel: EditNoteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(parentFragment!!)[EditNoteViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.dia_insert_tag, container, false)

        rootPOV.cancelBTN.setOnClickListener {
            dismiss()
        }

        rootPOV.doneBTN.setOnClickListener {
            if(viewModel.isValidTag.value == true) {
                viewModel.onInsertTagAction(rootPOV.tagTXT.text.toString())
                dismiss()
            } else {
                Toast.makeText(context, "Please enter a valid tag first", Toast.LENGTH_SHORT).show()
            }
        }

        rootPOV.tagTXT.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                viewModel.onTagToBeInsertedTextChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        viewModel.isValidTag.observe(this, Observer {
            val message = when(it) {
                true  -> "The entered text is a valid tag."
                false -> "The tag is already used in note."
            }
            rootPOV.messageLBL.text = message
        })

        return rootPOV
    }
}