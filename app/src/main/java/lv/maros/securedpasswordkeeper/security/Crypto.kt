package lv.maros.securedpasswordkeeper.security

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import lv.maros.securedpasswordkeeper.setup.CryptoResult
import timber.log.Timber
import java.lang.Exception
import java.security.MessageDigest
import javax.inject.Inject

class Crypto @Inject constructor(private val app: Application) {

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
        return (passkey.isNotEmpty()) &&
                (passkey.isNotBlank()) &&
                (passkey.length >= PASSKEY_MIN_LENGTH)
    }

    fun encryptAndSavePasskey(passkey: String): CryptoResult {
        return if (isPasskeyLegal(passkey)) {
            val hashData = hashString(passkey)

            if (hashData.isNullOrEmpty()) {
                CryptoResult.Error(CryptoResult.MSG_TYPE_HASHING_FAILED)
            } else { // save hash of the key
                getEncryptedSharedRef().edit().apply {
                    putString(PASSKEY_HASH_KEY, hashData)
                    apply()
                }
                CryptoResult.Success(CryptoResult.MSG_TYPE_SUCCESS)
            }
        } else { // check passkey content
            CryptoResult.Error(CryptoResult.MSG_TYPE_ILLEGAL_PASSKEY_PROVIDED)
        }
    }

    fun verifyPasskey(passkey: String): CryptoResult {
        val hashData = hashString(passkey)
        val hashSaved = getEncryptedSharedRef().getString(PASSKEY_HASH_KEY, null)
        Timber.d("hashData = $hashData")
        Timber.d("hashSaved = $hashSaved")

        return when {
            hashSaved.isNullOrEmpty() ->
                CryptoResult.Error(CryptoResult.MSG_TYPE_MISSING_SAVED_PASSKEY)
            hashData != hashSaved ->
                CryptoResult.Error(CryptoResult.MSG_TYPE_WRONG_PASSKEY_PROVIDED)
            hashData == hashSaved -> {
                CryptoResult.Success(CryptoResult.MSG_TYPE_SUCCESS)
            }
            else -> CryptoResult.Error(CryptoResult.MSG_TYPE_UNKNOWN_ERROR)
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
        private const val PASSKEY_MIN_LENGTH = 4
    }

}