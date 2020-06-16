package cn.yun.onetouch.basemvvmlibrary.bus

import cn.yun.onetouch.basemvvmlibrary.binding.command.BindingAction
import cn.yun.onetouch.basemvvmlibrary.binding.command.BindingConsumer
import java.lang.reflect.Type

class Messenger {

    var recipientsOfSubclassesAction = hashMapOf<Type, MutableList<WeakActionAndToken>>()
    var recipientsStrictAction = hashMapOf<Type, MutableList<WeakActionAndToken>>()


    /**
     * @param recipient the receiver,if register in activity the recipient always set "this",
     *                  and "WeakMessenger.getDefault().unregister(this)" in onDestroy,if in ViewModel,
     *                  you can also register with Activity context and also in onDestroy to unregister.
     * @param action    do something on message received
     */
    fun register(recipient: Any, action: BindingAction) {
        register(
            recipient = recipient,
            token = null,
            receiveDerivedMessagesToo = false,
            action = action
        )
    }

    /**
     * @param recipient the receiver,if register in activity the recipient always set "this",
     *                  and "WeakMessenger.getDefault().unregister(this)" in onDestroy,if in ViewModel,
     *                  you can also register with Activity context and also in onDestroy to unregister.
     * @param token     register with a unique token,when a messenger send a msg with same token,it
     *                  will
     *                  receive this msg
     * @param action    do something on message received
     */
    fun register(recipient: Any, token: Any?, action: BindingAction) {
        register(
            recipient = recipient,
            token = token,
            receiveDerivedMessagesToo = false,
            action = action
        )
    }

    /**
     * @param recipient                 the receiver,if register in activity the recipient always set "this",
     *                                  and "WeakMessenger.getDefault().unregister(this)" in onDestroy,if in ViewModel,
     *                                  you can also register with Activity context and also in onDestroy to unregister.
     * @param receiveDerivedMessagesToo whether Derived class of recipient can receive the message
     * @param action                    do something on message received
     */
    fun register(recipient: Any, receiveDerivedMessagesToo: Boolean, action: BindingAction) {
        register(
            recipient = recipient,
            token = null,
            receiveDerivedMessagesToo = receiveDerivedMessagesToo,
            action = action
        )
    }

    /**
     * @param recipient                 the receiver,if register in activity the recipient always set "this",
     *                                  and "WeakMessenger.getDefault().unregister(this)" in onDestroy,if in ViewModel,
     *                                  you can also register with Activity context and also in onDestroy to unregister.
     * @param token                     register with a unique token,when a messenger send a msg with same token,it
     *                                  will
     *                                  receive this msg
     * @param receiveDerivedMessagesToo whether Derived class of recipient can receive the message
     * @param action                    do something on message received
     */
    fun register(
        recipient: Any,
        token: Any?,
        receiveDerivedMessagesToo: Boolean,
        action: BindingAction
    ) {
        var messageType: Type = NoMsgType::class.java
        var recipients: HashMap<Type, MutableList<WeakActionAndToken>>

        if (receiveDerivedMessagesToo) {
            recipients = recipientsOfSubclassesAction
        } else {
            recipients = recipientsStrictAction
        }

        var list: MutableList<WeakActionAndToken>
        if (recipients.containsKey(messageType)) {
            list = recipients[messageType]!!
        } else {
            list = arrayListOf()
            recipients.put(messageType, list)
        }

        val weakAction = WeakAction<Any?>(recipient, action, null)
        val item = WeakActionAndToken(weakAction, token)
        list.add(item)
        cleanUp()
    }

    /**
     * @param recipient {}
     * @param tClass    class of T
     * @param action    this action has one params that type of tClass
     * @param <T>       message data type
     */
    fun <T> register(recipient: Any, bindingConsumer: BindingConsumer<T>, tClass: Class<T>) {
        register(
            recipient = recipient,
            token = null,
            receiveDerivedMessagesToo = false,
            bindingConsumer = bindingConsumer,
            tClass = tClass
        )
    }

