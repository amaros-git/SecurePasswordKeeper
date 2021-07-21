package lv.maros.keeper.security

import timber.log.Timber
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject

/**
 * @throws java.security.NoSuchAlgorithmException if no Provider supports a
 * MessageDigestSpi implementation for the
 * specified algorithm.
 */
class KeeperCryptor @Inject constructor() {

    // TODO REWORK to newInstance()
    private val messageDigest: MessageDigest =
        MessageDigest.getInstance(HASHING_ALGO_SHA_265)

    /**
     * returns hashed string using SHA-256 algorithm
     */
    fun hashData(data: String): String { // TODO GET RID OF NULL !
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


    companion object {
        const val HASHING_ALGO_SHA_265 = "SHA-256"
    }
}