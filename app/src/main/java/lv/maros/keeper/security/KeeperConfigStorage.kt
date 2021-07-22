package lv.maros.keeper.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import lv.maros.keeper.authentication.KeeperAuthenticator
import lv.maros.keeper.utils.IoDispatcher
import javax.inject.Inject

class KeeperConfigStorage @Inject constructor(
    @ApplicationContext private val app: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
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

    suspend fun clearAllStorage(): Boolean =
        withContext(ioDispatcher) {
            sharedRef.edit().run {
                clear()
                commit()
            }
        }

    suspend fun saveKeeperConfigParam(
        keeperConfigParam: String, value: String
    ): Boolean = withContext(ioDispatcher) {
        sharedRef.edit().run {
            putString(keeperConfigParam, value)
            commit()
        }
    }

    suspend fun saveKeeperConfigParam(
        keeperConfigParam: String, value: Boolean
    ): Boolean = withContext(ioDispatcher) {
        sharedRef.edit().run {
            putBoolean(keeperConfigParam, value)
            commit()
        }
    }

    /**
     * params.key is some Keeper Config Param,
     * param.value is param value
     *
     * If any fails, returns false. Otherwise true.
     */
    suspend fun saveKeeperConfigParams(
        params: Map<String, String>
    ): Boolean = withContext(ioDispatcher) {
        for (param in params) {
            val result = sharedRef.edit().run {
                putString(param.key, param.value)
                commit()
            }
            if (!result) return@withContext false
        }

        true
    }

    fun isPasskeyLegal(passkey: String): Boolean {
        return (passkey.isNotEmpty()) &&
                (passkey.isNotBlank()) &&
                (passkey.length >= KeeperAuthenticator.PASSKEY_MIN_LENGTH)
        // TODO spaces ?
    }

    fun isKeeperConfigured(): Boolean { //TODO
        return (!getAuthType().isNullOrEmpty() &&
                !getPasskeyHash().isNullOrEmpty())
    }

    private fun getAuthType() =
        getKeeperStringConfigParam(KEEPER_CONFIG_AUTH_TYPE_STRING)

    private fun getPasskeyHash() =
        getKeeperStringConfigParam(KEEPER_CONFIG_AUTH_TYPE_STRING)


    /**
     * return null if doesn't exist.
     */
    fun getKeeperStringConfigParam(keeperConfigParam: String) =
        sharedRef.getString(keeperConfigParam, null)

    fun getKeeperBooleanConfigParam(keeperConfigParam: String) =
        sharedRef.getBoolean(keeperConfigParam, false)

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

        const val KEEPER_CONFIG_AUTH_TYPE_STRING = "keeper_auth_type" // see
        const val KEEPER_CONFIG_PASSKEY_HASH_STRING = "keeper_passkey_hash"
        const val KEEPER_CONFIG_CHECKSUM_STRING = "keeper_checksum"
        const val KEEPER_CONFIG_USE_LOGIN_STRING = "keeper_use_login"

    }
}