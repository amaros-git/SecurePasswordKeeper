package lv.maros.keeper.security

import lv.maros.keeper.setup.CryptoResult
import timber.log.Timber
import java.security.MessageDigest
import javax.inject.Inject


class KeeperCryptor {

    @Inject
    lateinit var configStorage: KeeperConfigStorage

    /**
     * returns hashed string using SHA-256 algorithm
     */
    private fun hashData(data: String): String? {
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

    private fun isPasskeyLegal(passkey: String): Boolean {
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

    fun verifyPasskey(passkey: String): CryptoResult {
        val hashCurrent = hashData(passkey)
        val hashSaved =
            configStorage.getConfigParam(KeeperConfigStorage.KEEPER_CONFIG_PARAM_PASSKEY_HASH)

        return when {
            hashSaved.isNullOrEmpty() ->
                CryptoResult.Error(CryptoResult.MSG_TYPE_MISSING_SAVED_PASSKEY)

            hashCurrent != hashSaved ->
                CryptoResult.Error(CryptoResult.MSG_TYPE_WRONG_PASSKEY_PROVIDED)

            hashCurrent == hashSaved -> {
                CryptoResult.Success(CryptoResult.MSG_TYPE_SUCCESS)
            }
            // shall not happen
            else -> CryptoResult.Error(CryptoResult.MSG_TYPE_UNKNOWN_ERROR)
        }
    }

    companion object {

        private const val PASSKEY_MIN_LENGTH = 4
    }

}