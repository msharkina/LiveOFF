package com.lefesafety.liveoff.domain.usecase

import com.lefesafety.liveoff.domain.repository.UserDataRepository
import javax.inject.Inject

class UpdateChecklistProgress @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(checklistId: String, itemIndex: Int, isChecked: Boolean) {
        userDataRepository.setChecklistItemChecked(checklistId, itemIndex, isChecked)
    }
}
