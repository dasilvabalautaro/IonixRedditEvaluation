package com.globalhiddenodds.ionixredditevaluation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkInfo
import com.globalhiddenodds.ionixredditevaluation.R
import com.globalhiddenodds.ionixredditevaluation.ui.utils.Utils
import com.globalhiddenodds.ionixredditevaluation.ui.viewmodel.CrudDatabaseViewModel
import com.globalhiddenodds.ionixredditevaluation.ui.viewmodel.NewsPostingsViewModel
import com.skydoves.landscapist.coil.CoilImage


@Composable
fun BoardingBody(viewModel: NewsPostingsViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = "Boarding Screen" },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to the Ionix Reddit",
            style = MaterialTheme.typography.h5,
            color = Color.White
        )
        LoadImageBackground()
        DownloadPostings(viewModel)
    }
}

@Composable
fun LoadImageBackground() {
    CoilImage(
        imageModel = (R.drawable.abstract_background),
        modifier = Modifier.fillMaxSize(),
        contentDescription = "background_image",
        contentScale = ContentScale.FillBounds,
        placeHolder = ImageVector.vectorResource(id = R.drawable.loading_img),
        error = ImageVector.vectorResource(id = R.drawable.ic_broken_image)
    )
}

@Composable
private fun DownloadPostings(viewModel: NewsPostingsViewModel) {
    val context = LocalContext.current
    if (Utils.isConnect(context)) {
        ObserverWorkInfo(viewModel)
        if (viewModel.status.value == false) {
            viewModel.downPosting()
        }
    } else {
        Utils.notify(context, "Connectivity fail.")
    }
}

@Composable
private fun ObserverWorkInfo(newsPostViewModel: NewsPostingsViewModel) {
    val context = LocalContext.current
    val list by newsPostViewModel.outputWorkInfo.observeAsState()
    list?.let {
        if (it.isNotEmpty()) {
            val workInfo = it[0]
            when {
                workInfo.state.isFinished -> {
                    SavePostings()
                }
                workInfo.state == WorkInfo.State.FAILED -> {
                    Utils.notify(context, stringResource(R.string.lbl_down_failed))
                }
            }
        }
    }
}

@Composable
private fun SavePostings(viewModel: CrudDatabaseViewModel = hiltViewModel()) {
    val context = LocalContext.current
    ObserverTaskResultCrud(viewModel)
    viewModel.insert()
    Utils.notify(context, stringResource(R.string.lbl_task_down_finish))
}

@Composable
private fun ObserverTaskResultCrud(
    crudDatabaseViewModel: CrudDatabaseViewModel
) {
    val context = LocalContext.current
    val result by crudDatabaseViewModel.taskResult.observeAsState()
    result?.let {
        Utils.notify(context, it)
    }
}

@Composable
fun CustomCircularProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier.size(100.dp),
        color = Color.Green,
        strokeWidth = 10.dp
    )
}
