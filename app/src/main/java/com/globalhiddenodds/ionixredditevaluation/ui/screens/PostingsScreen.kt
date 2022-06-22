package com.globalhiddenodds.ionixredditevaluation.ui.screens

import android.graphics.Bitmap
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.globalhiddenodds.ionixredditevaluation.R
import com.globalhiddenodds.ionixredditevaluation.ui.data.PostingView
import com.globalhiddenodds.ionixredditevaluation.ui.utils.Utils
import com.globalhiddenodds.ionixredditevaluation.ui.viewmodel.CrudDatabaseViewModel
import com.skydoves.landscapist.coil.CoilImage
import kotlin.math.roundToInt

@Composable
fun PostingsBody() {
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
                .padding(15.dp)
        ) {
            Title(post.title)
            if (expanded.value) {
                val bitmap = Utils.decodeBase64(post.base64)
                ImagePost(bitmap)
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Statistics(post.score, "Score")
        Statistics(post.comments, "Comments")
    }
}

@Composable
fun Title(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 20.dp)
            .wrapContentWidth(Alignment.Start)
    )
}

@Composable
fun Statistics(data: Double, label: String) {
    val number = data.roundToInt()
    val content = " $number $label "
    Text(
        text = content,
        style = MaterialTheme.typography.caption,
        modifier = Modifier.padding(start = 12.dp)
    )
}

@Composable
fun ImagePost(bitmap: Bitmap) {
    CoilImage(
        imageModel = bitmap,
        contentScale = ContentScale.Inside,
        placeHolder = ImageVector.vectorResource(id = R.drawable.loading_img),
        error = ImageVector.vectorResource(id = R.drawable.ic_broken_image)
    )
}

