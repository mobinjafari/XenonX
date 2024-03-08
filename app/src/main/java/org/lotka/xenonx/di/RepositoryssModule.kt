package org.lotka.xenonx.di


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.chatwithme.domain.usecase.profileScreen.SignOut
import com.example.chatwithme.domain.usecase.profileScreen.UploadPictureToFirebase
import com.example.chatwithme.domain.usecase.userListScreen.AcceptPendingFriendRequestToFirebase
import com.example.chatwithme.domain.usecase.userListScreen.CheckChatRoomExistedFromFirebase
import com.example.chatwithme.domain.usecase.userListScreen.CheckFriendListRegisterIsExistedFromFirebase
import com.example.chatwithme.domain.usecase.userListScreen.CreateChatRoomToFirebase
import com.example.chatwithme.domain.usecase.userListScreen.CreateFriendListRegisterToFirebase
import com.example.chatwithme.domain.usecase.userListScreen.LoadAcceptedFriendRequestListFromFirebase
import com.example.chatwithme.domain.usecase.userListScreen.LoadPendingFriendRequestListFromFirebase
import com.example.chatwithme.domain.usecase.userListScreen.RejectPendingFriendRequestToFirebase
import org.lotka.xenonx.data.repository.Auth.ChatScreenRepositoryImpl
import org.lotka.xenonx.domain.usecase.chat.profileScreen.CreateOrUpdateProfileToFirebase
import org.lotka.xenonx.domain.usecase.chat.profileScreen.LoadProfileFromFirebase
import org.lotka.xenonx.domain.usecase.chat.profileScreen.ProfileScreenUseCases
import org.lotka.xenonx.domain.usecase.chat.profileScreen.SetUserStatusToFirebase

import org.lotka.xenonx.domain.usecase.chat.userListScreen.OpenBlockedFriendToFirebase

import org.lotka.xenonx.domain.usecase.chat.userListScreen.SearchUserFromFirebase
import org.lotka.xenonx.domain.usecase.chat.userListScreen.UserListScreenUseCases
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.lotka.xenonx.data.exceptions.NetworkExceptionHandler
import org.lotka.xenonx.domain.util.Constants
import org.lotka.xenonx.presentation.ui.app.BaseAppController
import org.lotka.xenonx.presentation.util.CustomUpdateManager
import org.lotka.xenonx.presentation.util.DispatchersProvider
import org.lotka.xenonx.presentation.util.DispatchersProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import org.lotka.xenonx.data.repository.Auth.AuthScreenRepositoryImpl
import org.lotka.xenonx.data.repository.Auth.ProfileScreenRepositoryImpl
import org.lotka.xenonx.data.repository.Auth.UserListScreenRepositoryImpl
import org.lotka.xenonx.domain.repository.auth.AuthScreenRepository
import org.lotka.xenonx.domain.repository.auth.ChatScreenRepository
import org.lotka.xenonx.domain.repository.auth.ProfileScreenRepository
import org.lotka.xenonx.domain.repository.auth.UserListScreenRepository
import org.lotka.xenonx.domain.usecase.chat.authScreen.AuthUseCases
import org.lotka.xenonx.domain.usecase.chat.authScreen.IsUserAuthenticatedInFirebase
import org.lotka.xenonx.domain.usecase.chat.authScreen.SignIn
import org.lotka.xenonx.domain.usecase.chat.authScreen.SignUp
import org.lotka.xenonx.domain.usecase.chat.chatScreen.BlockFriendToFirebase
import org.lotka.xenonx.domain.usecase.chat.chatScreen.ChatScreenUseCases
import org.lotka.xenonx.domain.usecase.chat.chatScreen.InsertMessageToFirebase
import org.lotka.xenonx.domain.usecase.chat.chatScreen.LoadMessageFromFirebase
import org.lotka.xenonx.domain.usecase.chat.chatScreen.LoadOpponentProfileFromFirebase
import javax.inject.Singleton
import org.lotka.xenonx.data.repository.home.HomeRemoteDataSource
import org.lotka.xenonx.data.repository.home.HomeRepositoryImpl
import org.lotka.xenonx.domain.repository.HomeRepository


