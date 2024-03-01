package org.lotka.xenonx.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import org.lotka.xenonx.data.user.USER_COLLECTION
import org.lotka.xenonx.domain.repository.AuthRepository
import org.lotka.xenonx.domain.util.ResultState
import org.lotka.xenonx.domain.util.ResultState2
import org.lotka.xenonx.presentation.ui.screens.chats.register.UserData
import org.lotka.xenonx.util.AuthResult2
import java.lang.Exception
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource
) : AuthRepository {
    override fun loginUser(email: String, password: String): Flow<ResultState<AuthResult2>> {
            return (dataSource.loginUser( email, password))
        }

    override fun registerUser(
        userName: String,
        emile: String,
        password: String
    ): Flow<ResultState<AuthResult2>> {
        return (dataSource.registerUser( userName, emile, password))
    }

    override suspend fun createOrUpdateProfile(
        name: String?,
        email: String?,
        number: String?,
        imageUrl: String?
    ): ResultState<UserData> {
        return  dataSource.createOrUpdateProfile(name, email, number, imageUrl)
    }


}





