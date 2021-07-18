package lv.maros.keeper.security

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import lv.maros.keeper.models.KeeperConfig
import lv.maros.keeper.utils.KeeperResult
import timber.log.Timber
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

    fun clearAllStorage(): Boolean {
        return sharedRef.edit().run {
            clear()
            commit()
        }
    }

    fun updateConfig(values: Map<String, String>): Boolean {
        for (value in values) {
            Timber.d("key = ${value.key}, value = ${value.value}")
            val result = sharedRef.edit().run {
                putString(value.key, value.value)
                commit()
            }
            if (!result) {
                return false
            }
        }

        return true
    }

    /**
     * return null if doesn't exist.
     */
    fun getConfigParam(keeperConfigParam: String): String? =
        sharedRef.getString(keeperConfigParam, null)


    companion object {
        private const val ENC_SHARED_REF_NAME = "keeper_shared_prefs"

        const val KEEPER_CONFIG_PARAM_PASSKEY_HASH = "passkey_hash"
    }
}