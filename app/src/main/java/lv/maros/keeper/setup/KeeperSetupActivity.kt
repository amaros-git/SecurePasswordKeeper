package lv.maros.keeper.setup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.R
import lv.maros.keeper.SharedKeeperViewModel

@AndroidEntryPoint
class KeeperSetupActivity : AppCompatActivity() {

    // Create viewModel to share it in Fragments
    private val viewModel: SharedSetupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keeper_setup)

        viewModel.setupIsFinished.observe(this) {
            it?.let {
                finishSetup()
            }
        }
    }

    private fun finishSetup() {
        val intent = Intent(this, KeeperSetupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}