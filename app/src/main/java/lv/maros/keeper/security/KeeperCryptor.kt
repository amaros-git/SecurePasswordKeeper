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

    fun encryptString(plaintext: String, encryptionKey: String): String =
        encryptDecrypt(Cipher.ENCRYPT_MODE, plaintext, encryptionKey)

    fun decryptString(encryptedData: String, encryptionKey: String): String =
        encryptDecrypt(Cipher.DECRYPT_MODE, encryptedData, encryptionKey)

    private fun encryptDecrypt(mode: Int, data: String, encryptionKey: String): String {
        Timber.d("data = $data")
        val bytes: ByteArray = cipher.run {
            init(mode, getSecretKey(encryptionKey))
            doFinal(data.toByteArray())
            iv
        }
        bytes.forEach {
            Timber.d(it.toString())
        }

        return ""
    }


    // TODO it is always new ..
    private fun getSecretKey(encryptionKey: String): SecretKey {
        val key = SecretKeySpec(encryptionKey.toByteArray(), SECRET_KEY_ALGO)
        Timber.d("secret key = ${key.encoded}")
        return key
    }


    companion object {
        const val HASHING_PROVIDER_ALGO_SHA_265 = "SHA-256"

        const val CIPHER_TRANSFORMATION_SCHEME = "AES/CBC/PKCS5PADDING"

        const val SECRET_KEY_ALGO = "AES"
    }
}