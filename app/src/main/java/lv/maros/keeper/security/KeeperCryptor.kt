package lv.maros.keeper.security

import lv.maros.keeper.setup.CryptoResult
import timber.log.Timber
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

// TODO should be singleton ?
class KeeperCryptor {

    /**
     * returns hashed string using SHA-256 algorithm
     */
    fun hashData(data: String): String? {
        return try {
            val msgDigest = MessageDigest.getInstance("SHA-256").apply {
                reset()
                update(data.toByteArray())
            }

            val hashBytes = msgDigest.digest()
            val hashString = StringBuffer()
            for (byte in hashBytes) {
                hashString.append(Integer.toHexString(0xFF and byte.toInt()))
            }

            hashString.toString()
        } catch (e: Exception) {
            Timber.e("exception during hashing")
            null
        }
    }

    fun isPasskeyLegal(passkey: String): Boolean {
        return (passkey.isNotEmpty()) &&
                (passkey.isNotBlank()) &&
                (passkey.length >= PASSKEY_MIN_LENGTH)
        // TODO spaces ?
    }

    fun encryptAndSavePasskey(passkey: String): CryptoResult {
        return if (isPasskeyLegal(passkey)) {
            val hashData = hashData(passkey)

            if (hashData.isNullOrEmpty()) {
                CryptoResult.Error(CryptoResult.MSG_TYPE_HASHING_FAILED)
            } else { // save hash of the key

                CryptoResult.Success(CryptoResult.MSG_TYPE_SUCCESS)
            }
        } else { // check passkey content
            CryptoResult.Error(CryptoResult.MSG_TYPE_ILLEGAL_PASSKEY_PROVIDED)
        }
    }

    fun verifyPasskey(passkey: String, hashSaved: String): CryptoResult {
        val hashCurrent = hashData(passkey)

        return when {
            hashCurrent != hashSaved ->
                CryptoResult.Error(CryptoResult.MSG_TYPE_PASSKEY_DOES_NOT_MATCH)

            hashCurrent == hashSaved -> {
                CryptoResult.Success(CryptoResult.MSG_TYPE_SUCCESS)
            }
            // shall not happen
            else -> CryptoResult.Error(CryptoResult.MSG_TYPE_UNKNOWN_ERROR)
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

    companion object {

        private const val PASSKEY_MIN_LENGTH = 4
    }

}