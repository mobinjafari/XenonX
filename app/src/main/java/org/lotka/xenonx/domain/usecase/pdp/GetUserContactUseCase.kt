package org.lotka.xenonx.domain.usecase.pdp


import org.lotka.xenonx.domain.model.model.contactInfo.ContactInformation
import org.lotka.xenonx.domain.repository.HomeRepository
import org.lotka.xenonx.domain.util.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetUserContactUseCase @Inject constructor(private val repository: HomeRepository) {

    suspend operator fun invoke(id: Int): Flow<ResultState<ContactInformation>> {
        return flowOf(repository.getUserContactInfo(id))
    }


}
