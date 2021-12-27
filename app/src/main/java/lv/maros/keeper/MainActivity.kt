package lv.maros.keeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.pages.setup.KeeperSetupActivity
import lv.maros.keeper.security.KeeperConfigStorage
import timber.log.Timber
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var keeperConfig: KeeperConfigStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        keeperConfig = KeeperConfigStorage(this)

        if (!isKeeperConfigured()) {
            Timber.d("Keeper is not configured")
            startSetupActivityAndFinish()
        }
        // else we start destination fragment from main nav graph
    }

    private fun startSetupActivityAndFinish() {
        val intent = Intent(this, KeeperSetupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

        finish()
    }

    private fun isKeeperConfigured(): Boolean {
        return keeperConfig.isKeeperConfigured()
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