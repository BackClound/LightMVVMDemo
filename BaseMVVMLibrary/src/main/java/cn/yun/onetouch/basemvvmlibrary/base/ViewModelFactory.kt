package cn.yun.onetouch.basemvvmlibrary.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.Exception

class ViewModelFactory (application: Application): ViewModelProvider.NewInstanceFactory() {
    val mApplication = application

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaseViewModel::class.java)) {
            return BaseViewModel<BaseModel>(application = mApplication,model = null) as T
        }

        //反射动态实例化viewmodel
        try {
            var className = modelClass.canonicalName
            var classViewModel = className?.let { Class.forName(it) }
            var cons = classViewModel?.getConstructor(Application::class.java)
            var viewModel = cons?.newInstance(mApplication)
            return viewModel as T
        } catch (exception: Exception) {
            throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }

    companion object {
        fun getInstance(application: Application) : ViewModelFactory{
            return ViewModelFactory(application)
        }
    }
}