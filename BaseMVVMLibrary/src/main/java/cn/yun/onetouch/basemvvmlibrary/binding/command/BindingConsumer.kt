package cn.yun.onetouch.basemvvmlibrary.binding.command

import io.reactivex.disposables.Disposable

interface BindingConsumer<in T> {
    fun call(disposable : T)
}