package lv.maros.securedpasswordkeeper

import android.app.Application
import timber.log.Timber

class KeeperApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}