package lv.maros.securedpasswordkeeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lv.maros.securedpasswordkeeper.views.AddPasswordFragment

class SharedPasswordViewModel : ViewModel() {


    fun savePassword(password: Password) {
        viewModelScope.launch(Dispatchers.Default)  {

        }
    }

}