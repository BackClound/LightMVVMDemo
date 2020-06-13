package cn.yun.onetouch.basemvvmlibrary.binding.command

interface BindingFunction<T> {
    fun call() : T
}