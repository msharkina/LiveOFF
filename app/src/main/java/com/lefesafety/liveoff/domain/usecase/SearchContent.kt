package com.lefesafety.liveoff.domain.usecase

import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchContent @Inject constructor(
    private val contentRepository: ContentRepository
) {
    operator fun invoke(query: String): Flow<List<Card>> =
        contentRepository.searchCards(query)
}
