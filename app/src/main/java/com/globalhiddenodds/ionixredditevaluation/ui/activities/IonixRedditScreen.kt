package com.globalhiddenodds.ionixredditevaluation.ui.activities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class IonixRedditScreen(val icon: ImageVector) {
    Boarding(
        icon = Icons.Filled.Download
    ),
    Postings(
        icon = Icons.Filled.List
    ),
    Search(
        icon = Icons.Filled.Search
    ),
    Close(
        icon = Icons.Filled.Close
    );

    companion object {
        fun fromRoute(route: String?): IonixRedditScreen =
            when (route?.substringBefore("/")) {
                Boarding.name -> Boarding
                Postings.name -> Postings
                Search.name -> Search
                Close.name -> Close
                null -> Boarding
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}