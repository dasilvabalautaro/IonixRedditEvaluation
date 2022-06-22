package com.globalhiddenodds.ionixredditevaluation.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.globalhiddenodds.ionixredditevaluation.R
import com.globalhiddenodds.ionixredditevaluation.datasource.network.data.PostingCloud
import com.globalhiddenodds.ionixredditevaluation.ui.activities.SearchWidgetState
import com.globalhiddenodds.ionixredditevaluation.ui.utils.Utils
import com.globalhiddenodds.ionixredditevaluation.ui.viewmodel.SearchPostingsViewModel
import com.skydoves.landscapist.coil.CoilImage


@Composable
fun SearchBody(viewModel: SearchPostingsViewModel = hiltViewModel()) {
    val context = LocalContext.current
    Scaffold(topBar = {
        MainAppBar(
            searchWidgetState = viewModel.searchWidgetState,
            searchTextState = viewModel.searchTextState,
            onTextChange = {
                viewModel.updateSearchTextState(it)
            },
            onCloseClicked = {
                viewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
            },
            onSearchClicked = {
                viewModel.updateSearchTextState(it)
            },
            onSearchTriggered = {
                if (Utils.isConnect(context)){
                    viewModel.updateSearchWidgetState(SearchWidgetState.OPENED)
                }else {
                    Utils.notify(context, "Connectivity fail.")
                }
            }
        )
    }) {
        ObserverResultSearch(viewModel)
    }
}

@Composable
fun MainAppBar(
    searchWidgetState: State<SearchWidgetState>,
    searchTextState: State<String>,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit
) {
    when (searchWidgetState.value) {
        SearchWidgetState.CLOSED -> {
            DefaultAppBar(
                onSearchClicked = onSearchTriggered
            )
        }
        SearchWidgetState.OPENED -> {
            SearchAppBar(
                text = searchTextState.value,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }
    }
}

@Composable
fun DefaultAppBar(onSearchClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "Search")
        },
        actions = {
            IconButton(onClick = { onSearchClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    )
    {
        TextField(modifier = Modifier
            .fillMaxWidth(),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = "Search here...",
                    color = Color.White
                )

            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White

                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )

                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            ))
    }
}

@Composable
private fun ObserverResultSearch(viewModel: SearchPostingsViewModel) {
    val list by viewModel.listSearch.observeAsState()

    list?.let {

        if (it.isNotEmpty()) {
            Greetings(postings = it)
        }
    }
}

@Composable
private fun Greetings(postings: List<PostingCloud>) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = postings) { post ->
            Greeting(post = post)
        }
    }
}

@Composable
private fun Greeting(post: PostingCloud) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(post)
    }
}

@Composable
private fun CardContent(post: PostingCloud) {
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
                ImageUrl(post.url)
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
fun ImageUrl(url: String) {
    CoilImage(
        imageModel = url,
        contentScale = ContentScale.Inside,
        placeHolder = ImageVector.vectorResource(id = R.drawable.loading_img),
        error = ImageVector.vectorResource(id = R.drawable.ic_broken_image)
    )
}

@Composable
@Preview
fun DefaultAppPreview() {
    DefaultAppBar(onSearchClicked = {})
}

@Composable
@Preview
fun SearchAppBarPreview() {
    SearchAppBar(text = "Some random text",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {}
    )
}