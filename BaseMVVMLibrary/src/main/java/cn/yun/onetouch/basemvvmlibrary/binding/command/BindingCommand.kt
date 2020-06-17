package cn.yun.onetouch.basemvvmlibrary.binding.command

/**
 * About : ReplyCommand
 * 执行的命令回调, 用于ViewModel与xml之间的数据绑定
 */
class BindingCommand<T>(
    bindingAction: BindingAction?,
    bindingConsumer: BindingConsumer<T>?,
    bindingFunction: BindingFunction<Boolean>?
) {
    var bindingAction = bindingAction
    var bindingConsumer = bindingConsumer
    var bindingFunction = bindingFunction

    /**
     * 执行bindingAction命令
     */
    fun execute() {
        canExecute().run {
            if (this) {
                bindingAction?.call()
            }
        }
    }

    /**
     * 执行BindingConsumer命令
     */
    fun execute(parameter: T) {
        canExecute().run {
            if (this) {
                bindingConsumer?.call(parameter)
            }
        }
    }

    /**
     * 是否需要执行
     */
    fun canExecute(): Boolean {
        return bindingFunction?.call() ?: false
    }
}