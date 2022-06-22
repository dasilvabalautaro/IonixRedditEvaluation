package com.globalhiddenodds.ionixredditevaluation.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.globalhiddenodds.ionixredditevaluation.datasource.network.SearchCloud
import com.globalhiddenodds.ionixredditevaluation.datasource.network.data.PostingCloud
import com.globalhiddenodds.ionixredditevaluation.di.IoDispatcher
import com.globalhiddenodds.ionixredditevaluation.ui.activities.SearchWidgetState
import com.globalhiddenodds.ionixredditevaluation.workers.getChildCloud
import com.globalhiddenodds.ionixredditevaluation.workers.getPostHintSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Pattern observer
@HiltViewModel
class SearchPostingsViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val mutableListSearch = MutableLiveData<List<PostingCloud>>()
    val listSearch: LiveData<List<PostingCloud>> by lazy { mutableListSearch.distinctUntilChanged() }
    private val searchMutableWidgetState: MutableState<SearchWidgetState> =
        mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> = searchMutableWidgetState

    private val searchMutableTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = searchMutableTextState

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        viewModelScope.launch {
            searchMutableWidgetState.value = newValue
        }
    }

    fun updateSearchTextState(newValue: String) {
        viewModelScope.launch {
            searchMutableTextState.value = newValue
            searchPostings(newValue)
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun searchPostings(filter: String) {
        val list = withContext(viewModelScope.coroutineContext + ioDispatcher) {
            var mapSearch = mutableMapOf<String?, Any>()
            var listPostingSearch = listOf<PostingCloud>()
            val responseSearch = SearchCloud.retrofitService.search(filter)
            mapSearch = responseSearch as MutableMap<String?, Any>
            val child = getChildCloud(mapSearch)
            if (child.isNotEmpty()) {
                listPostingSearch = getPostHintSearch(child)
            }

            return@withContext listPostingSearch
        }

        if (list.isNotEmpty()) {
            mutableListSearch.value = list
        }
    }
}