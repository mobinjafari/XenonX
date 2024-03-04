package org.lotka.xenonx.presentation.chat.chatrow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

import org.lotka.xenonx.domain.model.model.chat.MessageStatus
import org.lotka.xenonx.presentation.chat.chatrow.MessageTimeText
import org.lotka.xenonx.presentation.chat.chatrow.chatbubble.ChatBubbleConstraints
import org.lotka.xenonx.presentation.chat.chatrow.chatbubble.TextMessageInsideBubble
import org.lotka.xenonx.presentation.theme.theme.spacing

@Composable
fun SentMessageRow(
    text: String,
    quotedMessage: String? = null,
    quotedImage: Int? = null,
    messageTime: String,
    messageStatus: MessageStatus
) {

    // Whole column that contains chat bubble and padding on start or end
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                8.dp
            )
    ) {
        // This is chat bubble

        ChatBubbleConstraints(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { },
            content = {
                // 💬 Quoted message
//                if (quotedMessage != null || quotedImage != null) {
//                    QuotedMessageAlt(
//                        modifier = Modifier
//                            .padding(top = 4.dp, start = 4.dp, end = 4.dp)
//                            // 🔥 This is required to set Surface height before text is set
//                            .height(IntrinsicSize.Min)
//                            .background(Color(0xffDEF6D3), shape = RoundedCornerShape(8.dp))
//                            .clip(shape = RoundedCornerShape(8.dp))
//                            .clickable {
//
//                            },
//                        quotedMessage = quotedMessage,
//                        quotedImage = quotedImage
//                    )
//                }
                TextMessageInsideBubble(
                    modifier = Modifier .padding(
                        8.dp
                    ),
                    text = text,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                    messageStat = {
                        MessageTimeText(
                            modifier = Modifier.wrapContentSize(),
                            messageTime = messageTime,
                            messageStatus = messageStatus
                        )
                    }
                )
//                ChatFlexBoxLayout(
//                    modifier = Modifier.padding(
//                        start = 2.dp,
//                        top = 2.dp,
//                        end = 4.dp,
//                        bottom = 2.dp
//                    ),
//                    text = text,
//                    messageStat = {
//                        MessageTimeText(
//                            modifier = Modifier.wrapContentSize(),
//                            messageTime = messageTime,
//                            messageStatus = messageStatus
//                        )
//                    }
//                )
            }
        )
    }
}