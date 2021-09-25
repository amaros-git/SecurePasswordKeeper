package lv.maros.keeper.security

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
        val inputData = mutableListOf(
            "123456789012345",
            "adwqeqe",
            "c 343:24323cc?>Z\"!#$#%^&$%^"
        )

       /* inputData.forEach {
            val result = cryptor.encryptString(it, key, iv)
            println(result)
        }*/

        val encryptionResult = cryptor.encryptString(inputData[0], key, iv)
        assertThat(encryptionResult, `is`(instanceOf(KeeperResult.Success::class.java)))

        //val decoded = cryptor.decryptString(result, key, iv)
       // println("decoded = $decoded")
/*

        val bytes = result.encodeToByteArray()
        bytes.forEach {
            print(it)
            print(" ")
        }

        println()

        bytes.forEach {
            print((0xFF and it.toInt()))
            print(" ")
        }
*/



    }

    @Test
    fun decryptString() {
    }
}