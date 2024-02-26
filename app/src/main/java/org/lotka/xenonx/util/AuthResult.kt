package org.lotka.xenonx.util

data class AuthResult2(
    val success: Boolean,
    val userId: String? = null,
    val errorMessage: String? = null
)