package lv.maros.securedpasswordkeeper.security

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import lv.maros.securedpasswordkeeper.setup.CryptoResult
import timber.log.Timber

class Crypto (private val app: Application) {

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

    fun encryptAndSavePasskey(passkey: String) {
        Timber.d("savePasskey called")
        getEncryptedSharedRef().edit().apply {
            putString(PASSKEY_KEY, passkey)
            apply()
        }
    }

    fun verifyPasskey(): CryptoResult {
        val key = getEncryptedSharedRef().getString(PASSKEY_KEY, null)
        return CryptoResult.Success(CryptoResult.SUCCESS_MSG_TYPE)
    }

    companion object {
        private const val PASSKEY_KEY = "passkey"
        private const val ENC_SHARED_REF_NAME = "keeper_shared_prefs"
    }

}