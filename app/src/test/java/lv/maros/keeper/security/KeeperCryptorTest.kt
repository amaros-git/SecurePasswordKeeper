package lv.maros.keeper.security

import lv.maros.keeper.models.Password
import lv.maros.keeper.utils.KeeperResult
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class)
class KeeperCryptorTest {

    private val logTag = KeeperCryptorTest::class.java.simpleName

    private val key = "kF8g8hDDXvypd5KP"
    private val iv = "fbBMmGE2Z6GtKvEs"

    private val cryptor = KeeperCryptor()

    @Test
    fun hashData() {

    }

    @Test
    fun encryptString() {
        //Get test passwords and encrypted values
        val testPasswordMap = getTestPasswordMap()

        //Check each entry
        testPasswordMap.forEach { entry ->
            //Encrypt password and check if success is returned
            val encryptionResult = cryptor.encryptString(entry.key, key, iv)
            assertThat(encryptionResult, `is`(instanceOf(KeeperResult.Success::class.java)))

            //Check is a new encrypted password is equal to saved.
            val encryptedData = (encryptionResult as KeeperResult.Success).data
            assertThat(encryptedData, `is`(equalTo(entry.value)))
        }
    }

    @Test
    fun decryptString() {

    }
}

/**
 * key is password, value is this encrypted password
 */
fun getTestPasswordMap(): Map<String, String> {
    return mapOf(
        "Qwerty12345" to "a0/17/bf/e7/e0/c1/20/1c/7a/e1/8f/e0/8c/de/58/1d",
        "adwqeqe" to "85/8c/b8/b6/76/2b/0e/9f/6a/67/9f/57/8b/d5/ac/4c",
        "c343:24323cc?>Z\"!#$#%^&$%^" to "c7/3a/30/9a/ff/fa/f9/da/8f/89/f7/6b/86/3b/6f/21/37/34/1f/fe/71/b1/45/40/fa/e9/0d/18/6d/7b/38/67"
    )
}
