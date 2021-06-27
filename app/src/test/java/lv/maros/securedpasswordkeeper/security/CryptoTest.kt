package lv.maros.securedpasswordkeeper.security

import android.os.Build
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.ExpectFailure

import kotlinx.coroutines.ExperimentalCoroutinesApi
import lv.maros.securedpasswordkeeper.setup.CryptoResult

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.instanceOf

import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//@Config(sdk = [Build.VERSION_CODES.P])
class CryptoTest {

    private lateinit var crypto: Crypto

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setupCrypto() {
        crypto = Crypto(ApplicationProvider.getApplicationContext())
    }

    @After
    fun clearSharedReferences() {
        crypto.clearAll()
    }


    @Test
    fun encryptAndSavePasskey_receiveTestPasskeysAndTryToEncrypt() {
        // Create list of test passkeys
        val testPasskeys = listOf<String>(
            "qwerty123",
            "~!@#$%^&*(_)-=|}{:?><",
            "\"34d\"d\""
        )

        // Encrypt and save each key and check if Success. Test failed if any passkey fails
        testPasskeys.forEach { passkey ->
            val result = crypto.encryptAndSavePasskey(passkey)
            println("Testing $passkey")
            assertThat(result, `is`(instanceOf(CryptoResult.Success::class.java)))
        }
    }

    @Test
    fun verifyPasskey() {
    }

    @Test
    fun clearAll_addKeyDeleteKeyAndTryToVerifyKey() {

    }


}