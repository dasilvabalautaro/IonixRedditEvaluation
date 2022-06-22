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
    private val viewStatus = "VIEW_STATUS_DOWN"
    val outputWorkInfo: LiveData<List<WorkInfo>> = getNewsPostingUseCase.workInfo
    val status: LiveData<Boolean> by lazy { handle.getLiveData(viewStatus) }

    init {
        handle[viewStatus] = false
    }

    fun downPosting() {
        viewModelScope.launch {
            if (!handle.getLiveData<Boolean>(viewStatus).value!!) {
                getNewsPostingUseCase.downPosting()
                handle[viewStatus] = true
            }
        }
    }

    fun cancelDownPostings() {
        viewModelScope.launch {
            getNewsPostingUseCase.cancelWork()
        }
    }

}