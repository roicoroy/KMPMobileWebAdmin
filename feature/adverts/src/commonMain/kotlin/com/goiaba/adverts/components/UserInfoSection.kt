package com.goiaba.adverts.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.goiaba.data.models.profile.strapiUser.StrapiUser

@Composable
fun UserInfoSection(user: StrapiUser?) { // Made it public to be accessible from other packages
    user?.let {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "By ${it.username}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    } ?: run {
        // Show placeholder when user is null
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Anonymous",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}