package me.nathanfallet.bdeensisa.features.account

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.nathanfallet.bdeensisa.extensions.generateQRCode
import me.nathanfallet.bdeensisa.models.User
import me.nathanfallet.bdeensisa.models.UserToken
import me.nathanfallet.bdeensisa.services.APIService

class AccountViewModel(application: Application): AndroidViewModel(application) {

    // Properties

    private lateinit var saveToken: (UserToken) -> Unit

    private var qrCode = MutableLiveData<Bitmap>()

    // Getters

    fun getQrCode(): LiveData<Bitmap> {
        return qrCode
    }

    // Methods

    fun load(
        code: String?,
        saveToken: (UserToken) -> Unit
    ) {
        this.saveToken = saveToken
        code?.let {
            authenticate(it)
        }
    }

    fun launchLogin() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(APIService().authenticationUrl)
        )
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(getApplication(), browserIntent, null)
    }

    fun authenticate(code: String) {
        viewModelScope.launch {
            val token = APIService().authenticate(code)
            saveToken(token)
        }
    }

    fun generateQrCode(user: User) {
        viewModelScope.launch {
            val code = "bdeensisa://users/${user.id}".generateQRCode()
            qrCode.value = code
        }
    }

}