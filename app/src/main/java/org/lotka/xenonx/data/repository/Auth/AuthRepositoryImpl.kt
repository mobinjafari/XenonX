package com.kilid.portal.data.repository.auth

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import org.lotka.xenonx.data.repository.Auth.AuthDataSource
import org.lotka.xenonx.domain.repository.AuthRepository
import org.lotka.xenonx.domain.util.ResultState
import org.lotka.xenonx.util.AuthResult2
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource
) : AuthRepository {
    override fun loginUser(emile: String, password: String): Flow<ResultState<AuthResult2>> {
            return (dataSource.loginUser( emile, password))
        }

    override fun registerUser(
        userName: String,
        emile: String,
        password: String
    ): Flow<ResultState<AuthResult2>> {
        return (dataSource.registerUser( userName, emile, password))
    }
}





