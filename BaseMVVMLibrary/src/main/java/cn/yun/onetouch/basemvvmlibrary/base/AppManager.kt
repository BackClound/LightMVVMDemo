package cn.yun.onetouch.basemvvmlibrary.base

import android.app.Activity
import androidx.fragment.app.Fragment
import java.lang.Exception
import java.util.*

open class AppManager {

    /**
     * 添加activity到堆栈
     */
    public fun addActivity(activity: Activity) {
        if (activityStack == null) activityStack = Stack()
        activityStack?.add(activity)
    }

    /**
     * 移除指定的activity
     */
    fun removeActivity(activity: Activity) {
        activityStack?.remove(activity)
    }

    /**
     * 堆栈中是否有activity
     */
    fun isActivity(): Boolean {
        return activityStack?.isNotEmpty() ?: false
    }

    /**
     * 获取当前activity
     */
    fun currentActivity(): Activity? {
        return activityStack?.lastElement()
    }

    /**
     * 结束当前activity
     */
    fun finishActivity() {
        activityStack?.lastElement()?.let {
            finishActivity(it)
        }
    }

    /**
     * 结束指定的activity
     */
    fun finishActivity(activity: Activity){
        if (!activity.isFinishing) activity.finish()
    }

    /**
     * 结束指定类名的activity
     */
    fun finishActivity(clz: Class<*>) {
        activityStack?.let {
            for (activity in it) {
                if (activity::class.equals(clz)){
                    finishActivity(activity)
                    break
                }
            }
        }
    }

    /**
     * 结束所有的activity
     */
    fun finishAllActivity() {
        activityStack?.let {
            for (activity in it) {
                finishActivity(activity)
            }
        }
        activityStack?.clear()

    }

    /**
     * 添加fragment到堆栈
     */
    fun addFragment(fragment: Fragment) {
        if (fragmentStack == null) fragmentStack = Stack()
        fragmentStack?.add(fragment)
    }

    /**
     * 移除fragment
     */
    fun removeFragment(fragment: Fragment){
        fragmentStack?.remove(fragment)
    }

    /**
     * 是否有fragment
     */
    fun isFragment(): Boolean {
        return fragmentStack?.isEmpty() ?: false
    }

    /**
     * 获取当前fragment
     */
    fun currentFragment():Fragment? {
        return fragmentStack?.lastElement()
    }

    /**
     * 退出应用程序
     */
    fun exitApp(){
        try {
            finishAllActivity()
        } catch (exception: Exception) {
            activityStack?.clear()
        }
    }

    companion object {
        var activityStack: Stack<Activity>? = null
        var fragmentStack: Stack<Fragment>? = null
        var mInstance: AppManager? = null
            get() {
                if (field == null) {
                    field = AppManager()
                }
                return field
            }
    }
}