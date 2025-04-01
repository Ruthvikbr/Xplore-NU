package com.mobile.xplore_nu.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobile.xplore_nu.R
import com.mobile.xplore_nu.ui.components.common.RedButton

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    userName: String?,
    userEmail: String?,
    onLogoutClicked:() -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        GlideImage(
            model = R.drawable.user_profile,
            contentDescription = "",
            requestBuilderTransform = {
                it.circleCrop()
            },
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.CenterHorizontally)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            ProfileInfoRow(icon = Icons.Default.AccountBox, label = "Name", value = userName)
            ProfileInfoRow(icon = Icons.Default.Email, label = "Email", value = userEmail)
        }

        Spacer(modifier = Modifier.weight(1f))

        RedButton(
            label = "Logout",
            onClick = onLogoutClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 10.dp)
        )
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$label Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = label, fontWeight = FontWeight.Bold)
            Text(text = value ?: "", style = MaterialTheme.typography.bodyMedium)
        }
    }
}