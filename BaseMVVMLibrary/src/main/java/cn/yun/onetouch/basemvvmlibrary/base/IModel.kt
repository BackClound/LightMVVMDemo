package cn.yun.onetouch.basemvvmlibrary.base

interface IModel {
    /**
     * viewmodel 销毁时清除model，与viewmodel共存亡，Model层同样不能持有长生命周期对象
     */
    fun onCleared()
}