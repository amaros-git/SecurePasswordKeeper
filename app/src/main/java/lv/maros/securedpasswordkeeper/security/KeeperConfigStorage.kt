package lv.maros.securedpasswordkeeper.security

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import lv.maros.securedpasswordkeeper.models.KeeperConfiguration
import javax.inject.Inject

class KeeperConfigStorage @Inject constructor(private val app: Application) {

    private val sharedRef: SharedPreferences

    init {
        sharedRef = createEncryptedSharedRef()
    }

    private fun createEncryptedSharedRef(): SharedPreferences {
        return EncryptedSharedPreferences.create(
            app,
            ENC_SHARED_REF_NAME,
            getMasterKey(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getMasterKey(): MasterKey {
        return MasterKey.Builder(app)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    /**
     * return null if Configuration doesn't exist.
     * E.g. on the app first run.
     */
    fun getKeeperConfiguration(): KeeperConfiguration? {
        return null
    }

    companion object {
        private const val ENC_SHARED_REF_NAME = "keeper_shared_prefs"
    }
}