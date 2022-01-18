package lv.maros.secured.password.keeper

import android.content.Context
import kotlinx.coroutines.Dispatchers
import lv.maros.secured.password.keeper.data.PasswordDataSource
import lv.maros.secured.password.keeper.data.local.PasswordDatabase
import lv.maros.secured.password.keeper.data.local.PasswordsLocalRepository
import lv.maros.secured.password.keeper.security.KeeperConfigStorage
import lv.maros.secured.password.keeper.security.KeeperCryptor
import java.util.concurrent.locks.ReentrantLock

object ServiceLocator {

    private val lock = Any()

    //services
    private var localPasswordsRepository: PasswordDataSource? = null
    private var cryptor: KeeperCryptor? = null
    private var configStorage: KeeperConfigStorage? = null

    fun provideLocalRepository(context: Context): PasswordDataSource {
        synchronized(lock) {
            return localPasswordsRepository
                ?: PasswordsLocalRepository(PasswordDatabase.getInstance(context))
        }
    }

    fun provideKeeperCrypto() = KeeperCryptor()

    fun provideKeeperConfigStorage(context: Context) =
        KeeperConfigStorage.build(context) {Dispatchers.IO}
}