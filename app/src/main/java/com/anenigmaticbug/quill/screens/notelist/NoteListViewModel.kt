package com.anenigmaticbug.quill.screens.notelist

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anenigmaticbug.quill.R
import com.anenigmaticbug.quill.screens.notelist.view.model.UiOrder
import com.anenigmaticbug.quill.screens.notelist.view.model.ViewLayerNote
import com.anenigmaticbug.quill.screens.notelist.view.model.ViewLayerTag
import com.anenigmaticbug.quill.screens.shared.core.model.Id
import com.anenigmaticbug.quill.screens.shared.core.model.Note
import com.anenigmaticbug.quill.screens.shared.data.repo.NoteRepository
import com.anenigmaticbug.quill.util.*
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

class NoteListViewModel(private val nRepo: NoteRepository) : ViewModel() {

    val order: LiveData<UiOrder> = MutableLiveData()
    val toast: LiveData<String?> = MutableLiveData()


    // To remember the tag which was selected before the user used search.
    private lateinit var lastSelectedTag: String


    private val d1 = CompositeDisposable()
    private val d2 = CompositeDisposable()


    init {
        onTagSelectAction("Notes")
    }


    fun onTagSelectAction(tag: String) {
        lastSelectedTag = tag
        val combiner = BiFunction { t1: List<String>, t2: List<Note> ->
            val notesTag = ViewLayerTag("Notes", R.drawable.ic_notes_24dp_24dp, "Notes" == tag)
            val trashTag = ViewLayerTag("Trash", R.drawable.ic_trash_24dp_24dp, "Trash" == tag)

            val tags = t1.map { ViewLayerTag(it, R.drawable.ic_tag_24dp_24dp, it == tag) }.prepend(notesTag, trashTag)

            UiOrder.ShowWorking(tags, t2.filterByTag(tag).sortedByDescending { it.lastModified }.toViewLayer())
        }

        d1.set(Flowable.combineLatest(nRepo.getAllTags(), nRepo.getAllNotes(), combiner)
            .subscribe(
                {
                    order.toMut().postValue(it)
                },
                {
                    order.toMut().postValue(UiOrder.ShowFailure("Something went wrong while retrieving your notes"))
                }
            ))
    }

    fun onSearchTextChanged(query: String) {
        val combiner = BiFunction { t1: List<String>, t2: List<Note> ->
            val notesTag = ViewLayerTag("Notes", R.drawable.ic_notes_24dp_24dp, "Notes" == lastSelectedTag)
            val trashTag = ViewLayerTag("Trash", R.drawable.ic_trash_24dp_24dp, "Trash" == lastSelectedTag)

            val tags = t1.map { ViewLayerTag(it, R.drawable.ic_tag_24dp_24dp, it == lastSelectedTag) }.prepend(notesTag, trashTag)

            UiOrder.ShowWorking(tags, t2.filterByQuery(query).sortedByDescending { it.lastModified }.toViewLayer())
        }

        d1.set(Flowable.combineLatest(nRepo.getAllTags(), nRepo.getAllNotes(), combiner)
            .subscribe(
                {
                    order.toMut().postValue(it)
                },
                {
                    order.toMut().postValue(UiOrder.ShowFailure("Something went wrong while retrieving your notes"))
                }
            ))
    }

    fun onEndSearchAction() {
        onTagSelectAction(lastSelectedTag)
    }

    fun onTrashNoteAction(id: Id) {
        setTrashed(id, true)
    }

    @SuppressLint("CheckResult")
    fun onDeleteNoteAction(id: Id) {
        nRepo.deleteNoteById(id).subscribe(
            {
                toast.toMut().postValue("Note deleted")
            },
            {
                toast.toMut().postValue("Couldn't delete note")
            }
        )
    }

    fun onRestoreNoteAction(id: Id) {
        setTrashed(id, false)
    }


    private fun setTrashed(id: Id, value: Boolean) {
        d2.set(nRepo.getNoteById(id)
            .take(1)
            .subscribe(
                { _note ->
                    nRepo.updateNote(_note.copy(isTrashed = value))
                        .subscribe(
                            {
                                val message = when(value) {
                                    true  -> "Note trashed"
                                    false -> "Note restored"
                                }
                                toast.toMut().postValue(message)
                            },
                            {
                                toast.toMut().postValue("Couldn't finish the action")
                            }
                        )
                },
                {
                    order.toMut().postValue(UiOrder.ShowFailure("Something went wrong :("))
                }
            ))
    }


    private fun List<Note>.toViewLayer(): List<ViewLayerNote> {

        fun LocalDateTime.age(): String {
            val mn = 60
            val hr = 60*mn
            val dy = 24*hr
            val wk = 7*dy
            val mt = 30*dy
            val yr = 12*mt

            val timeDifference = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - this.toEpochSecond(ZoneOffset.UTC)

            return when(timeDifference) {
                in 0*hr..1*hr -> "${timeDifference/mn}m"
                in 1*hr..1*dy -> "${timeDifference/hr}H"
                in 1*dy..1*wk -> "${timeDifference/dy}D"
                in 1*wk..1*mt -> "${timeDifference/wk}W"
                in 1*mt..1*yr -> "${timeDifference/mt}M"
                else          -> "${timeDifference/yr}Y"
            }
        }

        return this.map {
            ViewLayerNote(it.id, it.heading, it.content, it.lastModified.age(), it.isTrashed)
        }
    }

    private fun List<Note>.filterByTag(tag: String): List<Note> {
        return when(tag) {
            "Notes" -> this.filter { !it.isTrashed }
            "Trash" -> this.filter {  it.isTrashed }
            else    -> this.filter { !it.isTrashed && tag in it.tags }
        }
    }

    private fun List<Note>.filterByQuery(query: String): List<Note> {

        fun String.containsIgnoreCase(substring: String): Boolean {
            return this.toLowerCase().contains(substring.toLowerCase())
        }

        return this.filter {
            // Search is case insensitive for heading and content. For tags it is case-sensitive
            it.heading.containsIgnoreCase(query) || it.heading.containsIgnoreCase(query) || query in it.tags
        }
    }


    override fun onCleared() {
        super.onCleared()
        d1.clear()
        d2.clear()
    }
}