package com.example.chatwithme.presentation.userlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.lotka.xenonx.domain.model.model.chat.FriendListRegister

import org.lotka.xenonx.presentation.theme.theme.spacing

@Composable
fun PendingFriendRequestList(
    item: FriendListRegister,
    onAcceptClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = item.requesterEmail,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Row() {
                TextButton(
                    onClick = { onCancelClick() }
                ) {
                    Text(text = "Decline", color = MaterialTheme.colorScheme.error)
                }
                TextButton(onClick = { onAcceptClick() }) {
                    Text(text = "Accept")
                }
            }

        }
    }
}