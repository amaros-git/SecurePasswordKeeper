package lv.maros.secured.password.keeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lv.maros.secured.password.keeper.data.dto.PasswordDTO
import lv.maros.secured.password.keeper.pages.setup.KeeperSetupActivity
import lv.maros.secured.password.keeper.security.KeeperConfigStorage
import timber.log.Timber

class KeeperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isKeeperConfigured()) {
            Timber.d("Keeper is not configured")
            startSetupActivityAndFinish()
        }

        startSetupActivityAndFinish()

        TEST_addPasswords()
        // else we start destination fragment from main nav graph
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

    //TODO REMOVE AFTER TESTS
    private fun TEST_addPasswords() {
        val repository = (application as KeeperApplication).localPasswordsRepository
        for (i in 0..3) {
            GlobalScope.launch {
                val passwords = repository.getAllPasswords()
                if (passwords.isNullOrEmpty()) {
                    repository.savePassword(
                        PasswordDTO(
                            "website_$i",
                            "username_$i",
                            "adksdjsadjdas_$i",
                            System.currentTimeMillis(),
                            0
                        )
                    )
                }
            }
        }

    }
}