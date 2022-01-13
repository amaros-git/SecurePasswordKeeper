package lv.maros.secured.password.keeper.security

import lv.maros.secured.password.keeper.utils.KeeperResult
import timber.log.Timber
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

/**
 * @throws java.security.NoSuchAlgorithmException if no Provider supports a
 * MessageDigestSpi implementation for the specified algorithm.
 * @throws javax.crypto.NoSuchPaddingException if <code>transformation</code>
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
            update(data.encodeToByteArray())
        }.digest()

        val hashString = StringBuffer()
        for (byte in hashBytes) {
            hashString.append(Integer.toHexString(0xFF and byte.toInt()))
        }

        return hashString.toString()
    }

    fun encryptString(data: String, key: String, iv: String): String {
        val encryptedBytes =
            encryptDecrypt(Cipher.ENCRYPT_MODE, data.encodeToByteArray(), key, iv)

        return convertToSignedHexString(encryptedBytes)
    }

    fun decryptString(encryptedData: String, key: String, iv: String): KeeperResult<String> {
        val encryptedByteList = encryptedData.split(ENCRYPTED_HEX_PASSWORD_DELIMITER)

        return if (encryptedByteList.size.rem(ENCRYPTED_PASSWORD_BLOCK_LENGTH) == 0) {
            val decryptedBytes = encryptDecrypt(
                Cipher.DECRYPT_MODE,
                convertToByteArray(encryptedByteList),
                key, iv)

            KeeperResult.Success(decryptedBytes.decodeToString())
        }
        else {
            KeeperResult.Error("Wrong length or format")
        }
    }


    private fun encryptDecrypt(mode: Int, data: ByteArray, key: String, iv: String): ByteArray {
        cipher.init(mode, getSecretKey(key), IvParameterSpec(iv.encodeToByteArray()))

        return cipher.doFinal(data)
    }


    private fun getSecretKey(key: String): SecretKey {
        val secretKey = SecretKeySpec(key.encodeToByteArray(), SECRET_KEY_ALGO)
        Timber.d("secret key hash = ${secretKey.hashCode()}")
        return secretKey
    }

    private fun convertToSignedHexString(
        bytes: ByteArray,
        delimiter: String = ENCRYPTED_HEX_PASSWORD_DELIMITER
    ): String {
        val strBuf = StringBuffer()

        for (i in bytes.indices) {
            val hex = String.format("%02x", bytes[i])
            strBuf.append(hex)

            if (i < (bytes.size - 1)) {
                strBuf.append(delimiter)
            }
        }

        return strBuf.toString()
    }

    private fun convertToByteArray(byteList: List<String>): ByteArray {
        val bytes = ByteArray(byteList.size)
        for (i in byteList.indices) {
            val int = Integer.parseInt(byteList[i], 16)
            bytes[i] = int.toByte()
        }

        return bytes
    }


    companion object {
        const val HASHING_PROVIDER_ALGO_SHA_265 = "SHA-256"

        const val CIPHER_TRANSFORMATION_SCHEME = "AES/CBC/PKCS5PADDING"

        const val SECRET_KEY_ALGO = "AES"

        const val ENCRYPTED_HEX_PASSWORD_DELIMITER = "/"

        const val ENCRYPTED_PASSWORD_BLOCK_LENGTH = 16
    }
}