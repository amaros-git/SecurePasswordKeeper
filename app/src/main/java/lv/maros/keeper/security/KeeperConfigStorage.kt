package lv.maros.keeper.security

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import lv.maros.keeper.utils.KeeperResult
import timber.log.Timber
import javax.inject.Inject

class KeeperConfigStorage @Inject constructor(
    @ApplicationContext private val app: Context
) {

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

  /*  fun calculateKeeperConfigChecksum(): String {
        val authType = getKeeperConfigParam(KEEPER_CONFIG_AUTH_TYPE)
        val passkeyHash = getKeeperConfigParam(KEEPER_CONFIG_PASSKEY_HASH)

        return cryptor.hashData(authType + passkeyHash)
    }*/

    fun clearAllStorage(): Boolean {
        return sharedRef.edit().run {
            clear()
            commit()
        }
    }

    fun saveKeeperConfigParam(keeperConfigParam: String, value: String): Boolean {
        return sharedRef.edit().run {
            putString(keeperConfigParam, value)
            commit()
        }
    }

    /**
     * params.key is some Keeper Config Param,
     * param.value is param value
     *
     * If any fails, returns false. Otherwise true.
     */
    fun saveConfigParams(params: Map<String, String>): Boolean {
        for (param in params) {
            val result = sharedRef.edit().run {
                putString(param.key, param.value)
                commit()
            }
            if (!result) return false
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
     *//*
    fun isKeeperConfigChecksumValid(): Boolean {
        val checksumSaved = getKeeperConfigParam(KEEPER_CONFIG_CHECKSUM)

        return if (!checksumSaved.isNullOrEmpty()) {
            val checksumCalculated = calculateKeeperConfigChecksum()

            !checksumCalculated.isNullOrEmpty() &&
            (checksumCalculated == checksumSaved)
        }
        else {
            Timber.d("Keeper config checksum is null or empty")
            false
        }
    }*/



    companion object {
        private const val ENC_SHARED_REF_NAME = "keeper_shared_prefs"

        const val KEEPER_CONFIG_AUTH_TYPE = "keeper_auth_type" // see
        const val KEEPER_CONFIG_PASSKEY_HASH = "keeper_passkey_hash"
        const val KEEPER_CONFIG_CHECKSUM = "keeper_checksum"

    }
}