package cn.yun.onetouch.basemvvmlibrary.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

class BaseApplication : Application() {

    var mContext = applicationContext

    override fun onCreate() {
        super.onCreate()
    }

    fun setApplication(application: Application) {
        mInstance = application

        //注册监听每个activity的生命周期，便于管理
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                TODO("Not yet implemented")
            }

            override fun onActivityStarted(activity: Activity) {
                TODO("Not yet implemented")
            }

            override fun onActivityDestroyed(activity: Activity) {
                AppManager.mInstance?.removeActivity(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                TODO("Not yet implemented")
            }

            override fun onActivityStopped(activity: Activity) {
                TODO("Not yet implemented")
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                AppManager.mInstance?.addActivity(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                TODO("Not yet implemented")
            }

        })
    }

    companion object {
        var mInstance: Application? = null
            get() {
                if (field == null) {
                    field = BaseApplication()
                }
                return field
            }


    }
}