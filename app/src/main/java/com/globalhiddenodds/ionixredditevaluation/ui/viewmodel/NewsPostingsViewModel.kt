package com.globalhiddenodds.ionixredditevaluation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.globalhiddenodds.ionixredditevaluation.domain.GetNewsPostingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// Pattern observer
@HiltViewModel
class NewsPostingsViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val getNewsPostingUseCase: GetNewsPostingUseCase
) : ViewModel() {

    val outputWorkInfo: LiveData<List<WorkInfo>> = getNewsPostingUseCase.workInfo

    fun downPosting() {
        viewModelScope.launch {
            getNewsPostingUseCase.downPosting()
        }
    }

    fun cancelDownPostings() {
        viewModelScope.launch {
            getNewsPostingUseCase.cancelWork()
        }
    }

}