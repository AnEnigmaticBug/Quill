package com.anenigmaticbug.quill.screens.notelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anenigmaticbug.quill.R
import com.anenigmaticbug.quill.screens.notelist.view.model.UiOrder
import com.anenigmaticbug.quill.screens.notelist.view.model.ViewLayerNote
import com.anenigmaticbug.quill.screens.notelist.view.model.ViewLayerTag
import com.anenigmaticbug.quill.screens.shared.core.model.Note
import com.anenigmaticbug.quill.screens.shared.data.repo.NoteRepository
import io.reactivex.Flowable
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito
import org.threeten.bp.LocalDateTime
import org.junit.rules.TestRule
import org.junit.Rule



class NoteListViewModelTest {

    @JvmField @Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NoteListViewModel

    private val notes = listOf(
        Note(2, "Hi", "Bye", listOf("Language"), LocalDateTime.now().minusDays(11)),
        Note(3, "Yo", "C u", listOf("Internet"), LocalDateTime.now().minusHours(3)),
        Note(4, "--", "---", listOf("DeadLife"), LocalDateTime.now().minusWeeks(2), true)
    )

    @Test
    fun test_OrderWhenNoTagsSelected() {
        val nRepo = Mockito.mock(NoteRepository::class.java).also {
            Mockito.`when`(it.getAllTags())
                .thenReturn(Flowable.just(listOf("Language", "Internet", "DeadLife")))

            Mockito.`when`(it.getAllNotes())
                .thenReturn(Flowable.just(notes))
        }

        viewModel = NoteListViewModel(nRepo)

        val expected = UiOrder.ShowWorking(
            listOf(
                ViewLayerTag("Notes", R.drawable.ic_notes_24dp_24dp, true),
                ViewLayerTag("Trash", R.drawable.ic_trash_24dp_24dp, false),
                ViewLayerTag("Language", R.drawable.ic_tag_24dp_24dp, false),
                ViewLayerTag("Internet", R.drawable.ic_tag_24dp_24dp, false),
                ViewLayerTag("DeadLife", R.drawable.ic_tag_24dp_24dp, false)
            ),
            listOf(
                ViewLayerNote(3, "Yo", "C u", "3H", false),
                ViewLayerNote(2, "Hi", "Bye", "1W", false)
            )
        )

        assertEquals(expected, viewModel.order.value)
    }

    @Test
    fun test_OrderWhenOneTagSelected() {
        val nRepo = Mockito.mock(NoteRepository::class.java).also {
            Mockito.`when`(it.getAllTags())
                .thenReturn(Flowable.just(listOf("Language", "Internet", "DeadLife")))

            Mockito.`when`(it.getAllNotes())
                .thenReturn(Flowable.just(notes))
        }

        viewModel = NoteListViewModel(nRepo)
        viewModel.onTagSelectAction("Internet")

        val expected = UiOrder.ShowWorking(
            listOf(
                ViewLayerTag("Notes", R.drawable.ic_notes_24dp_24dp, false),
                ViewLayerTag("Trash", R.drawable.ic_trash_24dp_24dp, false),
                ViewLayerTag("Language", R.drawable.ic_tag_24dp_24dp, false),
                ViewLayerTag("Internet", R.drawable.ic_tag_24dp_24dp, true),
                ViewLayerTag("DeadLife", R.drawable.ic_tag_24dp_24dp, false)
            ),
            listOf(
                ViewLayerNote(3, "Yo", "C u", "3H", false)
            )
        )

        assertEquals(expected, viewModel.order.value)
    }

    @Test
    fun test_OrderWhenAProblemOccurs() {
        val nRepo = Mockito.mock(NoteRepository::class.java).also {
            Mockito.`when`(it.getAllTags())
                .thenReturn(Flowable.just(listOf("Language", "Internet", "DeadLife")))

            Mockito.`when`(it.getAllNotes())
                .thenReturn(Flowable.error(IllegalStateException("ERROR!!")))
        }

        viewModel = NoteListViewModel(nRepo)
        viewModel.onTagSelectAction("Internet")

        assertTrue(viewModel.order.value is UiOrder.ShowFailure)
    }
}