    /**
     * see {}
     *
     * @param recipient                 receiver of message
     * @param receiveDerivedMessagesToo whether derived class of recipient can receive the message
     * @param tClass                    class of T
     * @param action                    this action has one params that type of tClass
     * @param <T>                       message data type
     */
    fun <T> register(
        recipient: Any,
        receiveDerivedMessagesToo: Boolean,
        bindingConsumer: BindingConsumer<T>,
        tClass: Class<T>
    ) {
        register(
            recipient = recipient,
            token = null,
            receiveDerivedMessagesToo = receiveDerivedMessagesToo,
            bindingConsumer = bindingConsumer,
            tClass = tClass
        )
    }

    /**
     * see {}
     *
     * @param recipient receiver of message
     * @param token     register with a unique token,when a messenger send a msg with same token,it
     *                  will
     *                  receive this msg
     * @param tClass    class of T for BindingConsumer
     * @param action    this action has one params that type of tClass
     * @param <T>       message data type
     */
    fun <T> register(
        recipient: Any,
        token: Any?,
        bindingConsumer: BindingConsumer<T>,
        tClass: Class<T>
    ) {
        register(
            recipient = recipient,
            token = token,
            receiveDerivedMessagesToo = false,
            bindingConsumer = bindingConsumer,
            tClass = tClass
        )
    }

    /**
     * see {}
     *
     * @param recipient                 receiver of message
     * @param token                     register with a unique token,when a messenger send a msg with same token,it
     *                                  will
     *                                  receive this msg
     * @param receiveDerivedMessagesToo whether derived class of recipient can receive the message
     * @param action                    this action has one params that type of tClass
     * @param tClass                    class of T for BindingConsumer
     * @param <T>                       message data type
     */

    fun <T> register(
        recipient: Any?,
        token: Any?,
        receiveDerivedMessagesToo: Boolean,
        bindingConsumer: BindingConsumer<T>?,
        tClass: Class<T>
    ) {
        val messageTyp: Type = tClass
        var recipients: HashMap<Type, MutableList<WeakActionAndToken>>

        if (receiveDerivedMessagesToo) {
            recipients = recipientsOfSubclassesAction
        } else {
            recipients = recipientsStrictAction
        }

        var list: MutableList<WeakActionAndToken>
        if (recipients.containsKey(messageTyp)) {
            list = recipients[messageTyp]!!
        } else {
            list = arrayListOf()
            recipients.put(messageTyp, list)
        }

        val weakAction = WeakAction<T>(recipient, null, bindingConsumer)
        val item = WeakActionAndToken(weakAction, token)
        list.add(item)
        cleanUp()

    }

    fun cleanUp() {
        cleanUpList(recipientsOfSubclassesAction)
        cleanUpList(recipientsStrictAction)

    }

    fun sendToTargetOrType(messageTargetType: Type?, token: Any?) {
        var messageType = NoMsgType::class.java

        if (!recipientsOfSubclassesAction.isNullOrEmpty()) {
            var listClone: MutableList<Type> = mutableListOf()
            listClone.addAll(recipientsOfSubclassesAction.keys)
            for (itemType in listClone) {
                var list: MutableList<WeakActionAndToken>? = mutableListOf()
                if (messageType == itemType || (itemType as Class<*>).isAssignableFrom(messageType) ||
                    classImplements(messageType, itemType)
                ) {
                    list = recipientsOfSubclassesAction[itemType]
                }
                sendToList(list = list, messageTargetType = messageTargetType, token = token)
            }
        }

        if (!recipientsStrictAction.isNullOrEmpty()) {
            if (recipientsStrictAction.containsKey(messageType)) {
                val list = recipientsStrictAction[messageType]
                sendToList(list, messageTargetType, token)
            }
        }
        cleanUp()
    }

