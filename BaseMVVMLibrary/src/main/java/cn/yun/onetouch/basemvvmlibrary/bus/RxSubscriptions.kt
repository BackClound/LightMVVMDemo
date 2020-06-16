package cn.yun.onetouch.basemvvmlibrary.bus

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * 管理 CompositeDisposable 复合订阅
 */
object RxSubscriptions {
    val mSubscriptions = CompositeDisposable()

    fun isDisposed(): Boolean {
        return mSubscriptions.isDisposed
    }

    fun add(disposable: Disposable) {
        synchronized(disposable) {
            mSubscriptions.add(disposable)
        }
    }

    fun remove(disposable: Disposable) {
        synchronized(disposable) {
            mSubscriptions.remove(disposable)
        }
    }

    fun clear () {
        mSubscriptions.clear()
    }

    fun dispose() {
        mSubscriptions.dispose()
    }

}