package org.lotka.xenonx.presentation.ui.screens.chats.home.profile

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kilid.portal.presentation.ui.screens.chats.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.lotka.xenonx.presentation.ui.screens.chats.register.UserData
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor():ViewModel(



)   {


    var userName = mutableStateOf("")

    var number = mutableStateOf("")
    var inProcess = mutableStateOf(false)

    fun uploadProfileImage(uri: Uri){
//        UploadImage()
    }







}