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

    fun encryptString(data: String, key: String, iv: String) : String =
        encryptDecrypt(Cipher.ENCRYPT_MODE, data, key, iv)

    fun decryptString(encryptedData: String, key: String, iv: String): String =
        encryptDecrypt(Cipher.DECRYPT_MODE, encryptedData, key, iv)

    private fun encryptDecrypt(mode: Int, data: String, key: String, iv: String): String {
        Timber.d("data = $data, key = $key, iv = $iv")
        val bytes: ByteArray = cipher.run {
            init(mode, getSecretKey(key))
            doFinal(data.toByteArray(Charsets.UTF_8))
        }
        bytes.forEach {
            Timber.d(it.toString())
        }

        return bytes.toString()
    }


    private fun getSecretKey(key: String): SecretKey {
        val secretKey = SecretKeySpec(key.encodeToByteArray(), SECRET_KEY_ALGO)
        Timber.d("secret key hash = ${secretKey.hashCode()}")
        return secretKey
    }


    companion object {
        const val HASHING_PROVIDER_ALGO_SHA_265 = "SHA-256"

        const val CIPHER_TRANSFORMATION_SCHEME = "AES/CBC/PKCS5PADDING"

        const val SECRET_KEY_ALGO = "AES"
    }
}