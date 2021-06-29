package lv.maros.securedpasswordkeeper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lv.maros.securedpasswordkeeper.authentication.AuthResult
import lv.maros.securedpasswordkeeper.models.KeeperUser
import lv.maros.securedpasswordkeeper.models.Password
import lv.maros.securedpasswordkeeper.security.Crypto
import lv.maros.securedpasswordkeeper.utils.SingleLiveEvent
import timber.log.Timber
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject


class SharedKeeperViewModel @Inject constructor (
    private val app: Application,
) : AndroidViewModel(app) {

    private val crypto = Crypto(app)

    val authenticationResult = SingleLiveEvent<AuthResult>()

    fun savePassword(password: Password) {
        viewModelScope.launch(Dispatchers.Default)  {

        }
    }

    fun verifyPasskey(passkey: String) {
        crypto.verifyPasskey(passkey)
    }

    fun encryptPassword(passwordString: String) {


        val plaintext: ByteArray = "test".toByteArray()
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)

        val key: SecretKey = keygen.generateKey()
        Timber.d("key = $key")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        val iv: ByteArray = cipher.iv

        Timber.d(iv.toString())
    }

}