package lv.maros.keeper.setup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.MainActivity
import lv.maros.keeper.R
import lv.maros.keeper.SharedKeeperViewModel
import lv.maros.keeper.models.KeeperConfig
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.utils.KEEPER_AUTH_TYPE_NONE
import javax.inject.Inject

@AndroidEntryPoint
class KeeperSetupActivity : AppCompatActivity() {

    // Create viewModel to share it in Fragments
    private val viewModel: SharedSetupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keeper_setup)

        viewModel.setupIsFinishedEvent.observe(this) {
            it?.let {
                finishSetup()
            }
        }

    }

    // what if fails ?
    private fun initKeeperConfig() {
        val encryptionKey = viewModel.createEncryptionKey()
        val iv = viewModel.createEncryptionIV()

        viewModel.saveKeeperConfig(KeeperConfig(
            KEEPER_AUTH_TYPE_NONE,
            null,
            encryptionKey,
            iv,
            false
        ))
    }

    private fun finishSetup() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}