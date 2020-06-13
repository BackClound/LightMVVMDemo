package cn.yun.onetouch.basemvvmlibrary.bus

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.ConcurrentHashMap

/**
 * 只会把订阅发生的时间点之后来自原始observable的数据发送给观察者
 *
 */

class RxBus {

    private var mBus: Subject<Any> = PublishSubject.create()
    private var mStickyEventMap: ConcurrentHashMap<Class<*>, Any> = ConcurrentHashMap()

    /**
     * 发送事件
     */
    fun post(event : Any) {
        mBus.onNext(event)
    }

    /**
     * 根据传递的eventType 类型返回特性类型(eventType)的被观察者
     *
     */
    fun <T> toObservable(eventType: Class<T>) : Observable<T>{
        return mBus.ofType(eventType)
    }

    /**
     * 判断是否有订阅者
     */
    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    fun reset() {
        mInstance = null
    }

    /**
     * sticky相关
     */
    /**
     * 发送一个新的sticky事件
     */
    fun postSticky (event: Any) {
        synchronized (event) {
            mStickyEventMap.put(event::class.java, event)
        }
        post(event)
    }

    /**
     * 根据传递的eventType类型返回特性类型(eventType)的被观察者
     */
    fun <Event> toObservableSticky(eventType : Class<Event>) : Observable<Event>{
        return synchronized(eventType) {
            val observable = mBus.ofType(eventType)
            val event = mStickyEventMap[eventType]
            if (event != null) {
                Observable.merge(
                    observable,
                    Observable.create { emitter ->
                        eventType.cast(event)?.let { emitter.onNext(it) }
                    })
            } else {
                observable
            }
        }
    }

    /**
     *根据eventType获取sticky事件
     */
    fun <Event> getStickyEvent (eventType: Class<Event>): Event?{
        return synchronized(eventType) {
            eventType.cast(mStickyEventMap[eventType])
        }
    }

    /**
     * 移除指定eventType的sticky事件
     */

    fun <Event> removeStickyEvent(eventType: Class<Event>): Event? {
        return synchronized(eventType) {
            eventType.cast(mStickyEventMap[eventType])
        }

    }

    /**
     * 移除所有的Sticky事件
     */
    fun removeAllSticky() {
        synchronized(Any()) {
            mStickyEventMap.clear()
        }
    }

    companion object RxBusInstance {
        private var mInstance :RxBus? = null
            get() {
                if (field == null) {
                    field = RxBus()
                }
                return field
            }

        fun getRxBusInstance(): RxBus {
            return mInstance!!
        }

    }

}