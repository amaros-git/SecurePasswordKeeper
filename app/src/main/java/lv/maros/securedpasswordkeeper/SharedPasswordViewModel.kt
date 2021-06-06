package lv.maros.securedpasswordkeeper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lv.maros.securedpasswordkeeper.views.AddPasswordFragment
import timber.log.Timber
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class SharedPasswordViewModel (app: Application) : AndroidViewModel(app) {


    fun savePassword(password: Password) {
        viewModelScope.launch(Dispatchers.Default)  {

        }
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