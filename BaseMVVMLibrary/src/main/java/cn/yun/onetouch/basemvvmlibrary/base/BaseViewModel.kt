package cn.yun.onetouch.basemvvmlibrary.base

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import cn.yun.onetouch.basemvvmlibrary.bus.event.SingleLiveEvent
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.lang.ref.WeakReference

class BaseViewModel<Model : BaseModel>(application: Application, model: Model?) :
    AndroidViewModel(application),
    IBaseViewModel, Consumer<Disposable> {
    var model: Model? = model
    private var uc = UIChangeLiveData()

    //弱引用持有者
    var lifecycle: WeakReference<LifecycleProvider<*>>? = null

    //管理Rxjava，主要针对Rxjava异步操作造成的内存泄漏
    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    protected fun addSubscribe(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    /**
     * 注入RxLifeCycle生命周期
     */
    fun injectLifeCycleProvider(lifecycleProvider: LifecycleProvider<*>) {
        lifecycle = WeakReference(lifecycleProvider)
    }

    fun showDialog(title: String?) {
        uc.showDialogEvent.postValue(title ?: "请稍后。。。")
    }

    fun dismissDialog() {
        uc.dismissDialogEvent.call()
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz : Class<*>) {
        startActivity(clz,null)
    }

    /**
     * 跳转界面
     * @param clz 所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity (clz: Class<*>, bundle: Bundle?) {
        var param  = hashMapOf<String, Any?>()
        param.put(ParameterField.CLASS.name, clz)
        bundle?.let {
            param.put(ParameterField.BUNDLE.name, it)
        }
        uc.startActivityEvent.postValue(param)
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    fun startCanonicalActivity(canonicalName: String) {
        startCanonicalActivity(canonicalName, null)
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    fun startCanonicalActivity(canonicalName: String, bundle: Bundle?) {
        var param = hashMapOf<String, Any?>()
        param.put(ParameterField.CANONICAL_NAME.name, canonicalName)
        bundle?.let {
            param.put(ParameterField.BUNDLE.name, it)
        }
        uc.startContainerActivityEvent.postValue(param)
    }

    /**
     * 关闭界面
     */
    fun finish() {
        uc.finishEvent.call()
    }

    /**
     * 返回上一层
     */
    fun backPress() {
        uc.onBackPressEvent.call()
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun accept(disposable: Disposable) {
        addSubscribe(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        model?.onCleared()
        //viewmodel销毁时会执行，同时取消所有操作
        mCompositeDisposable.clear()
    }

    inner class UIChangeLiveData : SingleLiveEvent<Model>() {
        val showDialogEvent = SingleLiveEvent<String>()
        val dismissDialogEvent = SingleLiveEvent<Void>()
        val startActivityEvent = SingleLiveEvent<Map<String, Any?>>()
        val startContainerActivityEvent = SingleLiveEvent<Map<String, Any?>>()
        val finishEvent = SingleLiveEvent<Void>()
        val onBackPressEvent = SingleLiveEvent<Void>()

        override fun observe(owner: LifecycleOwner, observer: Observer<in Model>) {
            super.observe(owner, observer)
        }
    }

    enum class ParameterField(name: String) {
        CLASS("Class"),
        CANONICAL_NAME("CANONICAL_NAME"),
        BUNDLE("BUNDLE")
    }

}