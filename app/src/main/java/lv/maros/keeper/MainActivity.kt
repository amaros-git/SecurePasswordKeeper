package lv.maros.keeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.setup.KeeperSetupActivity
import lv.maros.keeper.views.LoginFragmentDirections
import timber.log.Timber
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: SharedKeeperViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isKeeperConfigured()) {
            Timber.d("Keeper is not configured")
            startSetupActivityAndFinish()
        }

        for (i in 0..2) {
            getSecretKey("1234567890qwerty")
        }
        // else we start destination fragment from main nav graph
    }

    // TODO it is always new ..
    private fun getSecretKey(encryptionKey: String): SecretKey {
        val key = SecretKeySpec(encryptionKey.toByteArray(), KeeperCryptor.SECRET_KEY_ALGO)
        Timber.d("secret key = ${key.hashCode()}")
        return key
    }

    private fun startSetupActivityAndFinish() {
        val intent = Intent(this, KeeperSetupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun isKeeperConfigured(): Boolean {
        return viewModel.isKeeperConfigured()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController(R.id.nav_host_fragment).popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}