package com.globalhiddenodds.ionixredditevaluation.ui.activities

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkInfo
import com.globalhiddenodds.ionixredditevaluation.R
import com.globalhiddenodds.ionixredditevaluation.ui.data.PostingView
import com.globalhiddenodds.ionixredditevaluation.ui.utils.Utils
import com.globalhiddenodds.ionixredditevaluation.ui.values.IonixRedditTheme
import com.globalhiddenodds.ionixredditevaluation.ui.viewmodel.CrudDatabaseViewModel
import com.globalhiddenodds.ionixredditevaluation.ui.viewmodel.NewsPostingsViewModel
import com.skydoves.landscapist.coil.CoilImage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IonixRedditTheme {
                IonixRedditApp()
            }
        }
    }
}

@Composable
private fun IonixRedditApp(viewModel: NewsPostingsViewModel = hiltViewModel()) {
    var shouldShowOnBoarding by rememberSaveable { mutableStateOf(true) }
    if (shouldShowOnBoarding) {
        ObserverWorkInfo(viewModel)
        viewModel.downPosting()
        OnBoardingScreen(onContinueClicked = {
            shouldShowOnBoarding = false
        })
    } else {
        PostingRepository()
    }
}

@Composable
private fun OnBoardingScreen(onContinueClicked: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome to the Basics")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text(text = "Continue")
            }
        }
    }
}

@Composable
private fun Greeting(post: PostingView) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(post)
    }
}

@Composable
private fun CardContent(post: PostingView) {
    val expanded = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Title(post.title)
            if (expanded.value) {
                ImagePost(post.url)
            }
        }
        IconButton(onClick = { expanded.value = !expanded.value }) {
            Icon(
                imageVector = if (expanded.value) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded.value) {
                    stringResource(R.string.lbl_show_less)
                } else {
                    stringResource(R.string.lbl_show_more)
                }
            )
        }
    }
    Row(modifier = Modifier.padding(12.dp)) {
        Statistics(post.score.toString())
        Statistics(post.comments.toString())
    }
}

@Composable
private fun PostingRepository() {
    val viewModel: CrudDatabaseViewModel = hiltViewModel()
    val list by viewModel.listPosting.observeAsState()
    list?.let {
        if (it.isNotEmpty()) {
            Greetings(postings = it)
        }
    }
}

@Composable
private fun Greetings(postings: List<PostingView>) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = postings) { post ->
            Greeting(post = post)
        }
    }
}

@Composable
private fun Title(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .wrapContentWidth(Alignment.Start)
    )
}

@Composable
private fun Statistics(data: String) {
    Text(
        text = data,
        style = MaterialTheme.typography.caption
    )
}

@Composable
private fun ImagePost(url: String) {
    CoilImage(
        imageModel = url,
        contentScale = ContentScale.Inside,
        placeHolder = ImageVector.vectorResource(id = R.drawable.loading_img),
        error = ImageVector.vectorResource(id = R.drawable.ic_broken_image)
    )
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

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnBoardingPreview() {
    IonixRedditTheme {
        OnBoardingScreen(onContinueClicked = {})
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
private fun DefaultPreview() {
    IonixRedditTheme {
        PostingRepository()
    }
}