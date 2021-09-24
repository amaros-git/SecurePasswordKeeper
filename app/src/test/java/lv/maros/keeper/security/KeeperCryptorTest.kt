package lv.maros.keeper.security

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
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
            "12345",
            "adwqeqe",
            "c 343:24323cc?>Z\"!#$#%^&$%^"
        )

        inputData.forEach {
            val result = cryptor.encryptString(it, key, iv)
            System.err.println("TEST")
        }



    }

    @Test
    fun decryptString() {
    }
}