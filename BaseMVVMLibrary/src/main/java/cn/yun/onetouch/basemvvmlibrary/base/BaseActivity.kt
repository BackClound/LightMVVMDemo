package cn.yun.onetouch.basemvvmlibrary.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewbinding.ViewBinding
import cn.yun.onetouch.basemvvmlibrary.base.ParameterField.*
import cn.yun.onetouch.basemvvmlibrary.utils.materialDialogUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.lang.reflect.ParameterizedType

/**
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 */

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<*>>() : RxAppCompatActivity(),
    IBaseView {

    private var binding: V? = null
    private var viewModel: VM = initViewModel()
    private var viewModelId: Int = initVariableId()
    private var dialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //页面接受的参数方法
        initParams()
        //私有的初始化DataBinding和ViewModel的方法
        initViewDataBinding(savedInstanceState)
        //私有的ViewModel与View的契约事件回调
        registerUIChangeLiveDataCallBack()

        //页面数据初始化方法
        initData()

        //页面事件监听方法，一般用于viewmodel层转到View层的事件注册
        initViewObservable()

        //注册RxBus
        viewModel.registerRxBus()
    }

    fun registerUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.uc.showDialogEvent.observe(this, Observer {
            showDialog(it)
        })

        //加载对话框消失
        viewModel.uc.dismissDialogEvent.observe(this, Observer {
            dismissDialog()
        })
        //跳转新界面
        viewModel.uc.startActivityEvent.observe(this, Observer {
            var clz = it.get(CLASS.name) as Class<*>
            var bundle = it[BUNDLE] as Bundle
            startActivity(clz = clz, bundle = bundle)
        })

        //跳转ContainerActivity
        viewModel.uc.startContainerActivityEvent.observe(this, Observer {
            var clz = it[ParameterField.CANONICAL_NAME] as String
            var bundle = it[ParameterField.BUNDLE] as Bundle
            startContainerActivity(clz, bundle)
        })

        //关闭界面
        viewModel.uc.finishEvent.observe(this, Observer {
            finish()
        })
        //关闭上一层
        viewModel.uc.onBackPressEvent.observe(this, Observer {
            onBackPressed()
        })
    }

    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        //DataBindingUtil类需要在project的build中配置dataBinding {enabled true}
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState))
        if (viewModel == null) {
            var modeClass: Class<*>
            var type = javaClass.genericSuperclass
            modeClass = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<VM>
            } else {
                //如果没有指定泛型参数，默认使用BaseViewModel
                BaseViewModel::class.java
            }
            viewModel = createViewModel(this, modeClass) as VM
        }

        //关联viewmode
        binding?.setVariable(viewModelId, viewModel)
        //支持LiveData绑定XML，数据改变，UI自动更新
        binding?.lifecycleOwner = this
        //让LiveModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel)
        //注入RxLifeCycle生命周期
        viewModel.injectLifeCycleProvider(this)
    }

    private fun showDialog(title: String) {
        dialog?.let {
            it.builder.title(title).build().show()
        } ?: run {
            dialog = materialDialogUtils.showIndeterminateProgressDialog(this, title, true).build()
            dialog?.show()
        }
    }

    private fun dismissDialog() {
        if (dialog == null || dialog?.isShowing != true) return
        dialog?.dismiss()
    }

    private fun startActivity(clz: Class<*>, bundle: Bundle) {
        var intent = Intent(this, clz)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun startContainerActivity(canonicalName: String) {
        startContainerActivity(canonicalName = canonicalName, bundle = null)
    }

    fun startContainerActivity(canonicalName: String, bundle: Bundle?) {
        val intent = Intent(this, RxAppCompatActivity::class.java)
        intent.putExtra("fragment", canonicalName)
        bundle?.let {
            intent.putExtra("bundle", bundle)
        }
        startActivity(intent)
    }

    private fun <VM : ViewModel> createViewModel(activity: FragmentActivity, cls: Class<VM>): VM? {
        return ViewModelProviders.of(activity).get(cls)
    }

    abstract fun initViewModel(): VM

    /**
     * 初始化viewmodel的id
     * @return BR的ID
     */
    abstract fun initVariableId(): Int

    /**
     * 初始化根布局
     * @return 布局layout的ID
     */
    abstract fun initContentView(savedInstanceState: Bundle?): Int

    override fun initParams() {
    }

    override fun initData() {
    }

    override fun initViewObservable() {
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeRxBus()
        binding?.unbind()
    }
}