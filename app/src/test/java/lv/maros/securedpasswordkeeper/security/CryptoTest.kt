package lv.maros.securedpasswordkeeper.security

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
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

    }


    @Test
    fun encryptAndSavePasskey() {
    }

    @Test
    fun verifyPasskey() {
    }
}