package cn.yun.onetouch.basemvvmlibrary

interface IBaseView {
    /**
     * 初始化界面参数
     */
    fun initParams()

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 初始化view观察者的监听
     */
    fun initViewObservable()
}