    fun <T> sendToTargetOrType(message : T, messageTargetType: Type,token: Any?) {
        val messageType = message::class.java
        if (!recipientsOfSubclassesAction.isNullOrEmpty()) {
            val listClone = mutableListOf<Type>()
            listClone.addAll(recipientsOfSubclassesAction.keys)
            for (itemType in listClone) {
                var list:MutableList<WeakActionAndToken>? = mutableListOf()
                if (messageType == itemType || (itemType as Class<*>).isAssignableFrom(messageType) || classImplements(messageType, itemType)) {
                    list = this.recipientsOfSubclassesAction[itemType]
                }
                sendToList(message, list, messageTargetType, token)
            }
        }
    }

    class WeakActionAndToken(action: WeakAction<*>?, token: Any?) {
        var weakAction :WeakAction<*>? = action
        var weakToken = token
    }

    object NoMsgType {}

    companion object {
        private var mInstance: Messenger? = null
            get() {
                if (field == null) {
                    field = Messenger()
                }
                return field
            }

        fun getMessageInstance(): Messenger? {
            return mInstance
        }

        fun overrideDefault(newWeakMessenger: Messenger) {
            mInstance = newWeakMessenger
        }

        fun reset() {
            mInstance = null
        }

        fun cleanUpList(lists: HashMap<Type, MutableList<WeakActionAndToken>>?) {
            if (lists.isNullOrEmpty()) {
                return
            }

            for (item in lists.entries.iterator()) {
                val key = item.key
                val weakActionList = item.value
                if (!weakActionList.isNullOrEmpty()) {
                    for (action in weakActionList) {
                        if (action.weakAction == null || action.weakAction?.isLive() != true) {
                            weakActionList.remove(action)
                        }
                    }

                    if (weakActionList.size == 0) {
                        weakActionList.remove(key)
                    }
                }

            }
        }

        fun <T> sendToList(
            message: T,
            list: MutableList<WeakActionAndToken>?,
            messageTargetType: Type?,
            token: Any?
        ) {
            var messageType = message::class.java
            if (!list.isNullOrEmpty()) {
                // Clone to protect from people registering in a "receive message" method
                var listClone : MutableList<WeakActionAndToken> = mutableListOf()
                listClone.addAll(list)

                for (item in listClone) {
                    var executeAction = item.weakAction
                    if (executeAction != null && executeAction.isLive() && executeAction.getTarget() != null &&
                        (messageTargetType == null || item.weakToken?.javaClass == messageTargetType ||
                                classImplements(item.weakAction?.getTarget()?.javaClass, messageTargetType))
                        && ((item.weakToken == null && token == null) || (item.weakToken != null && item.weakToken == token))
                    ) {
                        executeAction.execute(message)
                    }
                }
            }
        }

        fun sendToList(
            list: MutableList<WeakActionAndToken>?,
            messageTargetType: Type?,
            token: Any?
        ) {
            if (!list.isNullOrEmpty()) {
                // Clone to protect from people registering in a "receive message" method
                var listClone = mutableListOf<WeakActionAndToken>()
                listClone.addAll(list)

                for (item in listClone) {
                    var executeAction = item.weakAction
                    if (executeAction != null && executeAction.isLive() && executeAction.getTarget() != null &&
                        (messageTargetType == null || executeAction.getTarget()?.javaClass == messageTargetType
                                || classImplements(
                            executeAction.getTarget()?.javaClass,
                            messageTargetType
                        ))
                        && ((item.weakToken == null && token == null) || (item.weakToken != null && item.weakToken == token))
                    ) {
                        executeAction.execute()
                    }
                }

            }
        }

        fun classImplements(instanceType: Type?, interfaceType: Type): Boolean {
            if (instanceType == null) {
                return false
            }
            var interfaces = (instanceType as Class<*>).interfaces
            for (currentInterface in interfaces) {
                if (currentInterface == interfaceType) {
                    return true
                }
            }
            return false
        }


    }
}