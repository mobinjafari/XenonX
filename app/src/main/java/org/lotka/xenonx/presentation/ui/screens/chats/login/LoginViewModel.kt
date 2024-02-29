package org.lotka.xenonx.presentation.ui.screens.chats.login


import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import org.lotka.xenonx.domain.usecase.auth.LoginUserUseCase
import com.kilid.portal.presentation.ui.screens.chats.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.lotka.xenonx.data.user.USER_COLLECTION
import org.lotka.xenonx.domain.util.ResultState
import org.lotka.xenonx.presentation.ui.screens.chats.register.UserData
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val registerUseCase: LoginUserUseCase,
    private val db: FirebaseFirestore
) : ViewModel() {


    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var inProcess = mutableStateOf(false)
    var inLogin = mutableStateOf(false)
    private val auth = FirebaseAuth.getInstance()
    val userData = mutableStateOf<UserData?>(null)

    private val _loginState = Channel<RegisterState>()
    val loginState = _loginState.receiveAsFlow()

    fun loginUser(email: String, password: String) = viewModelScope.launch {

        if (email.isEmpty() or password.isEmpty()) {
            _loginState.send(RegisterState(isError = "Please fill all fields"))
            return@launch
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    inLogin.value = true
                    inProcess.value = true

                    auth.currentUser?.uid?.let {
                        LoginProfile(it)
                    }

                } else {
                    RegisterState(isError = "Sign In Failed")
                }
            }
        }

        registerUseCase.invoke(email, password).collect { result ->
            when (result) {
                is ResultState.Error -> {
                    _loginState.send(RegisterState(isError = "Sign In Failed"))
                }

                is ResultState.Loading -> {
                    _loginState.send(RegisterState(isLoading = true))
                }

                is ResultState.Success -> {
                    _loginState.send(RegisterState(isSuccess = "Sign In Success"))

                }

                else -> {}
            }
        }
    }

    fun LoginProfile(name: String? = null, email: String? = null) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val data = UserData(
            userId = uid ?: "",
            email = email ?: userData.value?.email,
            name = name ?: userData.value?.name,

            )
        uid?.let {
            inProcess.value = true
            db.collection(USER_COLLECTION).document(it).addSnapshotListener() { value, error ->
                if (error != null) {
                    Toast.makeText(
                        db.app.applicationContext, "Error: ${error.message}", Toast.LENGTH_SHORT
                    )
                    if (value != null) {
                        var user = value?.toObject<UserData>()
                        userData.value = user
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