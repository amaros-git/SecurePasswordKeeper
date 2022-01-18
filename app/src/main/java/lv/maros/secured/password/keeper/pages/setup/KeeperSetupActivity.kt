package lv.maros.secured.password.keeper.pages.setup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import lv.maros.secured.password.keeper.KeeperActivity
import lv.maros.secured.password.keeper.R

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

        initKeeperConfig()

    }

    private fun initKeeperConfig() {
        viewModel.initKeeperConfig()
    }

    private fun finishSetup() {
        val intent = Intent(this, KeeperActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}