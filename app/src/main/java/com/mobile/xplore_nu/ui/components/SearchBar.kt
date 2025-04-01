package com.mobile.xplore_nu.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

@ExperimentalMaterial3Api
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit
) {
    androidx.compose.material3.SearchBar(
        query = query,
        onQueryChange = onQueryChanged,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange
    ) {

    }
}