package org.lotka.xenonx.domain.repository.auth


import kotlinx.coroutines.flow.Flow
import org.lotka.xenonx.data.util.Response

interface AuthScreenRepository {
    fun isUserAuthenticatedInFirebase(): Flow<Response<Boolean>>
    suspend fun signIn(email: String, password: String): Flow<Response<Boolean>>
    suspend fun signUp(email: String, password: String): Flow<Response<Boolean>>
}