package com.example.domain.usecase

import com.example.domain.repository.W3WRepository
import javax.inject.Inject

class GetW3WUseCase @Inject constructor(
    private val w3WRepository: W3WRepository
) {

    operator fun invoke(latitude: Double, longitude: Double) = w3WRepository.get3WordAddress(
        latitude, longitude
    )

}