@Module
@InstallIn(SingletonComponent::class)
object RepositoryssModule {
    @Provides
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseStorageInstance() = FirebaseStorage.getInstance()

    @Provides
    fun provideFirebaseDatabaseInstance() = FirebaseDatabase.getInstance()

//    @Provides
//    fun provideSharedPreferences(application: Application) =
//        application.getSharedPreferences("login", Context.MODE_PRIVATE)

    @Provides
    fun providesDataStore(application: Application) = application.dataStore

    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
    ): AuthScreenRepository = AuthScreenRepositoryImpl(auth)

    @Provides
    fun provideChatScreenRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase
    ): ChatScreenRepository = ChatScreenRepositoryImpl(auth, database)

    @Provides
    fun provideProfileScreenRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase,
        storage: FirebaseStorage
    ): ProfileScreenRepository = ProfileScreenRepositoryImpl(auth, database, storage)

    @Provides
    fun provideUserListScreenRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase
    ): UserListScreenRepository = UserListScreenRepositoryImpl(auth, database)

    @Provides
    fun provideAuthScreenUseCase(authRepository: AuthScreenRepository) = AuthUseCases(
        isUserAuthenticated = IsUserAuthenticatedInFirebase(authRepository),
        signIn = SignIn(authRepository),
        signUp = SignUp(authRepository)
    )

    @Provides
    fun provideChatScreenUseCase(chatScreenRepository: ChatScreenRepository) = ChatScreenUseCases(
        blockFriendToFirebase = BlockFriendToFirebase(chatScreenRepository),
        insertMessageToFirebase = InsertMessageToFirebase(chatScreenRepository),
        loadMessageFromFirebase = LoadMessageFromFirebase(chatScreenRepository),
        opponentProfileFromFirebase = LoadOpponentProfileFromFirebase(chatScreenRepository)
    )

    @Provides
    fun provideProfileScreenUseCase(profileScreenRepository: ProfileScreenRepository) =
        ProfileScreenUseCases(
            createOrUpdateProfileToFirebase = CreateOrUpdateProfileToFirebase(
                profileScreenRepository
            ),
            loadProfileFromFirebase = LoadProfileFromFirebase(profileScreenRepository),
            setUserStatusToFirebase = SetUserStatusToFirebase(profileScreenRepository),
            signOut = SignOut(profileScreenRepository),
            uploadPictureToFirebase = UploadPictureToFirebase(profileScreenRepository)
        )

    @Provides
    fun provideUserListScreenUseCase(userListScreenRepository: UserListScreenRepository) =
        UserListScreenUseCases(
            acceptPendingFriendRequestToFirebase = AcceptPendingFriendRequestToFirebase(
                userListScreenRepository
            ),
            checkChatRoomExistedFromFirebase = CheckChatRoomExistedFromFirebase(
                userListScreenRepository
            ),
            checkFriendListRegisterIsExistedFromFirebase = CheckFriendListRegisterIsExistedFromFirebase(
                userListScreenRepository
            ),
            createChatRoomToFirebase = CreateChatRoomToFirebase(userListScreenRepository),
            createFriendListRegisterToFirebase = CreateFriendListRegisterToFirebase(
                userListScreenRepository
            ),
            loadAcceptedFriendRequestListFromFirebase = LoadAcceptedFriendRequestListFromFirebase(
                userListScreenRepository
            ),
            loadPendingFriendRequestListFromFirebase = LoadPendingFriendRequestListFromFirebase(
                userListScreenRepository
            ),
            openBlockedFriendToFirebase = OpenBlockedFriendToFirebase(userListScreenRepository),
            rejectPendingFriendRequestToFirebase = RejectPendingFriendRequestToFirebase(
                userListScreenRepository
            ),
            searchUserFromFirebase = SearchUserFromFirebase(userListScreenRepository),
        )
}