package lv.maros.securedpasswordkeeper.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import timber.log.Timber
import java.util.concurrent.Executor

class KeeperMessageHandler private constructor(
    private val myLooper: Looper
) : Handler(myLooper) {

    override fun handleMessage(msg: Message) {
        when (msg.what) {
            MESSAGE_AUTHENTICATION_SUCCEEDED -> {
                Timber.d("Handling MESSAGE_AUTHENTICATION_SUCCEEDED")
            }
            MESSAGE_AUTHENTICATION_FAILED -> {
                Timber.d("Handling MESSAGE_AUTHENTICATION_FAILED")
            }
        }
    }

    companion object {
        // message types
        const val MESSAGE_AUTHENTICATION_SUCCEEDED   = 1
        const val MESSAGE_AUTHENTICATION_FAILED      = 2


        @Volatile
        private var INSTANCE: KeeperMessageHandler? = null

        fun getInstance(myLooper: Looper): KeeperMessageHandler {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = KeeperMessageHandler(myLooper)
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}