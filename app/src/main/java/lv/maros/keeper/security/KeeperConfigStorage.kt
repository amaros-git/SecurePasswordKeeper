package lv.maros.keeper.security

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import timber.log.Timber
import javax.inject.Inject

class KeeperConfigStorage @Inject constructor(private val app: Application) {

    private val sharedRef: SharedPreferences

    private val cryptor = KeeperCryptor()

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

    private fun calculateKeeperConfigChecksum(): String? {
        val authType = getKeeperConfigParam(KEEPER_CONFIG_AUTH_TYPE)
        val passkeyHash = getKeeperConfigParam(KEEPER_CONFIG_PASSKEY_HASH)

        return cryptor.hashData(authType + passkeyHash)
    }

    fun clearAllStorage(): Boolean {
        return sharedRef.edit().run {
            clear()
            commit()
        }
    }

    fun updateKeeperConfig(values: Map<String, String>): Boolean {
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
    fun getKeeperConfigParam(keeperConfigParam: String): String? =
        sharedRef.getString(keeperConfigParam, null)

    /**
     * simply recalculates checksum and compares with saved
     */
    fun isKeeperConfigChecksumValid(): Boolean {
        val checksumSaved = getKeeperConfigParam(KEEPER_CONFIG_CHECKSUM)

        return if (!checksumSaved.isNullOrEmpty()) {
            val checksumCalculated = calculateKeeperConfigChecksum()

            !checksumCalculated.isNullOrEmpty() &&
            !checksumSaved.isNullOrEmpty()
            (checksumCalculated == checksumSaved)
        }
        else {
            true
        }
    }



    companion object {
        private const val ENC_SHARED_REF_NAME = "keeper_shared_prefs"

        const val KEEPER_CONFIG_AUTH_TYPE = "keeper_auth_type" // see
        const val KEEPER_CONFIG_PASSKEY_HASH = "keeper_passkey_hash"
        const val KEEPER_CONFIG_CHECKSUM = "keeper_checksum"
    }
}