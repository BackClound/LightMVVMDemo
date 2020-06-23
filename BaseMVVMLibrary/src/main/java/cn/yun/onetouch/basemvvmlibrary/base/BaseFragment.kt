package cn.yun.onetouch.basemvvmlibrary.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import cn.yun.onetouch.basemvvmlibrary.utils.materialDialogUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.trello.rxlifecycle2.components.support.RxFragment
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel<*>> : RxFragment(), IBaseView {
    protected var binding: V? = null
    protected var viewModel: VM? = null
    protected var viewModelId: Int? = null
    private var dialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            initContentView(inflater, container, savedInstanceState),
            container,
            false
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //私有的初始化Databinding 和 ViewModel
        initViewDataBinding()

        //私有的ViewModel和View的契约事件回调
        registerUIChangeLiveDataCallBack()

        //页面数据初始化方法
        initData()
        //页面事件监听方法,一般用于ViewModel层转到View层的注册
        initViewObservable()

        //注册RxBus
        viewModel?.registerRxBus()
    }

    //注册ViewModel和View的契约事件回调
    private fun registerUIChangeLiveDataCallBack() {
        viewModel?.uc?.showDialogEvent?.observe(this, Observer { title ->
            showDialog(title = title)
        })

        viewModel?.uc?.dismissDialogEvent?.observe(this, Observer {
            dismissDialog()
        })

        viewModel?.uc?.startActivityEvent?.observe(this, Observer { params ->
            val clz = params.get(ParameterField.CLASS.name) as Class<*>
            val bundle = params.get(ParameterField.BUNDLE.name) as Bundle
            startActivity(clz, bundle)
        })

        viewModel?.uc?.startContainerActivityEvent?.observe(this, Observer { params ->
            val canonicalName = params.get(ParameterField.CANONICAL_NAME.name) as String
            val bundle = params.get(ParameterField.BUNDLE.name) as Bundle
            startContainerActivity(canonicalName = canonicalName, bundle = bundle)
        })

        viewModel?.uc?.finishEvent?.observe(this, Observer {
            activity?.finish()
        })

        viewModel?.uc?.onBackPressEvent?.observe(this, Observer {
            activity?.onBackPressed()
        })

    }

    fun showDialog(title: String) {
        if (dialog != null) {
            dialog?.builder?.title(title)?.build()?.show()
        } else {
            val builder =
                materialDialogUtils.showIndeterminateProgressDialog(context!!, title, true)
            dialog = builder.show()
        }
    }

    fun dismissDialog() {
        dialog?.isShowing?.let {
            if (it) {
                dialog?.dismiss()
            }
        }
    }

    private fun initViewDataBinding() {
        viewModelId = initVariableId()
        viewModel = initViewModel()
        if (viewModel == null) {
            val type = javaClass.genericSuperclass
            val modelClass: Class<*>
            modelClass = if (type is ParameterizedType) {
                type.actualTypeArguments[1]
            } else {
                BaseViewModel::class.java
            } as Class<VM>
            viewModel = createViewModel(this, modelClass) as VM
        }
        viewModelId?.let { binding?.setVariable(it, viewModel) }
        //z支持LiveData绑定xml，数据改变，UI自动更新
        binding?.lifecycleOwner = this

        //让viewModel拥有View的生命周期感应
        viewModel?.let {
            lifecycle.addObserver(it)
        }

        //注入RxLifeCycle生命周期
        viewModel?.injectLifeCycleProvider(this)
    }

    /**
     * 跳转目的地activity
     */
    fun startActivity(clz: Class<*>) {
        startActivity(Intent(context, clz::class.java))
    }

    /**
     * 跳转目的地activity并携带数据
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(context, clz::class.java)
        bundle?.let {
            intent.putExtras(it)
        }
        startActivity(intent)
    }

    /**
     * 跳转ContainerActivity
     */
    fun startContainerActivity(canonicalName: String) {
        startContainerActivity(canonicalName, null)
    }

    /**
     * 跳转ContainerActivity并携带数据
     */
    fun startContainerActivity(canonicalName: String, bundle: Bundle?) {
        val intent = Intent(context, AppCompatActivity::class.java)
        intent.putExtra("", canonicalName)
        bundle?.let {
            intent.putExtra("", bundle)
        }
        startActivity(intent)
    }


    fun <VM : ViewModel> createViewModel(rxFragment: RxFragment, modelClass: Class<VM>): VM? {
        return ViewModelProviders.of(rxFragment).get(modelClass)
    }

    abstract fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.removeRxBus()
        binding?.unbind()
    }

    /**
     * 初始化ViewModel
     */
    abstract fun initViewModel(): VM

    /**
     * 初始化ViewModelID
     */
    abstract fun initVariableId(): Int

    override fun initParams() {
    }

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun initViewObservable() {
        TODO("Not yet implemented")
    }
}