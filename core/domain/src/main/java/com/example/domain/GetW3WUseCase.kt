package com.example.domain

import com.example.data.repository.W3WRepository
import javax.inject.Inject

class GetW3WUseCase @Inject constructor(
    private val w3WRepository: W3WRepository
) {

    operator fun invoke(latitude: Double, longitude: Double) = w3WRepository.get3WordAddress(
        latitude, longitude
    )

}