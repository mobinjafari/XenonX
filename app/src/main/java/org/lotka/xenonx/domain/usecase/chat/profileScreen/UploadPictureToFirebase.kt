package com.example.chatwithme.domain.usecase.profileScreen

import android.net.Uri

import org.lotka.xenonx.domain.repository.auth.ProfileScreenRepository

class UploadPictureToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(url: Uri) = profileScreenRepository.uploadPictureToFirebase(url)
}