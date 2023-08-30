package lv.maros.secured.password.keeper

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import lv.maros.secured.password.keeper.pages.setup.KeeperSetupActivity
import timber.log.Timber

class KeeperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isKeeperConfigured()) {
            Timber.d("Keeper is not configured")
            startSetupActivityAndFinish()
        }

        // else we start destination fragment from main nav graph TODO OR Show login screen
    }

    private fun startSetupActivityAndFinish() {
        val intent = Intent(this, KeeperSetupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

        finish()
    }

    private fun isKeeperConfigured(): Boolean {
        val keeperConfig = (applicationContext as KeeperApplication).configStorage
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