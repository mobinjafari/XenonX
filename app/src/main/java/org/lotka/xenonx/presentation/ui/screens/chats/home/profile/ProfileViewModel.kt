package org.lotka.xenonx.presentation.ui.screens.chats.home.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.lotka.xenonx.data.repository.auth.AuthRemoteDataSource
import org.lotka.xenonx.data.user.USER_COLLECTION
import org.lotka.xenonx.presentation.ui.screens.chats.register.UserData
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStore: AuthRemoteDataSource
):ViewModel() {


    var userName = mutableStateOf("")

    var number = mutableStateOf("")
    var inProcess = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)

    fun uploadProfileImage(uri: Uri ){
      UploadImage(uri){
          createOrUpdateProfile(imageUrl = it.toString())
      }
    }

    private fun UploadImage(uri: Uri,onSuccess:(Uri)->Unit) {
        inProcess.value=true
        val storageRef = dataStore.storage.reference
        val uuid=UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
            inProcess.value=false
        }
            .addOnFailureListener{
//         we need a message here
            }
    }

    private val _NameTextField = MutableStateFlow("")
    val textFieldValue: StateFlow<String> = _NameTextField

    fun updateNameTextField(newValue: String) {
        _NameTextField.value = newValue
    }
    fun onNameTextFieldChanged(newValue: String) {
        viewModelScope.launch {
            updateNameTextField(newValue)
        }
    }

    private val _textFieldValueNumber = MutableStateFlow("")
    val textFieldNumber: StateFlow<String> = _textFieldValueNumber

    fun updateTextFieldNumber(newValue: String) {
        _NameTextField.value = newValue
    }
    fun onTextFieldNumberChanged(newValue: String) {
        viewModelScope.launch {
            updateNameTextField(newValue)
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





        }}}