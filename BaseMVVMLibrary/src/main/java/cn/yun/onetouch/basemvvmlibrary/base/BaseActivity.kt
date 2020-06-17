package cn.yun.onetouch.basemvvmlibrary.base

import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 */

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<*>> : RxAppCompatActivity(),
    IBaseView {

    private var binding: V? = null
    private var viewModel: VM? = null
    private var viewModelId: Int? = null
//    private val dialog : MaterialDialog

    override fun initParams() {
        TODO("Not yet implemented")
    }

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun initViewObservable() {
        TODO("Not yet implemented")
    }
}