package cn.yun.onetouch.basemvvmlibrary.binding.command

import io.reactivex.disposables.Disposable

interface BindingConsumer<T> {
    fun call(disposable : T)
}