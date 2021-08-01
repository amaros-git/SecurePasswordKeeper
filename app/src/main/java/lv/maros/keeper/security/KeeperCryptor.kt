package lv.maros.keeper.security

import timber.log.Timber
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

/**
 * @throws java.security.NoSuchAlgorithmException if no Provider supports a
 * MessageDigestSpi implementation for the specified algorithm.
 * @throws NoSuchPaddingException if <code>transformation</code>
 * contains a padding scheme that is not available.
 */
class KeeperCryptor @Inject constructor() {

    // TODO REWORK to newInstance()
    private val messageDigest: MessageDigest =
        MessageDigest.getInstance(HASHING_PROVIDER_ALGO_SHA_265)

    private val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION_SCHEME)

    /**
     * returns hashed string using SHA-256 algorithm
     */
    fun hashData(data: String): String {
        val hashBytes = messageDigest.apply {
            reset()
            update(data.toByteArray())
        }.digest()

        val hashString = StringBuffer()
        for (byte in hashBytes) {
            hashString.append(Integer.toHexString(0xFF and byte.toInt()))
        }

        return hashString.toString()
    }

    fun encryptString(plaintext: String, key: String) {
        val bytes: ByteArray = plaintext.toByteArray()

        cipher.init(Cipher.ENCRYPT_MODE, key)
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        val iv: ByteArray = cipher.iv

        Timber.d(iv.toString())
    }

    private fun createSecretKey(key: String) {

    }

   /* fun generateEncryptionKey(): String {
        val secretKey: SecretKey = KeyGenerator.getInstance("AES").let {
            it.init(256)
            it.generateKey()
        }

        val key = SecretKeySpec()

        val keyBytes = secretKey.encoded
        val hashString = StringBuffer()
        for (byte in keyBytes) {
            val hex = byte.toString()
            Timber.d("byte = $byte, hex = $hex")
            hashString.append(hex)
        }

        return hashString.toString()
    }
*/


    companion object {
        const val HASHING_PROVIDER_ALGO_SHA_265 = "SHA-256"

        const val CIPHER_TRANSFORMATION_SCHEME = "AES/CBC/PKCS5PADDING"
    }
}