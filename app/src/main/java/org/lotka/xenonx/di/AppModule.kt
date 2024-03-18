package org.lotka.xenonx.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
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

import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//  @Provides
//  @Singleton
//  fun FirebaseAuth(): FirebaseAuth = Firebase.auth
//



    @Provides
    @Singleton
    fun FirebaseAuthInstance(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return Firebase.storage

    }


    @Provides
    @Singleton
    fun resources(application: Application): Resources = application.resources

    @Provides
    @Singleton
    fun context(application: Application): Context = application


    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun dispatcher(dispatchersProvider: DispatchersProviderImpl): DispatchersProvider =
        dispatchersProvider.dispatchersProvider


    @Provides
    @Singleton
    fun apiExceptionHandler(
        gson: Gson,
        sharedPreferences: SharedPreferences
    ): NetworkExceptionHandler = NetworkExceptionHandler(gson)

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Application): SharedPreferences {
        return context.getSharedPreferences(Constants.TAG, Context.MODE_PRIVATE)
    }


    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context) =
        app as BaseAppController


    @ActivityScoped
    @Provides
    fun provideActivity(@ActivityContext activityContext: Context) = activityContext


    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        val analytics = FirebaseAnalytics.getInstance(context)
        analytics.setAnalyticsCollectionEnabled(true)
        return analytics
    }


    @Singleton
    @Provides
    fun provideUpdateManager(
        @ApplicationContext context: Context,
        analytics: FirebaseAnalytics
    ): CustomUpdateManager {
        return CustomUpdateManager(context, analytics)
    }
}