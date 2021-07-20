package lv.maros.keeper.security

import lv.maros.keeper.R
import lv.maros.keeper.setup.CryptoResult
import lv.maros.keeper.setup.views.PasskeyInputBottomDialog
import lv.maros.keeper.utils.KeeperResult
import timber.log.Timber
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject


object KeeperCryptor {

    /**
     * returns hashed string using SHA-256 algorithm
     */
    fun hashData(data: String): String? {
        return try {
            val hashBytes = MessageDigest.getInstance("SHA-256").apply {
                reset()
                update(data.toByteArray())
            }.digest()

            val hashString = StringBuffer()
            for (byte in hashBytes) {
                hashString.append(Integer.toHexString(0xFF and byte.toInt()))
            }

            hashString.toString()
        } catch (e: Exception) {
            Timber.e("exception during hashing, please try again")
            null
        }
    }

    fun encryptData(data: String, key: String) {
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