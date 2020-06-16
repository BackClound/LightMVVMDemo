package cn.yun.onetouch.basemvvmlibrary.bus

import cn.yun.onetouch.basemvvmlibrary.binding.command.BindingAction
import cn.yun.onetouch.basemvvmlibrary.binding.command.BindingConsumer
import java.lang.ref.WeakReference

class WeakAction<T>(target: Any?, action: BindingAction?, consumer: BindingConsumer<T>?) {
    var bindingAction: BindingAction? = null
    var bindingConsumer: BindingConsumer<T>? = null
    var isLive: Boolean? = false
    var targetWeak = null
    var weakReference: WeakReference<Any?>? = null

//    constructor(target: Any?, action: BindingAction?) {
//        bindingAction = action
//        weakReference = WeakReference(target)
//    }

    init {
        bindingAction = action
        bindingConsumer = consumer
        weakReference = WeakReference(target)
    }

    fun execute() {
        bindingAction?.call()
    }

    fun execute(parameter: T) {
        bindingConsumer?.call(parameter)
    }

    fun markForDeletion() {
        weakReference?.clear()
        weakReference = null
        bindingConsumer = null
        bindingAction = null
    }

    fun getAction(): BindingAction? {
        return bindingAction
    }

    fun getConsumer(): BindingConsumer<T>? {
        return bindingConsumer
    }

    fun getTarget(): Any? {
        return weakReference?.get()
    }

    fun isLive(): Boolean {
        return !(weakReference == null || weakReference?.get() == null)
    }

}