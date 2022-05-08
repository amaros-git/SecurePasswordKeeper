package lv.maros.secured.password.keeper.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import lv.maros.secured.password.keeper.data.dto.PasswordDTO
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.models.PasswordInputData
import lv.maros.secured.password.keeper.pages.passwords.PasswordListAdapter

//TODO rework setup extension to <T>
fun RecyclerView.setup(
    adapter: PasswordListAdapter
) {
    this.apply {
        layoutManager = LinearLayoutManager(this.context)
        this.adapter = adapter
    }
}

fun Fragment.setTitle(title: String) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }
}

fun Fragment.setDisplayHomeAsUpEnabled(bool: Boolean) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            bool
        )
    }
}

fun CharSequence.isNotBlankOrEmpty(): Boolean = !isBlank() && !isEmpty()

fun CharSequence.isBlankOrEmpty(): Boolean = isBlank() || isEmpty()

fun BottomNavigationView.uncheckAllItems() {
    menu.setGroupCheckable(0, true, false)
    for (i in 0 until menu.size()) {
        menu.getItem(i).isChecked = false
    }
    menu.setGroupCheckable(0, true, true)
}


//animate changing the view visibility
fun View.fadeIn() {
    this.visibility = View.VISIBLE
    this.alpha = 0f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeIn.alpha = 1f
        }
    })
}

//animate changing the view visibility
fun View.fadeOut() {
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.alpha = 1f
            this@fadeOut.visibility = View.GONE
        }
    })
}

fun PasswordDTO.toPassword() =
    Password(
        this.website,
        this.username,
        this.encryptedPassword,
        this.passwordLastModificationDate,
        this.id
    )

fun PasswordInputData.toPasswordDTO(encryptedPassword: String) =
    PasswordDTO(
        this.website,
        this.username,
        encryptedPassword,
        System.currentTimeMillis(),
        0 //Restore a real iD in case of Update.
    )

fun Password.toPasswordDTO() =
    PasswordDTO(
        this.website,
        this.username,
        this.encryptedPassword,
        this.passwordLastModificationDate,
        0
    )

