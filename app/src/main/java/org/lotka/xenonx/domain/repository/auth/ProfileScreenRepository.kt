package org.lotka.xenonx.domain.repository.auth

import android.net.Uri

import kotlinx.coroutines.flow.Flow
import org.lotka.xenonx.data.util.Response
import org.lotka.xenonx.domain.model.model.chat.User
import org.lotka.xenonx.domain.model.model.chat.UserStatus

interface ProfileScreenRepository {
    suspend fun signOut(): Flow<Response<Boolean>>
    suspend fun uploadPictureToFirebase(url: Uri): Flow<Response<String>>
    suspend fun createOrUpdateProfileToFirebase(user: User): Flow<Response<Boolean>>
    suspend fun loadProfileFromFirebase(): Flow<Response<User>>
    suspend fun setUserStatusToFirebase(userStatus: UserStatus): Flow<Response<Boolean>>
}