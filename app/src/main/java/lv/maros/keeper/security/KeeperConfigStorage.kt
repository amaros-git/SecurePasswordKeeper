package lv.maros.keeper.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import lv.maros.keeper.hilt.IoDispatcher
import lv.maros.keeper.models.KeeperConfig
import lv.maros.keeper.utils.KEEPER_AUTH_TYPE_NONE
import lv.maros.keeper.utils.PASSWORD_MIN_LENGTH
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

    fun isKeeperConfigured(): Boolean {
        var isConfigured = true

        val authType = getAuthType()
        if (authType.isNullOrEmpty()) { // 1. Check auth type is configured
            isConfigured = false
        }
        else if (authType != KEEPER_AUTH_TYPE_NONE) { //2. Check if passkey hash exist if auth type is secure
            val passkeyHash = getPasskeyHash()
            if (passkeyHash.isNullOrEmpty()) {
                isConfigured = false
            }
        }

        return isConfigured
    }

    fun getAuthType(): String? = getKeeperStringConfigParam(KEEPER_CONFIG_STRING_AUTH_TYPE)

    fun getPasskeyHash(): String? = getKeeperStringConfigParam(KEEPER_CONFIG_STRING_PASSKEY_HASH)

    fun getEncryptionKey(): String? = getKeeperStringConfigParam(KEEPER_CONFIG_STRING_ENCRYPTION_KEY)

    fun getEncryptionIV(): String? = getKeeperStringConfigParam(KEEPER_CONFIG_STRING_ENCRYPTION_KEY)

    fun isLoginEnabled(): Boolean = getKeeperBooleanConfigParam(KEEPER_CONFIG_BOOL_USE_LOGIN)


    /**
     * return null if doesn't exist.
     */
    fun getKeeperStringConfigParam(keeperConfigParam: String) =
        sharedRef.getString(keeperConfigParam, null)

    fun getKeeperBooleanConfigParam(keeperConfigParam: String) =
        sharedRef.getBoolean(keeperConfigParam, false)

    fun saveKeeperConfig(newConfig: KeeperConfig): Boolean {
        return false
    }

    fun getKeeperConfig(): KeeperConfig {
        return KeeperConfig(
            getAuthType(),
            getPasskeyHash(),
            getEncryptionKey(),
            getEncryptionIV(),
            isLoginEnabled()
        )
    }

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

        private const val KEEPER_CONFIG_STRING_AUTH_TYPE = "keeper_auth_type" // see Keeper Constants
        private const val KEEPER_CONFIG_STRING_PASSKEY_HASH = "keeper_passkey_hash"
        private const val KEEPER_CONFIG_STRING_ENCRYPTION_KEY = "keeper_encryption_key"
        private const val KEEPER_CONFIG_STRING_ENCRYPTION_IV = "keeper_encryption_iv"
        private const val KEEPER_CONFIG_BOOL_USE_LOGIN = "keeper_use_login"
    }
}