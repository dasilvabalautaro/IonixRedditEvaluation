package com.globalhiddenodds.ionixredditevaluation.ui.viewmodel

import androidx.lifecycle.*
import com.globalhiddenodds.ionixredditevaluation.datasource.database.data.Posting
import com.globalhiddenodds.ionixredditevaluation.datasource.database.data.toPostingView
import com.globalhiddenodds.ionixredditevaluation.di.IoDispatcher
import com.globalhiddenodds.ionixredditevaluation.domain.CrudDatabaseUseCase
import com.globalhiddenodds.ionixredditevaluation.ui.data.PostingView
import com.globalhiddenodds.ionixredditevaluation.workers.FilterNewsWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrudDatabaseViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val crudDatabaseUseCase: CrudDatabaseUseCase,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
): ViewModel() {
    private val viewStatus = "VIEW_STATUS_INSERT"
    private val taskResultMutableLive = MutableLiveData<String>()
    val taskResult: LiveData<String> = taskResultMutableLive
    val listPosting: LiveData<List<PostingView>> by lazy {
        Transformations.map(
            crudDatabaseUseCase.listPosting.distinctUntilChanged()) {
            transformPosting(it)
        }
    }

    init {
        handle[viewStatus] = false
    }

    private fun transformPosting(list: List<Posting>): List<PostingView>{
        val listPostView = mutableListOf<PostingView>()
        viewModelScope.launch(ioDispatcher) {
            list.map { listPostView.add(it.toPostingView()) }
        }
        return listPostView
    }

    fun insert(){
        viewModelScope.launch {
            if (FilterNewsWorker.listPostingCloud.isNotEmpty() &&
                    !handle.getLiveData<Boolean>(viewStatus).value!!){
                handle[viewStatus] = true
                var result = crudDatabaseUseCase.cleanDatabase()
                taskResultMutableLive.value = "Delete Posting: $result"
                result = crudDatabaseUseCase.insertPosting()
                taskResultMutableLive.value = "Insert Posting: $result"
            }
        }
    }
}