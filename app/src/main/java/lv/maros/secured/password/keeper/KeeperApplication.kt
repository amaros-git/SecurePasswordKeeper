package lv.maros.secured.password.keeper

import android.app.Application
import androidx.work.Configuration
import lv.maros.secured.password.keeper.data.PasswordDataSource
import lv.maros.secured.password.keeper.security.KeeperConfigStorage
import lv.maros.secured.password.keeper.security.KeeperCryptor
import timber.log.Timber

class KeeperApplication : Application(), Configuration.Provider {

    val localPasswordsRepository: PasswordDataSource
        get() = ServiceLocator.provideLocalRepository(this)

    val cryptor: KeeperCryptor
        get() = ServiceLocator.provideKeeperCrypto(this)

    val configStorage: KeeperConfigStorage
        get() = ServiceLocator.provideKeeperConfigStorage(this)

    private fun getWorkerConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .build()
        } else {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.ERROR)
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override val workManagerConfiguration: Configuration
        get() = getWorkerConfiguration()


}