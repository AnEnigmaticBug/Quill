package com.anenigmaticbug.quill.screens.notelist.view.model

sealed class UiOrder {

    data class ShowWorking(val tags: List<ViewLayerTag>, val notes: List<ViewLayerNote>) : UiOrder()

    data class ShowFailure(val message: String) : UiOrder()
}