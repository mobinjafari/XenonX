package org.lotka.xenonx.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.request.CachePolicy
import coil.request.ImageRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.lotka.xenonx.R


@Composable
fun FastImage(
    imageUrl: Any?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
        .background(Color.Gray)
        .size(90.dp)
) {
    Box(
        modifier = Modifier
            .background(Color.Gray)
            .then(modifier)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(false)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.DISABLED)
                .crossfade(false)
                .allowHardware(true)
                .error(R.drawable.nd_noimage)
                .build(),
            placeholder = painterResource(R.drawable.mediamodifier_design__4_ ),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize() // This makes the AsyncImage fill the Box
        )
    }

}