package org.lotka.xenonx.data.repository.auth


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await
import org.lotka.xenonx.data.user.USER_COLLECTION
import org.lotka.xenonx.domain.util.ResultState
import org.lotka.xenonx.domain.util.ResultState2
import org.lotka.xenonx.presentation.ui.screens.chats.register.UserData
import org.lotka.xenonx.util.AuthResult2
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSource @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore,
    val storage: FirebaseStorage,
    val context: android.content.Context
) : AuthDataSource {

    override fun loginUser(email: String, password: String): Flow<ResultState<AuthResult2>> {
        return flow {
            emit(ResultState.Loading(true))
           // val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(ResultState.Success(AuthResult2(success = true, userId = 12.toString())))
            emit(ResultState.Loading(false))
        }.catch { e ->
//            emit(ResultState.Error(AuthResult(success = false, errorMessage = e.message)))
        }
    }

    override fun registerUser(
        userName: String,
        email: String,
        password: String
    ): Flow<ResultState<AuthResult2>> {
        return flow {
            emit(ResultState.Loading(true))
//            val authResult =
//                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(ResultState.Success(AuthResult2(success = true, userId = 12.toString())))
            emit(ResultState.Loading(false))
        }.catch { e ->
//            emit(ResultState.Error(AuthResult(success = false, errorMessage = e.message)))
        }
    }

    override suspend fun createOrUpdateProfile(
        name: String?,
        email: String?,
        number: String?,
        imageUrl: String?
    ): ResultState<UserData> {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            return try {
                if (uid != null) {
                    val userData = UserData(uid, name, number, imageUrl)
                    // Update user data in Firestore
                    firestore.collection(USER_COLLECTION).document(uid).set(userData).await()
                    ResultState.Success(userData)
                } else {
                    ResultState2.Error(Exception("User not authenticated"))
                }
            } catch (e: Exception) {
                ResultState2.Error(Exception("User not authenticated"))
            }
        }
    }




