package lv.maros.secured.password.keeper.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lv.maros.secured.password.keeper.hilt.IoDispatcher
import lv.maros.secured.password.keeper.models.KeeperConfig
import lv.maros.secured.password.keeper.utils.KEEPER_AUTH_TYPE_NONE
import lv.maros.secured.password.keeper.utils.isNotBlankOrEmpty
import javax.inject.Inject

/**
 * May throw on instantiation. TODO
 */
class KeeperConfigStorage @Inject constructor(
    @ApplicationContext private val app: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
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

    private fun saveKeeperConfigParam(keeperConfigParam: String, value: String): Boolean =
        sharedRef.edit().run {
            putString(keeperConfigParam, value)
            commit()
        }

    private fun saveKeeperConfigParam(keeperConfigParam: String, value: Boolean): Boolean =
        sharedRef.edit().run {
            putBoolean(keeperConfigParam, value)
            commit()
        }

    /**
     * return null if doesn't exist.
     */
    private fun getKeeperStringConfigParam(keeperConfigParam: String) =
        sharedRef.getString(keeperConfigParam, null)

    private fun getKeeperBooleanConfigParam(keeperConfigParam: String) =
        sharedRef.getBoolean(keeperConfigParam, false)

    /**
     * few helper methods
     */
    private fun saveAuthType(authType: String): Boolean {
        return saveKeeperConfigParam(KEEPER_CONFIG_STRING_AUTH_TYPE, authType)
    }

    private fun savePasskeyHash(passkeyHash: String): Boolean {
        return saveKeeperConfigParam(KEEPER_CONFIG_STRING_ENCRYPTION_IV, passkeyHash)
    }

    private fun saveEncryptionKey(encryptionKey: String): Boolean {
        return saveKeeperConfigParam(KEEPER_CONFIG_STRING_ENCRYPTION_KEY, encryptionKey)
    }

    private fun saveEncryptionIV(encryptionIV: String): Boolean {
        return saveKeeperConfigParam(KEEPER_CONFIG_STRING_ENCRYPTION_IV, encryptionIV)
    }

    private fun saveUseLogin(useLogin: Boolean): Boolean {
        return saveKeeperConfigParam(KEEPER_CONFIG_BOOL_USE_LOGIN, useLogin)
    }


    fun isKeeperConfigured(): Boolean {
        val (authType, passkeyHash, encryptionKey, encryptionIV, useLogin) = getKeeperConfig()

        return when {
            null != authType && authType.isNotBlankOrEmpty() -> true

            authType != KEEPER_AUTH_TYPE_NONE -> {
                null != passkeyHash && passkeyHash.isNotBlankOrEmpty()
            }

            null != encryptionKey && encryptionKey.isNotBlankOrEmpty() -> true

            null != encryptionIV && encryptionIV.isNotBlankOrEmpty() -> true

            else -> false
        }
    }


    fun getAuthType(): String? =
        getKeeperStringConfigParam(KEEPER_CONFIG_STRING_AUTH_TYPE)

    fun getPasskeyHash(): String? =
        getKeeperStringConfigParam(KEEPER_CONFIG_STRING_PASSKEY_HASH)

    fun getEncryptionKey(): String? =
        getKeeperStringConfigParam(KEEPER_CONFIG_STRING_ENCRYPTION_KEY)

    fun getEncryptionIV(): String? =
        getKeeperStringConfigParam(KEEPER_CONFIG_STRING_ENCRYPTION_IV)

    fun isLoginEnabled(): Boolean =
        getKeeperBooleanConfigParam(KEEPER_CONFIG_BOOL_USE_LOGIN)

    suspend fun clearAllStorage(): Boolean =
        withContext(ioDispatcher) {
            sharedRef.edit().run {
                clear()
                commit()
            }
        }

    suspend fun saveOrUpdateKeeperConfig(newConfig: KeeperConfig): Boolean {
        val (authType, passkeyHash, encryptionKey, encryptionIV, useLogin) = newConfig

        var error = 0 //if error > 0, means save or update failed. So, false is returned
        return withContext(ioDispatcher) {
            if (null != authType && !saveAuthType(authType)) error++
            if (null != passkeyHash && !savePasskeyHash(passkeyHash)) error++
            if (null != encryptionKey && !saveEncryptionKey(encryptionKey)) error++
            if (null != encryptionIV && !saveEncryptionIV(encryptionIV)) error++
            if (!saveUseLogin(useLogin)) error++

            0 == error
        }
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

        private const val KEEPER_CONFIG_STRING_AUTH_TYPE =
            "keeper_auth_type" // see Keeper Constants
        private const val KEEPER_CONFIG_STRING_PASSKEY_HASH = "keeper_passkey_hash"
        private const val KEEPER_CONFIG_STRING_ENCRYPTION_KEY = "keeper_encryption_key"
        private const val KEEPER_CONFIG_STRING_ENCRYPTION_IV = "keeper_encryption_iv"
        private const val KEEPER_CONFIG_BOOL_USE_LOGIN = "keeper_use_login"
    }
}