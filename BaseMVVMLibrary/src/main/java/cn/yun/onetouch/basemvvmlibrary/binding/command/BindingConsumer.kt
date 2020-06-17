package cn.yun.onetouch.basemvvmlibrary.binding.command

interface BindingConsumer<in T> {
    fun call(disposable : T)
}