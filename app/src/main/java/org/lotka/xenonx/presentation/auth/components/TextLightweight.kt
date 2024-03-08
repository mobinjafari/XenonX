package org.lotka.xenonx.presentation.auth.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.lotka.xenonx.presentation.theme.theme.spacing

@Composable
fun TextLightweight() {
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = "Lightweight instant messaging",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.outline
    )
}