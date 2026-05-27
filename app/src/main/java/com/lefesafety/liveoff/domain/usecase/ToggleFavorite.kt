package com.lefesafety.liveoff.domain.usecase

import com.lefesafety.liveoff.domain.model.ContentType
import com.lefesafety.liveoff.domain.repository.UserDataRepository
import javax.inject.Inject

class ToggleFavorite @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(contentId: String, contentType: ContentType) {
        userDataRepository.toggleFavorite(contentId, contentType)
    }
}
