package com.anenigmaticbug.quill.screens.editnote.view.model

sealed class UiOrder {

    data class ShowWorking(val note: ViewLayerNote) : UiOrder()

    data class ShowFailure(val message: String) : UiOrder()
}