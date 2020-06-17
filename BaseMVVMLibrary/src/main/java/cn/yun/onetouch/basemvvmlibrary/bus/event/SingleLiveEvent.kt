package cn.yun.onetouch.basemvvmlibrary.bus.event

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

open class SingleLiveEvent<T>: MutableLiveData<T>() {

    val mPending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        GlobalScope.launch(Dispatchers.Main) {
            if (hasActiveObservers()) {
                Log.e(TAG," Multiple observers registered but only one will be notified of changes.")
            }
            super.observe(owner, Observer { t ->
                mPending.compareAndSet(true, false)
                observer.onChanged(t)
            })
        }
    }

    override fun setValue(value: T?) {
        GlobalScope.launch(Dispatchers.Main) {
            mPending.set(true)
            super.setValue(value)
        }
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    fun call() {
        GlobalScope.launch {
            setValue(null)
        }
    }

    companion object {
        const val TAG = "SingleLiveEvent"
    }
}