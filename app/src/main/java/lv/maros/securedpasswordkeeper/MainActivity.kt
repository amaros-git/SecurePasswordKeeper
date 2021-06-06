package lv.maros.securedpasswordkeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import timber.log.Timber
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController(R.id.nav_host_fragment).popBackStack()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}