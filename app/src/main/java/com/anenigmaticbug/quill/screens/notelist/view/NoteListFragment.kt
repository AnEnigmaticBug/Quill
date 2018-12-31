package com.anenigmaticbug.quill.screens.notelist.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.anenigmaticbug.quill.R
import com.anenigmaticbug.quill.screens.notelist.NoteListViewModel
import com.anenigmaticbug.quill.screens.notelist.NoteListViewModelFactory
import com.anenigmaticbug.quill.screens.notelist.view.adapters.NotesAdapter
import com.anenigmaticbug.quill.screens.notelist.view.adapters.TagsAdapter
import com.anenigmaticbug.quill.screens.notelist.view.model.UiOrder
import com.anenigmaticbug.quill.screens.notelist.view.model.ViewLayerNote
import com.anenigmaticbug.quill.screens.notelist.view.model.ViewLayerTag
import com.anenigmaticbug.quill.screens.shared.core.model.Id
import kotlinx.android.synthetic.main.fra_note_list.view.*
import kotlinx.android.synthetic.main.viw_side_drawer.view.*

class NoteListFragment : Fragment(), NotesAdapter.ClickListener, TagsAdapter.ClickListener {

    private lateinit var viewModel: NoteListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, NoteListViewModelFactory())[NoteListViewModel::class.java]

        val rootPOV = inflater.inflate(R.layout.fra_note_list, container, false)

        rootPOV.notesRCY.adapter = NotesAdapter(this)
        rootPOV.tagsRCY.adapter = TagsAdapter(this)

        rootPOV.menuBTN.setOnClickListener {
            (rootPOV as SlidingPaneLayout).openPane()
        }

        rootPOV.notesRCY.setOnScrollListener(object : RecyclerView.OnScrollListener() {

            var wasScrollingUp = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                wasScrollingUp = when {
                    dy > 0 -> {
                        if(!wasScrollingUp) {
                            hidePriTopBar()
                        }
                        true
                    }
                    else   -> {
                        if(wasScrollingUp) {
                            showPriTopBar()
                        }
                        false
                    }
                }
            }
        })

        rootPOV.tagsRCY.setOnScrollListener(object : RecyclerView.OnScrollListener() {

            var wasScrollingUp = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                wasScrollingUp = when {
                    dy > 0 -> {
                        if(!wasScrollingUp) {
                            hideSecTopBar()
                        }
                        true
                    }
                    else   -> {
                        if(wasScrollingUp) {
                            showSecTopBar()
                        }
                        false
                    }
                }
            }
        })

        viewModel.order.observe(this, Observer {
            when(it) {
                is UiOrder.ShowWorking -> showWorkingState(it.tags, it.notes)
                is UiOrder.ShowFailure -> showFailureState(it.message)
            }
        })

        return rootPOV
    }

    override fun onNoteOfIdSelected(id: Id) {
    }

    override fun onTagSelected(tag: String) {
        viewModel.onTagSelectAction(tag)
    }

    fun showPriTopBar() {
        val animators = listOf(
            view!!.topBarPriBackgroundPOV,
            view!!.menuBTN,
            view!!.currentTagLBL,
            view!!.searchBTN
        ).map { ObjectAnimator.ofFloat(it, "translationY", 0f).apply {
            duration = 300
        } }
        AnimatorSet().also {
            it.playTogether(animators)
            it.start()
        }
    }

    fun hidePriTopBar() {
        val animators = listOf(
            view!!.topBarPriBackgroundPOV,
            view!!.menuBTN,
            view!!.currentTagLBL,
            view!!.searchBTN
        ).map { ObjectAnimator.ofFloat(it, "translationY", -200f).apply {
            duration = 300
        } }
        AnimatorSet().also {
            it.playTogether(animators)
            it.start()
        }
    }

    fun showSecTopBar() {
        val animators = listOf(
            view!!.topBarSecBackgroundPOV,
            view!!.drawerTitleLBL
        ).map { ObjectAnimator.ofFloat(it, "translationY", 0f).apply {
            duration = 300
        } }
        AnimatorSet().also {
            it.playTogether(animators)
            it.start()
        }
    }

    fun hideSecTopBar() {
        val animators = listOf(
            view!!.topBarSecBackgroundPOV,
            view!!.drawerTitleLBL
        ).map { ObjectAnimator.ofFloat(it, "translationY", -200f).apply {
            duration = 300
        } }
        AnimatorSet().also {
            it.playTogether(animators)
            it.start()
        }
    }

    private fun showWorkingState(tags: List<ViewLayerTag>, notes: List<ViewLayerNote>) {
        view!!.currentTagLBL.text = "#${tags.find { it.isSelected }!!.name}"
        (view!!.tagsRCY.adapter as TagsAdapter).submitList(tags)
        (view!!.notesRCY.adapter as NotesAdapter).submitList(notes)
    }

    private fun showFailureState(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}