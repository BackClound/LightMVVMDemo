package cn.yun.onetouch.basemvvmlibrary.bus

import io.reactivex.observers.DisposableObserver
import java.lang.Exception

/**
 * 为RxBus使用的Subscriber, 主要提供next事件的try,catch
 */

abstract class RxBusSubscriber<T> : DisposableObserver<T>() {
    override fun onComplete() {}

    override fun onNext(t: T) {
        try {
            onEvent(t)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    abstract fun onEvent(t : T)

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }
}