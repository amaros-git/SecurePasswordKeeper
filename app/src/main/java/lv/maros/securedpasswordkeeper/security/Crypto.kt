package lv.maros.securedpasswordkeeper.security

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import lv.maros.securedpasswordkeeper.setup.CryptoResult
import timber.log.Timber
import java.lang.Exception
import java.security.MessageDigest

class Crypto(private val app: Application) {

    private fun getEncryptedSharedRef(): SharedPreferences {
        val mainKey = MasterKey.Builder(app)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            app,
            ENC_SHARED_REF_NAME,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * returns hashed string using SHA-256 algorithm
     */
    private fun hashString(data: String): String? {
        return try {
            val md = MessageDigest.getInstance("SHA-256").apply {
                reset()
                update(data.toByteArray())
            }
            md.digest().toString()
        } catch (e: Exception) {
            Timber.e("exception during hashing")
            null
        }
    }

    private fun isPasskeyLegal(passkey: String): Boolean {
        return (passkey.isNotEmpty()) && (passkey.isNotBlank())
    }

    fun encryptAndSavePasskey(passkey: String): CryptoResult {
        return if (isPasskeyLegal(passkey)) {
            val hashData = hashString(passkey)

            if (hashData.isNullOrEmpty()) {
                CryptoResult.Error(CryptoResult.HASHING_FAILED_MSG_TYPE)
            } else {
                getEncryptedSharedRef().edit().apply {
                    putString(PASSKEY_HASH_KEY, passkey)
                    apply()
                }
                CryptoResult.Success(CryptoResult.SUCCESS_MSG_TYPE)
            }
        } else { // check passkey content
            CryptoResult.Error(CryptoResult.ILLEGAL_PASSKEY_MSG_TYPE)
        }
    }

    fun verifyPasskey(passkey: String): CryptoResult {
        val hashData = hashString(passkey)
        val hashSaved = getEncryptedSharedRef().getString(PASSKEY_HASH_KEY, null)

        return when {
            hashSaved.isNullOrEmpty() ->
                CryptoResult.Error(CryptoResult.MISSING_PASSKEY_MSG_TYPE)
            hashData != hashSaved ->
                CryptoResult.Error(CryptoResult.WRONG_PASSKEY_PROVIDED_MSG_TYPE)
            hashData == hashSaved -> {
                CryptoResult.Success(CryptoResult.SUCCESS_MSG_TYPE)
            }
            else -> CryptoResult.Error(CryptoResult.UNKNOWN_ERROR_MSG_TYPE)
        }
    }

    fun clearAll() {
        val editor = getEncryptedSharedRef().edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val PASSKEY_HASH_KEY = "passkey_hash"
        private const val ENC_SHARED_REF_NAME = "keeper_shared_prefs"
    }

}