package lv.maros.secured.password.keeper

import android.app.Application
import android.content.Context
import kotlinx.coroutines.Dispatchers
import lv.maros.secured.password.keeper.data.PasswordDataSource
import lv.maros.secured.password.keeper.data.local.PasswordDatabase
import lv.maros.secured.password.keeper.data.local.PasswordsLocalRepository
import lv.maros.secured.password.keeper.security.KeeperConfigStorage
import lv.maros.secured.password.keeper.security.KeeperCryptor
import java.util.concurrent.locks.ReentrantLock

//TODO rework because services are null always
object ServiceLocator {

    private val lock = Any()

    //services
    private var localPasswordsRepository: PasswordDataSource? = null
    private var cryptor: KeeperCryptor? = null
    private var configStorage: KeeperConfigStorage? = null

    fun provideLocalRepository(app: Application): PasswordDataSource {
        synchronized(lock) {
            return localPasswordsRepository
                ?: PasswordsLocalRepository(PasswordDatabase.getInstance(app))
        }
    }

    fun provideKeeperCrypto(app: Application): KeeperCryptor {
        synchronized(lock) {
            return cryptor ?: KeeperCryptor(app)
        }
    }

    fun provideKeeperConfigStorage(context: Context): KeeperConfigStorage {
        synchronized(lock) {
            return configStorage ?: KeeperConfigStorage.build(context) { Dispatchers.IO }
        }
    }
}