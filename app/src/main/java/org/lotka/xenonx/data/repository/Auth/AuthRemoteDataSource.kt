import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await
import org.lotka.xenonx.data.repository.Auth.AuthDataSource
import org.lotka.xenonx.domain.util.ResultState
import org.lotka.xenonx.util.AuthResult2
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthDataSource {

    override fun loginUser(email: String, password: String): Flow<ResultState<AuthResult2>> {
        return flow {
            emit(ResultState.Loading(true))
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(ResultState.Success(AuthResult2(success = true, userId = authResult.user?.uid)))
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
            val authResult =
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(ResultState.Success(AuthResult2(success = true, userId = authResult.user?.uid)))
            emit(ResultState.Loading(false))
        }.catch { e ->
//            emit(ResultState.Error(AuthResult(success = false, errorMessage = e.message)))
        }
    }


}
