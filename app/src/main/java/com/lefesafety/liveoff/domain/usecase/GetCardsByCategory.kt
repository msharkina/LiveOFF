package com.lefesafety.liveoff.domain.usecase

import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCardsByCategory @Inject constructor(
    private val contentRepository: ContentRepository
) {
    operator fun invoke(categoryId: String): Flow<List<Card>> =
        contentRepository.getCardsByCategory(categoryId)
}
