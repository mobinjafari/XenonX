package org.lotka.xenonx.presentation.ui.screens.chats.register

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.toObject
import com.kilid.portal.presentation.ui.screens.chats.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.lotka.xenonx.data.repository.auth.AuthRemoteDataSource
import org.lotka.xenonx.data.user.USER_COLLECTION
import org.lotka.xenonx.domain.usecase.auth.RegisterUseCase
import org.lotka.xenonx.domain.util.ResultState
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val loginUserUseCase: RegisterUseCase,
    private val dataStore: AuthRemoteDataSource
) : ViewModel() {

    var email = mutableStateOf("")
    var userName = mutableStateOf("")
    var password = mutableStateOf("")
    var number = mutableStateOf("")
    var inProcess = mutableStateOf(false)

    private val _registerState = Channel<RegisterState>()
    val registerState = _registerState.receiveAsFlow()
    val registerIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)


    init {
        val curentUser = FirebaseAuth.getInstance().currentUser
           registerIn.value = curentUser != null
        curentUser?.uid.let {
            createOrUpdateProfile(it)
        }
    }




    fun registerUser(number: String,userName: String, email: String, password: String) = viewModelScope.launch {

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _registerState.send(RegisterState(isError = "Please fill all fields"))
            return@launch
        }
        dataStore.firestore.collection(USER_COLLECTION).whereEqualTo(number, number).get().addOnSuccessListener {
            if (!it.isEmpty) {
                createOrUpdateProfile()
            }else {
                RegisterState(isError = "User Not Found")
                inProcess.value = false
            }
        }
        loginUserUseCase.invoke(userName, email, password).collect { result ->
            when (result) {
                is ResultState.Error -> {
                    _registerState.send(RegisterState(isError = "Sign Up Failed ${result.error?.error}"))
                }
                is ResultState.Loading -> {
                    _registerState.send(RegisterState(isLoading = true))
                }
                is ResultState.Success -> {
                    registerIn.value = true
                    _registerState.send(RegisterState(isSuccess = "Sign Up Success"))
                }
                else -> {}
            }
        }
    }

    fun createOrUpdateProfile(name: String? = null, email: String? = null, number: String? = null, imageUrl: String? = null) {
        val uid = dataStore.firebaseAuth.currentUser?.uid
         val data = UserData(
           userId = uid?:"",
           name = name?: userData.value?.name,
             number = number?:userData.value?.number
             , imageUrl = imageUrl?:userData.value?.imageUrl
         )
         uid?.let {
             inProcess.value= true
             dataStore.firestore.collection(USER_COLLECTION).document(it).addSnapshotListener() { value, error ->
                 if (error != null) {
                     Toast.makeText(
                         dataStore.firestore.app.applicationContext, "Error: ${error.message}",  Toast.LENGTH_SHORT
                     )
                     if (value != null) {
                     var user = value?.toObject<UserData>()
                     userData.value=user
                     inProcess.value = true

                     }
                     return@addSnapshotListener
                 } else {
                     userData.value = value?.toObject(UserData::class.java)
                     inProcess.value = false

                 }

             }

         }




    }


}
