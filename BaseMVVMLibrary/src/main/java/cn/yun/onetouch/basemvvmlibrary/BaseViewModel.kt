package cn.yun.onetouch.basemvvmlibrary

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

abstract class BaseViewModel<Model: BaseModel>(application: Application) : AndroidViewModel(application),IBaseViewModel,Consumer<Disposable> {
    abstract var model :Model







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

    override fun accept(t: Disposable?) {
        TODO("Not yet implemented")
    }

}