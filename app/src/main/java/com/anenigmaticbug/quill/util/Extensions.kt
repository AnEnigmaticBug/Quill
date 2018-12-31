package com.anenigmaticbug.quill.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun CompositeDisposable.set(disposable: Disposable) {
    clear()
    add(disposable)
}

fun<T> LiveData<T>.toMut(): MutableLiveData<T> {
    return when(this) {
        is MutableLiveData -> this
        else               -> throw IllegalArgumentException("Not a MutableLiveData")
    }
}

fun<T> List<T>.prepend(vararg elems: T): List<T> {
    return elems.toMutableList() + this
}